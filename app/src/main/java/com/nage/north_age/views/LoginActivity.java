package com.nage.north_age.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nage.north_age.BaseActivity;
import com.nage.north_age.R;

import java.util.Objects;

import static com.nage.north_age.App.mAuth;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    CheckBox cbRememberMe;
    TextInputEditText etEmail, etPassword;
    TextInputLayout tilEmail, tilPassword;

    @Override
    public void onBackPressed() {
        onBackPressed(true);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        cbRememberMe = findViewById(R.id.cbRememberMe);

        findViewById(R.id.cvLogin).setOnClickListener(this);
        findViewById(R.id.cvGuestLogin).setOnClickListener(this);
        findViewById(R.id.tvSignUp).setOnClickListener(this);
        findViewById(R.id.tvRememberMe).setOnClickListener(this);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    }

    //Login function
    void login() {
        if (!validateForm()) {
            return;
        }
        showLoadingDialog(LoginActivity.this, "Giriş yapılıyor...", "Iptal");

        mAuth.signInWithEmailAndPassword(Objects.requireNonNull(etEmail.getText()).toString(), Objects.requireNonNull(etPassword.getText()).toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            //...
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Kullanıcı adı/parola hatalı.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        hideLoadingDialog(LoginActivity.this);
                    }
                });
    }

    void saveData() {
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");
    }

    //Guest Login function
    void guestLogin() {
        showLoadingDialog(LoginActivity.this, "Giriş yapılıyor...", "Iptal");
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            assert user != null;
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Authentication failed. " + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                        hideLoadingDialog(LoginActivity.this);
                        // ...
                    }
                });


        //...
//        startActivity(new Intent(LoginActivity.this, MainActivity.class));
//        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//        finish();
    }

    boolean validateForm() {
        boolean isValid = true;
        if (Objects.requireNonNull(etPassword.getText()).toString().isEmpty()) {
            tilPassword.setErrorEnabled(true);
            tilPassword.setError("Boş olamaz!");
            isValid = false;
        } else {
            tilPassword.setErrorEnabled(false);
        }
        if (Objects.requireNonNull(etEmail.getText()).toString().isEmpty()) {
            tilEmail.setErrorEnabled(true);
            tilEmail.setError("Boş olamaz!");
            isValid = false;
        } else {
            tilEmail.setErrorEnabled(false);
        }
        return isValid;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cvLogin:
                login();
                break;
            case R.id.cvGuestLogin:
                guestLogin();
                break;
            case R.id.tvSignUp:
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.tvRememberMe:
                cbRememberMe.setChecked(!cbRememberMe.isChecked());
                break;
        }
    }
}
