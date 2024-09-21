package com.example.electbd;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ResultViewAdapter extends CursorAdapter {
    private final DatabaseHelper dbHelper;
    private Context context;

    public ResultViewAdapter(Context context, Cursor cursor, int i) {
        super(context,cursor,i);
        this.context = context;
        this.dbHelper = new DatabaseHelper(context);


    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.just_result_view, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView resultVoteView = view.findViewById(R.id.tv_candidate_results);
        TextView resultNameView = view.findViewById(R.id.list_view_results);
        ImageView resultImageView = view.findViewById(R.id.iv_result_candidate);


        String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_NAME));
        byte[] imageBytes = cursor.getBlob(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PRODUCT_IMAGE_URI));

        int votes =cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_VOTES));

        resultNameView.setText(name);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        resultImageView.setImageBitmap(bitmap);
        resultVoteView.setText(String.valueOf(votes));
    }





}
