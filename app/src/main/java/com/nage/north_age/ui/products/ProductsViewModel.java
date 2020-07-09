package com.nage.north_age.ui.products;

import android.util.Log;

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

    public LiveData<List<Product>> getProducts(int categoryID) {
        Map<String, String> filter = new HashMap<>();
        filter.put("category", String.valueOf(categoryID));
        filter.put("per_page", "100");
        woocommerce.ProductRepository().filter(filter).enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.body() != null) {
                    mData.setValue(response.body());
                    for (String name : response.headers().names()) {
                        for (String value : response.headers().values(name)) {
                            Log.d("TAG", "onResponse: headerNames: " + name + " - Value:" + value);
                        }
                    }
                }
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