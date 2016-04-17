package com.example.tonio.actionbar;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by tonio on 4/11/2016.
 */
public class SearchInterface extends Fragment {
    private SearchListener activityCommander;

    public interface SearchListener {
        public void SearchInterfaceButtons(String button);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            activityCommander = (SearchListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString());
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.search_interface, container, false);

        Button Back = (Button)v.findViewById(R.id.bckSearch);


        Back.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        activityCommander.SearchInterfaceButtons("back");

                    }
                }
        );

        return  v;
    }
}
