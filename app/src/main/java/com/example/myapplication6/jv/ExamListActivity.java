package com.example.myapplication6.jv;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication6.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class ExamListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ExamAdapter adapter;
    private List<DocumentSnapshot> examList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_list);

        recyclerView = findViewById(R.id.recyclerViewExams);
        progressBar = findViewById(R.id.progressBar);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ExamAdapter(examList, doc -> {
            Intent intent = new Intent(this, ExamActivity.class);
            intent.putExtra("documentId", doc.getId());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        fetchExams();
    }

    private void fetchExams() {
        progressBar.setVisibility(View.VISIBLE);
        FirebaseFirestore.getInstance().collection("exams")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    progressBar.setVisibility(View.GONE);
                    if (queryDocumentSnapshots.isEmpty()) {
                        showNoExamsAlert();
                    } else {
                        examList.clear();
                        examList.addAll(queryDocumentSnapshots.getDocuments());
                        adapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    new AlertDialog.Builder(this)
                            .setTitle("Error")
                            .setMessage("Failed to load exams: " + e.getMessage())
                            .setPositiveButton("OK", (d, w) -> finish())
                            .show();
                });
    }

    private void showNoExamsAlert() {
        new AlertDialog.Builder(this)
                .setTitle("No Exams Found")
                .setMessage("There are no AI-generated exams yet. Create one in the Debug menu!")
                .setPositiveButton("OK", (d, w) -> finish())
                .show();
    }

    private static class ExamAdapter extends RecyclerView.Adapter<ExamAdapter.ViewHolder> {
        private final List<DocumentSnapshot> exams;
        private final OnItemClickListener listener;

        interface OnItemClickListener {
            void onItemClick(DocumentSnapshot doc);
        }

        ExamAdapter(List<DocumentSnapshot> exams, OnItemClickListener listener) {
            this.exams = exams;
            this.listener = listener;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exam, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            DocumentSnapshot doc = exams.get(position);
            holder.subject.setText(doc.getString("subject"));
            holder.createdBy.setText("Created by ID: " + doc.getString("createdBy"));
            Object createdAt = doc.get("createdAt");
            holder.date.setText(createdAt != null ? createdAt.toString() : "N/A");

            holder.itemView.setOnClickListener(v -> listener.onItemClick(doc));
        }

        @Override
        public int getItemCount() {
            return exams.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView subject, createdBy, date;

            ViewHolder(View itemView) {
                super(itemView);
                subject = itemView.findViewById(R.id.textViewSubject);
                createdBy = itemView.findViewById(R.id.textViewCreatedBy);
                date = itemView.findViewById(R.id.textViewDate);
            }
        }
    }
}
