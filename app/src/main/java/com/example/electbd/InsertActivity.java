package com.example.electbd;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class InsertActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_PICK = 1;
    private EditText CandidateID;
    private EditText CandidateName;
    private ImageView view_image;
    private Button SelectImage ;
    private Button InsertDetails;

    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private byte[] imageByteArray;
    private DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        CandidateID = findViewById(R.id.et_candidate_id);
        CandidateName = findViewById(R.id.et_candidate_name);
        view_image = findViewById(R.id.iv_upload_image);
        SelectImage = findViewById(R.id.btn_select_image);
        InsertDetails = findViewById(R.id.btn_insert_details);

        databaseHelper = new DatabaseHelper(this);

        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                Uri imageUri = result.getData().getData();
                try {
                    Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    view_image.setImageBitmap(imageBitmap);
                    imageByteArray = bitmapToByteArray(imageBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        SelectImage.setOnClickListener(view -> showImageSelectionDialog());

        InsertDetails.setOnClickListener(view -> insertCandidate());
    }

    private void showImageSelectionDialog() {
        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");
        imagePickerLauncher.launch(pickIntent);
    }

    private byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void insertCandidate() {
        String id = CandidateID.getText().toString();
        String name = CandidateName.getText().toString();

        if (id.isEmpty()||name.isEmpty() || imageByteArray == null) {
            Toast.makeText(this, "Please fill all fields and select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        else {
            databaseHelper.insertCandidate(id, name, imageByteArray);
            Toast.makeText(this, "Candidate inserted successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(InsertActivity.this, AdminActivity.class);
            startActivity(intent);
        }
    }
}