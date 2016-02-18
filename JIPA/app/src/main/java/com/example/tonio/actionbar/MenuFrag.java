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

public class MenuFrag extends Fragment {
   private MenuListener activityCommander;

    public interface MenuListener {
        public void ButtonsM(String button);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            activityCommander = (MenuListener) activity;
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


                        activityCommander.ButtonsM("notecard");
                    }
                }
        );
        t.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        activityCommander.ButtonsM("tips");
                    }
                }
        );

        return v;
    }
}
