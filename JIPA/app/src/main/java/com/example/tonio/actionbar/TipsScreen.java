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
public class TipsScreen extends Fragment {
   private TipsListener activityCommander;

    public interface TipsListener{
        public void TipsButtons(String button);
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        try{
            activityCommander = (TipsListener)activity;
        }catch (ClassCastException e){
            throw new ClassCastException(activity.toString());
        }
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.tips, container, false);

        Button backtips = (Button)v.findViewById(R.id.bckTips);
        Button next = (Button)v.findViewById(R.id.nxtTip);
        Button backtotips = (Button)v.findViewById(R.id.backToTipsButton);
       Button details = (Button)v.findViewById(R.id.details);
        backtotips.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        activityCommander.TipsButtons("backToTips");
                    }
                }
        );
        details.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        activityCommander.TipsButtons("details");
                    }
                }
        );
        backtips.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        activityCommander.TipsButtons("back");
                    }
                }
        );
        next.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        activityCommander.TipsButtons("next");
                    }
                }
        );

        return v;
    }
}
