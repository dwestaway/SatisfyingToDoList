package com.dwestaway.satisfyingtodolist.ui.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dwestaway.satisfyingtodolist.ListItemModel;
import com.dwestaway.satisfyingtodolist.MainActivity;
import com.dwestaway.satisfyingtodolist.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Fragment_main2 extends Fragment {

    private ListView listView;

    private static ArrayList<ListItemModel> modelArrayList;

    private ListAdapter listAdapter;

    public static MainActivity mainActivity;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        listView = getView().findViewById(R.id.listView);

        loadData();

        //fix this, does not remember colours set
        setTaskColours(listView);

        //get main activity to access variables from it
        mainActivity = (MainActivity) getActivity();

        //setup adapter
        listAdapter = new ListAdapter(getActivity(), modelArrayList);
        //set adapter to listView
        listView.setAdapter(listAdapter);



        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                //remove the task from modelArrayList
                modelArrayList.remove(position);

                //remove the task's view from listView (prevents task colour bugs when adding new tasks)
                parent.removeViewInLayout(view);

                listAdapter.notifyDataSetChanged();

                checkIfAllTasksDone();

                setTaskColours(parent);

                saveData();

                return true;
            }
        });

    }

    //save tasks into local storage
    private static void saveData() {

        //get instance of sharedPreferences
        SharedPreferences sharedPreferences = mainActivity.getSharedPreferences("shared preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();

        //save the task arraylist as a string
        String json = gson.toJson(modelArrayList);

        //save the string to sharedPreferences
        editor.putString("task list tomorrow", json);
        editor.apply();
    }
    //load tasks from local storage
    private void loadData() {

        //get instance of sharedPreferences
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("shared preferences", Context.MODE_PRIVATE);

        Gson gson = new Gson();

        //get tasks from sharedPreferences as a string
        String json = sharedPreferences.getString("task list tomorrow", null);

        Type type = new TypeToken<ArrayList<ListItemModel>>() {}.getType();

        //load string into task array list
        modelArrayList = gson.fromJson(json, type);

        //if no list is stored, create one
        if (modelArrayList == null) {

            modelArrayList = new ArrayList<> ();
        }
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

    public static void newTask(String task, Boolean everyday) {

        modelArrayList.add(new ListItemModel(task, false, everyday));

        saveData();

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
                    listItem.setBackgroundColor(getResources().getColor(R.color.taskDone));
                }
                else
                {
                    listItem.setBackgroundColor(getResources().getColor(R.color.task));
                }
            }

        }
    }
}