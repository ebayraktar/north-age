package com.nage.north_age.ui.category;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.nage.north_age.R;
import com.nage.north_age.adapters.CategoryAdapter;
import com.nage.north_age.ui.products.ProductsFragment;
import com.nage.north_age.views.MainActivity;

import java.util.List;
import java.util.Objects;

import me.gilo.woodroid.models.Category;

public class CategoryFragment extends Fragment implements CategoryAdapter.OnCategoryListener {

    private static final String ARG_CATEGORY_ID = "categoryID";

    private int categoryID;

    LottieAnimationView av_splash_animation;
    RecyclerView rcyCategories;
    CategoryAdapter categoryAdapter;
    List<Category> currentCategories;

    public CategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        //inflater.inflate(R.menu.main, menu);
        final MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
        myActionMenuItem.setVisible(true);
        final SearchView svMenu = (SearchView) myActionMenuItem.getActionView();

        svMenu.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Toast like print
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
                categoryAdapter.getFilter().filter(s);
                return false;
            }
        });
        //super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String _categoryID = getArguments().getString(ARG_CATEGORY_ID);
            assert _categoryID != null;
            categoryID = Integer.parseInt(_categoryID);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        rcyCategories = view.findViewById(R.id.rcyCategories);
        av_splash_animation = view.findViewById(R.id.av_splash_animation);
        Objects.requireNonNull(((MainActivity) getActivity())).setTitle(R.string.categories);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        rcyCategories.setLayoutManager(mLayoutManager);
        categoryAdapter = new CategoryAdapter(this);
        rcyCategories.setAdapter(categoryAdapter);
        //mViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);
        CategoryViewModel mViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        mViewModel.getCategories(categoryID).observe(getViewLifecycleOwner(), new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
                currentCategories = categories;
                Category allProducts = new Category();
                allProducts.setId(categoryID);
                allProducts.setName("Tüm ürünler");
                currentCategories.add(allProducts);
                categoryAdapter.setCategories(currentCategories);
                av_splash_animation.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onCategoryClick(int position) {
        Category category = currentCategories.get(position);
        Bundle bundle = new Bundle();
        bundle.putString("categoryID", String.valueOf(category.getId()));
        ProductsFragment fragment = new ProductsFragment();
        fragment.setArguments(bundle);
        Objects.requireNonNull((MainActivity) getActivity()).addFragment(fragment);
    }
}