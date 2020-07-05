package com.nage.north_age;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

import me.gilo.woodroid.Woocommerce;

public class BaseActivity extends AppCompatActivity {

    boolean doubleBackToExitPressedOnce = false;
    AlertDialog loadingDialog, alertDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public void onBackPressed(boolean pressTwice) {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    public void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void showDialog(Context context, String title, String message) {
        this.showDialog(context, title, message, R.drawable.ic_baseline_info_red_24);
    }

    public void showDialog(Context context, String title, String message, String positive, String lottieFile) {
        if (alertDialog != null && alertDialog.isShowing()) {
            return;
        }
        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("InflateParams") View customView = inflater.inflate(R.layout.dialog_default, null);
        LottieAnimationView av_splash_animation = customView.findViewById(R.id.av_splash_animation);
        TextView tvTitle = customView.findViewById(R.id.tvTitle);
        TextView tvMessage = customView.findViewById(R.id.tvMessage);
        TextView tvPositive = customView.findViewById(R.id.tvPositive);

        av_splash_animation.setAnimation(lottieFile);
        tvTitle.setText(title);
        tvMessage.setText(message);
        tvPositive.setText(positive);
        customView.findViewById(R.id.cvPositive).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alertDialog != null && alertDialog.isShowing()) {
                    alertDialog.dismiss();
                }
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        alertDialog = builder.setView(customView).create();
        alertDialog.show();
    }

    public void showDialog(Context context, String title, String message, int icon) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setIcon(icon)
                .setNegativeButton("TAMAM", null)
                .create().show();
    }

    public void showLoadingDialog(Context context, String title, String button) {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            return;
        }
        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("InflateParams") View customView = inflater.inflate(R.layout.dialog_loading, null);

        TextView tvTitle = customView.findViewById(R.id.tvTitle);
        TextView tvNegative = customView.findViewById(R.id.tvNegative);
        customView.findViewById(R.id.cvCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loadingDialog != null && loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                }
            }
        });

        tvTitle.setText(title);
        tvNegative.setText(button);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        loadingDialog = builder.setView(customView).create();
        loadingDialog.show();
    }

    public void hideLoadingDialog(Context context) {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }
}
