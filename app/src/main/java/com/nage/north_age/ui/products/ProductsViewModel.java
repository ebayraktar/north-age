package com.nage.north_age.ui.products;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.gilo.woodroid.models.Product;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.nage.north_age.App.woocommerce;

public class ProductsViewModel extends ViewModel {
    private MutableLiveData<List<Product>> mData;

    public ProductsViewModel() {
        mData = new MutableLiveData<>();
    }

    public LiveData<List<Product>> getProducts() {
        Map<String, String> filter = new HashMap<>();
        filter.put("category", "469");
        woocommerce.ProductRepository().filter(filter).enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                List<Product> categoriesResponse = response.body();
                if (categoriesResponse != null)
                    mData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {

            }
        });
        return mData;
    }
//    public LiveData<List<Product>> getProducts(int[] ids){
//
//    }
}