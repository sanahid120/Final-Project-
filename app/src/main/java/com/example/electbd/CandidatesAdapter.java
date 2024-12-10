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

public class CandidatesAdapter extends CursorAdapter {
    private final DatabaseHelper dbHelper;
    private Context context;

    public CandidatesAdapter(Context context, Cursor cursor, int i) {
    super(context,cursor,i);
        this.context = context;
        this.dbHelper = new DatabaseHelper(context);
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.list_candidate, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView nameView = view.findViewById(R.id.tv_candidate_name);
        Button vote = view.findViewById(R.id.bt_vote);
        ImageView candidateImage = view.findViewById(R.id.iv_candidate);
        TextView resultVoteView = view.findViewById(R.id.tv_candidate_results);
        TextView resultNameView = view.findViewById(R.id.list_view_results);
        ImageView resultImageView = view.findViewById(R.id.iv_result_candidate);

        String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_NAME));
        byte[] imageBytes = cursor.getBlob(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PRODUCT_IMAGE_URI));
        int candidateID = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));

        nameView.setText(name);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        candidateImage.setImageBitmap(bitmap);
        if (dbHelper.hasVoted(UserProfile.userinfo)) {
            vote.setEnabled(false);
        } else {
            vote.setEnabled(true);
        }
        vote.setOnClickListener(v -> {
                boolean success = dbHelper.incrementVoteCount(candidateID);
                if (success) {
                    Toast.makeText(context, "Vote recorded for " + name, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, View_Results.class);
                    context.startActivity(intent);
                    dbHelper.setVoted(UserProfile.userinfo);
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(context, "Failed to record vote", Toast.LENGTH_SHORT).show();
                }
        });
    }
}
