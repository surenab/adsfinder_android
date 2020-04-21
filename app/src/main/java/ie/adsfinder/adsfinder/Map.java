package ie.adsfinder.adsfinder;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.mapbox.mapboxsdk.style.expressions.Expression;
import com.mapbox.mapboxsdk.style.layers.CircleLayer;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.layers.TransitionOptions;
import com.mapbox.mapboxsdk.style.sources.GeoJsonOptions;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.utils.BitmapUtils;

import org.apache.commons.lang3.StringUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ie.adsfinder.adsfinder.api.DetailHouseResult;
import ie.adsfinder.adsfinder.api.HouseMapResult;
import ie.adsfinder.adsfinder.api.HuseMapModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mapbox.mapboxsdk.style.expressions.Expression.all;
import static com.mapbox.mapboxsdk.style.expressions.Expression.division;
import static com.mapbox.mapboxsdk.style.expressions.Expression.exponential;
import static com.mapbox.mapboxsdk.style.expressions.Expression.get;
import static com.mapbox.mapboxsdk.style.expressions.Expression.gte;
import static com.mapbox.mapboxsdk.style.expressions.Expression.has;
import static com.mapbox.mapboxsdk.style.expressions.Expression.interpolate;
import static com.mapbox.mapboxsdk.style.expressions.Expression.literal;
import static com.mapbox.mapboxsdk.style.expressions.Expression.lt;
import static com.mapbox.mapboxsdk.style.expressions.Expression.rgb;
import static com.mapbox.mapboxsdk.style.expressions.Expression.stop;
import static com.mapbox.mapboxsdk.style.expressions.Expression.toNumber;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.circleBlur;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.circleColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.circleRadius;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconSize;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textField;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textOffset;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textSize;

public class Map extends AppCompatActivity implements OnMapReadyCallback, MapboxMap.OnMapClickListener {
    private MapView mapView;
    private MapboxMap mapboxMap;
    private GeoJsonSource geoJsonSource;
    private AdsfinderCarListService adsfinderService;
    private static final String CLUSTER_EARTHQUAKE_TRIANGLE_ICON_ID = "quake-triangle-icon-id";
    private static final String SINGLE_EARTHQUAKE_TRIANGLE_ICON_ID = "home_image";
    private static final String EARTHQUAKE_SOURCE_ID = "earthquakes";
    private static final String POINT_COUNT = "point_count";
    private PermissionsManager permissionsManager;
    private LocationEngine locationEngine;
    private LocationLayerPlugin locationLayerPlugin;
    private Location oroginLocation;
    private List<Feature> currentFeatures = new ArrayList<>();
    private ProgressBar tv_Progress;
    private ConstraintLayout mapCard;
    private ProgressBar progressBarLoading;
    private Button cardMoreButton;
    private Integer house_ad_id;

