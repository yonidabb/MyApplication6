package com.example.myapplication6.jv;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication6.R;
import com.example.myapplication6.models.Score;

import java.util.ArrayList;
import java.util.List;

public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ScoreVH> {

    private final List<Score> items = new ArrayList<>();

    // ✅ האם להציג את המשתמש (UID / שם)
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

        // ✅ Your Scores -> לא מציגים UID (אבל משאירים את ה-TextView כדי לא לשבור XML)
        holder.userText.setText(showUser ? s.getUser() : "");

        // ✅ לא להשתמש ב-getScoreInSeconds כי אצלך score כבר בשניות
        holder.scoreText.setText(s.getScore() + "s");
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