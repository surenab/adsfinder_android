package ie.adsfinder.adsfinder;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ie.adsfinder.adsfinder.api.CarResult;
import ie.adsfinder.adsfinder.api.CarsModel;
import ie.adsfinder.adsfinder.api.ElectronicResult;
import ie.adsfinder.adsfinder.api.ElectronicsModel;
import ie.adsfinder.adsfinder.api.HouseResult;
import ie.adsfinder.adsfinder.api.HousesModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchResult extends AppCompatActivity {
    private AsyncTask<String, Void, String> runningTask;
    private ProgressBar progressBar;
    private CardsAdapter carAdapter;
    private HousesAdapter houseAdapter;
    private ElectronicsAdapter electronicAdapter;
    private RecyclerView recyclerView;
    private GridLayoutManager listGridLayoutManager;
    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 10;
    private int currentPage = PAGE_START;
    private AdsfinderCarListService adsfinderService;
    private static final String TAG = "SearchResult";

    private String searchType;
    private List<String> county;
    private List<String> area;
    private List<String> make;
    private List<String> model;
    private List<String> fueltype;
    private Long min_price;
    private Long max_price;
    private Long min_firstregyear;
    private Long max_firstregyear;
    private Long min_kilometer;
    private Long max_kilometer;
    private Float min_enginesize;
    private Float max_enginesize;
    private String transmission;
    private String condition;
    private String keyword;

    private List<String> category_name;
    private Long min_beds;
    private Long max_beds;
    private Long min_bathrooms;
    private Long max_bathrooms;
    private List<String> property_type;
    private String property_category;
    private List<String> ber_classification;

    private void clear_car_inputs() {
        if (max_price==100000 || max_price==60000) {
            max_price=null;
        }
        if (min_firstregyear==1950) {
            min_firstregyear=null;
        }
        if (max_firstregyear==2020) {
            max_firstregyear=null;
        }
        if (min_kilometer==0) {
            min_kilometer=null;
        }
        if (max_kilometer==300000) {
            max_kilometer=null;
        }
        if (min_enginesize==0.0) {
            min_enginesize=null;
        }
        if (max_enginesize==10.0) {
            max_enginesize=null;
        }
        if (transmission.isEmpty() || transmission.equals("any")) {
            transmission=null;
        }
        if (condition.isEmpty() || condition.equals("any")) {
            condition=null;
        }
        if (make.isEmpty()) {
            make=null;
        }
        if (model.isEmpty()) {
            model=null;
        } else {
            make=null;
        }
    }
    private void clear_electronic_inputs() {
        if (max_price == 100000 || max_price == 60000) {
            max_price = null;
        }
        if (category_name.isEmpty()) {
            category_name = null;
        }
    }
    private void clear_house_inputs() {
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
        if (property_type.isEmpty()) {
            property_type = null;
        }
        if (ber_classification.isEmpty()) {
            ber_classification = null;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_view);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        searchType = getIntent().getStringExtra("SearchType");
        county = getIntent().getStringArrayListExtra("county");
        area = getIntent().getStringArrayListExtra("area");
        min_price = getIntent().getLongExtra("min_price", 0);
        max_price = getIntent().getLongExtra("max_price", 100000);
        if (min_price==0) {
            min_price=null;
        }
        if (county.isEmpty()) {
            county=null;
        }
        if (area.isEmpty()) {
            area=null;
        } else {
            county=null;
        }
        if (searchType.equals("Cars")) {
            make = getIntent().getStringArrayListExtra("model_name");
            model = getIntent().getStringArrayListExtra("make_name");
            fueltype = getIntent().getStringArrayListExtra("fueltype");
            min_firstregyear = getIntent().getLongExtra("min_firstregyear", 1950);
            max_firstregyear = getIntent().getLongExtra("max_firstregyear", 2020);
            min_kilometer = getIntent().getLongExtra("min_kilometer", 0);
            max_kilometer = getIntent().getLongExtra("max_kilometer", 300000);
            min_enginesize = getIntent().getFloatExtra("min_enginesize", (float) 0.0);
            max_enginesize = getIntent().getFloatExtra("max_enginesize", (float) 10.0);
            transmission = getIntent().getStringExtra("transmission");
            condition = getIntent().getStringExtra("condition");
            clear_car_inputs();
        } else if (searchType.equals("Electronics")) {
            keyword = getIntent().getStringExtra("keyword");
            category_name = getIntent().getStringArrayListExtra("category_name");
            clear_electronic_inputs();
        } else if (searchType.equals("Houses")) {
            min_beds = getIntent().getLongExtra("min_beds", 0);
            max_beds = getIntent().getLongExtra("max_beds", 6);
            min_bathrooms = getIntent().getLongExtra("min_bathrooms", 0);
            max_bathrooms = getIntent().getLongExtra("max_bathrooms", 5);
            property_type = getIntent().getStringArrayListExtra("property_type");
            property_category = getIntent().getStringExtra("property_category");
            ber_classification = getIntent().getStringArrayListExtra("ber_classification");
            if (max_price == 1000000) {
                max_price = null;
            }
            clear_house_inputs();
        }


        progressBar = findViewById(R.id.main_progress);
        recyclerView = findViewById(R.id.card_list);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            listGridLayoutManager = new GridLayoutManager(this, 4);
        } else {
            listGridLayoutManager = new GridLayoutManager(this, 2);
        }
        recyclerView.setLayoutManager(listGridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        Button back_button = findViewById(R.id.filter_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if (searchType.equals("Cars")) {
            carAdapter = new CardsAdapter(this);
            recyclerView.setAdapter(carAdapter);
        } else if (searchType.equals("Houses")) {
            houseAdapter = new HousesAdapter(this);
            recyclerView.setAdapter(houseAdapter);
        } else {
            electronicAdapter = new ElectronicsAdapter(this);
            recyclerView.setAdapter(electronicAdapter);
        }


        recyclerView.addOnScrollListener(new PaginationScrollListener(listGridLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (searchType.equals("Cars")) {
                            loadNextPage();
                        } else if (searchType.equals("Houses")) {
                            loadHouseNextPage();
                        } else if (searchType.equals("Electronics")) {
                            loadElectronicNextPage();
                        }

                    }
                }, 1000);
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
        adsfinderService = AdsfinderApi.getClient().create(AdsfinderCarListService.class);

        if (searchType.equals("Cars")) {
            loadFirstPage();
        } else if (searchType.equals("Houses")) {
            loadHouseFirstPage();
        } else if (searchType.equals("Electronics")) {
            loadElectronicFirstPage();
        }
    }

    private void loadElectronicFirstPage() {
        callElectronicsApi().enqueue(new Callback<ElectronicsModel>() {
            @Override
            public void onResponse(Call<ElectronicsModel> call, Response<ElectronicsModel> response) {
                progressBar.setVisibility(View.GONE);
                List<ElectronicResult> results = fetchElectronicResults(response);
                if (results!=null && !results.isEmpty()) {
                    electronicAdapter.addAll(results);
                    if (currentPage <= TOTAL_PAGES) electronicAdapter.addLoadingFooter();
                    else isLastPage = true;
                }
            }
            @Override
            public void onFailure(Call<ElectronicsModel> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }
    private List<ElectronicResult> fetchElectronicResults(Response<ElectronicsModel> response) {
        ElectronicsModel electronics = response.body();
        return electronics.getResults();
    }
    private void loadElectronicNextPage() {
        callElectronicsApi().enqueue(new Callback<ElectronicsModel>() {
            @Override
            public void onResponse(Call<ElectronicsModel> call, Response<ElectronicsModel> response) {
                electronicAdapter.removeLoadingFooter();
                isLoading = false;
                List<ElectronicResult> results = fetchElectronicResults(response);
                if (results!=null && !results.isEmpty()) {
                    electronicAdapter.addAll(results);
                    if (currentPage != TOTAL_PAGES) electronicAdapter.addLoadingFooter();
                    else isLastPage = true;
                }
            }
            @Override
            public void onFailure(Call<ElectronicsModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


    private void loadHouseFirstPage() {
        callHousesApi().enqueue(new Callback<HousesModel>() {
            @Override
            public void onResponse(Call<HousesModel> call, Response<HousesModel> response) {
                progressBar.setVisibility(View.GONE);
                List<HouseResult> houseresults = fetchHouseResults(response);
                if (houseresults!=null && !houseresults.isEmpty()) {
                    houseAdapter.addAll(houseresults);
                    if (currentPage <= TOTAL_PAGES) {
                        houseAdapter.addLoadingFooter();
                    } else {
                        isLastPage = true;
                    }
                }
            }
            @Override
            public void onFailure(Call<HousesModel> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }
    private List<HouseResult> fetchHouseResults(Response<HousesModel> response) {
        HousesModel houses = response.body();
        return houses.getResults();
    }
    private void loadHouseNextPage() {
        callHousesApi().enqueue(new Callback<HousesModel>() {
            @Override
            public void onResponse(Call<HousesModel> call, Response<HousesModel> response) {
                houseAdapter.removeLoadingFooter();
                isLoading = false;
                List<HouseResult> houseresults = fetchHouseResults(response);
                if (houseresults!=null && !houseresults.isEmpty()) {
                    houseAdapter.addAll(houseresults);
                    if (currentPage != TOTAL_PAGES) houseAdapter.addLoadingFooter();
                    else isLastPage = true;
                }
            }

            @Override
            public void onFailure(Call<HousesModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void loadFirstPage() {
        callCarsApi().enqueue(new Callback<CarsModel>() {
            @Override
            public void onResponse(Call<CarsModel> call, Response<CarsModel> response) {
                progressBar.setVisibility(View.GONE);
                List<CarResult> results = fetchResults(response);
                if (results!=null && !results.isEmpty()) {
                    carAdapter.addAll(results);
                    if (currentPage <= TOTAL_PAGES) carAdapter.addLoadingFooter();
                    else isLastPage = true;
                }
            }
            @Override
            public void onFailure(Call<CarsModel> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }
    private List<CarResult> fetchResults(Response<CarsModel> response) {
        if (response.isSuccessful()) {
            CarsModel cars = response.body();
            return cars.getResults();
        } else {
            return null;
        }
    }
    private void loadNextPage() {
        callCarsApi().enqueue(new Callback<CarsModel>() {
            @Override
            public void onResponse(Call<CarsModel> call, Response<CarsModel> response) {
                carAdapter.removeLoadingFooter();
                isLoading = false;
                List<CarResult> results = fetchResults(response);
                if (results!=null && !results.isEmpty()) {
                    carAdapter.addAll(results);
                    if (currentPage != TOTAL_PAGES) carAdapter.addLoadingFooter();
                    else isLastPage = true;
                }
            }

            @Override
            public void onFailure(Call<CarsModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


    private Call<CarsModel> callCarsApi() {
        return adsfinderService.getCars(currentPage * 24, min_price,max_price,min_firstregyear,max_firstregyear,min_kilometer,max_kilometer,min_enginesize,max_enginesize, fueltype,transmission,model,make,county,area, null);
    }
    private Call<HousesModel> callHousesApi() { return adsfinderService.getHouses(currentPage * 24, county, area, min_price, max_price, min_beds, max_beds, min_bathrooms, max_bathrooms, property_type, ber_classification, property_category, null); }
    private Call<ElectronicsModel> callElectronicsApi() { return adsfinderService.getElectronics(currentPage * 24,min_price, max_price,category_name,  county, area, keyword); }
}
