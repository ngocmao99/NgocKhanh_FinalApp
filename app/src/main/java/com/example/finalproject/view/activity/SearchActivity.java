package com.example.finalproject.view.activity;

import static com.example.finalproject.utils.Constants.LOCAL;
import static com.example.finalproject.utils.Constants.PATH_PROPERTIES;
import static com.example.finalproject.utils.Constants.PROVINCE_NAME;
import static com.example.finalproject.utils.Constants.TAG_DISTRICT;
import static com.example.finalproject.utils.Constants.TAG_PROVINCE;
import static com.example.finalproject.utils.Constants.TAG_TYPE;
import static com.example.finalproject.utils.Constants.TYPE_PATH;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.finalproject.R;
import com.example.finalproject.databinding.ActivitySearchBinding;
import com.example.finalproject.models.District;
import com.example.finalproject.models.Property;
import com.example.finalproject.models.PropertyType;
import com.example.finalproject.models.Province;
import com.google.android.material.slider.LabelFormatter;
import com.google.android.material.slider.RangeSlider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private ActivitySearchBinding binding;

    private DatabaseReference df;

    private ArrayList<Property> basicResults;

    private String uProvince;

    private String postalCode;
    private String uDistrict;

    RangeSlider rangeSlider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        df = FirebaseDatabase.getInstance().getReference();

        rangeSlider = findViewById(R.id.sliderBudget);
        rangeSlider.setLabelFormatter(new LabelFormatter() {
            @NonNull
            @Override
            public String getFormattedValue(float value) {
                NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
                currencyFormat.setCurrency(Currency.getInstance("USD"));
                return currencyFormat.format(value);
            }
        });
        handleTypeOption();
        handleProvinces();

        binding.Find.setOnClickListener(view -> {
            basicResults = new ArrayList<>();
            String Type = binding.propertyTypeFind.getText().toString().trim();
            String Province = binding.mactvProvince.getText().toString().trim();
            String District = binding.districtFind.getText().toString().trim();
            int Bath = Integer.parseInt(binding.bathRoom.getText().toString().trim());
            int Bed = Integer.parseInt(binding.bedRoom.getText().toString().trim());
            int Price = Integer.parseInt(binding.price.getText().toString().trim());
            if ((Province.isEmpty() || Type.isEmpty() || District.isEmpty() || Bath == 0 || Bed == 0 || Price == 0)) {


                Toast.makeText(view.getContext(), getString(R.string.txt_search_empty), Toast.LENGTH_SHORT).show();

            } else {

                searchAll(Province, Type, District, Bath, Bed, Price);

            }
            ;


        });
    }
    //Search multiple attributes
    private void searchAll(String Province, String Type, String District, int Bath, int Bed, int Price
    ) {
        df.child(PATH_PROPERTIES).orderByChild(LOCAL).equalTo(Province).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Property property = ds.getValue(Property.class);
                        if (property.getPropertyType().equals(Type) && property.getProvince().equals(Province) &&
                                property.getDistrict().equals(District) && property.getBathroom()==(Bath)
                                && property.getBedroom()==(Bed) && property.getPrice()==(Price)) {
                                basicResults.add(property);
                        }
                    }

                    navigateToResult(basicResults);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    private void navigateToResult(ArrayList<Property> pArrayList) {
        Intent intent = new Intent(SearchActivity.this, ResultSearchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Bundle dataSend = new Bundle();
        dataSend.putParcelableArrayList("ResultList", pArrayList);
        intent.putExtras(dataSend);
        startActivity(intent);
        Animatoo.animateSlideLeft(SearchActivity.this);
    }
    //drop-down menu - Provinces
    private void handleProvinces() {
        List<String> provincesName = new ArrayList<>();
        df.child(LOCAL).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Log.d(TAG_PROVINCE, "getProvinceList: data was found ");
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Province mProvince;
                        mProvince = ds.getValue(Province.class);
                        provincesName.add(mProvince.getName());
                    }
                    String province = binding.mactvProvince.getText().toString().trim();
                    ArrayAdapter<String> provinceAdapter = new ArrayAdapter<>(SearchActivity.this, R.layout.item_dropdownlist, provincesName);
                    binding.mactvProvince.setAdapter(provinceAdapter);
                    if(!province.isEmpty()){
                        // if province field doesn't empty => get the value and push value to method handleDistrict()
                        handleDistrict(province);
                    }
                    else{
                        binding.mactvProvince.setOnItemClickListener((adapterView, view, i, l) -> {
                            String selectedItem = adapterView.getItemAtPosition(i).toString();
                            uProvince = selectedItem;
                            binding.mactvProvince.getText().clear();
                            handleDistrict(selectedItem);

                        });
                    }
                } else {
                    Log.d(TAG_PROVINCE, "getProvinceList: data not found ");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    //drop-down menu - District
    private void handleDistrict(String selectedProvince) {
        df.child(LOCAL).orderByChild(PROVINCE_NAME).equalTo(selectedProvince).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    Log.d(TAG_PROVINCE, "handleDistrict: getProvinceCode: No data found");
                } else {
                    Log.d(TAG_PROVINCE, "handleDistrict: getProvinceCode: Data was found ");
                    for (DataSnapshot mDs : snapshot.getChildren()) {
                        Province sProvince = mDs.getValue(Province.class);
                        String provinceId = sProvince.getCode();
                        postalCode = sProvince.getZipCode();
                        getDistrictsList(provinceId);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getDistrictsList(String code) {
        List<String> districtNameList = new ArrayList<>();
        df.child("Districts").orderByChild("provinceId").equalTo(code).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    Log.d(TAG_DISTRICT, "getDistrict: No data found");
                } else {
                    Log.d(TAG_DISTRICT, "getDistricts: Data was found");
                    for (DataSnapshot mSnapshot : snapshot.getChildren()) {
                        District sDistrict = mSnapshot.getValue(District.class);
                        String name = sDistrict.getName();
                        districtNameList.add(name);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Districts","getDistrictList: was not found");
            }
        });
        //setup drop-down menu for mact district
        ArrayAdapter<String> districtAdapter = new ArrayAdapter<>(this, R.layout.item_dropdownlist, districtNameList);
        binding.districtFind.setAdapter(districtAdapter);
        binding.districtFind.setThreshold(1);
        binding.districtFind.setOnItemClickListener((adapterView, view, i, l) -> {
            uDistrict = adapterView.getItemAtPosition(i).toString();

        });
    }
    private void handleTypeOption() {
        //declare a list String to contain type name
        List<String> types = new ArrayList<>();
        //using database references and access to propertyTypes path on firebase and get data
        df.child(TYPE_PATH).addValueEventListener(new ValueEventListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Log.d(TAG_TYPE,"getTypes: data was found");
                    for (DataSnapshot ds : snapshot.getChildren()){
                        //declare a variable has type is PropertyType
                        PropertyType type;
                        //assign the data that get from firebase to type variable and wrap to PropertyType.class
                        type = ds.getValue(PropertyType.class);
                        //add item to types list that initialized
                        types.add(type.getTypeName());
                    }
                    //declare a array adapter and assign the types to it
                    ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(SearchActivity.this,R.layout.item_dropdownlist,types);
                    //set rounded background for drop down list
                    binding.propertyTypeFind.setDropDownBackgroundDrawable(getDrawable(R.drawable.dropdownbg));
                    binding.propertyTypeFind.setAdapter(typeAdapter);
                    binding.propertyTypeFind.setThreshold(1);
                }else {
                    Log.d(TAG_TYPE,"Snapshot doesn't exists");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}