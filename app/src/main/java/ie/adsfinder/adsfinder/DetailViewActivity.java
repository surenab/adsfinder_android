package ie.adsfinder.adsfinder;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ie.adsfinder.adsfinder.api.DetailResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static ie.adsfinder.adsfinder.AdsfinderUtils.capitalizeWord;

public class DetailViewActivity extends AppCompatActivity implements OnMapReadyCallback, PermissionsListener {
    private final String Youtube_API_Key = "AIzaSyC24EWTnlpb52KbmNb743DuNGJe6dlCIsg";
    private final String YOUTUBE_CODE = "X_YJrbDooO8";
    private TabAdapter tabAdapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private GeneralInfoAdapter generalInfoAdapter;
    private RecyclerView genRecyclerView;
    private AdsfinderCarListService adsfinderService;
    private MapView mapView;
    private MapboxMap mapboxMap;
    private TextView tv_description,tv_deatil_header,tv_deatil_price;
    private ImageView tv_donor_image;
    private YouTubePlayerView tv_video;
    private DetailResult result;
    private WebView displayVideo;
    private PermissionsManager permissionsManager;
    private YouTubePlayerFragment youTubePlayerFragment = YouTubePlayerFragment.newInstance();
    private ProgressBar progressBar;
    private LinearLayout wholePage;
    private SliderLayout sliderLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_detail_view);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        progressBar = findViewById(R.id.detail_main_progress);
        wholePage = findViewById(R.id.whole_detail_page);
        progressBar.setVisibility(View.VISIBLE);
        wholePage.setVisibility(View.GONE);


        sliderLayout = findViewById(R.id.imageSlider);
        sliderLayout.setIndicatorAnimation(SliderLayout.Animations.FILL); //set indicator animation by using SliderLayout.Animations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderLayout.setScrollTimeInSec(4); //set scroll delay in seconds :

        viewPager = findViewById(R.id.viewDetailsTabPager);
        setupViewPager(viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);




        genRecyclerView = findViewById(R.id.general_info_list);
        genRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        genRecyclerView.setItemAnimator(new DefaultItemAnimator());
        generalInfoAdapter = new GeneralInfoAdapter(this);
        genRecyclerView.setAdapter(generalInfoAdapter);


        adsfinderService = AdsfinderApi.getClient().create(AdsfinderCarListService.class);



        Integer ad_id = getIntent().getIntExtra("id", -1);
        String ad_type = getIntent().getStringExtra("SType");

        if (ad_id!=-1) {
            if (ad_type.equals("Cars")) {
                loadCarDataFromApi(ad_id);
            } else if (ad_type.equals("Houses")) {
                loadHouseDataFromApi(ad_id);
            } else if (ad_type.equals("Electronics")) {
                loadElectronicDataFromApi(ad_id);
            }
        }


        mapView = findViewById(R.id.mapDetailView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);


    }
    public void fillTabInfo() {
        tv_description = findViewById(R.id.description_text);
        tv_description.setText(result.getDescription());

        String video_url = result.getVideo();
        if (video_url != null && video_url.contains("youtube")) {
            String frameVideo = "<html><body><br> <iframe width='100%' height='100%' min-height='400' src='" + video_url + "' frameborder='0' allowfullscreen='allowfullscreen'></iframe></body></html>";
            displayVideo = (WebView) findViewById(R.id.webview);
            final WebSettings settings = displayVideo.getSettings();
            settings.setJavaScriptEnabled(true);
            settings.setJavaScriptCanOpenWindowsAutomatically(true);
            settings.setPluginState(WebSettings.PluginState.ON);
            settings.setLoadWithOverviewMode(true);
            settings.setUseWideViewPort(true);
            displayVideo.setWebChromeClient(new WebChromeClient());
            displayVideo.setPadding(0, 0, 0, 0);
            displayVideo.loadData(frameVideo, "text/html", "utf-8");
        }
    }
    public void fillGeneralInfo() {
        List<List<String>> generalResults = new ArrayList<List<String>>();
        JsonObject generalInfo = result.getGeneralinfo();
        for(Map.Entry<String, JsonElement> entry : generalInfo.entrySet()) {
            ArrayList<String> list = new ArrayList<String>();
            if (entry.getValue().isJsonNull()) { continue; }
            if (entry.getKey().equals("donor_logo")) { continue; }
            list.add(entry.getKey());
            list.add(entry.getValue().getAsString());
            generalResults.add(list);
        }
        generalInfoAdapter.addAll(generalResults);
    }

    public void fillHeaderInfo() {
        //tv_deatil_header = findViewById(R.id.deatil_header);
        //tv_deatil_header.setText(capitalizeWord(result.getHeader()));
        tv_deatil_price = findViewById(R.id.deatil_price);
        if (result.getPrice()!= null) {
            tv_deatil_price.setText(Integer.toString(result.getPrice()) + "€");
        }
        tv_donor_image = findViewById(R.id.donorLogoImage);
        String donor_umage_url = result.getGeneralinfo().get("donor_logo").getAsString();
        Glide.with(getApplicationContext()).load(donor_umage_url).apply(new RequestOptions().placeholder(R.color.colorPrimary).dontAnimate().skipMemoryCache(true)).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }
            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                return false;
            }
        }).into(tv_donor_image);

        tv_donor_image.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String sourceUrl = result.getUrl();
                Uri uri = Uri.parse(sourceUrl);
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
            }
        });
    }

    public void fillMap() {
        if (mapboxMap!=null) {
            GeoJsonSource geoJsonSource = new GeoJsonSource("source-id", Feature.fromGeometry(Point.fromLngLat(result.getLightaddress().getLongitude(), result.getLightaddress().getLatitude())));
            Style style = mapboxMap.getStyle();
            style.addSource(geoJsonSource);
            setCameraPosition(result.getLightaddress().getLongitude(), result.getLightaddress().getLatitude());
        }
    }

    public void fillImageCarusel() {
        final List<String> photos =  result.getPhotos();
        for (int i = 0; i <= photos.size()-1; i++) {
            SliderView sliderView = new SliderView(this);
                sliderView.setImageUrl(photos.get(i));
            sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
            sliderView.setDescription(capitalizeWord(result.getHeader()));
            final int finalI = i;
            sliderView.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(SliderView sliderView) {
                    Intent full_image_intent = new Intent(DetailViewActivity.this, FullscreenImageActivity.class);
                    full_image_intent.putExtra("photos", (ArrayList<String>) photos);
                    full_image_intent.putExtra("header", result.getHeader());
                    DetailViewActivity.this.startActivity(full_image_intent);
                }
            });
            sliderLayout.addSliderView(sliderView);
        }
    }

    public void makePagVisable() {
        wholePage.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    public void fillFullData() {
        fillGeneralInfo();
        fillTabInfo();
        fillHeaderInfo();
        fillMap();
        fillImageCarusel();
    }

    private void loadCarDataFromApi(final Integer ad_id) {
        callCarApi(ad_id).enqueue(new Callback<DetailResult>() {
            @Override
            public void onResponse(Call<DetailResult> call, Response<DetailResult> response) {
                result = response.body();
                fillFullData();
                makePagVisable();
            }
            @Override
            public void onFailure(Call<DetailResult> call, Throwable t) { t.printStackTrace(); }
        });
    }
    private void loadHouseDataFromApi(Integer id) {
        callHouseApi(id).enqueue(new Callback<DetailResult>() {
            @Override
            public void onResponse(Call<DetailResult> call, Response<DetailResult> response) {
                result = response.body();
                fillFullData();
                makePagVisable();
            }
            @Override
            public void onFailure(Call<DetailResult> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
    private void loadElectronicDataFromApi(Integer id) {
        callElectronicApi(id).enqueue(new Callback<DetailResult>() {
            @Override
            public void onResponse(Call<DetailResult> call, Response<DetailResult> response) {
                result = response.body();
                fillFullData();
                makePagVisable();
            }
            @Override
            public void onFailure(Call<DetailResult> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
    private void setupViewPager(ViewPager viewPager) {
        TabAdapter adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(new DescriptionFragment(), "Description");
        adapter.addFragment(new VideoFragment(), "Video");
        adapter.addFragment(new RelatedFragment(), "Related");
        viewPager.setAdapter(adapter);
    }

    class TabAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public TabAdapter(FragmentManager manager) {
            super(manager);

        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private Call<DetailResult> callCarApi(Integer id) { return adsfinderService.getCarById(id); }
    private Call<DetailResult> callHouseApi(Integer id) { return adsfinderService.getHouseById(id); }
    private Call<DetailResult> callElectronicApi(Integer id) { return adsfinderService.getElectronicById(id); }

    @Override
    public void onMapReady(@NonNull MapboxMap map) {
        this.mapboxMap = map;
        map.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                try {
                    //style.addImage(SINGLE_EARTHQUAKE_TRIANGLE_ICON_ID, BitmapUtils.getBitmapFromDrawable(getResources().getDrawable(R.drawable.ic_map_marker)));
                    //enableLocationComponent(style);
                    style.addImage(("marker_icon"), BitmapFactory.decodeResource(getResources(), R.drawable.ic_map_marker));
                    //loadHouseData(style);
                } catch (Exception exception) {
                }
            }
        });
    }
    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            LocationComponent locationComponent = mapboxMap.getLocationComponent();
            locationComponent.activateLocationComponent(LocationComponentActivationOptions.builder(this, loadedMapStyle).build());
            locationComponent.setLocationComponentEnabled(true);
            locationComponent.setCameraMode(CameraMode.TRACKING);
            locationComponent.setRenderMode(RenderMode.COMPASS);
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, "Your Location", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    enableLocationComponent(style);
                }
            });
        } else {
            Toast.makeText(this, "Granted", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }
    @Override
    @SuppressWarnings( {"MissingPermission"})
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

    private void setCameraPosition(Float lat, Float lan) {
        mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lan), 12.0));
    }
}
