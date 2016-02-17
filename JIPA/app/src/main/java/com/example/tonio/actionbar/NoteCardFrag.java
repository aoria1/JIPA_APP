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
public class NoteCardFrag extends Fragment {

        NoteCardListener activityCommander;

        public interface NoteCardListener{
            public void ButtonsNC(String button);
        }

        @Override
        public void onAttach(Activity activity){
            super.onAttach(activity);
            try{
                activityCommander = (NoteCardListener)activity;
            }catch (ClassCastException e){
                throw new ClassCastException(activity.toString());
            }
        }
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View v = inflater.inflate(R.layout.notecard, container, false);
            Button answer = (Button)v.findViewById(R.id.answerBtn);
            Button back = (Button)v.findViewById(R.id.backBtn);
            Button next = (Button)v.findViewById(R.id.nextqBtn);
            answer.setOnClickListener(
                    new View.OnClickListener() {
                        public void onClick(View v) {
                            activityCommander.ButtonsNC("answerBtn");
                        }
                    }
            );
            back.setOnClickListener(
                    new View.OnClickListener() {
                        public void onClick(View v) {
                            activityCommander.ButtonsNC("backBtn");
                        }
                    }
            );
            next.setOnClickListener(
                    new View.OnClickListener() {
                        public void onClick(View v) {
                            activityCommander.ButtonsNC("nextBtn");
                        }
                    }
            );
            return v;
        }
}
