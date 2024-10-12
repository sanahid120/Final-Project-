package com.example.electbd;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UserProfile extends AppCompatActivity {

    private ImageView profileImageView;
    private EditText usernameEditText;
    private EditText birthdayEditText; // Assuming you have birthday in the layout
    private EditText phoneEditText;    // Assuming you have phone in the layout
    private EditText userIdTextView;   // Assuming user ID is displayed in a TextView
    private Button saveButton;
    static String userinfo;
    private TextView changeEmailOrPhoneTextView;
    private TextView changePasswordTextView,back;

    private DatabaseHelper dbHelper;
    private String userId; // Example userId, should be retrieved from session or intent

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);


        // Initialize views
        back=findViewById(R.id.btn_back_userProfile);
        profileImageView = findViewById(R.id.profile_image);
        usernameEditText = findViewById(R.id.change_username_edit_text);
        birthdayEditText = findViewById(R.id.change_birthday_edit_text);
        phoneEditText = findViewById(R.id.change_phone_edit_text);
        userIdTextView = findViewById(R.id.user_id_edit_text);
        saveButton = findViewById(R.id.save_button);
        changeEmailOrPhoneTextView = findViewById(R.id.tv_profile_change_email_or_phone);
        changePasswordTextView = findViewById(R.id.tv_profile_change_password);

        dbHelper = new DatabaseHelper(this);

        back.setOnClickListener(v->{
            finish();
        });
        loadUserInfo(userinfo);
        // Handle save button click
        saveButton.setOnClickListener(v -> {
            String newUsername = usernameEditText.getText().toString();
            userId = userIdTextView.getText().toString();
            if (!newUsername.isEmpty()) {
                // Update the username in the database
                dbHelper.updateUsername(userId, newUsername);
                Toast.makeText(UserProfile.this, "Username saved!", Toast.LENGTH_SHORT).show();
            } else {
                usernameEditText.setError("Username is Empty!");
            }
        });

        // Handle change email/phone click
        changeEmailOrPhoneTextView.setOnClickListener(v -> {
            Toast.makeText(UserProfile.this, "Change email or phone clicked", Toast.LENGTH_SHORT).show();
        });

        // Handle change password click
        changePasswordTextView.setOnClickListener(v -> {
            Toast.makeText(UserProfile.this, "Change password clicked", Toast.LENGTH_SHORT).show();
        });
    }


    // Load user info from the database and set it in the EditText fields
    void loadUserInfo(String Username) {
        // Query the database for user info based on userId
        Cursor cursor = dbHelper.getUserInfo(Username);

        if (cursor != null && cursor.moveToFirst()) {
            // Retrieve user details from the cursor
            String username = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_USERNAME));
            String birthday = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_BIRTHDAY));
            String phone = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_MOBILE));
            String userID = String.valueOf(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COL_NID)));

            usernameEditText.setText(username);
            birthdayEditText.setText(birthday);
            phoneEditText.setText(phone);
            userIdTextView.setText(userID);
            cursor.close();

        } else {
            Toast.makeText(this, "User not found!", Toast.LENGTH_SHORT).show();
        }
    }
}