    private Long min_price;
    private Long max_price;
    private Long min_beds;
    private Long max_beds;
    private Long min_bathrooms;
    private Long max_bathrooms;
    private List<String> property_type = new ArrayList<>();
    private String property_category;
    FrameLayout activity_map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_map);
        activity_map = new FrameLayout(this);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        cardMoreButton = findViewById(R.id.cardMoreButton);
        cardMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detail_intent = new Intent(Map.this, DetailViewActivity.class);
                detail_intent.putExtra("SType", "Houses");
                detail_intent.putExtra("id", house_ad_id);
                startActivity(detail_intent);
            }
        });

        progressBarLoading = findViewById(R.id.map_page_progress);
        mapView = findViewById(R.id.mapView);
        progressBarLoading.setVisibility(View.VISIBLE);
        mapView.setVisibility(View.INVISIBLE);

        adsfinderService = AdsfinderApi.getClient().create(AdsfinderCarListService.class);

        min_price = getIntent().getLongExtra("min_price", 0);
        max_price = getIntent().getLongExtra("max_price", 100000);
        min_beds = getIntent().getLongExtra("min_beds", 0);
        max_beds = getIntent().getLongExtra("max_beds", 6);
        min_bathrooms = getIntent().getLongExtra("min_bathrooms", 0);
        max_bathrooms = getIntent().getLongExtra("max_bathrooms", 5);
        property_type = getIntent().getStringArrayListExtra("property_type");
        property_category = getIntent().getStringExtra("property_category");
        if (min_price==0) {
            min_price=null;
        }
        if (max_price == 1000000) {
            max_price = null;
        }
        if (min_beds == 0) {
            min_beds = null;
        }
        if (max_beds == 6) {
            max_beds = null;
        }
        if (min_bathrooms == 0) {
            min_bathrooms = null;
        }
        if (max_bathrooms == 5) {
            max_bathrooms = null;
        }
        if (property_type!=null && property_type.isEmpty()) {
            property_type = null;
        }

        mapCard = findViewById(R.id.singleHouseMapCard);

        Button clickButton = findViewById(R.id.closeCardButton);
        clickButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapCard.setVisibility(View.INVISIBLE);
            }
        });


        Bundle extras = getIntent().getExtras();

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

    }
    @Override
    public void onMapReady(@NonNull MapboxMap map) {
        this.mapboxMap = map;
        map.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                try {
                    style.addImage(SINGLE_EARTHQUAKE_TRIANGLE_ICON_ID, BitmapUtils.getBitmapFromDrawable(getResources().getDrawable(R.drawable.ic_map_marker)));
                    style.setTransition(new TransitionOptions(0, 0, false));
                    loadHouseData(style);
                } catch (Exception exception) {
                    //Log.d("MapView", exception.toString());
                }
                mapboxMap.addOnMapClickListener(Map.this);
            }
        });
    }
    @Override
    public boolean onMapClick(@NonNull LatLng point) {
        PointF screenPoint = mapboxMap.getProjection().toScreenLocation(point);
        List<Feature> features = mapboxMap.queryRenderedFeatures(screenPoint, "unclustered-points");
        if (!features.isEmpty()) {
            tv_Progress = findViewById(R.id.ad_progress);
            tv_Progress.setVisibility(View.VISIBLE);
            mapCard.setVisibility(View.VISIBLE);
            Feature selectedFeature = features.get(0);
            Number id = selectedFeature.getNumberProperty("id");
            house_ad_id = id.intValue();
            loadSingleHouseData(id.intValue());
        }
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.map_main, menu);
        return true;
    }

    private Call<HuseMapModel> callMapHouseApi() {
        return adsfinderService.getMapHouses(min_price, max_price, min_beds, max_beds, min_bathrooms, max_bathrooms, property_type, property_category);
    }
    private List<HouseMapResult> fetchHouseMapResults(Response<HuseMapModel> response) {
        HuseMapModel houses = response.body();
        return houses.getResults();
    }
    private void loadHouseData(@NonNull final Style loadedMapStyle) {
        callMapHouseApi().enqueue(new Callback<HuseMapModel>() {
            @Override
            public void onResponse(Call<HuseMapModel> call, Response<HuseMapModel> response) {
                List<HouseMapResult> results = fetchHouseMapResults(response);
                //addClusteredApiJsonSource(loadedMapStyle, results);
                addClusteredGeoJsonSource2(loadedMapStyle, results);
                progressBarLoading.setVisibility(View.GONE);
                mapView.setVisibility(View.VISIBLE);

            }
            @Override
            public void onFailure(Call<HuseMapModel> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    private Call<DetailHouseResult> callSingleHouseApi(Integer id) {
        return adsfinderService.getMapHouseById(id);
    }
    private void loadSingleHouseData(Integer id) {
        callSingleHouseApi(id).enqueue(new Callback<DetailHouseResult>() {
            @Override
            public void onResponse(Call<DetailHouseResult> call, Response<DetailHouseResult> response) {
                tv_Progress = findViewById(R.id.ad_progress);
                tv_Progress.setVisibility(View.VISIBLE);

                DetailHouseResult result = response.body();

                TextView tv_donor = findViewById(R.id.cardDonorView);
                tv_donor.setText(" "+StringUtils.capitalize(result.getDonor()));


                TextView tv_county = findViewById(R.id.cardCountyView);
                tv_county.setText(AdsfinderUtils.capitalizeWord(" "+result.getLightaddress().getCounty().trim()+", "+result.getLightaddress().getTown().trim()));

                TextView tv_cardBedView = findViewById(R.id.cardBedView);
                tv_cardBedView.setText(" "+Integer.toString(result.getGeneralinfo().getBeds()) + " Beds");

                TextView tv_cardBathView = findViewById(R.id.cardBathView);
                tv_cardBathView.setText(" "+Integer.toString(result.getGeneralinfo().getBaths()) + " Bathrooms");

                TextView tv_price = findViewById(R.id.cardPriceVIew);
                if (result.getPrice()==null) {
                    tv_price.setText(" Unknown");
                } else {
                    tv_price.setText(" "+result.getPrice() + "€");
                }

                ImageView card_photo = findViewById(R.id.cardImageView);
                Glide.with(getWindow().getContext()).load(result.getMainimageurl()).apply(new RequestOptions().dontAnimate().skipMemoryCache(true)).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Log.d("IMAGE", "LOADED FAIL");
                        tv_Progress.setVisibility(View.GONE);
                        return false;
                    }
                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        Log.d("IMAGE", "LOADED SUCCESS RUNNING GONE");
                        tv_Progress.setVisibility(View.GONE);
                        return false;
                    }
                }).into(card_photo);
                Log.d("IMAGE", "Done");
                tv_Progress.setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<DetailHouseResult> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    private void addClusteredGeoJsonSource2(@NonNull Style loadedMapStyle, List<HouseMapResult> results) {
        try {
            if (false) {
                loadedMapStyle.addSource(new GeoJsonSource(EARTHQUAKE_SOURCE_ID, new URL("https://www.mapbox.com/mapbox-gl-js/assets/earthquakes.geojson"), new GeoJsonOptions().withCluster(true).withClusterMaxZoom(14).withClusterRadius(50)));
            } else {
                List<Feature> symbolLayerIconFeatureList = new ArrayList<>();
                Feature temp;
                for (int i = 0; i < results.size(); i++) {
                    temp = Feature.fromGeometry(Point.fromLngLat(results.get(i).getAddress__longitude(), results.get(i).getAddress__latitude()));
                    temp.addNumberProperty("id",results.get(i).getId());
                    temp.addNumberProperty("mag", 1);
                    symbolLayerIconFeatureList.add(temp);
                }
                currentFeatures = symbolLayerIconFeatureList;
                FeatureCollection collection = FeatureCollection.fromFeatures(symbolLayerIconFeatureList);
                GeoJsonOptions routeGeoJsonOptions = new GeoJsonOptions().withCluster(true).withClusterMaxZoom(14).withClusterRadius(50);
                GeoJsonSource routeSource = new GeoJsonSource(EARTHQUAKE_SOURCE_ID, collection, routeGeoJsonOptions);
                loadedMapStyle.addSource(routeSource);
            }
        } catch (Exception uriSyntaxException) {
            //Log.e("Check the URL %s" , uriSyntaxException.getMessage());
        }

        SymbolLayer unclusteredSymbolLayer = new SymbolLayer("unclustered-points", EARTHQUAKE_SOURCE_ID);

        unclusteredSymbolLayer.setProperties(iconImage(SINGLE_EARTHQUAKE_TRIANGLE_ICON_ID),
                iconSize(division(get("mag"),
                literal(4.0f))),
                iconColor(interpolate(exponential(1), get("mag"), stop(2.0, rgb(0, 255, 0)), stop(4.5, rgb(0, 0, 255)), stop(7.0, rgb(255, 0, 0)))),
                iconOffset(new Float[] {0f, -9.0f}),
                iconAllowOverlap(true)
        );
        unclusteredSymbolLayer.setFilter(has("mag"));

        loadedMapStyle.addLayer(unclusteredSymbolLayer);

        int[][] layers = new int[][]{
                new int[]{150, Color.parseColor("#E55E5E")},
                new int[]{20, Color.parseColor("#F9886C")},
                new int[]{0, Color.parseColor("#FBB03B")}
        };

        for (int i = 0; i < layers.length; i++) {
            CircleLayer circles = new CircleLayer("cluster-" + i, EARTHQUAKE_SOURCE_ID);
            circles.setProperties(
                    circleColor(layers[i][1]),
                    circleRadius(28f),
                    circleBlur(0.5f)
            );
            Expression pointCount = toNumber(get(POINT_COUNT));

            circles.setFilter(
                    i == 0
                            ? all(has(POINT_COUNT),
                            gte(pointCount, literal(layers[i][0]))
                    ) : all(has(POINT_COUNT),
                            gte(pointCount, literal(layers[i][0])),
                            lt(pointCount, literal(layers[i - 1][0]))));
            loadedMapStyle.addLayer(circles);
        }
        loadedMapStyle.addLayer(new SymbolLayer("count",
                EARTHQUAKE_SOURCE_ID).withProperties(textField(Expression.toString(get(POINT_COUNT))),
                textSize(12f),
                textColor(Color.BLACK),
                textIgnorePlacement(true),
                textOffset(new Float[] {0f, 0f}),
                textAllowOverlap(true)));
    }

    private void addClusteredApiJsonSource(@NonNull Style loadedMapStyle, List<HouseMapResult> results) {
        List<Feature> symbolLayerIconFeatureList = new ArrayList<>();
        Feature temp;
        for (int i = 0; i < results.size(); i++) {
            temp = Feature.fromGeometry(Point.fromLngLat(results.get(i).getAddress__longitude(), results.get(i).getAddress__latitude()));
            temp.addNumberProperty("id",results.get(i).getId());
            temp.addNumberProperty("mag", 2);
            symbolLayerIconFeatureList.add(temp);
        }
        FeatureCollection collection = FeatureCollection.fromFeatures(symbolLayerIconFeatureList);
        GeoJsonOptions routeGeoJsonOptions = new GeoJsonOptions().withCluster(true).withClusterMaxZoom(14).withClusterRadius(50);
        GeoJsonSource routeSource = new GeoJsonSource("houses", collection, routeGeoJsonOptions);
        loadedMapStyle.addSource(routeSource);

        SymbolLayer unclustered = new SymbolLayer("unclustered-points", "houses").withProperties(iconImage("home_image"), iconSize(division(get("mag"), literal(4.0f))));
        unclustered.setFilter(has("mag"));
        loadedMapStyle.addLayer(unclustered);




        int[] layers = new int[] {500, 200, 100, 50, 20, 10, 0};

        for (int i = 0; i < layers.length; i++) {
            SymbolLayer circles = new SymbolLayer("cluster-" + i, EARTHQUAKE_SOURCE_ID);

            circles.setProperties(
                    iconImage(CLUSTER_EARTHQUAKE_TRIANGLE_ICON_ID)
            );

            Expression pointCount = toNumber(get("point_count"));

            circles.setFilter(
                    i == 0
                            ? all(has("point_count"),
                            gte(pointCount, literal(layers[i]))
                    ) : all(has("point_count"),
                            gte(pointCount, literal(layers[i])),
                            lt(pointCount, literal(layers[i - 1]))
                    )
            );
            loadedMapStyle.addLayer(circles);
        }

        SymbolLayer count = new SymbolLayer("count", "houses");
        count.setProperties(textField(Expression.toString(get("point_count"))), textSize(12f), textColor(Color.WHITE), textIgnorePlacement(true), textAllowOverlap(true));
        loadedMapStyle.addLayer(count);
        Toast.makeText(this, "Map data loaded", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }
    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }
    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_map_filter:
                Intent search_intent = new Intent(Map.this, HouseMapFilterActivity.class);
                startActivity(search_intent);
                return true;
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_map_refresh:
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
