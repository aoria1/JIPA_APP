package com.example.tonio.actionbar;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by tonio on 3/28/2016.
 */
public class QuizScreen extends Fragment {
    private QuizListener activityCommander;

    public interface QuizListener {
        public void QuizButtons(String button);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            activityCommander = (QuizListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString());
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.quiz, container, false);

        Button nextQuestion = (Button)v.findViewById(R.id.nextQuestion);
        nextQuestion.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {


                        activityCommander.QuizButtons("nextQuestion");
                    }
                }
        );

        return v;
    }
}
