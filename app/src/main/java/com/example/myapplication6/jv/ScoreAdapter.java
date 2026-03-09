package com.example.myapplication6.jv;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication6.R;
import com.example.myapplication6.models.Score;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ScoreVH> {

    private final List<Score> items = new ArrayList<>();
    private final Map<String, String> nameCache = new HashMap<>();

    private boolean showUser = true;

    public void setShowUser(boolean showUser) {
        this.showUser = showUser;
        notifyDataSetChanged();
    }

    public void setItems(List<Score> newItems) {
        items.clear();
        if (newItems != null) items.addAll(newItems);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ScoreVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_score, parent, false);
        return new ScoreVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreVH holder, int position) {
        Score s = items.get(position);

        holder.scoreText.setText(s.getScore() + "s");

        if (!showUser) {
            holder.userText.setVisibility(View.GONE);
            resetRowStyle(holder);
            return;
        }

        holder.userText.setVisibility(View.VISIBLE);

        String uid = s.getUser();
        String currentUid = FirebaseAuth.getInstance().getCurrentUser() != null
                ? FirebaseAuth.getInstance().getCurrentUser().getUid()
                : null;

        if (uid == null || uid.trim().isEmpty()) {
            holder.userText.setText(getRankPrefix(position) + "Unknown Player");
            resetRowStyle(holder);
            return;
        }

        if (nameCache.containsKey(uid)) {
            String name = nameCache.get(uid);
            bindRow(holder, position, uid, currentUid, name);
            return;
        }

        holder.userText.setText(getRankPrefix(position) + "Loading...");
        resetRowStyle(holder);

        FirebaseFirestore.getInstance()
                .collection("profiles")
                .document(uid)
                .get()
                .addOnSuccessListener(document -> {
                    String name = "Unknown Player";

                    if (document.exists()) {
                        String dbName = document.getString("name");
                        if (dbName != null && !dbName.trim().isEmpty()) {
                            name = dbName;
                        }
                    }

                    nameCache.put(uid, name);

                    int currentPos = holder.getAdapterPosition();
                    if (currentPos != RecyclerView.NO_POSITION) {
                        Score currentScore = items.get(currentPos);
                        if (uid.equals(currentScore.getUser())) {
                            bindRow(holder, currentPos, uid, currentUid, name);
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    nameCache.put(uid, "Unknown Player");

                    int currentPos = holder.getAdapterPosition();
                    if (currentPos != RecyclerView.NO_POSITION) {
                        Score currentScore = items.get(currentPos);
                        if (uid.equals(currentScore.getUser())) {
                            bindRow(holder, currentPos, uid, currentUid, "Unknown Player");
                        }
                    }
                });
    }

    private void bindRow(ScoreVH holder, int position, String uid, String currentUid, String name) {
        String displayName = getRankPrefix(position) + name;

        if (currentUid != null && currentUid.equals(uid)) {
            displayName += " (You)";
            highlightCurrentUser(holder);
        } else {
            resetRowStyle(holder);
        }

        holder.userText.setText(displayName);
    }

    private String getRankPrefix(int position) {
        int rank = position + 1;

        switch (rank) {
            case 1:
                return "🥇 ";
            case 2:
                return "🥈 ";
            case 3:
                return "🥉 ";
            default:
                return rank + ". ";
        }
    }

    private void highlightCurrentUser(ScoreVH holder) {
        holder.itemView.setBackgroundColor(0x223CFF9B); // ירקרק עדין
        holder.userText.setTypeface(null, Typeface.BOLD);
        holder.scoreText.setTypeface(null, Typeface.BOLD);
        holder.userText.setTextColor(0xFFFFFFFF);
        holder.scoreText.setTextColor(0xFF7CFFB2);
    }

    private void resetRowStyle(ScoreVH holder) {
        holder.itemView.setBackgroundColor(0xFF1B2432);
        holder.userText.setTypeface(null, Typeface.BOLD);
        holder.scoreText.setTypeface(null, Typeface.BOLD);
        holder.userText.setTextColor(0xFFFFFFFF);
        holder.scoreText.setTextColor(0xFF7CFFB2);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ScoreVH extends RecyclerView.ViewHolder {
        TextView userText, scoreText;

        ScoreVH(@NonNull View itemView) {
            super(itemView);
            userText = itemView.findViewById(R.id.userText);
            scoreText = itemView.findViewById(R.id.scoreText);
        }
    }
}