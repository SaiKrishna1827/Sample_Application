package com.android.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;

public class Details extends AppCompatActivity {

    EditText first_name,last_name,email,category,description,price,availability;
    String first_name_str,last_name_str,email_str,category_str,description_str,price_str,availability_str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Details");

        first_name = (EditText) findViewById(R.id.first_name);
        last_name = (EditText) findViewById(R.id.last_name);
        email = (EditText) findViewById(R.id.email);
        category = (EditText) findViewById(R.id.category);
        description = (EditText) findViewById(R.id.description);
        price = (EditText) findViewById(R.id.price);
        availability = (EditText) findViewById(R.id.availability);

        Intent intent = getIntent();
        first_name_str = intent.getStringExtra("first_name");
        last_name_str = intent.getStringExtra("last_name");
        email_str = intent.getStringExtra("email");
        category_str = intent.getStringExtra("category");
        description_str = intent.getStringExtra("description");
        price_str = intent.getStringExtra("price");
        availability_str = intent.getStringExtra("availability");

        first_name.setText(first_name_str);
        last_name.setText(last_name_str);
        email.setText(email_str);
        category.setText(category_str);
        description.setText(description_str);
        price.setText(price_str);
        availability.setText(availability_str);


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                break;
            }
        }
        return true;
    }

}
