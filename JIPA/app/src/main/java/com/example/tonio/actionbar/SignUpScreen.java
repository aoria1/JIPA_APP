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
public class SignUpScreen extends Fragment {
   private SignUpListener activityCommander;
    private EditText firstn;
    private EditText lastn;
    private EditText email;
    private EditText password;

    public interface SignUpListener{
        public void SignUpButtons(String button,String firstName,String lastName,String email,String password);
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        try{
            activityCommander = (SignUpListener)activity;
        }catch (ClassCastException e){
            throw new ClassCastException(activity.toString());
        }
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.signup, container, false);

        Button createBtn = (Button)v.findViewById(R.id.caBtn);
        TextView allA = (TextView)v.findViewById(R.id.alog);

        firstn = (EditText)v.findViewById(R.id.firstN);
        lastn = (EditText)v.findViewById(R.id.lastN);
        email = (EditText)v.findViewById(R.id.emailS);
        password = (EditText)v.findViewById(R.id.passwordS);
        allA.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        activityCommander.SignUpButtons("allBtn",null,null,null,null);
                    }
                }
        );
        createBtn.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        String fn;
                        String ln;
                        String e;
                        String p;
                        fn = firstn.getText().toString();
                        ln = lastn.getText().toString();
                        e = email.getText().toString();
                        p = password.getText().toString();
                        activityCommander.SignUpButtons("creatBtn",fn,ln,e,p);

                    }
                }
        );
        return v;
    }
}
