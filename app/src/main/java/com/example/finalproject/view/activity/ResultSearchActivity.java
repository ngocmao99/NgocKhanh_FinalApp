package com.example.finalproject.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.finalproject.R;
import com.example.finalproject.databinding.ActivityResultSearchBinding;
import com.example.finalproject.models.Property;
import com.example.finalproject.view.adapter.PropertyAdapter;

import java.util.ArrayList;

public class ResultSearchActivity extends AppCompatActivity implements PropertyAdapter.OnItemClickListener{

    private ActivityResultSearchBinding binding;

    private ArrayList<Property> resultsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResultSearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        resultsList = new ArrayList<>();

        Bundle dataReceive = getIntent().getExtras();
        if(dataReceive != null){
            resultsList = dataReceive.getParcelableArrayList("ResultList");
            int numOfResults = resultsList.size();
            if(numOfResults != 0){

                binding.tvNotification.setVisibility(View.GONE);

                binding.results.setVisibility(View.VISIBLE);

                binding.results.setHasFixedSize(true);

                PropertyAdapter adapterResult = new PropertyAdapter(ResultSearchActivity.this,resultsList,this);

                binding.results.setAdapter(adapterResult);
            }
            else {
                binding.tvNotification.setText(getString(R.string.txt_not_have_fit_item));
                binding.tvNotification.setVisibility(View.VISIBLE);
                binding.results.setVisibility(View.INVISIBLE);
            }


        }
        else {

        }
    }

    @Override
    public void onClickGoToDetailProperty(Property property) {

    }

    @Override
    public void onClickEditProperty(Property property) {

    }

    @Override
    public void onClickRemoveProperty(Property property) {

    }
}