package com.nage.north_age.ui.productDetail;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import static com.nage.north_age.App.woocommerce;

import me.gilo.woodroid.models.Product;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailViewModel extends ViewModel {
    private MutableLiveData<Product> productLiveData;
    // TODO: Implement the ViewModel

    public ProductDetailViewModel() {
        productLiveData = new MutableLiveData<>();
    }

    public LiveData<Product> getProductDetail(int id) {
        woocommerce.ProductRepository().product(id).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                Product product = response.body();
                if (product != null)
                    productLiveData.setValue(product);
                else
                    productLiveData.setValue(null);
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });
        return productLiveData;
    }
}