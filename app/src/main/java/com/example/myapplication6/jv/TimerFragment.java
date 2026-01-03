package com.example.myapplication6.jv;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication6.R;
import com.example.myapplication6.jv.ScoresActivity;

public class TimerFragment extends Fragment {

    private TextView timerText;
    private CountDownTimer timer;

    private long timeLeftMillis = 5 * 60 * 1000; // 5 דקות

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_timer, container, false);
        timerText = view.findViewById(R.id.timerText);
        startTimer();
        return view;
    }

    private void startTimer() {
        timer = new CountDownTimer(timeLeftMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftMillis = millisUntilFinished;
                updateUI();
            }

            @Override
            public void onFinish() {
                Intent intent = new Intent(getActivity(), ScoresActivity.class);
                intent.putExtra("FINAL_SCORE", timeLeftMillis);
                intent.putExtra("USER_NAME", "demo_user"); // זמני, נחליף בהתחברות
                startActivity(intent);
                requireActivity().finish();
            }
        }.start();
    }

    private void updateUI() {
        long totalSeconds = timeLeftMillis / 1000;
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        timerText.setText(String.format("%02d:%02d", minutes, seconds));
    }

    private void goToScore() {
        Intent intent = new Intent(getActivity(), ScoresActivity.class);
        intent.putExtra("FINAL_TIME", timeLeftMillis);
        startActivity(intent);
        requireActivity().finish();
    }
}