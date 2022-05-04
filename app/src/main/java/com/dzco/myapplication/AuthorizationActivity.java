package com.dzco.myapplication;

import android.Manifest;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AuthorizationActivity extends AppCompatActivity {

    private TextInputEditText emailField;
    private TextInputEditText passwordField;
    private Button signinButton;
    private Button registrationButton;
    private Button supportButton;
    private RelativeLayout root;
    private FirebaseAuth auth;
    private DatabaseReference db;
    private InProgress progressDialog;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
            case 11:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    finish();
                }
                return;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);

        int internetPermissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET);
        if (internetPermissionStatus == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.INTERNET}, 10);
        }
        int internetPermissionStatus2 = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE);
        if (internetPermissionStatus2 == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_NETWORK_STATE}, 11);
        }
        emailField = (TextInputEditText) findViewById(R.id.email_signin_field);
        passwordField = (TextInputEditText) findViewById(R.id.password_signin_field);
        root = (RelativeLayout) findViewById(R.id.authoization_activity);
        auth = FirebaseAuth.getInstance(); //создаём подключение к сервису авторизации
        db = FirebaseDatabase.getInstance().getReference("Users"); //записываем ссылку на таблицу Users
        progressDialog = new InProgress(AuthorizationActivity.this);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(AuthorizationActivity.this, MainActivity.class));
            finish();
        }


        //кнопка регистрации
        registrationButton = (Button) findViewById(R.id.registration_button);
        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AuthorizationActivity.this, RegistrationActivity.class));
            }
        });

        //кнопка входа
        signinButton = (Button) findViewById(R.id.signin_button);
        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(emailField.getText().toString())) {
                    Snackbar.make(root, R.string.enter_email_label, Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (passwordField.getText().toString().length() < 8) {
                    Snackbar.make(root, R.string.enter_password_label, Snackbar.LENGTH_SHORT).show();
                    return;
                }

                progressDialog.show();
                auth.signInWithEmailAndPassword(emailField.getText().toString(), passwordField.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                db.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                    @Override
                                    public void onSuccess(DataSnapshot dataSnapshot) {
                                        progressDialog.hide();
                                        startActivity(new Intent(AuthorizationActivity.this, MainActivity.class));
                                        finish();
                                    }
                                });

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.hide();
                                Snackbar.make(root, R.string.failed_to_login_label, Snackbar.LENGTH_SHORT).show();
                            }
                });

            }
        });

        supportButton = (Button) findViewById(R.id.contact_support_button);
        supportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:dzco_vaccination@gmail.com"));
                startActivity(browser);
            }
        });
    }
}