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
public class LoginFrag extends Fragment {
    private LogListener activityCommander;

    public interface LogListener {
        public void ButtonsL(String button);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            activityCommander = (LogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString());
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.login, container, false);

        Button logBtn = (Button) v.findViewById(R.id.lgBtn3);

        logBtn.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        activityCommander.ButtonsL("login");
                    }
                }
        );

        return v;
    }
}
