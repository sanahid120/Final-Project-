package com.example.electbd;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfile extends AppCompatActivity {

    private CircleImageView profileImageView;
    private EditText usernameEditText;
    private EditText birthdayEditText;
    private EditText phoneEditText;
    private EditText userIdTextView;
    private Button saveButton;
    static String userinfo;
    private TextView changeEmailOrPhoneTextView;
    private TextView changePasswordTextView,back;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private byte[] imageByteArray;
    private DatabaseHelper dbHelper;
    private String userID; // Example userId, should be retrieved from session or intent

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        back=findViewById(R.id.btn_back_userProfile);
        profileImageView = findViewById(R.id.user_profile_image);
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

        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                Uri imageUri = result.getData().getData();
                try {
                    Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    profileImageView.setImageBitmap(imageBitmap);
                    imageByteArray = bitmapToByteArray(imageBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        profileImageView.setOnClickListener(view -> {
            showImageSelectionDialog();
        });

        // Handle save button click
        saveButton.setOnClickListener(v -> {
            String newUsername = usernameEditText.getText().toString().trim();

            if (dbHelper.InsertUserImage(userinfo, imageByteArray)){
                loadUserInfo(userinfo);
            }
            if (!newUsername.isEmpty()) {
                dbHelper.updateUsername(userID, newUsername);
                Toast.makeText(UserProfile.this, "Username saved!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(UserProfile.this,CandidatesActivity.class));
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
            userID = String.valueOf(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_NID)));
            byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_IMAGE));
            usernameEditText.setText(username);
            birthdayEditText.setText(birthday);
            phoneEditText.setText(phone);
            userIdTextView.setText(userID);
            if (image != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                profileImageView.setImageBitmap(bitmap);
                imageByteArray = image;
            }
            cursor.close();
            cursor.close();
        } else {
            Toast.makeText(this, "User not found!", Toast.LENGTH_SHORT).show();
        }
    }

    private void showImageSelectionDialog() {
        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");
        imagePickerLauncher.launch(pickIntent);
    }
    private byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}
