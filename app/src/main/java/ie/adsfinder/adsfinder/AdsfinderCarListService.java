package ie.adsfinder.adsfinder;

import com.google.gson.JsonObject;

import ie.adsfinder.adsfinder.api.CarForm;
import ie.adsfinder.adsfinder.api.CarResult;
import ie.adsfinder.adsfinder.api.CarsModel;
import ie.adsfinder.adsfinder.api.DetailResult;
import ie.adsfinder.adsfinder.api.ElectronicResult;
import ie.adsfinder.adsfinder.api.ElectronicsModel;
import ie.adsfinder.adsfinder.api.HouseResult;
import ie.adsfinder.adsfinder.api.HousesModel;
import ie.adsfinder.adsfinder.api.HuseMapModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AdsfinderCarListService {

    @GET("api/v1/cars/car-list")
    Call<CarsModel> getCars(
            @Query("offset") int pageIndex
    );
    @GET("api/v1/houses/house-list")
    Call<HousesModel> getHouses(
            @Query("offset") int pageIndex
    );
    @GET("api/v1/electronics/electronic-list")
    Call<ElectronicsModel> getElectronics(
            @Query("offset") int pageIndex
    );

    @GET("api/v1/houses/houses_map_view")
    Call<HuseMapModel> getMapHouses();

    @GET("api/v1/houses/house-detail/{id}")
    Call<HouseResult> getMapHouseById(@Path("id") Integer id);

    @GET("api/v1/houses/house-detail/{id}")
    Call<DetailResult> getHouseById(@Path("id") Integer id);

    @GET("api/v1/cars/car-detail/{id}")
    Call<DetailResult> getCarById(@Path("id") Integer id);

    @GET("api/v1/electronics/electronic-detail/{id}")
    Call<DetailResult> getElectronicById(@Path("id") Integer id);

    @GET("api/v1/forms/carformdata")
    Call<CarForm> getCarFilterFormData();

    @GET("api/v1/forms/countyarea")
    Call<JsonObject> getLocationFormData();

}