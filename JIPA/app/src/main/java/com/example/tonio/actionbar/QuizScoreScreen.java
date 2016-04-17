package com.example.tonio.actionbar;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by tonio on 4/3/2016.
 */
public class QuizScoreScreen extends Fragment {
    private QuizScoreListener activityCommander;

    public interface QuizScoreListener {
        public void QuizScoreButtons(String button);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            activityCommander = (QuizScoreListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString());
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.quiz_score, container, false);

        Button done = (Button)v.findViewById(R.id.done);
        done.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {


                        activityCommander.QuizScoreButtons("done");
                    }
                }
        );

        return v;
    }
}
