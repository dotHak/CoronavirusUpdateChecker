package com.hubert.coronavirusUpdate.ui.global;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.hubert.coronavirusUpdate.R;

public class GlobalFragment extends Fragment {

    private GlobalViewModel globalViewModel;
    private String currentCases;
    private String currentDeaths;
    private String currentRecovered;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        globalViewModel = new ViewModelProvider(this).get(GlobalViewModel.class);
        View root = inflater.inflate(R.layout.fragment_global, container, false);
        final TextView casesView = root.findViewById(R.id.global_cases);
        final TextView deathsView = root.findViewById(R.id.global_deaths);
        final TextView recoveredView = root.findViewById(R.id.global_recovered);

        if(currentCases != null){
            casesView.setText(currentCases);
            deathsView.setText(currentDeaths);
            recoveredView.setText(currentRecovered);
        }

        setCases(casesView);
        setDeaths(deathsView);
        setRecovered(recoveredView);
        return root;
    }

    public void setCases(TextView textView){
        globalViewModel.getCases().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                if(!s.equals(currentCases)){
                    textView.setText(s);
                    currentCases = s;
                }
            }
        });
    }

    public void setDeaths(TextView textView){
        globalViewModel.getDeaths().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                if(!s.equals(currentDeaths)){
                    textView.setText(s);
                    currentDeaths = s;
                }
            }
        });
    }

    public void setRecovered(TextView textView){
        globalViewModel.getRecovered().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                if(!s.equals(currentRecovered)){
                    textView.setText(s);
                    currentRecovered = s;
                }
            }
        });
    }

}
