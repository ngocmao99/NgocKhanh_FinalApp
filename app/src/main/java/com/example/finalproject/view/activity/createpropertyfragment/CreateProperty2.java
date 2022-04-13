package com.example.finalproject.view.activity.createpropertyfragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.finalproject.R;
import com.example.finalproject.base.BaseFragment;
import com.example.finalproject.databinding.CreateProperty2Binding;


public class CreateProperty2 extends BaseFragment {

    private CreateProperty2Binding binding;
    public CreateProperty2() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = CreateProperty2Binding.inflate(inflater,container,false);

        View rootView = binding.getRoot();

        return rootView;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String[] cities = getResources().getStringArray(R.array.city_array);
        ArrayAdapter<String> provinceAdapter = new ArrayAdapter<>(getActivity(),R.layout.item_dropdownlist,cities);
        binding.province.setAdapter(provinceAdapter);
        binding.province.setThreshold(1);



    binding.province.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String item = parent.getItemAtPosition(position).toString();
            Toast.makeText(getContext(), "Item"+item, Toast.LENGTH_SHORT).show();
        }
    });


    }

}
