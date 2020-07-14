package com.nage.north_age.ui.productDetail;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import static com.nage.north_age.App.woocommerce;

import me.gilo.woodroid.models.Product;
import me.gilo.woodroid.models.ProductReview;
import me.gilo.woodroid.models.filters.ProductReviewFilter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailViewModel extends ViewModel {
    private MutableLiveData<Product> productLiveData;
    private MutableLiveData<List<ProductReview>> reviewLiveData;
    // TODO: Implement the ViewModel

    public ProductDetailViewModel() {
        productLiveData = new MutableLiveData<>();
        reviewLiveData = new MutableLiveData<>();
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

    public LiveData<List<ProductReview>> getProductReviews(int id) {
        int[] productID = new int[1];
        productID[0] = id;
        ProductReviewFilter filter = new ProductReviewFilter();
        filter.setProduct(productID);
        woocommerce.ReviewRepository().reviews(filter).enqueue(new Callback<List<ProductReview>>() {
            @Override
            public void onResponse(Call<List<ProductReview>> call, Response<List<ProductReview>> response) {
                if (response.body() != null)
                    reviewLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<ProductReview>> call, Throwable t) {

            }
        });
        return reviewLiveData;
    }
}