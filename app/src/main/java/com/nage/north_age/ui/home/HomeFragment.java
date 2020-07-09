package com.nage.north_age.ui.home;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.nage.north_age.R;
import com.nage.north_age.adapters.HomeSliderAdapter;
import com.nage.north_age.interfaces.SliderOnClickListener;
import com.nage.north_age.models.HomeButton;
import com.nage.north_age.models.SliderItem;
import com.nage.north_age.ui.productDetail.ProductDetailFragment;
import com.nage.north_age.views.MainActivity;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.List;

import me.gilo.woodroid.models.Product;

public class HomeFragment extends Fragment implements View.OnClickListener {

    HomeViewModel mViewModel;
    SearchView svHome;

    private static final String TAG = "TAG";
    SliderView imageSlider;
    HomeSliderAdapter homeSliderAdapter;
    LottieAnimationView av_splash_animation;
    ImageView ivBestSellers, ivTheNewest, ivCampaigns;
    TextView tvBestSeller, tvTheNewest, tvCampaigns;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        svHome = root.findViewById(R.id.svHome);
        imageSlider = root.findViewById(R.id.imageSlider);
        av_splash_animation = root.findViewById(R.id.av_splash_animation);
        ivBestSellers = root.findViewById(R.id.ivBestSellers);
        tvBestSeller = root.findViewById(R.id.tvBestSeller);
        ivTheNewest = root.findViewById(R.id.ivTheNewest);
        tvTheNewest = root.findViewById(R.id.tvTheNewest);
        ivCampaigns = root.findViewById(R.id.ivCampaigns);
        tvCampaigns = root.findViewById(R.id.tvCampaigns);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        svHome.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Arama Sayfası")
                        .setMessage(query + " aranıyor...")
                        .setPositiveButton("TAMAM", null)
                        .create().show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        initSlider();
        initViewModel();
        initButtons();
    }

    void initSlider() {
        homeSliderAdapter = new HomeSliderAdapter(getContext());
        imageSlider.setSliderAdapter(homeSliderAdapter);

        homeSliderAdapter.setOnClickListener(new SliderOnClickListener() {
            @Override
            public void OnClick(int ID) {
                Log.d(TAG, "OnClick: " + ID);
                Bundle bundle = new Bundle();
                bundle.putString("productID", String.valueOf(ID));
                ((MainActivity) getActivity()).loadFragment(R.id.nav_product_detail, bundle);
            }
        });

        imageSlider.setIndicatorEnabled(true);
        imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        imageSlider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        imageSlider.setIndicatorSelectedColor(getContext().getResources().getColor(R.color.colorAccent));
        imageSlider.setIndicatorUnselectedColor(getContext().getResources().getColor(R.color.colorPrimaryDark));
        imageSlider.setScrollTimeInSec(4); //set scroll delay in seconds :
        imageSlider.startAutoCycle();

    }

    void initViewModel() {
        mViewModel = new HomeViewModel();
        mViewModel.getCategories().observe(getViewLifecycleOwner(), new Observer<List<SliderItem>>() {
            @Override
            public void onChanged(List<SliderItem> sliderItems) {
                homeSliderAdapter.renewItems(sliderItems);
                av_splash_animation.setVisibility(View.GONE);
            }
        });

        mViewModel.getHomeButtons().observe(getViewLifecycleOwner(), new Observer<List<HomeButton>>() {
            @Override
            public void onChanged(List<HomeButton> homeButtons) {
                for (HomeButton button : homeButtons) {
                    switch (button.getButtonID()) {
                        case 0:
                            Glide.with(getContext())
                                    .load(button.getImageUrl())
                                    .fitCenter()
                                    .placeholder(R.drawable.ic_outline_terrain_24)
                                    .error(R.drawable.ic_outline_broken_image_24)
                                    .into(ivBestSellers);
                            tvBestSeller.setText(button.getDescription());
                            break;
                        case 1:
                            Glide.with(getContext())
                                    .load(button.getImageUrl())
                                    .fitCenter()
                                    .placeholder(R.drawable.ic_outline_terrain_24)
                                    .error(R.drawable.ic_outline_broken_image_24)
                                    .into(ivTheNewest);
                            tvTheNewest.setText(button.getDescription());
                            break;
                        case 2:
                            Glide.with(getContext())
                                    .load(button.getImageUrl())
                                    .fitCenter()
                                    .placeholder(R.drawable.ic_outline_terrain_24)
                                    .error(R.drawable.ic_outline_broken_image_24)
                                    .into(ivCampaigns);
                            tvCampaigns.setText(button.getDescription());
                            break;
                    }
                }
            }
        });
    }

    void initButtons() {
        ivBestSellers.setOnClickListener(this);
        ivTheNewest.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.ivBestSellers:
                bundle.putString(ProductDetailFragment.ARG_PRODUCT_ID, "0");
                ((MainActivity) getActivity()).loadFragment(R.id.nav_products, bundle);
                break;
            case R.id.ivTheNewest:
                bundle = new Bundle();
                bundle.putString(ProductDetailFragment.ARG_PRODUCT_ID, "0");
                ((MainActivity) getActivity()).loadFragment(R.id.nav_products, bundle);
                break;
            case R.id.ivCampaigns:
                break;
            default:

                break;
        }
    }
}