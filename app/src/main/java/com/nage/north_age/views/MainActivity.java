package com.nage.north_age.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.nage.north_age.BaseActivity;
import com.nage.north_age.R;
import com.nage.north_age.ui.about.AboutFragment;
import com.nage.north_age.ui.category.CategoryFragment;
import com.nage.north_age.ui.contact.ContactFragment;
import com.nage.north_age.ui.home.HomeFragment;

import java.util.List;

import me.gilo.woodroid.models.Category;
import me.gilo.woodroid.models.filters.ProductCategoryFilter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.nage.north_age.App.woocommerce;

public class MainActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener, NavigationView.OnNavigationItemSelectedListener {

    static String TAG = "TAG";
    SearchView svMenu;
    DrawerLayout drawer;
    NavigationView navigationView;
    HomeFragment homeFragment;
    AboutFragment aboutFragment;
    ContactFragment contactFragment;
    Fragment currentFragment;
    Toolbar toolbar;

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                if (getSupportFragmentManager().getBackStackEntryCount() == 1)
                    setSearchVisible(false);
                onBackPressed(false);
            } else {
                onBackPressed(true);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigationView = findViewById(R.id.nav_view);
        drawer = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);

        navigationView.setNavigationItemSelectedListener(this);
        toggle.syncState();

        if (savedInstanceState == null) {
            homeFragment = new HomeFragment();
            aboutFragment = new AboutFragment();
            contactFragment = new ContactFragment();
            currentFragment = homeFragment;
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.nav_host_fragment, homeFragment)
                    .add(R.id.nav_host_fragment, aboutFragment)
                    .add(R.id.nav_host_fragment, contactFragment)
                    .hide(aboutFragment)
                    .hide(contactFragment)
                    .commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
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
                            addMenuItem(category.getName(), category.getId());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {

            }
        });
        //setSearchVisible(false);
    }

    public void addFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentManager manager = getSupportFragmentManager();
            String backStateName = fragment.getClass().getName();
            boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);
            if (!fragmentPopped && manager.findFragmentByTag(backStateName) == null) {
                FragmentTransaction ft = manager.beginTransaction();
                ft.replace(R.id.nav_host_fragment, fragment, backStateName);
                ft.addToBackStack(backStateName);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            } else {
                manager.popBackStack();
                addFragment(fragment);
            }
            setSearchVisible(true);
        }
    }

    void replaceFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentManager manager = getSupportFragmentManager();
            Log.d(TAG, "replaceFragment: " + manager.getBackStackEntryCount());
            if (manager.getBackStackEntryCount() > 0) {
                manager.popBackStack(manager.getBackStackEntryAt(0).getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
            FragmentTransaction ft = manager.beginTransaction();
            if (currentFragment != null) {
                ft.hide(currentFragment);
            }
            ft.show(fragment);
            ft.commit();
            currentFragment = fragment;
            setSearchVisible(false);
        }
    }


    void addMenuItem(String title, final int categoryID) {
        navigationView.getMenu().add(R.id.nav_categories, R.id.nav_exit, 0, title).setIcon(R.drawable.ic_outline_style_24).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Bundle bundle = new Bundle();
                bundle.putString("categoryID", String.valueOf(categoryID));
                CategoryFragment fragment = new CategoryFragment();
                fragment.setArguments(bundle);
                addFragment(fragment);
                navigationView.setCheckedItem(item.getItemId());
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    void exit() {
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }

    void setSearchVisible(boolean visible) {
        //if (toolbar.getMenu().findItem(R.id.action_search) != null)
        toolbar.getMenu().findItem(R.id.action_search).setVisible(visible);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        final MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
        svMenu = (SearchView) myActionMenuItem.getActionView();
        svMenu.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Toast like print
                Log.d(TAG, "onQueryTextSubmit: " + query);
                //UserFeedback.show( "SearchOnQueryTextSubmit: " + query);
                if (!svMenu.isIconified()) {
                    svMenu.setIconified(true);
                }
                myActionMenuItem.collapseActionView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                // UserFeedback.show( "SearchOnQueryTextChanged: " + s);
                return false;
            }
        });

        return true;
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_login:
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                break;
            case R.id.action_settings:

                break;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.nav_home:
                fragment = homeFragment;
                break;
            case R.id.nav_about:
                fragment = aboutFragment;
                break;
            case R.id.nav_contact:
                fragment = contactFragment;
                break;
            case R.id.nav_exit:
                exit();
                break;
            default:
                fragment = null;
                break;
        }

        replaceFragment(fragment);
        drawer.closeDrawer(GravityCompat.START);
        navigationView.setCheckedItem(item.getItemId());
        return true;
    }
}