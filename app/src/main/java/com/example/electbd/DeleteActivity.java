package com.example.electbd;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DeleteActivity extends AppCompatActivity {
    private byte[] ImageByteArray;

    private ImageView imageView;
    private EditText id;
    private EditText name;
    private DatabaseHelper databaseHelper;
    private TextView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete); // Set the layout here

        // Initialize views after setContentView()
        id = findViewById(R.id.et_delete_id);
        name = findViewById(R.id.et_delete_name);
        Button search = findViewById(R.id.bt_delete_search);
        Button delete = findViewById(R.id.bt_delete_candidate);
        imageView = findViewById(R.id.et_delete_image);
        back = findViewById(R.id.btn_back_delete_candidate); // Initialize here

        databaseHelper = new DatabaseHelper(this);

        back.setOnClickListener(v -> {
            finish(); // Close the current activity when back is pressed
        });

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

        delete.setOnClickListener(v -> {
            String candidateID = id.getText().toString().trim();
            if (candidateID.isEmpty()) {
                Toast.makeText(this, "Please enter a candidate ID to delete", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean isDeleted = databaseHelper.deleteCandidate(candidateID);

            if (isDeleted) {
                Toast.makeText(this, "Candidate deleted successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Deletion failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
