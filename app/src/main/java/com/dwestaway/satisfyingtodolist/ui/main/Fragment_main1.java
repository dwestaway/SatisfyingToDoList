package com.dwestaway.satisfyingtodolist.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.dwestaway.satisfyingtodolist.ListItemModel;
import com.dwestaway.satisfyingtodolist.R;

import java.util.ArrayList;


public class Fragment_main1 extends Fragment {

    //private ViewPager viewPager;
    //private RecyclerView recyclerView;
    private ListView listView;

    private ArrayList<ListItemModel> modelArrayList;

    private ListAdapter listAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);



    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        listView = getView().findViewById(R.id.listView);

        loadCards();
    }

    private void loadCards() {

        //create array for list items
        modelArrayList = new ArrayList<>();

        //add list items
        modelArrayList.add(new ListItemModel("Stretch"));

        modelArrayList.add(new ListItemModel("Workout"));

        modelArrayList.add(new ListItemModel("Meditate"));


        //setup adapter
        listAdapter = new ListAdapter(getActivity(), modelArrayList);
        //set adapter to listView
        listView.setAdapter(listAdapter);



    }
}