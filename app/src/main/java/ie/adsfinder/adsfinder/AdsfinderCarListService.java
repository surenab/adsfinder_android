package ie.adsfinder.adsfinder;

import com.google.gson.JsonObject;

import java.util.List;

import ie.adsfinder.adsfinder.api.CarForm;
import ie.adsfinder.adsfinder.api.CarResult;
import ie.adsfinder.adsfinder.api.CarsModel;
import ie.adsfinder.adsfinder.api.DetailHouseResult;
import ie.adsfinder.adsfinder.api.DetailResult;
import ie.adsfinder.adsfinder.api.ElectronicResult;
import ie.adsfinder.adsfinder.api.ElectronicsModel;
import ie.adsfinder.adsfinder.api.HouseResult;
import ie.adsfinder.adsfinder.api.HousesModel;
import ie.adsfinder.adsfinder.api.HuseMapModel;
import ie.adsfinder.adsfinder.api.PropertyBerResult;
import ie.adsfinder.adsfinder.api.PropertyCategoryResult;
import ie.adsfinder.adsfinder.api.PropertyTypeResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AdsfinderCarListService {

    @GET("api/v1/cars/car-list")
    Call<CarsModel> getCars(
            @Query("offset") Integer pageIndex,
            @Query("min_price") Long min_price,
            @Query("max_price") Long max_price,
            @Query("min_firstregyear") Long min_firstregyear,
            @Query("max_firstregyear") Long max_firstregyear,
            @Query("min_kilometer") Long min_kilometer,
            @Query("max_kilometer") Long max_kilometer,
            @Query("min_enginesize") Float min_enginesize,
            @Query("max_enginesize") Float max_enginesize,
            @Query("fueltype") List<String> fueltype,
            @Query("transmission") String  transmission,
            @Query("model_name") List<String>  model_name,
            @Query("make_name") List<String>  make_name,
            @Query("county") List<String> county,
            @Query("area") List<String>  area,
            @Query("search") String search
    );
    @GET("api/v1/houses/house-list")
    Call<HousesModel> getHouses(
            @Query("offset") int pageIndex,
            @Query("county") List<String> county,
            @Query("area") List<String>  area,
            @Query("min_price") Long min_price,
            @Query("max_price") Long max_price,
            @Query("min_beds") Long min_beds,
            @Query("max_beds") Long max_beds,
            @Query("min_bathrooms") Long min_bathrooms,
            @Query("max_bathrooms") Long max_bathrooms,
            @Query("property_type") List<String>  property_type,
            @Query("ber_classification") List<String>  ber_classification,
            @Query("property_category") String property_category,
            @Query("search") String search
    );
    @GET("api/v1/electronics/electronic-list")
    Call<ElectronicsModel> getElectronics(
            @Query("offset") int pageIndex,
            @Query("min_price") Long min_price,
            @Query("max_price") Long max_price,
            @Query("electronic_category_name") List<String> electronic_category_name,
            @Query("county") List<String> county,
            @Query("area") List<String>  area,
            @Query("search") String  search
    );


    @GET("api/v1/cars/featuredcar-list")
    Call<CarsModel> getFeatureCars();
    @GET("api/v1/houses/featuredhouse-list")
    Call<HousesModel> getFeatureHouses();
    @GET("api/v1/electronics/featuredelectronic-list")
    Call<ElectronicsModel> getFeatureElectronics();


    @GET("api/v1/houses/houses_map_view")
    Call<HuseMapModel> getMapHouses(
            @Query("min_price") Long min_price,
            @Query("max_price") Long max_price,
            @Query("min_beds") Long min_beds,
            @Query("max_beds") Long max_beds,
            @Query("min_bathrooms") Long min_bathrooms,
            @Query("max_bathrooms") Long max_bathrooms,
            @Query("property_type") List<String>  property_type,
            @Query("property_category") String property_category
    );

    @GET("api/v1/houses/house-detail/{id}")
    Call<DetailHouseResult> getMapHouseById(@Path("id") Integer id);

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

    @GET("api/v1/forms/propertytypes")
    Call<PropertyTypeResult> getPropertyTypeFormData();

    @GET("api/v1/forms/propertycategories")
    Call<PropertyCategoryResult> getPropertyCategoryFormData();

    @GET("api/v1/forms/propertyber")
    Call<PropertyBerResult> getPropertyBerFormData();

}