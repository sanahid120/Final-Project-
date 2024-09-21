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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        Button view =findViewById(R.id.btn_view_candidates);
        Button insert =findViewById(R.id.btn_insert_candidates);
        Button update =findViewById(R.id.btn_update_candidates);
        Button delete =findViewById(R.id.btn_dlt_candidates);
        TextView profile =findViewById(R.id.btn_profile);
        TextView id_server =findViewById(R.id.btn_id_server);



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
        id_server.setOnClickListener(v-> {
            Intent intent = new Intent(AdminActivity.this,Id_Server.class);
            startActivity(intent);
        });

    }
}
