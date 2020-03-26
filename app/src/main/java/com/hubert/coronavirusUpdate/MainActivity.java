package com.hubert.coronavirusUpdate;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    public static final String globalCases = "globalCases";
    public static final String globalDeaths = "globalDeaths";
    public static final String globalRecovered = "globalRecovered";
    public static final String currentCountryName = "currentCountryName";
    public static final String newCurrentCountryName = "newCurrentCountryName";
    public static final String currentCountryCases = "currentCountryCases";
    public static final String currentCountryTodayCases = "currentCountryTodayCases";
    public static final String currentCountryDeaths = "currentCountryDeaths";
    public static final String currentCountryTodayDeaths = "currentCountryTodayDeaths";
    public static final String currentCountryRecovered = "currentCountryRecovered";
    public static final String currentCountryActive = "currentCountryActive";
    public static final String currentCountryCritical = "currentCountryCritical";
    public static final String allCountryNames = "allCountryNames";
    public static final String currentListNeedUpdate = "currentListNeedUpdate";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_global, R.id.navigation_search, R.id.navigation_about)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

}
