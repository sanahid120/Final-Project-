package com.example.electbd;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DeleteActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private byte[] ImageByteArray;

    private ImageView imageView;
    private EditText id;
    private EditText name;
    private DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        id = findViewById(R.id.et_delete_id);
        name = findViewById(R.id.et_delete_name);
        Button search = findViewById(R.id.bt_delete_search);
        Button delete = findViewById(R.id.bt_delete_candidate);
        imageView = findViewById(R.id.et_delete_image);

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

        delete.setOnClickListener(v-> {
            String candidateID = id.getText().toString().trim();
            if (candidateID.isEmpty()) {
                Toast.makeText(this, "Please enter a candidate ID to delete", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean isDeleted = databaseHelper.deleteCandidate(candidateID);

            if (isDeleted) {
                Toast.makeText(this, "Candidate deleted successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DeleteActivity.this, AdminActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Deletion failed", Toast.LENGTH_SHORT).show();
            }

        });

    }
}