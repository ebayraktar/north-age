package com.nage.north_age.views;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.nage.north_age.BaseActivity;
import com.nage.north_age.R;

import java.util.Objects;

import static com.nage.north_age.App.mAuth;

public class SignUpActivity extends BaseActivity implements View.OnClickListener {
    TextInputEditText etName, etSurname, etEmail, etPassword, etPasswordConfirm;
    TextInputLayout tilName, tilSurname, tilEmail, tilPassword, tilPasswordConfirm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        tilName = findViewById(R.id.tilName);
        tilSurname = findViewById(R.id.tilSurname);
        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);
        tilPasswordConfirm = findViewById(R.id.tilPasswordConfirm);

        etName = findViewById(R.id.etName);
        etSurname = findViewById(R.id.etSurname);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etPasswordConfirm = findViewById(R.id.etPasswordConfirm);

        findViewById(R.id.imgBtnBack).setOnClickListener(this);
        findViewById(R.id.cvSignUp).setOnClickListener(this);
        showDialog(SignUpActivity.this, "BAŞARILI", "Kullanıcı başarılı bir şekilde oluşturuldu", "Tamam", "22894-check-animation.json");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    private void SignUp() {
        if (!validateForm()) {
            return;
        }
        mAuth.createUserWithEmailAndPassword(Objects.requireNonNull(etEmail.getText()).toString(), Objects.requireNonNull(etPassword.getText()).toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "onComplete: ");
                            updateProfile();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d("TAG", "onComplete: " + task.getException());
                            showDialog(SignUpActivity.this, "HATA!", "Kullanıcı oluşturulurken hata oluştu", "Tamam", "22893-error-animation.json");
                            //updateUI(null);
                        }
                        // ...
                    }
                });
    }

    private boolean validateForm() {
        boolean isValid = true;
        if (Objects.requireNonNull(etName.getText()).toString().isEmpty()) {
            tilName.setErrorEnabled(true);
            tilName.setError("Boş olamaz!");
            isValid = false;
        } else {
            tilName.setErrorEnabled(false);
        }

        if (Objects.requireNonNull(etSurname.getText()).toString().isEmpty()) {
            tilSurname.setErrorEnabled(true);
            tilSurname.setError("Boş olamaz!");
            isValid = false;
        } else {
            tilSurname.setErrorEnabled(false);
        }

        if (Objects.requireNonNull(etEmail.getText()).toString().isEmpty()) {
            tilEmail.setErrorEnabled(true);
            tilEmail.setError("Boş olamaz!");
            isValid = false;
        } else {
            tilEmail.setErrorEnabled(false);
        }

        if (Objects.requireNonNull(etPassword.getText()).toString().isEmpty()) {
            tilPassword.setErrorEnabled(true);
            tilPassword.setError("Boş olamaz!");
            isValid = false;
        } else {
            tilPassword.setErrorEnabled(false);
        }

        if (Objects.requireNonNull(etPasswordConfirm.getText()).toString().isEmpty()) {
            tilPasswordConfirm.setErrorEnabled(true);
            tilPasswordConfirm.setError("Boş olamaz!");
            isValid = false;
        } else {
            tilPasswordConfirm.setErrorEnabled(false);
        }

        if (!etPasswordConfirm.getText().toString().equals(etPassword.getText().toString())) {
            tilPassword.setErrorEnabled(true);
            tilPasswordConfirm.setErrorEnabled(true);
            tilPassword.setError("Parola eşleşmedi");
            tilPasswordConfirm.setError("Parola eşleşmedi");
            isValid = false;
        } else {
            tilPassword.setErrorEnabled(false);
            tilPasswordConfirm.setErrorEnabled(false);
        }

        return isValid;
    }

    void updateProfile() {
        FirebaseUser user = mAuth.getCurrentUser();
        //String deviceID = App.getDeviceID(SignUpActivity.this);

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(Objects.requireNonNull(etName.getText()).toString() + " " + Objects.requireNonNull(etSurname.getText()).toString())
                .build();

        assert user != null;
        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d("TAG", "onComplete: ");
                        if (!task.isSuccessful()) {
                            updateFailed();
                        }
                        showDialog(SignUpActivity.this, "BAŞARILI", "Kullanıcı başarılı bir şekilde oluşturuldu", "Tamam", "22894-check-animation.json");
                    }
                });
    }

    void updateFailed() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBtnBack:
                onBackPressed();
                break;
            case R.id.cvSignUp:
                SignUp();
                break;
        }
    }
}
