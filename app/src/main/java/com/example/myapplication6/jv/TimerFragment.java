package com.example.myapplication6.jv;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication6.ActivityTimer;
import com.example.myapplication6.R;
import com.example.myapplication6.jv.ScoresActivity;

public class TimerFragment extends Fragment {

    private TextView timerText;
    ActivityTimer activityTimer = ActivityTimer.getInstance();


    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_timer, container, false);
        timerText = view.findViewById(R.id.timerText);
        activityTimer.setFragment(this);
        activityTimer.startTimer();
        return view;
    }



    public void updateUI() {
        long totalSeconds = activityTimer.getTimeLeftMillis() / 1000;
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        timerText.setText(String.format("%02d:%02d", minutes, seconds));
//        Log.d("TimerFragment", " -> game time: "+activityTimer.getGameTime() );

    }

    private void goToScore() {
        Intent intent = new Intent(getActivity(), ScoresActivity.class);
        intent.putExtra("FINAL_TIME", activityTimer.getTimeLeftMillis());
        startActivity(intent);
        requireActivity().finish();
    }
}