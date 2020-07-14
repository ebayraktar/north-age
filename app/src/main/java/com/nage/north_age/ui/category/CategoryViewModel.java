package com.nage.north_age.ui.category;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import me.gilo.woodroid.models.Category;
import me.gilo.woodroid.models.filters.ProductCategoryFilter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.nage.north_age.App.woocommerce;

public class CategoryViewModel extends ViewModel {

    private MutableLiveData<List<Category>> mData;

    public CategoryViewModel() {
        mData = new MutableLiveData<>();
    }

    public LiveData<List<Category>> getCategories(int parentID) {
        ProductCategoryFilter productCategoryFilter = new ProductCategoryFilter();
        int[] parents = new int[1];
        parents[0] = parentID;
        productCategoryFilter.setParent(parents);
        productCategoryFilter.setPer_page(100);
        woocommerce.CategoryRepository().categories(productCategoryFilter).enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.body() != null)
                    mData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {

            }
        });
        return mData;
    }
}