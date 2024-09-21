package com.example.electbd;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CandidatesActivity extends AppCompatActivity {
    private ListView listViewCandidates;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidates);

        TextView results =findViewById(R.id.bt_view_result);
        TextView logout = findViewById(R.id.tv_logout_candidate);
        TextView profile = findViewById(R.id.tv_candidate_profile);
        listViewCandidates = findViewById(R.id.list_view_Candidates);

        logout.setOnClickListener(v->{
            Intent intent = new Intent(CandidatesActivity.this, MainActivity.class);
            startActivity(intent);
        });
        profile.setOnClickListener(v->{
            Intent intent = new Intent(CandidatesActivity.this, MainActivity.class);
            startActivity(intent);
        });

        displayCandidates();
        results.setOnClickListener(v->{
            Intent intent = new Intent(CandidatesActivity.this, View_Results.class);
            startActivity(intent);
        });


        }
    private void displayCandidates() {
        DatabaseHelper databaseHelper=new DatabaseHelper(CandidatesActivity.this);
        Cursor cursor = databaseHelper.getCandidates();
        CandidatesAdapter adapter = new CandidatesAdapter(this, cursor, 0);
        listViewCandidates.setAdapter(adapter);

    }

}