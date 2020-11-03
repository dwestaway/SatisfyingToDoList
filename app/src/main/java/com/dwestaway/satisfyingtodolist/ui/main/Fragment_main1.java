package com.dwestaway.satisfyingtodolist.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.dwestaway.satisfyingtodolist.ListItemModel;
import com.dwestaway.satisfyingtodolist.MainActivity;
import com.dwestaway.satisfyingtodolist.R;

import java.util.ArrayList;


public class Fragment_main1 extends Fragment {

    private ListView listView;

    private static ArrayList<ListItemModel> modelArrayList;

    private ListAdapter listAdapter;

    public MainActivity mainActivity;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        listView = getView().findViewById(R.id.listView);

        //create array for list items
        modelArrayList = new ArrayList<>();

        //add list items
        modelArrayList.add(new ListItemModel("Stretch", false));

        modelArrayList.add(new ListItemModel("Workout", false));

        modelArrayList.add(new ListItemModel("Meditate", false));



        //get main activity to access variables from it
        mainActivity = (MainActivity) getActivity();

        //setup adapter
        listAdapter = new ListAdapter(getActivity(), modelArrayList);
        //set adapter to listView
        listView.setAdapter(listAdapter);


        //when a task is clicked, turn green and set task to completed, or if delete mode is enabled; delete the task
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                if(modelArrayList.get(position).getTaskDone() == false)
                {
                    modelArrayList.get(position).setTaskDone(true);

                    checkIfAllTasksDone();
                }
                else if(modelArrayList.get(position).getTaskDone() == true)
                {
                    modelArrayList.get(position).setTaskDone(false);
                }

                setTaskColours(parent);

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                modelArrayList.remove(position);

                listAdapter.notifyDataSetChanged();

                checkIfAllTasksDone();

                setTaskColours(parent);

                return true;
            }
        });

        //deleteMode(listView);

        View view2;

        view2 = listAdapter.getView(listView.getFirstVisiblePosition() + 0, null, listView);

        view2.setBackgroundColor(getResources().getColor(R.color.red));



    }

    private void checkIfAllTasksDone() {

        int tasksDoneCount = 0;

        for(int i = 0; i < modelArrayList.size(); i++)
        {
            if (modelArrayList.get(i).getTaskDone() == true)
            {
                tasksDoneCount++;

                if(tasksDoneCount == modelArrayList.size())
                {
                    Toast.makeText(getContext(), "Today's tasks completed, well done!", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public static void newTask(String task) {

        modelArrayList.add(new ListItemModel(task, false));
    }

    /* Loop through all tasks and set colour to green if task is done, else set to grey,
        this must be called after any changes to tasks
     */
    private void setTaskColours(AdapterView<?> parent)
    {

        for(int i = 0; i < modelArrayList.size(); i++)
        {
            View listItem = parent.getChildAt(parent.getFirstVisiblePosition() + i);

            if(listItem != null)
            {
                if(modelArrayList.get(i).getTaskDone() == true)
                {
                    listItem.setBackgroundColor(getResources().getColor(R.color.green));
                }
                else
                {
                    listItem.setBackgroundColor(getResources().getColor(R.color.lightGrey));
                }
            }

        }
    }



}