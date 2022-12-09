package com.example.finalproject.view.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.R;
import com.example.finalproject.models.Item;
import com.example.finalproject.models.Property;
import com.squareup.picasso.Picasso;

public class PropertyDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_detail);

        Bundle bundle = getIntent().getExtras();
        if(bundle == null){
            return;
        }
        Property property = (Property) bundle.get("object item");
        TextView tvNameItem = findViewById(R.id.title_detailP);
        tvNameItem.setText(property.getPropertyName());
        ImageView view = findViewById(R.id.propertyImageD);
        String imageUri=null;
        imageUri=property.getPropertyImage();
        Picasso.get().load(imageUri).into(view);

}
}