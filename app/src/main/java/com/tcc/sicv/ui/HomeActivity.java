package com.tcc.sicv.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.tcc.sicv.R;
import com.tcc.sicv.base.BaseActivity;

public class HomeActivity extends BaseActivity {
    private Button purchaseButton;
    private Button myVehiclesButton;
    private Button maintenanceButton;
    private Button nfButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setupToolbar(R.id.main_toolbar,R.string.app_name,false);
        setupViews();
        creatingObservers();
    }

    private void creatingObservers() {
        purchaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, BuyVehiclesActivity.class));
            }
        });

        myVehiclesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, MyVehiclesActivity.class));
            }
        });

        maintenanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, MaintenanceActivity.class));
            }
        });

        nfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, MyTicketsActivity.class));
            }
        });
    }

    private void setupViews() {
        purchaseButton = findViewById(R.id.purchaseButton);
        myVehiclesButton = findViewById(R.id.vehiclesButton);
        maintenanceButton = findViewById(R.id.maintenanceButton);
        nfButton = findViewById(R.id.nfButton);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_profile:
                startActivity(new Intent(HomeActivity.this,ProfileActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
