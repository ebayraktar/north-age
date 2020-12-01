package com.nage.north_age.ui.products;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.nage.north_age.models.NorthAgeModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import me.gilo.woodroid.models.Product;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.nage.north_age.App.woocommerce;

public class ProductsViewModel extends ViewModel {
    private MutableLiveData<List<Product>> mData;
    private MutableLiveData<NorthAgeModel> northAgeProducts;

    public ProductsViewModel() {
        mData = new MutableLiveData<>();
        northAgeProducts = new MutableLiveData<>();
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

    public LiveData<NorthAgeModel> getProducts(int categoryID, final int page, final int perPage) {
        final NorthAgeModel model = new NorthAgeModel();
        Map<String, String> filter = new HashMap<>();
        filter.put("category", String.valueOf(categoryID));
        filter.put("per_page", String.valueOf(perPage));
        filter.put("page", String.valueOf(page));
        woocommerce.ProductRepository().filter(filter).enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.body() != null) {
                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());
                    int totalData = Integer.parseInt(Objects.requireNonNull(response.headers().get("X-WP-Total")));
                    int totalPage = Integer.parseInt(Objects.requireNonNull(response.headers().get("X-WP-TotalPages")));
                    model.setTotalData(totalData);
                    model.setTotalPages(totalPage);
                    model.setObjectJson(json);
                    Log.d("TAG", "onResponse: page: " + page + " totalPage: " + totalPage);
                    if (page < totalPage)
                        model.setHasNext(true);
                    if (page != 1)
                        model.setHasPrev(true);
                    northAgeProducts.setValue(model);
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {

            }
        });
        return northAgeProducts;
    }
}