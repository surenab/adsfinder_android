package ie.adsfinder.adsfinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.androidbuts.multispinnerfilter.KeyPairBoolData;
import com.androidbuts.multispinnerfilter.MultiSpinnerSearch;
import com.androidbuts.multispinnerfilter.SpinnerListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ie.adsfinder.adsfinder.api.CarForm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CarFilterActivity extends AppCompatActivity {
    private String TAG = "CARFILTER ";
    private AdsfinderCarListService adsfinderService;
    //Selected values from form
    private List<String> selected_fueltypes;
    private List<String> selected_county;
    private List<String> selected_area;
    private List<String> selected_makes;
    private List<String> selected_models;
    private String selected_transmissions;
    private String selected_conditions;
    private Integer seleceted_price_from;
    private Integer seleceted_price_to;
    private Integer seleceted_mileage_to;
    private Integer seleceted_mileage_from;
    private Integer seleceted_enginesize_to;
    private Integer seleceted_enginesize_from;
    private Integer seleceted_year_to;
    private Integer seleceted_year_from;




    // values from from API call
    private JsonObject countyarea;
    private JsonObject makemodels;
    private List<String> transmissions;
    private List<String> conditions;
    private List<String> fueltypes;

    // View in current layout
    MultiSpinnerSearch searchMake;
    MultiSpinnerSearch searchArea;
    MultiSpinnerSearch searchModel;
    MultiSpinnerSearch searchCounty;
    MultiSpinnerSearch searchFuelType;
    CrystalRangeSeekbar year_spinner;
    CrystalRangeSeekbar mileage_spinner;
    CrystalRangeSeekbar enginesize_spinner;
    CrystalRangeSeekbar price_spinner;
    RadioGroup transmission_radioGroup;
    RadioGroup condition_radioGroup;
    TextView tvPriceMin;
    TextView tvPriceMax;
    TextView tvYearMin;
    TextView tvYearMax;
    TextView tvEngineSizeMin;
    TextView tvEngineSizeMax;
    TextView tvMIleageMin;
    TextView tvMIleageMax;
    TextView clickTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_filter);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);


        adsfinderService = AdsfinderApi.getClient().create(AdsfinderCarListService.class);
        selected_makes = new ArrayList<>();
        selected_models = new ArrayList<>();
        selected_fueltypes = new ArrayList<>();
        selected_county = new ArrayList<>();
        selected_area = new ArrayList<>();

        searchCounty = findViewById(R.id.searchCounty);
        searchArea = findViewById(R.id.searchArea);
        searchArea.setClickable(false);
        searchArea.setEnabled(false);
        searchMake = findViewById(R.id.searchMake);
        searchMake.setColorSeparation(true);
        searchModel = findViewById(R.id.searchModel);
        searchModel.setClickable(false);
        searchModel.setEnabled(false);
        searchFuelType = findViewById(R.id.searchFuelType);


        price_spinner = findViewById(R.id.rangePrice);
        price_spinner.setMaxStartValue(60000);
        price_spinner.apply();
        tvPriceMin = findViewById(R.id.textPriceMin);
        tvPriceMax = findViewById(R.id.textPriceMax);
        price_spinner.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number number, Number number1) {
                tvPriceMin.setText(String.valueOf(number)+" €");
                tvPriceMax.setText(String.valueOf(number1)+" €");
            }
        });

        year_spinner = findViewById(R.id.rangeYear);
        year_spinner.setMaxStartValue(2020);
        year_spinner.setRight(2020);
        year_spinner.apply();
        tvYearMin = findViewById(R.id.textYearMin);
        tvYearMax = findViewById(R.id.textYearMax);
        year_spinner.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number number, Number number1) {
                tvYearMin.setText(String.valueOf(number));
                tvYearMax.setText(String.valueOf(number1));
            }
        });

        mileage_spinner = findViewById(R.id.rangeMileage);
        mileage_spinner.setMaxStartValue(300000);
        mileage_spinner.apply();
        tvMIleageMin = findViewById(R.id.textMileageMin);
        tvMIleageMax = findViewById(R.id.textMileageMax);
        mileage_spinner.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number number, Number number1) {
                Integer minV = number.intValue()/1000;
                Integer maxV = number1.intValue()/1000;
                tvMIleageMin.setText(minV.toString()+"k KM");
                tvMIleageMax.setText(maxV.toString()+"k KM");
            }
        });
        enginesize_spinner = findViewById(R.id.range_enginesize);
        enginesize_spinner.setMaxStartValue(10);
        enginesize_spinner.apply();
        tvEngineSizeMin = findViewById(R.id.textEngineSizeMin);
        tvEngineSizeMax = findViewById(R.id.textEngineSizeMax);
        enginesize_spinner.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number number, Number number1) {
                tvEngineSizeMin.setText(String.valueOf(number)+"L");
                tvEngineSizeMax.setText(String.valueOf(number1)+"L");
            }
        });

        transmission_radioGroup = findViewById(R.id.transmission_group);
        condition_radioGroup = findViewById(R.id.condition_group);
        clickTextView = findViewById(R.id.textView14);



        callCarFilterFormData().enqueue(new Callback<CarForm>() {
            @Override
            public void onResponse(Call<CarForm> call, Response<CarForm> response) {
                CarForm result = response.body();
                makemodels = result.getMakemodel();
                transmissions = result.getTransmission();
                conditions = result.getCondition();
                fueltypes = result.getFueltype();

                Set<String> listM = makemodels.keySet();
                List<KeyPairBoolData> listMakes = convertSetToLBoolData(listM);
                fillCarMakeForm(listMakes);

                List<KeyPairBoolData> tfueltypes = convertListToLBoolData(fueltypes);
                fillTransFuelCondForm(tfueltypes);
            }

            @Override
            public void onFailure(Call<CarForm> call, Throwable t) {
                t.printStackTrace();
            }
        });

        callLocationFormData().enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                countyarea = response.body();
                Set<String> listC = countyarea.keySet();
                List<KeyPairBoolData> listCounties = convertSetToLBoolData(listC);
                fillCountyForm(listCounties);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
            }
        });

        Button applyButton = findViewById(R.id.applyFilter);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = transmission_radioGroup.getCheckedRadioButtonId();
                RadioButton transmission_radioButton = findViewById(selectedId);
                int selectedId_condition = condition_radioGroup.getCheckedRadioButtonId();
                RadioButton condition_radioButton = findViewById(selectedId_condition);

                Intent search_intent = new Intent(CarFilterActivity.this, SearchResult.class);
                search_intent.putExtra("SearchType", "Cars");
                search_intent.putStringArrayListExtra("county", new ArrayList<String>(new HashSet(selected_county)));
                search_intent.putStringArrayListExtra("area", (ArrayList<String>) selected_area);
                search_intent.putStringArrayListExtra("fueltype", (ArrayList<String>) selected_fueltypes);
                search_intent.putStringArrayListExtra("model_name", (ArrayList<String>) selected_makes);
                search_intent.putStringArrayListExtra("make_name", new ArrayList<String>(new HashSet(selected_models)));

                search_intent.putExtra("min_price", price_spinner.getSelectedMinValue());
                search_intent.putExtra("max_price", price_spinner.getSelectedMaxValue());
                search_intent.putExtra("min_firstregyear", year_spinner.getSelectedMinValue());
                search_intent.putExtra("max_firstregyear", year_spinner.getSelectedMaxValue());
                search_intent.putExtra("min_kilometer", mileage_spinner.getSelectedMinValue());
                search_intent.putExtra("max_kilometer", mileage_spinner.getSelectedMaxValue());
                search_intent.putExtra("min_enginesize", enginesize_spinner.getSelectedMinValue());
                search_intent.putExtra("max_enginesize", enginesize_spinner.getSelectedMaxValue());
                if (transmission_radioButton.getText() == "Any") {
                    search_intent.putExtra("transmission", "");
                } else {
                    search_intent.putExtra("transmission", StringUtils.lowerCase(transmission_radioButton.getText().toString()));
                }
                if (condition_radioButton.getText() == "Any") {
                    search_intent.putExtra("condition", "");
                } else {
                    search_intent.putExtra("condition", StringUtils.lowerCase(condition_radioButton.getText().toString()));
                }
                startActivity(search_intent);
            }
        });

    }


    private List<KeyPairBoolData> convertListToLBoolData(List<String> list) {
        List<KeyPairBoolData> data = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            KeyPairBoolData h = new KeyPairBoolData();
            h.setId(i + 1);
            h.setName(StringUtils.capitalize(list.get(i)));
            h.setSelected(false);
            data.add(h);
        }
        return data;
    }
    private List<KeyPairBoolData> convertSetToLBoolData(Set<String> list) {
        List<KeyPairBoolData> data = new ArrayList<>();
        Integer i = 0;
        for (String s : list) {
            KeyPairBoolData h = new KeyPairBoolData();
            h.setId(i + 1);
            h.setName(StringUtils.capitalize(s));
            h.setSelected(false);
            data.add(h);
            i++;
        }
        return data;
    }

    private void fillCarMakeForm(final List<KeyPairBoolData> listMakes) {
        searchMake.clearAnimation();
        searchMake.setItems(listMakes, -1, new SpinnerListener() {
            @Override
            public void onItemsSelected(List<KeyPairBoolData> items) {
                selected_makes.clear();
                selected_models.clear();
                for (int i = 0; i < items.size(); i++) {
                    if (items.get(i).isSelected()) {
                        selected_makes.add(StringUtils.lowerCase(items.get(i).getName()));
                    }
                }
                if (!selected_makes.isEmpty()) {
                    searchModel.setClickable(true);
                    searchModel.setEnabled(true);
                } else {
                    searchModel.setClickable(false);
                    searchModel.setEnabled(false);
                }
                List<String> modelFillList = new ArrayList<>();
                for (int k = 0; k < selected_makes.size(); k++) {
                    JsonElement jelement = makemodels.get(StringUtils.lowerCase(selected_makes.get(k)));
                    JsonArray jelement_array = jelement.getAsJsonArray();
                    for (int j = 0; j < jelement_array.size(); j++) {
                        modelFillList.add(jelement_array.get(j).getAsJsonObject().get("name").getAsString());
                    }
                }
                if (!modelFillList.isEmpty()) {
                    fillCarModelForm(convertListToLBoolData(modelFillList));
                }
                searchMake.setEmptyTitle("Not Found Car manufacturer");

            }
        });
        searchMake.setLimit(3, new MultiSpinnerSearch.LimitExceedListener() {
            @Override
            public void onLimitListener(KeyPairBoolData data) {
                Toast.makeText(getApplicationContext(), "Limit exceed ", Toast.LENGTH_LONG).show();
            }
        });
    }
    private void fillCarModelForm(List<KeyPairBoolData> listModels) {
        searchModel.setClickable(true);
        searchModel.setEnabled(true);
        searchModel.setItems(listModels, -1, new SpinnerListener() {
            @Override
            public void onItemsSelected(List<KeyPairBoolData> items) {
                for (int i = 0; i < items.size(); i++) {
                    if (items.get(i).isSelected()) {
                        selected_models.add(StringUtils.lowerCase(items.get(i).getName()));
                    }
                }
            }
        });
    }
    private void fillCountyForm(final List<KeyPairBoolData> countyModel) {
        searchCounty.setItems(countyModel, -1, new SpinnerListener() {
            @Override
            public void onItemsSelected(List<KeyPairBoolData> items) {
                selected_county.clear();
                selected_area.clear();
                for (int i = 0; i < items.size(); i++) {
                    if (items.get(i).isSelected()) {
                        selected_county.add(StringUtils.lowerCase(items.get(i).getName()));
                    }
                }
                if (!selected_county.isEmpty()) {
                    searchArea.setClickable(true);
                    searchArea.setEnabled(true);
                } else {
                    searchArea.setClickable(false);
                    searchArea.setEnabled(false);
                }
                List<String> modelFillList = new ArrayList<>();
                for (int k = 0; k < selected_county.size(); k++) {
                    JsonElement jelement = countyarea.get(StringUtils.lowerCase(selected_county.get(k)));
                    JsonArray jelement_array = jelement.getAsJsonArray();
                    for (int j = 0; j < jelement_array.size(); j++) {
                        modelFillList.add(jelement_array.get(j).getAsJsonObject().get("town").getAsString());
                    }
                }
                if (!modelFillList.isEmpty()) {
                    fillAreaForm(convertListToLBoolData(modelFillList));
                }
            }
        });
    }
    private void fillAreaForm(List<KeyPairBoolData> areaModel) {
        searchArea.setClickable(true);
        searchArea.setEnabled(true);
        searchArea.setItems(areaModel, -1, new SpinnerListener() {
            @Override
            public void onItemsSelected(List<KeyPairBoolData> items) {
                for (int i = 0; i < items.size(); i++) {
                    if (items.get(i).isSelected()) {
                        selected_area.add(StringUtils.lowerCase(items.get(i).getName()));
                    }
                }
            }
        });
    }
    private void fillTransFuelCondForm(List<KeyPairBoolData> ffueltypes) {
        searchFuelType.setItems(ffueltypes, -1, new SpinnerListener() {
            @Override
            public void onItemsSelected(List<KeyPairBoolData> items) {
                selected_fueltypes.clear();
                for (int i = 0; i < items.size(); i++) {
                    if (items.get(i).isSelected()) {
                        selected_fueltypes.add(StringUtils.lowerCase(items.get(i).getName()));
                    }
                }
            }
        });
    }


    private Call<CarForm> callCarFilterFormData() { return adsfinderService.getCarFilterFormData(); }
    private Call<JsonObject> callLocationFormData() { return adsfinderService.getLocationFormData(); }


}
