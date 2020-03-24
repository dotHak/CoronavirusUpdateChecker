package com.hubert.coronavirusUpdate.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hubert.coronavirusUpdate.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SearchAdapter extends BaseAdapter implements Filterable {

    private Context context;
    private List<Country> originalCountries;
    private List<Country> countries;
    private ListFilter listFilter;
    public static Map<String, String> countriesCode = new HashMap<>();

    public SearchAdapter(@NonNull Context context, List<Country> objects) {
        this.context = context;
        countries = objects;
        originalCountries = objects;
    }

    @Override
    public int getCount() {
        return countries.size();
    }

    @Override
    public Object getItem(int position) {
        return countries.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Country country = (Country) getItem(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.search_list_item, parent, false);
        processCountryCode();

        TextView cases = convertView.findViewById(R.id.country_search_cases);
        TextView active = convertView.findViewById(R.id.country_search_active);
        TextView name = convertView.findViewById(R.id.country_search_name);
        TextView todayCases = convertView.findViewById(R.id.country_search_today_cases);
        TextView deaths = convertView.findViewById(R.id.country_search_deaths);
        TextView todayDeaths = convertView.findViewById(R.id.country_search_today_deaths);
        TextView recovered = convertView.findViewById(R.id.country_search_recovered);
        TextView critical = convertView.findViewById(R.id.country_search_critical);
        ImageView flagView = convertView.findViewById(R.id.flagView);

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
            resId = context.getResources().getIdentifier(countriesCode.get(country.getName())+"_16","drawable",context.getPackageName());
        }
        flagView.setImageResource(resId);

        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        if(listFilter == null){
            listFilter = new ListFilter();
        }
        return listFilter;
    }

    public static void processCountryCode(){
        if(countriesCode.isEmpty()) {
            for (String iso : Locale.getISOCountries()) {
                Locale l = new Locale("", iso);
                countriesCode.put(l.getDisplayCountry(), iso.toLowerCase());
            }
        }

    }

    private class ListFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            final FilterResults results = new FilterResults();
            if(constraint == null || constraint.length() == 0){ ;
                results.values = originalCountries;
                results.count = originalCountries.size();
            }else {
                final String constraintString = constraint.toString().toLowerCase();

                final List<Country> values = originalCountries;

                final List<Country> newValues = new ArrayList<>();

                for (Country value: originalCountries) {
                    final String valueText = value.getName().toLowerCase();

                    if (valueText.startsWith(constraintString)) {
                        newValues.add(value);
                    } else {
                        final String[] words = valueText.split(" ");
                        for (String word : words) {
                            if (word.startsWith(constraintString)) {
                                newValues.add(value);
                                break;
                            }
                        }
                    }
                }

                results.values = newValues;
                results.count = newValues.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
             countries = (List<Country>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }

}

