package com.example.tonio.actionbar;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * Created by tonio on 2/15/2016.
 */
public class NoteCardInterface extends Fragment {

      private   NoteCardListener activityCommander;

        public interface NoteCardListener{
            public void NoteCardInterfaceButtons(String button);

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
            ImageButton like = (ImageButton)v.findViewById(R.id.likebtn);
            ImageButton dislike = (ImageButton)v.findViewById(R.id.dislikebtn);
            FloatingActionButton fav = (FloatingActionButton)v.findViewById(R.id.fab);

            fav.setOnClickListener(
                    new View.OnClickListener() {
                        public void onClick(View v) {
                            activityCommander.NoteCardInterfaceButtons("fav");
                        }
                    }
            );
            like.setOnClickListener(
                    new View.OnClickListener() {
                        public void onClick(View v) {
                            activityCommander.NoteCardInterfaceButtons("like");
                        }
                    }
            );
            dislike.setOnClickListener(
                    new View.OnClickListener() {
                        public void onClick(View v) {
                            activityCommander.NoteCardInterfaceButtons("dislike");
                        }
                    }
            );
            answer.setOnClickListener(
                    new View.OnClickListener() {
                        public void onClick(View v) {
                            activityCommander.NoteCardInterfaceButtons("answerBtn");
                        }
                    }
            );
            back.setOnClickListener(
                    new View.OnClickListener() {
                        public void onClick(View v) {
                            activityCommander.NoteCardInterfaceButtons("backBtn");
                        }
                    }
            );
            next.setOnClickListener(
                    new View.OnClickListener() {
                        public void onClick(View v) {
                            activityCommander.NoteCardInterfaceButtons("nextBtn");
                        }
                    }
            );
            return v;
        }
}
