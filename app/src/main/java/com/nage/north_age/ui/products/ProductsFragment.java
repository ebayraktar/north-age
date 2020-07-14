package com.nage.north_age.ui.products;

import android.os.Bundle;
import android.util.Log;
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
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.nage.north_age.R;
import com.nage.north_age.adapters.ProductAdapter;
import com.nage.north_age.ui.productDetail.ProductDetailFragment;
import com.nage.north_age.views.MainActivity;

import java.util.List;
import java.util.Objects;

import me.gilo.woodroid.models.Product;

public class ProductsFragment extends Fragment implements ProductAdapter.OnProductListener {

    private static final String ARG_CATEGORY_ID = "categoryID";

    private ProductsViewModel mViewModel;
    private int categoryID;

    LottieAnimationView av_splash_animation;
    RecyclerView rcyProducts;
    ProductAdapter productAdapter;
    List<Product> currentProducts;


    public ProductsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param categoryID Parameter 1.
     * @return A new instance of fragment ProductsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductsFragment newInstance(String categoryID) {
        ProductsFragment fragment = new ProductsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CATEGORY_ID, categoryID);
        fragment.setArguments(args);
        return fragment;
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

        mViewModel = ViewModelProviders.of(this).get(ProductsViewModel.class);
        mViewModel.getProducts(categoryID).observe(getViewLifecycleOwner(), new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                currentProducts = products;
                productAdapter.setProducts(products);
                av_splash_animation.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onProductClick(int position) {
        Product product = currentProducts.get(position);
        Bundle bundle = new Bundle();
        bundle.putString("productID", String.valueOf(product.getId()));
        ProductDetailFragment fragment = new ProductDetailFragment();
        fragment.setArguments(bundle);
        ((MainActivity) getActivity()).addFragment(fragment);
    }
}