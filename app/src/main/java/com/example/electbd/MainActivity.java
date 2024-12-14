package com.example.electbd;

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

        SessionManager sessionManager = new SessionManager(this);
        if (sessionManager.isLoggedIn()) {
            // Redirect to the main activity
            startActivity(new Intent(this, CandidatesActivity.class));
            finish();
        }

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
        etUsername = findViewById(R.id.et_email_phone_login);
        etPassword = findViewById(R.id.et_password_login);
        btnLogin = findViewById(R.id.btn_login_signin);
        btnRegister = findViewById(R.id.tv_login_signup);
    }

    private void navigateToRegistration() {
        startActivity(new Intent(MainActivity.this,Register_Activity.class));
        finish();
    }

    private void handleLogin() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validate inputs
        if (username.isEmpty() || password.isEmpty()) {
            if(username.isEmpty()){
                etUsername.setError("Enter Username");
            }
            if (password.isEmpty()){
                etPassword.setError("Enter Password");
            }
            showToast("Please fill the necessary information!");
            return;
        }

        // Check if user is admin
        if (dbhelper.checkAdminCredentials(username, password)) {
            navigateToActivity(AdminActivity.class);
            finish();
        }
        // Check if user info matches
        else if (checkUserinfo(username, password)) {
            showToast("Login Successful!");
            navigateToActivity(CandidatesActivity.class);
            SessionManager sessionManager=new SessionManager(this);
            sessionManager.setLogin(true,username);
            UserProfile.userinfo=username;
            finish();
        }
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
