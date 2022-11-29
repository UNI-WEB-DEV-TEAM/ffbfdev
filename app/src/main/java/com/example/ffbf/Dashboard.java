package com.example.ffbf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class Dashboard extends AppCompatActivity {

    //Declare the active imposters

    Button viewRestaurant, viewStreetFood, viewCatering;
    String node = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        setContentView(R.layout.activity_dashboard);

        viewRestaurant = findViewById(R.id.view_restaurants);
        viewStreetFood = findViewById(R.id.view_street_food);
        viewCatering = findViewById(R.id.view_catering);


        viewRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                node = "Restaurant";
                Intent i = new Intent(Dashboard.this, EateryEntries.class);
                i.putExtra("type", node);
                startActivity(i);
            }
        });

        viewStreetFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                node = "Street_Food";
                Intent i = new Intent(Dashboard.this, EateryEntries.class);
                i.putExtra("type", node);
                startActivity(i);
            }
        });

        viewCatering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                node = "Catering";
                Intent i = new Intent(Dashboard.this, EateryEntries.class);
                i.putExtra("this", node);
                startActivity(i);

            }
        });


    }
    //Display the navigation menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_nav, menu);
        return true;
    }
    //Set the paths to every activity inside the navigation
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.dashboard:
                startActivity(new Intent(this, Dashboard.class));
                break;
            case R.id.profile:
                startActivity(new Intent(this, Profile.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}