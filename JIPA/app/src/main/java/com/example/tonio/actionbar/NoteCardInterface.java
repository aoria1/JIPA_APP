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
        private   ImageButton like;
        private ImageButton dislike;
        private boolean checkImage;
        private ImageButton search;
        private  FloatingActionButton fav;
        private int checklike = 0;
        private int checkdislike = 0;


        public interface NoteCardListener{
            public void NoteCardInterfaceButtons(String button, int check);

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
             like = (ImageButton)v.findViewById(R.id.likebtn);
            dislike = (ImageButton)v.findViewById(R.id.dislikebtn);
            fav = (FloatingActionButton)v.findViewById(R.id.fab);
            search = (ImageButton)v.findViewById(R.id.searchbtn);
            fav.setOnClickListener(
                    new View.OnClickListener() {
                        public void onClick(View v) {
                            activityCommander.NoteCardInterfaceButtons("fav",0);
                            if(fav.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.ic_favorite_border_blue_24dp).getConstantState()){
                                fav.setImageResource(R.drawable.ic_favorite_blue_24dp);

                            }else
                            fav.setImageResource(R.drawable.ic_favorite_border_blue_24dp);


                        }
                    }
            );
            like.setOnClickListener(
                    new View.OnClickListener() {
                        public void onClick(View v) {

                            activityCommander.NoteCardInterfaceButtons("like",checklike);
                            like.setImageResource(R.drawable.thumbsupgreen);
                            checkImage = true;
                            if(checklike == 1){
                                like.setImageResource(R.drawable.thumbsup);
                                checklike = 0;
                            }else {
                                checklike = 1;
                            }

                            if(checkImage=true){
                                dislike.setImageResource(R.drawable.thumbsdown);

                            }
                            checkImage = false;
                        }
                    }
            );
            dislike.setOnClickListener(
                    new View.OnClickListener() {
                        public void onClick(View v) {
                            activityCommander.NoteCardInterfaceButtons("dislike",checkdislike);
                            dislike.setImageResource(R.drawable.thumbsdownred);
                            checkImage = true;
                            if(checkdislike == 1){
                                dislike.setImageResource(R.drawable.thumbsdown);
                                checkdislike = 0;
                            }else {
                                checkdislike = 1;
                            }

                            if(checkImage=true){
                                like.setImageResource(R.drawable.thumbsup);

                            }
                            checkImage = false;
                        }
                    }
            );
            answer.setOnClickListener(
                    new View.OnClickListener() {
                        public void onClick(View v) {
                            activityCommander.NoteCardInterfaceButtons("answerBtn",0);
                        }
                    }
            );
            back.setOnClickListener(
                    new View.OnClickListener() {
                        public void onClick(View v) {
                            activityCommander.NoteCardInterfaceButtons("backBtn,",0);

                        }
                    }
            );

            next.setOnClickListener(
                    new View.OnClickListener() {
                        public void onClick(View v) {
                            activityCommander.NoteCardInterfaceButtons("nextBtn",0);

                        }
                    }
            );
            search.setOnClickListener(
                    new View.OnClickListener() {
                        public void onClick(View v) {
                            activityCommander.NoteCardInterfaceButtons("search",0);

                        }
                    }
            );
            return v;
        }
}
