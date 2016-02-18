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

public class HomeFrag extends Fragment {
   private HomeListener activityCommander;

    public interface HomeListener {
        public void ButtonsH(String button);
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
        nc.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {


                        activityCommander.ButtonsH("notecard");
                    }
                }
        );
        t.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        activityCommander.ButtonsH("tips");
                    }
                }
        );

        return v;
    }
}
