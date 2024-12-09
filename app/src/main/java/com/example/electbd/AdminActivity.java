package com.example.electbd;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AdminActivity extends AppCompatActivity {
    Button view,insert,update,delete;
    TextView profile,ID_Server,logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        view =findViewById(R.id.btn_view_candidates);
        insert =findViewById(R.id.btn_insert_candidates);
        update =findViewById(R.id.btn_update_candidates);
        delete =findViewById(R.id.btn_dlt_candidates);
        profile =findViewById(R.id.btn_Profile);
        ID_Server =findViewById(R.id.btn_id_server);
        logout = findViewById(R.id.tv_logout_admin);

        logout.setOnClickListener(v->{
            finish();
            Intent intent = new Intent(AdminActivity.this,MainActivity.class);
            startActivity(intent);
        });

        view.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this,View_Results.class);
            startActivity(intent);

        });

        insert.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this,InsertActivity.class);
            startActivity(intent);
        });

        update.setOnClickListener(v->{
            Intent intent = new Intent(AdminActivity.this,UpdateActivity.class);
            startActivity(intent);
        });

        delete.setOnClickListener(v-> {
            Intent intent = new Intent(AdminActivity.this,DeleteActivity.class);
            startActivity(intent);
        });
        profile.setOnClickListener(v-> {
            Intent intent = new Intent(AdminActivity.this,Admin_Profile.class);
            startActivity(intent);
        });

    }
}

