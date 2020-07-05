package com.nage.north_age.views;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.nage.north_age.BaseActivity;
import com.nage.north_age.R;

import java.util.List;

import me.gilo.woodroid.models.Category;
import me.gilo.woodroid.models.filters.ProductCategoryFilter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.nage.north_age.App.woocommerce;

public class MainActivity extends BaseActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private NavigationView navigationView;

    static String TAG = "TAG";

    @Override
    public void onBackPressed() {
        onBackPressed(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Log.d(TAG, "onMenuItemClick: " + item.getTitle());
                return false;
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_about, R.id.nav_exit)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initialize();
    }

    void initialize() {
        ProductCategoryFilter productCategoryFilter = new ProductCategoryFilter();
        productCategoryFilter.setParent(new int[1]);
        woocommerce.CategoryRepository().categories(productCategoryFilter).enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                List<Category> categoriesResponse = response.body();
                if (categoriesResponse != null) {
                    for (Category category : categoriesResponse) {
                        if (category.getCount() > 0)
                            addMenuItem(category.getName() + " [" + category.getCount() + "]");
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void addMenuItem(String title) {
        //navigationView.getMenu().add(title);
        navigationView.getMenu().add(R.id.nav_categories, R.id.nav_exit, 0, title);
//        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//
//                Log.d(TAG, "onNavigationItemSelected: " + item.getTitle());
//                return false;
//            }
//        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        Log.d(TAG, "onSupportNavigateUp: ");
        hideKeyboard(this);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}