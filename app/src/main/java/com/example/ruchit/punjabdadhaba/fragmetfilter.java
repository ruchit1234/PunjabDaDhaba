package com.example.ruchit.punjabdadhaba;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yalantis.filter.adapter.FilterAdapter;
import com.yalantis.filter.listener.FilterListener;
import com.yalantis.filter.widget.Filter;
import com.yalantis.filter.widget.FilterItem;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class fragmetfilter extends Fragment implements FilterListener<Tag> {


    private int[] mColors;
    private String[] mTitles;
    private Filter<Tag> mFilter;
    public static fragmetfilter newInstance() {
        fragmetfilter fragment = new fragmetfilter();
        return fragment;
    }
    public fragmetfilter() {

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter, container, false);
        mColors = getResources().getIntArray(R.array.colors);
        mTitles = getResources().getStringArray(R.array.filter_titles);

        mFilter = view.findViewById(R.id.filter);
        mFilter.setAdapter(new Adapter(getTags()));
        mFilter.setListener(this);
        mFilter.setNoSelectedItemText("Select A Category");
        mFilter.build();
      
        return view;
    }


    private List<Tag> getTags() {
        List<Tag> tags = new ArrayList<>();

        for (int i = 0; i < mTitles.length; ++i) {
            tags.add(new Tag(mTitles[i], mColors[i]));
        }

        return tags;
    }
   
   
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onFilterDeselected(Tag tag) {
        SharedPreferences settings = getActivity().getSharedPreferences("PREFS_NAME", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.remove("name");
        editor.commit();

    }

    @Override
    public void onFilterSelected(Tag tag) {
        if (tag.getText().equals("All Items")) {
            mFilter.deselectAll();
            SharedPreferences settings = getActivity().getSharedPreferences("PREFS_NAME", MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.remove("name");
            editor.commit();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

            OrderItemFragment orderItemFragment = new OrderItemFragment();
            transaction.add(R.id.main_frame, orderItemFragment);
            transaction.commit();
            TextView tv=getActivity().findViewById(R.id.title);
            tv.setText("All Items");
        }
        else {
            String s = tag.getText();
            SharedPreferences settings = getActivity().getSharedPreferences("PREFS_NAME", MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("name", s);
            editor.commit();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            OrderItemFragment orderItemFragment = new OrderItemFragment();
            fragmetfilter filterFragment = new fragmetfilter();
            transaction.replace(R.id.main_frame, orderItemFragment);
            //transaction.add(R.id.main_frame,filterFragment);
            transaction.commit();
            TextView tv=getActivity().findViewById(R.id.title);
            tv.setText(s);
        }
    }

    @Override
    public void onFiltersSelected(@NotNull ArrayList<Tag> arrayList) {

    }
    class Adapter extends FilterAdapter<Tag> {

        Adapter(@NotNull List<? extends Tag> items) {
            super(items);
        }

        @NotNull
        @Override
        public FilterItem createView(int position, Tag item) {
            FilterItem filterItem = new FilterItem(getContext());

            filterItem.setStrokeColor(mColors[0]);
            filterItem.setTextColor(mColors[0]);
            filterItem.setCornerRadius(14);
            filterItem.setCheckedTextColor(ContextCompat.getColor(getContext(), android.R.color.white));
            filterItem.setColor(ContextCompat.getColor(getContext(), android.R.color.white));
            filterItem.setCheckedColor(mColors[position]);
            filterItem.setText(item.getText());
            filterItem.deselect();

            return filterItem;
        }
    }

    @Override
    public void onNothingSelected() {

    }
}
