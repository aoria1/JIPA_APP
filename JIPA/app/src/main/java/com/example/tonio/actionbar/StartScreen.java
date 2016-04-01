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
public class StartScreen extends Fragment {
    StartListener activityCommander;

    public interface StartListener{
        public void StartScreenButtons(String button);
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        try{
            activityCommander = (StartListener)activity;
        }catch (ClassCastException e){
            throw new ClassCastException(activity.toString());
        }
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.firstscreen, container, false);
        Button sigBtn = (Button)v.findViewById(R.id.signupBtn);
        Button logBtn = (Button)v.findViewById(R.id.loginBtn);

        sigBtn.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        activityCommander.StartScreenButtons("signBtn");
                    }
                }
        );
        logBtn.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        activityCommander.StartScreenButtons("logBtn");
                    }
                }
        );

        return v;
    }
}
