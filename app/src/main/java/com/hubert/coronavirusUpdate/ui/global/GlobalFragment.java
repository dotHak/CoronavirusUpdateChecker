package com.hubert.coronavirusUpdate.ui.global;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.hubert.coronavirusUpdate.MainActivity;
import com.hubert.coronavirusUpdate.R;
import com.hubert.coronavirusUpdate.model.Country;
import com.hubert.coronavirusUpdate.model.SearchAdapter;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class GlobalFragment extends Fragment{

    private GlobalViewModel globalViewModel;
    private SharedPreferences sharedPreferences;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        globalViewModel = new ViewModelProvider(this).get(GlobalViewModel.class);
        View root = inflater.inflate(R.layout.fragment_global, container, false);
        setRetainInstance(true);

        final TextView casesView = root.findViewById(R.id.global_cases);
        final TextView deathsView = root.findViewById(R.id.global_deaths);
        final TextView recoveredView = root.findViewById(R.id.global_recovered);
        final AutoCompleteTextView textInputLayout = root.findViewById(R.id.text_auto);

        sharedPreferences = getActivity().getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        setCases(casesView);
        setDeaths(deathsView);
        setRecovered(recoveredView);
        setCountryData(root);

        List<String> countries = new ArrayList<>(globalViewModel.getCountryNames());
        ArrayAdapter adapter = new ArrayAdapter(requireContext(), R.layout.list_item, countries);
        textInputLayout.setAdapter(adapter);


        textInputLayout.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String countryString = parent.getItemAtPosition(position).toString();
                sharedPreferences.edit().putString(
                        MainActivity.newCurrentCountryName, countryString).apply();
                setCountryData(root);
            }
        });

        return root;
    }

    private void setCases(TextView textView){
        globalViewModel.getCases().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
                textView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setDeaths(TextView textView){
        globalViewModel.getDeaths().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
                textView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setRecovered(TextView textView){
        globalViewModel.getRecovered().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
                    textView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setCountryData(View view){
        TextView cases = view.findViewById(R.id.country_search_cases);
        TextView active = view.findViewById(R.id.country_search_active);
        TextView name = view.findViewById(R.id.country_search_name);
        TextView todayCases = view.findViewById(R.id.country_search_today_cases);
        TextView deaths = view.findViewById(R.id.country_search_deaths);
        TextView todayDeaths = view.findViewById(R.id.country_search_today_deaths);
        TextView recovered = view.findViewById(R.id.country_search_recovered);
        TextView critical = view.findViewById(R.id.country_search_critical);
        ImageView flagView = view.findViewById(R.id.flagView);

        globalViewModel.getCurrentCountry().observe(getViewLifecycleOwner(), new Observer<Country>() {
            @Override
            public void onChanged(Country country) {
                name.setText(country.getName());
                name.setVisibility(View.VISIBLE);
                cases.setText(NumberFormat.getInstance().format(country.getCases()));
                cases.setVisibility(View.VISIBLE);
                todayCases.setText(NumberFormat.getInstance().format(country.getTodayCases()));
                todayCases.setVisibility(View.VISIBLE);
                active.setText(NumberFormat.getInstance().format(country.getActive()));
                active.setVisibility(View.VISIBLE);
                todayCases.setVisibility(View.VISIBLE);
                deaths.setText(NumberFormat.getInstance().format(country.getDeaths()));
                deaths.setVisibility(View.VISIBLE);
                todayDeaths.setText(NumberFormat.getInstance().format(country.getTodayDeaths()));
                todayDeaths.setVisibility(View.VISIBLE);
                recovered.setText(NumberFormat.getInstance().format(country.getRecovered()));
                recovered.setVisibility(View.VISIBLE);
                critical.setText(NumberFormat.getInstance().format(country.getCritical()));
                critical.setVisibility(View.VISIBLE);

                SearchAdapter.processCountryCode();
                Context context = flagView.getContext();
                int resId = !(SearchAdapter.countriesCode.get(country.getName()) == null)?
                        context.getResources().getIdentifier(
                                SearchAdapter.countriesCode.get(country.getName())+"_16","drawable",
                                context.getPackageName()): context.getResources().getIdentifier("unknown",
                        "drawable", context.getPackageName());

                flagView.setImageResource(resId);
            }
        });

    }
}
