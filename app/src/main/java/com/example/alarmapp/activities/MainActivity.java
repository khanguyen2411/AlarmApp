package com.example.alarmapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.alarmapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupBottomNav();
    }

    // thiết lập bottomNavigationView với NavController
    void setupBottomNav(){
        bottomNavigationView = findViewById(R.id.bottom_nav_view);
        //đặt các icon về màu mặc định
        bottomNavigationView.setItemIconTintList(null);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        // thiết lập actionbar tương ứng với từng fragment
        appBarConfiguration = new AppBarConfiguration.Builder(
            R.id.alarmFragment, R.id.clockFragment, R.id.stopwatchFragment, R.id.countdownFragment
        ).build();

        // thiết lập bottom nav map với app bar
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
    }

    // tạo menu ở action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);
        return true;
    }

    //xử lý khi nhấn vào menu
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.appInfo:
                Toast.makeText(getApplicationContext(), "Thực hiện bởi Anomyous", Toast.LENGTH_LONG).show();
                break;
            case R.id.exitApp:
                finish();
        }

        return super.onOptionsItemSelected(item);
    }
}