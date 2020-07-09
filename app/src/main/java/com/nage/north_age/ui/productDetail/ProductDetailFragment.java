package com.nage.north_age.ui.productDetail;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.nage.north_age.R;
import com.nage.north_age.adapters.HomeSliderAdapter;
import com.nage.north_age.interfaces.SliderOnClickListener;
import com.nage.north_age.models.SliderItem;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

import me.gilo.woodroid.models.Attribute;
import me.gilo.woodroid.models.Category;
import me.gilo.woodroid.models.Image;
import me.gilo.woodroid.models.Product;
import me.gilo.woodroid.models.Tag;

public class ProductDetailFragment extends Fragment {

    private ProductDetailViewModel mViewModel;

    public static ProductDetailFragment newInstance() {
        return new ProductDetailFragment();
    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String ARG_PRODUCT_ID = "productID";
    HomeSliderAdapter productSliderAdapter;

    ImageView ivProductFav;
    LottieAnimationView av_splash_animation;
    SliderView imageSliderProduct;
    TextView tvProductName, tvProductPrice, tvProductDescription, tvCategories, tvTags;
    LinearLayout llAttributes;
    RelativeLayout rlRoot;
    CardView cvProductFav, cvSoldOut, cvReviews;
    FloatingActionButton fabAdd;
    boolean isFav;
    Product currentProduct;
    // TODO: Rename and change types of parameters
    private int productID;

    public ProductDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param productID Product ID.
     * @return A new instance of fragment AboutFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductDetailFragment newInstance(String productID) {
        ProductDetailFragment fragment = new ProductDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PRODUCT_ID, productID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String _productID = getArguments().getString(ARG_PRODUCT_ID);
            assert _productID != null;
            productID = Integer.parseInt(_productID);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_detail, container, false);
        ivProductFav = view.findViewById(R.id.ivProductFav);
        av_splash_animation = view.findViewById(R.id.av_splash_animation);
        imageSliderProduct = view.findViewById(R.id.imageSliderProduct);
        tvProductName = view.findViewById(R.id.tvProductName);
        tvProductDescription = view.findViewById(R.id.tvProductDescription);
        tvProductPrice = view.findViewById(R.id.tvProductPrice);
        llAttributes = view.findViewById(R.id.llAttributes);
        rlRoot = view.findViewById(R.id.rlRoot);
        cvProductFav = view.findViewById(R.id.cvProductFav);
        cvSoldOut = view.findViewById(R.id.cvSoldOut);
        cvReviews = view.findViewById(R.id.cvReviews);
        tvCategories = view.findViewById(R.id.tvCategories);
        tvTags = view.findViewById(R.id.tvTags);
        fabAdd = view.findViewById(R.id.fabAdd);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initSlider();

        cvProductFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "";
                if (currentProduct != null) {
                    message = currentProduct.getName();
                }
                if (isFav) {
                    ivProductFav.setImageResource(R.drawable.ic_baseline_favorite_off_24);
                    message += " favorilerden çıkarıldı";
                } else {
                    ivProductFav.setImageResource(R.drawable.ic_baseline_favorite_on_24);
                    message += " favorilere eklendi";
                }
                isFav = !isFav;
                Snackbar.make(v, message, BaseTransientBottomBar.LENGTH_SHORT).setAction("GERİ AL", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isFav) {
                            ivProductFav.setImageResource(R.drawable.ic_baseline_favorite_off_24);
                        } else {
                            ivProductFav.setImageResource(R.drawable.ic_baseline_favorite_on_24);
                        }
                        isFav = !isFav;
                    }
                }).show();
            }
        });
        cvReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("İncelemeler")
                        .setMessage("İncelemeler sayfası açılıyor...")
                        .setNegativeButton("TAMAM", null)
                        .create().show();
            }
        });
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Ürün ekleme")
                        .setMessage(currentProduct.getName() + " için sepete ekleme sayfası açılıyor...")
                        .setNegativeButton("TAMAM", null)
                        .create().show();
            }
        });
        mViewModel = ViewModelProviders.of(this).get(ProductDetailViewModel.class);
        mViewModel.getProductDetail(productID).observe(getViewLifecycleOwner(), new Observer<Product>() {
            @Override
            public void onChanged(Product product) {
                hideLoading();
                if (product != null)
                    initProduct(product);
                else
                    invalidProductID();
            }
        });
    }

    private void invalidProductID() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("HATA")
                .setMessage("Ürün detayı bulunamadı!")
                .setNegativeButton("TAMAM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //getActivity().onBackPressed();
                    }
                })
                .create().show();
    }

    @SuppressLint("SetTextI18n")
    private void initProduct(Product product) {
        currentProduct = product;
        tvProductName.setText(product.getName());
        tvProductPrice.setText("₺ " + product.getPrice());
        tvProductDescription.setText(product.getShort_description());
        llAttributes.removeAllViews();
        for (Attribute attribute : product.getAttributes()) {
            TextView tvAtrb = new TextView(getContext());
            tvAtrb.setText(attribute.getName() + ": ");
            for (int i = 0; i < attribute.getOptions().length; i++) {
                String option = attribute.getOptions()[i];
                if (i == 0) {
                    tvAtrb.setText(tvAtrb.getText() + option);
                } else {
                    tvAtrb.setText(tvAtrb.getText() + ", " + option);
                }
            }
            llAttributes.addView(tvAtrb);
        }
        for (int i = 0; i < product.getCategories().size(); i++) {
            Category category = product.getCategories().get(i);
            if (i == 0) {
                tvCategories.setText(category.getName());
            } else {
                tvCategories.setText(tvCategories.getText() + ", " + category.getName());
            }
        }
        for (int i = 0; i < product.getTags().size(); i++) {
            Tag tag = product.getTags().get(i);
            if (i == 0) {
                tvTags.setText(tag.getName());
            } else {
                tvTags.setText(tvTags.getText() + ", " + tag.getName());
            }
        }
        List<SliderItem> items = new ArrayList<>();
        for (Image image : product.getImages()) {
            items.add(new SliderItem(image.getId(), image.getSrc()));
        }
        productSliderAdapter.renewItems(items);
//        Gson gson = new Gson();
//        String json = gson.toJson(product);
//        Log.d("TAG", "initProduct: " + json);
//        if (!product.isIn_stock()) {
//            cvSoldOut.setVisibility(View.VISIBLE);
//        }
    }

    void initSlider() {
        productSliderAdapter = new HomeSliderAdapter(getContext());
        imageSliderProduct.setSliderAdapter(productSliderAdapter);

        productSliderAdapter.setOnClickListener(new SliderOnClickListener() {
            @Override
            public void OnClick(int ID) {
                Log.d("TAG", "OnClick: " + ID);
            }
        });

        imageSliderProduct.setIndicatorEnabled(true);
        imageSliderProduct.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        imageSliderProduct.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        imageSliderProduct.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        imageSliderProduct.setIndicatorSelectedColor(getContext().getResources().getColor(R.color.colorAccent));
        imageSliderProduct.setIndicatorUnselectedColor(getContext().getResources().getColor(R.color.colorPrimaryDark));
        imageSliderProduct.setScrollTimeInSec(4); //set scroll delay in seconds :
        imageSliderProduct.startAutoCycle();
    }

    private void hideLoading() {
        av_splash_animation.setVisibility(View.GONE);
    }

}