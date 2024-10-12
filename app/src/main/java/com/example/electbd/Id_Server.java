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
import android.widget.TextView;
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

public class Id_Server extends AppCompatActivity {
    private EditText id,name,father_name,mother_name,phone,address;
    private Button insert,update,delete;
    private DatabaseHelper dbHelper;
    private ImageView image;
    private TextView back;

    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private byte[] imageByteArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id_server);

        initializeUIComponents();

        dbHelper = new DatabaseHelper(this);

        insert.setOnClickListener(v ->handleInsert());
        update.setOnClickListener(v -> handleUpdate());
        delete.setOnClickListener(v -> handleDelete());
        back.setOnClickListener(v->{
            finish();
            Intent intent = new Intent(Id_Server.this, AdminActivity.class);
            startActivity(intent);
        });

        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                Uri imageUri = result.getData().getData();
                try {
                    Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    image.setImageBitmap(imageBitmap);
                    imageByteArray = bitmapToByteArray(imageBitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        image.setOnClickListener(view -> showImageSelectionDialog());
    }

    private void showImageSelectionDialog() {
        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");
        imagePickerLauncher.launch(pickIntent);
    }
    private byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }


    private void initializeUIComponents() {

        id = findViewById(R.id.et_server_id);
        name = findViewById(R.id.et_name);
        father_name = findViewById(R.id.et_f_name);
        mother_name = findViewById(R.id.et_m_name);
        phone = findViewById(R.id.et_id_person_phone);
        address = findViewById(R.id.et_address);
        image = findViewById(R.id.iv_up_id_image);
        insert = findViewById(R.id.btn_insert);
        update = findViewById(R.id.btn_serverID_update);
        delete = findViewById(R.id.btn_serverID_delete);
        back = findViewById(R.id.back_button);
    }

    private void handleDelete() {
        Toast.makeText(this, "Delete is still in progress!", Toast.LENGTH_SHORT).show();

    }

    private void handleUpdate() {
        Toast.makeText(this, "Update is still in progress!", Toast.LENGTH_SHORT).show();
    }

    private void handleInsert() {
        String insert_id =id.getText().toString().trim();
        String insert_name= name.getText().toString().trim();
        String insert_f_name= father_name.getText().toString().trim();
        String insert_m_name= mother_name.getText().toString().trim();
        String insert_phone= phone.getText().toString().trim();
        String insert_address= address.getText().toString().trim();

        if (insert_id.isEmpty()|| insert_name.isEmpty()||insert_f_name.isEmpty()||insert_m_name.isEmpty()||insert_phone.isEmpty()||insert_address.isEmpty()||imageByteArray==null){
            Toast.makeText(this, "Fill all the fields and select image first", Toast.LENGTH_SHORT).show();
        }
        else{
            boolean isInserted = dbHelper.InsertIDServer(insert_id,insert_name,insert_f_name,insert_m_name,insert_phone,insert_address,imageByteArray);
            if (isInserted) {
                Toast.makeText(this, "Insertion successful!", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, "Insertion failed! Try again.", Toast.LENGTH_SHORT).show();
            }
            Intent intent = new Intent(Id_Server.this, Id_Server.class);
            startActivity(intent);
        }
    }


}