package com.hubert.coronavirusUpdate.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.hubert.coronavirusUpdate.R;
import com.hubert.coronavirusUpdate.model.Country;
import com.hubert.coronavirusUpdate.model.SearchAdapter;

import java.util.List;

public class SearchFragment extends Fragment {
    private SearchView searchView;
    private ListView listView;
    private SearchAdapter adapter;
    private SearchViewModel searchViewModel;
    private List<Country> countryList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        final View view = inflater.inflate(R.layout.fragment_search, container, false);
        searchView = view.findViewById(R.id.searchView);
        adapter = new SearchAdapter(getActivity(), countryList);

        if(countryList != null){
            listView = view.findViewById(R.id.searchList);
            listView.setAdapter(adapter);
        }

        searchViewModel.getCountryList().observe(getViewLifecycleOwner(), new Observer<List<Country>>() {
            @Override
            public void onChanged(List<Country> countries) {
                if(!countries.equals(countryList)){
                    adapter = new SearchAdapter(getActivity(), countries);
                    listView = view.findViewById(R.id.searchList);
                    listView.setAdapter(adapter);
                    countryList = countries;
                }
            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });


        return view;
    }


    public void showError(String text){
        Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
    }


}
