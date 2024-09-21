package com.example.electbd;
/*
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
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class UpdateActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
private byte[] ImageByteArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_update);
        EditText id = findViewById(R.id.et_update_id);
        EditText name = findViewById(R.id.et_update_name);
        Button search = findViewById(R.id.bt_search);
        Button select_image = findViewById(R.id.bt_select_image);
        Button insert = findViewById(R.id.bt_insert_update);
        ImageView imageView = findViewById(R.id.image);

        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        search.setOnClickListener(view -> {
            String cid = id.getText().toString().trim();
            if (cid.isEmpty()) {
                Toast.makeText(this, "Please enter a product name to search", Toast.LENGTH_SHORT).show();
                return;
            }

            Cursor cursor = databaseHelper.getByCid(cid);
            if (cursor != null && cursor.moveToFirst()) {
                String candidateName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_NAME));
                byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PRODUCT_IMAGE_URI));

                name.setText(String.valueOf(candidateName));

                if (image != null) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                    imageView.setImageBitmap(bitmap);
                    ImageByteArray = image;
                }
                cursor.close();
            } else {
                Toast.makeText(this, "Product not found", Toast.LENGTH_SHORT).show();
            }
        });

        select_image.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);

        });


        insert.setOnClickListener(view -> {

        });
    }


}*/

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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class UpdateActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private byte[] ImageByteArray;

    private ImageView imageView;
    private EditText id;
    private EditText name;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        id = findViewById(R.id.et_update_id);
        name = findViewById(R.id.et_update_name);
        Button search = findViewById(R.id.bt_search);
        Button select_image = findViewById(R.id.bt_select_image);
        Button insert = findViewById(R.id.bt_insert_update);
        imageView = findViewById(R.id.image);

        databaseHelper = new DatabaseHelper(this);

        search.setOnClickListener(view -> {
            String cid = id.getText().toString().trim();
            if (cid.isEmpty()) {
                Toast.makeText(this, "Please enter an ID to search", Toast.LENGTH_SHORT).show();
                return;
            }

            Cursor cursor = databaseHelper.getByCid(cid);
            if (cursor != null && cursor.moveToFirst()) {
                String candidateName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_NAME));
                byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PRODUCT_IMAGE_URI));

                name.setText(candidateName);

                if (image != null) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                    imageView.setImageBitmap(bitmap);
                    ImageByteArray = image;
                }
                cursor.close();
            } else {
                Toast.makeText(this, "Candidate not found", Toast.LENGTH_SHORT).show();
            }
        });

        select_image.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        insert.setOnClickListener(view -> {
            String candidateId = id.getText().toString().trim();
            String candidateName = name.getText().toString().trim();

            if (candidateId.isEmpty() || candidateName.isEmpty() || ImageByteArray == null) {
                Toast.makeText(this, "Please fill all fields and select an image", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean isUpdated = databaseHelper.updateCandidate(candidateId, candidateName, ImageByteArray);

            if (isUpdated) {
                Toast.makeText(this, "Candidate updated successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UpdateActivity.this, AdminActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                imageView.setImageBitmap(bitmap);
                ImageByteArray = getBytesFromBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to select image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}
