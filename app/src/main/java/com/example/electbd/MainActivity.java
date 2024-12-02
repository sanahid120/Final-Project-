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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
public class MainActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin;
    private TextView btnRegister;
    DatabaseHelper dbhelper= new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        initializeUIComponents();

        // Insert default admin credentials if not already present
        dbhelper.insertAdminCredentials("admin", "admin");

        // Set listeners
        btnRegister.setOnClickListener(v -> navigateToRegistration());
        btnLogin.setOnClickListener(v -> handleLogin());
    }

    private void initializeUIComponents() {
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_email_phone_login);
        btnLogin = findViewById(R.id.btn_login_signin);
        btnRegister = findViewById(R.id.tv_login_signup);
    }

    private void navigateToRegistration() {
        startActivity(new Intent(MainActivity.this,Register_Activity.class));
    }

    private void handleLogin() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validate inputs
        if (username.isEmpty() || password.isEmpty()) {
            showToast("Please fill the necessary information!");
            etUsername.setError("Enter Username");
            etPassword.setError("Enter Password");
            return;
        }

        // Check if user is admin
        if (dbhelper.checkAdminCredentials(username, password)) {
            navigateToActivity(AdminActivity.class);
        }
        // Check if user info matches
        else if (checkUserinfo(username, password)) {
            showToast("Login Successful!");
            navigateToActivity(CandidatesActivity.class);
            UserProfile.userinfo=username;
        }
        // Else, show an error
        else {
            showToast("Unregistered Information!");
        }
    }

    private boolean checkUserinfo(String username, String password) {
        return dbhelper.checkUser(username, password);
    }

    private void navigateToActivity(Class<?> targetActivity) {
        Intent intent = new Intent(MainActivity.this, targetActivity);
        startActivity(intent);
    }

    private void showToast(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }


}
