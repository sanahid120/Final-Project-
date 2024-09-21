package com.example.electbd;
/*
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText etUsername = findViewById(R.id.et_username);
        EditText etPassword = findViewById(R.id.et_password);
        Button btnLogin = findViewById(R.id.btn_login);
        Button btnRegister = findViewById(R.id.btn_register);

        btnRegister.setOnClickListener(v-> {
            Intent intent = new Intent(MainActivity.this, Register_Activity.class);
            startActivity(intent);

        });

        btnLogin.setOnClickListener(v->{
            Toast.makeText(MainActivity.this,"login button clicked",Toast.LENGTH_SHORT).show();
            String username = etUsername.getText().toString();
            String password = etPassword.getText().toString();
            String admin_username = "admin";
            String admin_password = "admin";

            if (username.equals(admin_username)&&password.equals(admin_password) ){
                Intent intent_to_admin = new Intent(MainActivity.this, AdminActivity.class);
                startActivity(intent_to_admin);
            }
            else if(username.isEmpty() || password.isEmpty()){
                Toast.makeText(MainActivity.this,"Please fill the necessary information!!",Toast.LENGTH_SHORT).show();
            }

            else {

                DatabaseHelper dbhelper = new DatabaseHelper(MainActivity.this);

                boolean result = dbhelper.checkUser(username,password);
                if (result){
                    Intent intent = new Intent(MainActivity.this,CandidatesActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(MainActivity.this,"Unregistered Information!!",Toast.LENGTH_LONG).show();

                }

            }

        });

    }
}*/

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin, btnRegister;
    private String admin_username="admin";
    private String admin_password="admin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        initializeUIComponents();

        // Set listeners
        btnRegister.setOnClickListener(v -> navigateToRegistration());
        btnLogin.setOnClickListener(v -> handleLogin());
    }

    private void initializeUIComponents() {
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);
    }

    private void navigateToRegistration() {
        Intent intent = new Intent(MainActivity.this, Register_Activity.class);
        startActivity(intent);
    }

    private void handleLogin() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validate inputs
        if (username.isEmpty() || password.isEmpty()) {
            showToast("Please fill the necessary information!");
        }

        // Check if user is admin
        else if (isAdmin(username, password)) {
            navigateToActivity(AdminActivity.class);
        }

        // Check user info
        else if (checkUserinfo(username, password)) {
            showToast("Login Successful!");
            navigateToActivity(CandidatesActivity.class);
        }

        // else show message
        else {
            showToast("Unregistered Information!");
        }
    }

    public void setAdmin_username(String admin_username) {
        this.admin_username = admin_username;
    }

    public String getAdmin_username() {
        return admin_username;
    }

    public void setAdmin_password(String admin_password) {
        this.admin_password = admin_password;
    }

    public String getAdmin_password() {
        return admin_password;
    }

    private boolean isAdmin(String username, String password) {

        return username.equals(admin_username) && password.equals(admin_password);
    }

    private boolean checkUserinfo(String username, String password) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        return dbHelper.checkUser(username, password);
    }

    private void navigateToActivity(Class<?> targetActivity) {
        Intent intent = new Intent(MainActivity.this, targetActivity);
        startActivity(intent);
    }

    private void showToast(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
