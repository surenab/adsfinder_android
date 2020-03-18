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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "on SearchResult onCreate method");
        setContentView(R.layout.result_view);
        searchType = getIntent().getStringExtra("SearchType");
        Log.d("SHOW DATA", searchType);
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

        FloatingActionButton back_button = findViewById(R.id.back_button);
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
                Log.d(TAG, "Load more items");
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
                Log.d(TAG, "Last Page");
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                Log.d(TAG, "is loading");
                return isLoading;
            }
        });
        adsfinderService = AdsfinderApi.getClient().create(AdsfinderCarListService.class);

        Log.d("searchType", searchType);
        if (searchType.equals("Cars")) {
            Log.d(TAG, "Running Cars first");
            loadFirstPage();
        } else if (searchType.equals("Houses")) {
            Log.d(TAG, "Running House first");
            loadHouseFirstPage();
        } else if (searchType.equals("Electronics")) {
            loadElectronicFirstPage();
        }
        Log.d(TAG, "Running First elements");
    }


    private void loadElectronicFirstPage() {
        Log.d("SHOW DATA", "loadHouseFirstPage");
        callElectronicsApi().enqueue(new Callback<ElectronicsModel>() {
            @Override
            public void onResponse(Call<ElectronicsModel> call, Response<ElectronicsModel> response) {
                progressBar.setVisibility(View.GONE);
                List<ElectronicResult> results = fetchElectronicResults(response);
                Log.d("SHOW DATA", results.toString());
                electronicAdapter.addAll(results);
                if (currentPage <= TOTAL_PAGES) electronicAdapter.addLoadingFooter();
                else isLastPage = true;
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
        Log.d("SHOW DATA", "loadHOuseNextPage");
        callElectronicsApi().enqueue(new Callback<ElectronicsModel>() {
            @Override
            public void onResponse(Call<ElectronicsModel> call, Response<ElectronicsModel> response) {
                electronicAdapter.removeLoadingFooter();
                isLoading = false;
                List<ElectronicResult> results = fetchElectronicResults(response);
                electronicAdapter.addAll(results);
                if (currentPage != TOTAL_PAGES) electronicAdapter.addLoadingFooter();
                else isLastPage = true;
            }
            @Override
            public void onFailure(Call<ElectronicsModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


    private void loadHouseFirstPage() {
        Log.d("SHOW DATA", "loadHouseFirstPage");
        callHousesApi().enqueue(new Callback<HousesModel>() {
            @Override
            public void onResponse(Call<HousesModel> call, Response<HousesModel> response) {
                progressBar.setVisibility(View.GONE);
                List<HouseResult> houseresults = fetchHouseResults(response);
                houseAdapter.addAll(houseresults);
                if (currentPage <= TOTAL_PAGES) {
                    houseAdapter.addLoadingFooter();
                } else {
                    isLastPage = true;
                }
            }
            @Override
            public void onFailure(Call<HousesModel> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }
    private List<HouseResult> fetchHouseResults(Response<HousesModel> response) {
        Log.d("SHOW DATA", "fetchHouseResults");
        HousesModel houses = response.body();
        return houses.getResults();
    }
    private void loadHouseNextPage() {
        Log.d("SHOW DATA", "loadHOuseNextPage");
        callHousesApi().enqueue(new Callback<HousesModel>() {
            @Override
            public void onResponse(Call<HousesModel> call, Response<HousesModel> response) {
                houseAdapter.removeLoadingFooter();
                isLoading = false;
                List<HouseResult> houseresults = fetchHouseResults(response);
                Log.d("SHOW DATA", houseresults.toString());
                houseAdapter.addAll(houseresults);
                if (currentPage != TOTAL_PAGES) houseAdapter.addLoadingFooter();
                else isLastPage = true;
            }

            @Override
            public void onFailure(Call<HousesModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void loadFirstPage() {
        Log.d(TAG, "car firts page loading");

        callCarsApi().enqueue(new Callback<CarsModel>() {
            @Override
            public void onResponse(Call<CarsModel> call, Response<CarsModel> response) {
                progressBar.setVisibility(View.GONE);
                List<CarResult> results = fetchResults(response);
                carAdapter.addAll(results);
                if (currentPage <= TOTAL_PAGES) carAdapter.addLoadingFooter();
                else isLastPage = true;
            }
            @Override
            public void onFailure(Call<CarsModel> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }
    private List<CarResult> fetchResults(Response<CarsModel> response) {
        Log.d(TAG, "car fetch data loading");
        CarsModel cars = response.body();
        return cars.getResults();
    }
    private void loadNextPage() {
        Log.d(TAG, "car next page loading");
        callCarsApi().enqueue(new Callback<CarsModel>() {
            @Override
            public void onResponse(Call<CarsModel> call, Response<CarsModel> response) {
                carAdapter.removeLoadingFooter();
                isLoading = false;
                List<CarResult> results = fetchResults(response);
                carAdapter.addAll(results);
                if (currentPage != TOTAL_PAGES) carAdapter.addLoadingFooter();
                else isLastPage = true;
            }

            @Override
            public void onFailure(Call<CarsModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


    private Call<CarsModel> callCarsApi() {
        Log.d(TAG, "calling CAR API method");
        return adsfinderService.getCars(currentPage * 24);
    }
    private Call<HousesModel> callHousesApi() { return adsfinderService.getHouses(currentPage * 24); }
    private Call<ElectronicsModel> callElectronicsApi() { return adsfinderService.getElectronics(currentPage * 24); }
}
