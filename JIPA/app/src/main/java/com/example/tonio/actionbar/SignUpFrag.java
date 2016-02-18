package com.example.tonio.actionbar;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by tonio on 2/15/2016.
 */
public class SignUpFrag extends Fragment {
   private SignUpListener activityCommander;
    public interface SignUpListener{
        public void ButtonsNC(String button);
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
        allA.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        activityCommander.ButtonsNC("allBtn");
                    }
                }
        );
        createBtn.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        activityCommander.ButtonsNC("creatBtn");
                    }
                }
        );
        return v;
    }
}
