package com.example.tonio.actionbar;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by tonio on 2/15/2016.
 */
public class LoginScreen extends Fragment {
    private LogListener activityCommander;
    private EditText email;
    private EditText password;
    public interface LogListener {
        public void LoginScreenButtons(String button,String email, String password);
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
        TextView signTxt = (TextView)v.findViewById(R.id.notMem);
         email = (EditText)v.findViewById(R.id.email);
        password = (EditText)v.findViewById(R.id.password);
        signTxt.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {

                        activityCommander.LoginScreenButtons("signUp",null,null);

                    }
                }
        );

        logBtn.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        String e;
                        String p;
                       e = email.getText().toString();
                        p = password.getText().toString();
                        activityCommander.LoginScreenButtons("login",e,p);

                    }
                }
        );

        return v;
    }
}
