package com.nage.north_age.ui.products;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nage.north_age.R;
import com.nage.north_age.adapters.ProductAdapter;
import com.nage.north_age.models.NorthAgeModel;
import com.nage.north_age.ui.productDetail.ProductDetailFragment;
import com.nage.north_age.views.MainActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import me.gilo.woodroid.models.Product;

public class ProductsFragment extends Fragment implements ProductAdapter.OnProductListener, View.OnClickListener {

    private static final String ARG_CATEGORY_ID = "categoryID";

    private ProductsViewModel mViewModel;
    private int categoryID;

    LottieAnimationView av_splash_animation;
    RecyclerView rcyProducts;
    ProductAdapter productAdapter;
    List<Product> currentProducts;
    ImageView ivBack, ivForward, ivViewTwo, ivViewThree, ivFilter;
    Spinner spPerPage;
    TextView tvPage;
    int currentPage;
    int perPage;
    List<Integer> perPageList;

    public ProductsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        Log.d("TAG", "onCreateOptionsMenu: HEAD");
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
                productAdapter.getFilter().filter(s);
                return false;
            }
        });
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
        View view = inflater.inflate(R.layout.fragment_products, container, false);
        av_splash_animation = view.findViewById(R.id.av_splash_animation);
        rcyProducts = view.findViewById(R.id.rcyProducts);
        ivBack = view.findViewById(R.id.ivBack);
        tvPage = view.findViewById(R.id.tvPage);
        ivForward = view.findViewById(R.id.ivForward);
        ivViewTwo = view.findViewById(R.id.ivViewTwo);
        ivViewThree = view.findViewById(R.id.ivViewThree);
        ivFilter = view.findViewById(R.id.ivFilter);
        spPerPage = view.findViewById(R.id.spPerPage);
        Objects.requireNonNull(((MainActivity) getActivity())).setTitle(R.string.products);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("TAG", "onActivityCreated: " + categoryID);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        rcyProducts.setLayoutManager(mLayoutManager);
        productAdapter = new ProductAdapter(this);
        rcyProducts.setAdapter(productAdapter);
        currentPage = 1;
        perPage = 10;
        perPageList = new ArrayList<>();
        perPageList.add(10);
        perPageList.add(20);
        perPageList.add(50);
        perPageList.add(100);
        mViewModel = new ViewModelProvider(this).get(ProductsViewModel.class);
        ivBack.setOnClickListener(this);
        ivForward.setOnClickListener(this);
        ivViewThree.setOnClickListener(this);
        ivFilter.setOnClickListener(this);
        ivViewTwo.setOnClickListener(this);
        SpinnerAdapter adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, perPageList);
        spPerPage.setAdapter(adapter);
        spPerPage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                perPage = perPageList.get(position);
                currentPage = 1;
                getProducts();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        initialize();
    }

    void initialize() {
        mViewModel.getProducts(categoryID, currentPage, perPage).observe(getViewLifecycleOwner(), new Observer<NorthAgeModel>() {
            @Override
            public void onChanged(NorthAgeModel northAgeModel) {
                Gson gson = new Gson();
                List<Product> products = gson.fromJson(northAgeModel.getObjectJson(),
                        new TypeToken<List<Product>>() {
                        }.getType());
                currentProducts = products;
                productAdapter.setProducts(products);
                av_splash_animation.setVisibility(View.INVISIBLE);
                setPageControls(northAgeModel);
            }
        });
    }

    void getProducts() {
        av_splash_animation.setVisibility(View.VISIBLE);
        mViewModel.getProducts(categoryID, currentPage, perPage);
    }

    void setPageControls(NorthAgeModel northAgeModel) {
        if (northAgeModel.isHasNext()) {
            ivForward.setVisibility(View.VISIBLE);
        } else {
            ivForward.setVisibility(View.INVISIBLE);
        }

        if (northAgeModel.isHasPrev()) {
            ivBack.setVisibility(View.VISIBLE);
        } else {
            ivBack.setVisibility(View.INVISIBLE);
        }
        tvPage.setText(String.valueOf(currentPage));
    }

    @Override
    public void onProductClick(int position) {
        Product product = currentProducts.get(position);
        Bundle bundle = new Bundle();
        bundle.putString("productID", String.valueOf(product.getId()));
        ProductDetailFragment fragment = new ProductDetailFragment();
        fragment.setArguments(bundle);
        Objects.requireNonNull(((MainActivity) getActivity())).addFragment(fragment);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                currentPage--;
                getProducts();
                break;
            case R.id.ivForward:
                currentPage++;
                getProducts();
                break;
            case R.id.ivViewThree:
                RecyclerView.LayoutManager mThreeLayoutManager = new GridLayoutManager(getContext(), 3);
                rcyProducts.setLayoutManager(mThreeLayoutManager);
                break;
            case R.id.ivViewTwo:
                RecyclerView.LayoutManager mTwoLayoutManager = new GridLayoutManager(getContext(), 2);
                rcyProducts.setLayoutManager(mTwoLayoutManager);
                break;
            case R.id.ivFilter:

                break;
        }
    }
}