package ie.adsfinder.adsfinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
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

public class ElectronicFilterActivity extends AppCompatActivity {
    private String TAG = "ElectronicFILTER ";
    private AdsfinderCarListService adsfinderService;
    //Selected values from form
    private List<String> selected_electronic_category_name;
    private List<String> selected_county;
    private List<String> selected_area;


    // values from from API call
    private JsonObject countyarea;
    private List<String> electronic_category_name;

    // View in current layout
    MultiSpinnerSearch searchArea;
    MultiSpinnerSearch searchCounty;
    MultiSpinnerSearch searchelEctronicCategoryName;
    CrystalRangeSeekbar price_spinner;
    TextView tvPriceMin;
    TextView tvPriceMax;
    private AutoCompleteTextView tvEditKeyword;

    List<String> allCategories = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electronic_filter);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        allCategories.add("audioequipment");
        allCategories.add("camcorders");
        allCategories.add("cameras");
        allCategories.add("computeraccessories");
        allCategories.add("dvds");
        allCategories.add("games");
        allCategories.add("laptops");
        allCategories.add("mp3players");
        allCategories.add("other");
        allCategories.add("otherelectronics");
        allCategories.add("pc");
        allCategories.add("phones");
        allCategories.add("printers");
        allCategories.add("screens");
        allCategories.add("tablets");
        allCategories.add("tv");
        List<KeyPairBoolData> fcategory_names = convertListToLBoolData(allCategories);

        adsfinderService = AdsfinderApi.getClient().create(AdsfinderCarListService.class);
        selected_electronic_category_name = new ArrayList<>();
        selected_county = new ArrayList<>();
        selected_area = new ArrayList<>();

        searchCounty = findViewById(R.id.searchCounty);
        searchArea = findViewById(R.id.searchArea);
        searchArea.setClickable(false);
        searchArea.setEnabled(false);
        searchelEctronicCategoryName = findViewById(R.id.searchCategoryName);


        price_spinner = findViewById(R.id.rangePrice);
        price_spinner.setMaxStartValue(5000);
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
        fillCategoryForm(fcategory_names);

        tvEditKeyword = findViewById(R.id.editSearchKeyword);

        List<String> allKeywords = new ArrayList<String>();
        allKeywords.add("audioequipment");
        allKeywords.add("camcorders");
        allKeywords.add("cameras");
        allKeywords.add("computeraccessories");
        allKeywords.add("dvds");
        allKeywords.add("games");
        allKeywords.add("laptops");
        allKeywords.add("mp3players");
        allKeywords.add("other");
        allKeywords.add("otherelectronics");
        allKeywords.add("pc");
        allKeywords.add("phones");
        allKeywords.add("printers");
        allKeywords.add("screens");
        allKeywords.add("tablets");
        allKeywords.add("tv");
        allKeywords.add("iphone");
        allKeywords.add("apple");
        allKeywords.add("sony");
        allKeywords.add("samsung");
        allKeywords.add("galaxy");
        allKeywords.add("new");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, allKeywords);
        tvEditKeyword.setAdapter(adapter);

        Button applyButton = findViewById(R.id.applyFilter);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent search_intent = new Intent(ElectronicFilterActivity.this, SearchResult.class);
                search_intent.putExtra("SearchType", "Electronics");
                search_intent.putStringArrayListExtra("county", new ArrayList<String>(new HashSet(selected_county)));
                search_intent.putStringArrayListExtra("area", (ArrayList<String>) selected_area);
                search_intent.putStringArrayListExtra("category_name", (ArrayList<String>) selected_electronic_category_name);
                search_intent.putExtra("min_price", price_spinner.getSelectedMinValue());
                search_intent.putExtra("max_price", price_spinner.getSelectedMaxValue());
                search_intent.putExtra("keyword", tvEditKeyword.getText().toString());
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
    private void fillCategoryForm(List<KeyPairBoolData> fcategory_names) {
        searchelEctronicCategoryName.setItems(fcategory_names, -1, new SpinnerListener() {
            @Override
            public void onItemsSelected(List<KeyPairBoolData> items) {
                selected_electronic_category_name.clear();
                for (int i = 0; i < items.size(); i++) {
                    if (items.get(i).isSelected()) {
                        selected_electronic_category_name.add(StringUtils.lowerCase(items.get(i).getName()));
                    }
                }
            }
        });
    }


    private Call<JsonObject> callLocationFormData() { return adsfinderService.getLocationFormData(); }


}
