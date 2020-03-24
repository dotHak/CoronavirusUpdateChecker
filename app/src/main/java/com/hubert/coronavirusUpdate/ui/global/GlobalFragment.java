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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.hubert.coronavirusUpdate.R;
import com.hubert.coronavirusUpdate.model.Country;
import com.hubert.coronavirusUpdate.model.SearchAdapter;

import java.text.NumberFormat;
import java.util.List;

public class GlobalFragment extends Fragment{

    private GlobalViewModel globalViewModel;
    private String currentCases;
    private String currentDeaths;
    private String currentRecovered;
    private SharedPreferences sharedPreferences;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        globalViewModel = new ViewModelProvider(this).get(GlobalViewModel.class);
        View root = inflater.inflate(R.layout.fragment_global, container, false);
        final TextView casesView = root.findViewById(R.id.global_cases);
        final TextView deathsView = root.findViewById(R.id.global_deaths);
        final TextView recoveredView = root.findViewById(R.id.global_recovered);
        final AutoCompleteTextView textInputLayout = root.findViewById(R.id.text_auto);
        List<String> countries = globalViewModel.getCountryNames();

        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        if(currentCases != null){
            casesView.setText(currentCases);
            deathsView.setText(currentDeaths);
            recoveredView.setText(currentRecovered);
        }

        setCases(casesView);
        setDeaths(deathsView);
        setRecovered(recoveredView);
        setCountryData(root);

        ArrayAdapter adapter = new ArrayAdapter(requireContext(), R.layout.list_item, countries);
        textInputLayout.setAdapter(adapter);
        textInputLayout.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String countryString = parent.getItemAtPosition(position).toString();
                sharedPreferences.edit().putString("country", countryString).apply();
                setCountryData(root);
            }
        });
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

    private void setCountryData(View view){
        String countryString = sharedPreferences.getString("country","Ghana");
        globalViewModel.setCurrentCountry(countryString);
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
                cases.setText(NumberFormat.getInstance().format(country.getCases()));
                todayCases.setText(NumberFormat.getInstance().format(country.getTodayCases()));
                active.setText(NumberFormat.getInstance().format(country.getActive()));
                deaths.setText(NumberFormat.getInstance().format(country.getDeaths()));
                todayDeaths.setText(NumberFormat.getInstance().format(country.getTodayDeaths()));
                recovered.setText(NumberFormat.getInstance().format(country.getRecovered()));
                critical.setText(NumberFormat.getInstance().format(country.getCritical()));

                int resId;
                Context context = flagView.getContext();
                if(country.getName().equals("USA")){
                    resId = context.getResources().getIdentifier("us_16","drawable",context.getPackageName());
                }else {
                    SearchAdapter.processCountryCode();
                    resId = context.getResources().getIdentifier(SearchAdapter.countriesCode.get(country.getName())+"_16","drawable",context.getPackageName());
                }
                flagView.setImageResource(resId);
            }
        });

    }

    public void showError(String text, Context context){
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

}
