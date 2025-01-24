/*package com.example.electbd;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Admin_Profile extends AppCompatActivity {
    EditText username,current_Password,new_Password;
    Button save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile);
        InitializeUI();
        save.setOnClickListener(v->{
            MainActivity mainActivity =new MainActivity();
            String Username = username.getText().toString().trim();
            String Current_Password = current_Password.getText().toString().trim();
            String New_Password = new_Password.getText().toString().trim();
            if (!Username.isEmpty()&&!Current_Password.isEmpty()&&!New_Password.isEmpty()){
                if(Username.equals(mainActivity.getAdmin_username())){
                    Toast.makeText(this, "User name is not changed!", Toast.LENGTH_SHORT).show();
                }
                if (!Current_Password.equals(mainActivity.getAdmin_password())){
                    current_Password.setError("Password is Incorrect! ");
                    current_Password.requestFocus();

                }
                else if(New_Password.equals(Current_Password)&& New_Password.equals(mainActivity.getAdmin_password())){
                    new_Password.setError("This password is not allowed!");
                    new_Password.requestFocus();

                }
                else {
                    mainActivity.setAdmin_username(Username);
                    mainActivity.setAdmin_password(New_Password);
                    setText();
                    Intent intent = new Intent(this,AdminActivity.class);
                    startActivity(intent);
                }
            }
            else {
                if(Username.isEmpty()){
                    username.setError("username is empty");
                    username.requestFocus();
                }
                if (Current_Password.isEmpty()){
                    current_Password.setError("Enter current password");
                    username.requestFocus();
                }
                if(New_Password.isEmpty()){
                    new_Password.setError("enter new password");
                    new_Password.requestFocus();
                }
            }
        });
    }

    private void setText() {
        username.setText("");
        current_Password.setText("");
        new_Password.setText("");
    }
    private void InitializeUI() {
        username=findViewById(R.id.et_username);
        current_Password=findViewById(R.id.et_current_password);
        new_Password = findViewById(R.id.et_new_password);
        save=findViewById(R.id.btn_save_changes);
    }
}*/
package com.example.electbd;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Admin_Profile extends AppCompatActivity {
    private EditText username, currentPassword, newPassword;
    private Button save;
    private TextView back;

    MainActivity mainActivity =new MainActivity();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile);
        InitializeUI();
        
        back.setOnClickListener(v->{
            finish();
        });
        save.setOnClickListener(v -> {
            String enteredUsername = username.getText().toString().trim();
            String enteredCurrentPassword = currentPassword.getText().toString().trim();
            String enteredNewPassword = newPassword.getText().toString().trim();

            DatabaseHelper dbhelper = new DatabaseHelper(this);
            String currentAdminUsername = dbhelper.getCurrentAdminUsername();
            String currentAdminPassword = dbhelper.getCurrentAdminPassword();

            // Validate input
            if (!validateInput(enteredUsername, enteredCurrentPassword, enteredNewPassword)) {
                return;
            }

            // Check if current password is correct
            if (!enteredCurrentPassword.equals(currentAdminPassword)) {
                currentPassword.setError("Incorrect Current Password!");
                currentPassword.requestFocus();
                return;
            }

            // Ensure new password is different from the current one
            if (enteredNewPassword.equals(currentAdminPassword)) {
                newPassword.setError("New password cannot be the same as the current password!");
                newPassword.requestFocus();
                return;
            }

            // Check if username has changed
            if (enteredUsername.equals(currentAdminUsername)) {
                Toast.makeText(this, "Username has not changed!", Toast.LENGTH_SHORT).show();
            } else {
                // Update admin credentials in the database
                boolean isUpdated = dbhelper.updateAdminCredentials(enteredUsername, enteredNewPassword);
                if (isUpdated) {
                    showToast("Admin credentials updated successfully!");
                    Intent intent = new Intent(Admin_Profile.this, AdminActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    showToast("Failed to update admin credentials.");
                }
            }
        });
    }

    private void InitializeUI() {
        username = findViewById(R.id.et_username);
        currentPassword = findViewById(R.id.et_current_password);
        newPassword = findViewById(R.id.et_new_password);
        save = findViewById(R.id.btn_save_changes);
        back =findViewById(R.id.btn_back_adminProfile);
    }

    private boolean validateInput(String username, String currentPassword, String newPassword) {
        boolean isValid = true;

        if (username.isEmpty()) {
            this.username.setError("Username is empty");
            this.username.requestFocus();
            isValid = false;
        }
        if (currentPassword.isEmpty()) {
            this.currentPassword.setError("Enter current password");
            this.currentPassword.requestFocus();
            isValid = false;
        }
        if (newPassword.isEmpty()) {
            this.newPassword.setError("Enter new password");
            this.newPassword.requestFocus();
            isValid = false;
        }
        return isValid;
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }



}
