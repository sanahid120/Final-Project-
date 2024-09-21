package com.example.electbd;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class View_Results extends AppCompatActivity {
    private ListView resultViewCandidates;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_results);

        resultViewCandidates = findViewById(R.id.result_view_candidates);
        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        displayCandidates();



    }
    private void displayCandidates() {
        DatabaseHelper databaseHelper=new DatabaseHelper(View_Results.this);
        Cursor cursor = databaseHelper.getCandidates();
        ResultViewAdapter adapter = new ResultViewAdapter(this, cursor, 0);
        resultViewCandidates.setAdapter(adapter);

    }
}