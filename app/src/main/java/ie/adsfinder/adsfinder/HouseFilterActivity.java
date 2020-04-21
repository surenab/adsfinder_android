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
import ie.adsfinder.adsfinder.api.PropertyBerResult;
import ie.adsfinder.adsfinder.api.PropertyCategoryResult;
import ie.adsfinder.adsfinder.api.PropertyTypeResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HouseFilterActivity extends AppCompatActivity {
    private String TAG = "HOUSEFILTER ";
    private AdsfinderCarListService adsfinderService;
    //Selected values from form
    private List<String> selected_propertytypes;
    private List<String> selected_county;
    private List<String> selected_area;
    private String selected_propertycategory;
    private List<String> selected_bers;




    // values from from API call
    private JsonObject countyarea;
    private List<String> propertytypes;
    private List<String> bers;

    // View in current layout
    RadioGroup searchPropertCategory;
    MultiSpinnerSearch searchArea;
    MultiSpinnerSearch searchPropertyTypes;
    MultiSpinnerSearch searchCounty;
    MultiSpinnerSearch searchBers;
    CrystalRangeSeekbar bed_spinner;
    CrystalRangeSeekbar bath_spinner;
    CrystalRangeSeekbar price_spinner;
    TextView tvPriceMin;
    TextView tvPriceMax;
    TextView tvBedMin;
    TextView tvBedMax;
    TextView tvBathMin;
    TextView tvBathMax;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_filter);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);


        adsfinderService = AdsfinderApi.getClient().create(AdsfinderCarListService.class);
        selected_propertytypes = new ArrayList<>();
        selected_bers = new ArrayList<>();
        selected_county = new ArrayList<>();
        selected_area = new ArrayList<>();

        searchCounty = findViewById(R.id.searchCounty);
        searchArea = findViewById(R.id.searchArea);
        searchArea.setClickable(false);
        searchArea.setEnabled(false);
        searchPropertCategory = findViewById(R.id.pcategory_group);
        searchPropertyTypes = findViewById(R.id.searchPropertyType);
        searchBers = findViewById(R.id.searchBer);


        price_spinner = findViewById(R.id.rangePrice);
        price_spinner.setMaxStartValue(1000000);
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

        bed_spinner = findViewById(R.id.rangeBeds);
        bed_spinner.setMaxStartValue(6);
        bed_spinner.apply();
        tvBedMin = findViewById(R.id.textBedMin);
        tvBedMax = findViewById(R.id.textBedMax);
        bed_spinner.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number number, Number number1) {
                tvBedMin.setText(String.valueOf(number));
                if (number1.intValue()==6) {
                    tvBedMax.setText("Max");
                } else {
                    tvBedMax.setText(String.valueOf(number1));
                }

            }
        });

        bath_spinner = findViewById(R.id.rangeBaths);
        bath_spinner.setMaxStartValue(5);
        bath_spinner.apply();
        tvBathMin = findViewById(R.id.textBathMin);
        tvBathMax = findViewById(R.id.textBathMax);
        bath_spinner.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number number, Number number1) {
                Integer minV = number.intValue();
                Integer maxV = number1.intValue();
                tvBathMin.setText(minV.toString());
                if (maxV==5) {
                    tvBathMax.setText("Max");
                } else {
                    tvBathMax.setText(maxV.toString());
                }
            }
        });

        RadioGroup radioGroup = findViewById(R.id.pcategory_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                if (checkedId==R.id.pcategory_sale) {
                    price_spinner.setMaxValue(1000000);
                    price_spinner.setSteps(1000);
                    price_spinner.setMaxStartValue(1000000);
                    price_spinner.apply();
                } else {
                    price_spinner.setMaxValue(4000);
                    price_spinner.setSteps(50);
                    price_spinner.setMaxStartValue(4000);
                    price_spinner.apply();
                }
            }
        });

        callPropertyTypeFormData().enqueue(new Callback<PropertyTypeResult>() {
            @Override
            public void onResponse(Call<PropertyTypeResult> call, Response<PropertyTypeResult> response) {
                PropertyTypeResult result = response.body();
                List<KeyPairBoolData> tpropertytypes = convertListToLBoolData(result.getProperty_types());
                fillPropertyTypeForm(tpropertytypes);
            }
            @Override
            public void onFailure(Call<PropertyTypeResult> call, Throwable t) {
                t.printStackTrace();
            }
        });

        callPropertyBerFormData().enqueue(new Callback<PropertyBerResult>() {
            @Override
            public void onResponse(Call<PropertyBerResult> call, Response<PropertyBerResult> response) {
                PropertyBerResult result = response.body();
                propertytypes = result.getBer_classification();
                List<KeyPairBoolData> tpropertycategories = convertListToLBoolData(propertytypes);
                fillBerForm(tpropertycategories);
            }
            @Override
            public void onFailure(Call<PropertyBerResult> call, Throwable t) {
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
                Intent search_intent = new Intent(HouseFilterActivity.this, SearchResult.class);
                search_intent.putExtra("SearchType", "Houses");
                search_intent.putStringArrayListExtra("county", new ArrayList<String>(new HashSet(selected_county)));
                search_intent.putStringArrayListExtra("area", (ArrayList<String>) selected_area);
                int selectedId = searchPropertCategory.getCheckedRadioButtonId();
                RadioButton pcategory_radioButton = findViewById(selectedId);
                selected_propertycategory = StringUtils.lowerCase(pcategory_radioButton.getText().toString());
                search_intent.putStringArrayListExtra("property_type", (ArrayList<String>) selected_propertytypes);
                search_intent.putExtra("property_category", selected_propertycategory);
                search_intent.putStringArrayListExtra("ber_classification", (ArrayList<String>) selected_bers);
                search_intent.putExtra("min_price", price_spinner.getSelectedMinValue());
                search_intent.putExtra("max_price", price_spinner.getSelectedMaxValue());
                search_intent.putExtra("min_beds", bed_spinner.getSelectedMinValue());
                search_intent.putExtra("max_beds", bed_spinner.getSelectedMaxValue());
                search_intent.putExtra("min_bathrooms", bath_spinner.getSelectedMinValue());
                search_intent.putExtra("max_bathrooms", bath_spinner.getSelectedMaxValue());
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
    private void fillPropertyTypeForm(List<KeyPairBoolData> fpropertytypes) {
        searchPropertyTypes.setItems(fpropertytypes, -1, new SpinnerListener() {
            @Override
            public void onItemsSelected(List<KeyPairBoolData> items) {
                selected_propertytypes.clear();
                for (int i = 0; i < items.size(); i++) {
                    if (items.get(i).isSelected()) {
                        selected_propertytypes.add(StringUtils.lowerCase(items.get(i).getName()));
                    }
                }
            }
        });
    }
    private void fillBerForm(List<KeyPairBoolData> fber) {
        searchBers.setItems(fber, -1, new SpinnerListener() {
            @Override
            public void onItemsSelected(List<KeyPairBoolData> items) {
                selected_bers.clear();
                for (int i = 0; i < items.size(); i++) {
                    if (items.get(i).isSelected()) {
                        selected_bers.add(StringUtils.lowerCase(items.get(i).getName()));
                    }
                }
            }
        });
    }


    private Call<JsonObject> callLocationFormData() { return adsfinderService.getLocationFormData(); }
    private Call<PropertyTypeResult> callPropertyTypeFormData() { return adsfinderService.getPropertyTypeFormData(); }
    private Call<PropertyBerResult> callPropertyBerFormData() { return adsfinderService.getPropertyBerFormData(); }

}
