package ie.adsfinder.adsfinder;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.JsonParser;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.text.HtmlCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.mapbox.mapboxsdk.style.expressions.Expression;
import com.mapbox.mapboxsdk.style.layers.CircleLayer;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.layers.TransitionOptions;
import com.mapbox.mapboxsdk.style.sources.GeoJsonOptions;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.utils.BitmapUtils;

import org.apache.commons.lang3.StringUtils;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.security.acl.Permission;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.Future;

import ie.adsfinder.adsfinder.api.CarResult;
import ie.adsfinder.adsfinder.api.CarsModel;
import ie.adsfinder.adsfinder.api.ElectronicsModel;
import ie.adsfinder.adsfinder.api.HouseMapResult;
import ie.adsfinder.adsfinder.api.HouseResult;
import ie.adsfinder.adsfinder.api.HousesModel;
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
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconSize;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textField;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textOffset;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textSize;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;

public class Map extends AppCompatActivity implements OnMapReadyCallback, MapboxMap.OnMapClickListener {
    private MapView mapView;
    private MapboxMap mapboxMap;
    private GeoJsonSource geoJsonSource;
    private String map_type;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_map);

        adsfinderService = AdsfinderApi.getClient().create(AdsfinderCarListService.class);

        Spinner spinner = findViewById(R.id.county_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.counties_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        mapCard = findViewById(R.id.singleHouseMapCard);

        Button clickButton = findViewById(R.id.closeCardButton);
        clickButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapCard.setVisibility(View.INVISIBLE);
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        map_type = extras.getString("map_type");

        mapView = (MapView) findViewById(R.id.mapView);
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
                    Log.d("MapView", exception.toString());
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
            loadSingleHouseData(id.intValue());
        }
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.map_main, menu);
        return true;
    }

    private Call<HuseMapModel> callMapHouseApi() {
        return adsfinderService.getMapHouses();
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
            }
            @Override
            public void onFailure(Call<HuseMapModel> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    private Call<HouseResult> callSingleHouseApi(Integer id) {
        return adsfinderService.getHouseById(id);
    }
    private void loadSingleHouseData(Integer id) {
        callSingleHouseApi(id).enqueue(new Callback<HouseResult>() {
            @Override
            public void onResponse(Call<HouseResult> call, Response<HouseResult> response) {
                HouseResult result = response.body();

                TextView tv_header = findViewById(R.id.cardHeaderView);
                tv_header.setText(AdsfinderUtils.capitalizeWord(result.getProperty_title()));

                TextView tv_donor = findViewById(R.id.cardDonorView);
                String donor_text = new String("<a href='").concat(result.getUrl()).concat("' target='_top'>").concat(StringUtils.capitalize(result.getDonor())).concat("</a>");
                tv_donor.setText(HtmlCompat.fromHtml(donor_text, HtmlCompat.FROM_HTML_MODE_LEGACY));


                TextView tv_county = findViewById(R.id.cardCountyView);
                tv_county.setText(AdsfinderUtils.capitalizeWord(result.getAddress().getTown().trim()+", "+result.getAddress().getCounty().trim()));

                TextView tv_cardBedView = findViewById(R.id.cardBedView);
                tv_cardBedView.setText(Integer.toString(result.getBeds()) + " Beds");

                TextView tv_cardBathView = findViewById(R.id.cardBathView);
                tv_cardBathView.setText(Integer.toString(result.getBathrooms()) + " Bathrooms");

                TextView tv_price = findViewById(R.id.cardPriceVIew);
                if (result.getPrice()==null) {
                    tv_price.setText("Unknown");
                } else {
                    tv_price.setText(result.getPrice() + "€");
                }

                ImageView card_photo = findViewById(R.id.cardImageView);
                tv_Progress = findViewById(R.id.ad_progress);

                Glide.with(getWindow().getContext()).load(result.getMainimageurl()).listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        tv_Progress.setVisibility(View.GONE);
                        return false;
                    }
                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        tv_Progress.setVisibility(View.GONE);
                        return false;
                    }
                }).diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().crossFade().into(card_photo);
            }
            @Override
            public void onFailure(Call<HouseResult> call, Throwable t) {
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
            Log.e("Check the URL %s" , uriSyntaxException.getMessage());
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
            case R.id.menu_streets:
                mapboxMap.setStyle(Style.MAPBOX_STREETS);
                return true;
            case R.id.menu_dark:
                mapboxMap.setStyle(Style.DARK);
                return true;
            case R.id.menu_light:
                mapboxMap.setStyle(Style.LIGHT);
                return true;
            case R.id.menu_outdoors:
                mapboxMap.setStyle(Style.OUTDOORS);
                return true;
            case R.id.menu_satellite:
                mapboxMap.setStyle(Style.SATELLITE);
                return true;
            case R.id.menu_satellite_streets:
                mapboxMap.setStyle(Style.SATELLITE_STREETS);
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
