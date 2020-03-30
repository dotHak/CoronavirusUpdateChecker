package com.hubert.coronavirusUpdate.ui.about;

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

public class AboutFragment extends Fragment {

    private AboutViewModel aboutViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        aboutViewModel = new ViewModelProvider(this).get(AboutViewModel.class);
        View root = inflater.inflate(R.layout.fragment_about, container, false);
        final TextView textView = root.findViewById(R.id.text_about);
        setRetainInstance(true);
        final TextView moreData = root.findViewById(R.id.aboutVirus);
        final TextView hSpread = root.findViewById(R.id.spreadWays);
        final TextView mSpread = root.findViewById(R.id.howSpread);
        final TextView mSigns = root.findViewById(R.id.mSigns);
        final TextView hSigns = root.findViewById(R.id.hSigns);
        final TextView hPrevent = root.findViewById(R.id.hPrevent);
        final TextView mPrevent = root.findViewById(R.id.mPrevent);
        final TextView mTreatment = root.findViewById(R.id.mTreatment);
        final TextView hTreatment = root.findViewById(R.id.hTreatment);

        aboutViewModel.getTextAbout().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        aboutViewModel.getDataOnVirus().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                moreData.setText(s);
            }
        });

        aboutViewModel.getSpreadVirus().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                hSpread.setText(s);
            }
        });

        aboutViewModel.getHowVirusSpread().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                mSpread.setText(s);
            }
        });

        aboutViewModel.getMSigns().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                mSigns.setText(s);
            }
        });

        aboutViewModel.getHSigns().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                hSigns.setText(s);
            }
        });

        aboutViewModel.getHPrevent().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                hPrevent.setText(s);
            }
        });

        aboutViewModel.getMPrevent().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                mPrevent.setText(s);
            }
        });

        aboutViewModel.getMTreatment().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                mTreatment.setText(s);
            }
        });

        aboutViewModel.getHTreatment().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                hTreatment.setText(s);
            }
        });

        return root;
    }
}
