package com.nage.north_age.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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
    DrawerLayout drawer;
    NavController navController;

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
                switch (item.getItemId()) {
                    case R.id.action_login:
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        break;
                }
                Log.d(TAG, "onMenuItemClick: " + item.getTitle());
                return false;
            }
        });

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_about, R.id.nav_contact, R.id.nav_products, R.id.nav_exit)
                .setDrawerLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initialize();
    }

    public void loadFragment(int navigateID, Bundle bundle) {
        Navigation.findNavController(this, R.id.nav_host_fragment).navigate(navigateID, bundle);
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
                            addMenuItem(category.getName(), category.getId());
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

    private void addMenuItem(String title, final int categoryID) {
        navigationView.getMenu().add(R.id.nav_categories, R.id.nav_exit, 0, title).setIcon(R.drawable.ic_outline_style_24).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Log.d(TAG, "onMenuItemClick: " + item.getTitle());
                Bundle bundle = new Bundle();
                bundle.putString("categoryID", String.valueOf(categoryID));
                loadFragment(R.id.nav_categories, bundle);
                drawer.close();
                return true;
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        hideKeyboard(this);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}