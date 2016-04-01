package com.example.tonio.actionbar;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by tonio on 2/15/2016.
 */

public class HomeInterface extends Fragment {
   private HomeListener activityCommander;

    public interface HomeListener {
        public void HomeInterfaceButtons(String button);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            activityCommander = (HomeListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString());
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.menu, container, false);
        Button nc = (Button) v.findViewById(R.id.NoteCards);
        Button t = (Button)v.findViewById(R.id.tips);
        Button quiz = (Button)v.findViewById(R.id.quiz);
        quiz.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {


                        activityCommander.HomeInterfaceButtons("quiz");
                    }
                }
        );
        nc.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {


                        activityCommander.HomeInterfaceButtons("notecard");
                    }
                }
        );
        t.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        activityCommander.HomeInterfaceButtons("tips");
                    }
                }
        );

        return v;
    }
}
