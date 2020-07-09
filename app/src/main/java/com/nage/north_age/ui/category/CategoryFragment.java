package com.nage.north_age.ui.category;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.nage.north_age.R;
import com.nage.north_age.adapters.CategoryAdapter;
import com.nage.north_age.views.MainActivity;

import java.util.List;

import me.gilo.woodroid.models.Category;

public class CategoryFragment extends Fragment implements CategoryAdapter.OnCategoryListener {

    private static final String ARG_CATEGORY_ID = "categoryID";

    private CategoryViewModel mViewModel;
    private int categoryID;

    LottieAnimationView av_splash_animation;
    RecyclerView rcyCategories;
    CategoryAdapter categoryAdapter;
    List<Category> currentCategories;

    public CategoryFragment() {
        // Required empty public constructor
    }

    public static CategoryFragment newInstance(String _categoryID) {
        CategoryFragment fragment = new CategoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CATEGORY_ID, _categoryID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String _categoryID = getArguments().getString(ARG_CATEGORY_ID);
            assert _categoryID != null;
            categoryID = Integer.parseInt(_categoryID);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        rcyCategories = view.findViewById(R.id.rcyCategories);
        av_splash_animation = view.findViewById(R.id.av_splash_animation);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        rcyCategories.setLayoutManager(mLayoutManager);
        categoryAdapter = new CategoryAdapter(this);
        rcyCategories.setAdapter(categoryAdapter);

        mViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);
        mViewModel.getCategories(categoryID).observe(getViewLifecycleOwner(), new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
                currentCategories = categories;
                categoryAdapter.setCategories(categories);
                av_splash_animation.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onCategoryClick(int position) {
        Category category = currentCategories.get(position);
        Bundle bundle = new Bundle();
        bundle.putString("categoryID", String.valueOf(category.getId()));
        ((MainActivity) getActivity()).loadFragment(R.id.nav_products, bundle);
    }
}