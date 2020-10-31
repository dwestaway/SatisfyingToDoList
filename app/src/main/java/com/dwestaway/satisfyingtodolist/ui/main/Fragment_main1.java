package com.dwestaway.satisfyingtodolist.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

    private Boolean isDone;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);



    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        isDone = false;

        listView = getView().findViewById(R.id.listView);

        //create array for list items
        modelArrayList = new ArrayList<>();

        //add list items
        modelArrayList.add(new ListItemModel("Stretch", false));

        modelArrayList.add(new ListItemModel("Workout", false));

        modelArrayList.add(new ListItemModel("Meditate", false));



        //setup adapter
        listAdapter = new ListAdapter(getActivity(), modelArrayList);
        //set adapter to listView
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(modelArrayList.get(position).getTaskDone() == false)
                {
                    view.setBackgroundColor(getResources().getColor(R.color.green));
                    modelArrayList.get(position).setTaskDone(true);
                }
                else if(modelArrayList.get(position).getTaskDone() == true)
                {
                    view.setBackgroundColor(getResources().getColor(R.color.lightGrey));
                    modelArrayList.get(position).setTaskDone(false);
                }

            }
        });
    }

}