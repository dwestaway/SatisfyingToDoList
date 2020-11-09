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

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.dwestaway.satisfyingtodolist.ListItemModel;
import com.dwestaway.satisfyingtodolist.MainActivity;
import com.dwestaway.satisfyingtodolist.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;


public class Fragment_main1 extends Fragment {

    private ListView listView;

    private static ArrayList<ListItemModel> modelArrayList;

    private ListAdapter listAdapter;

    public static MainActivity mainActivity;

    private SoundPool soundPool;
    private int doneSound;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        listView = getView().findViewById(R.id.listView);

        loadData();

        //fix this, does not remember colours set
        //does not need fixing? (8/11/2020)
        setTaskColours(listView);

        //get main activity to access variables from it
        mainActivity = (MainActivity) getActivity();

        saveData();

        //setup adapter
        listAdapter = new ListAdapter(getActivity(), modelArrayList);
        //set adapter to listView
        listView.setAdapter(listAdapter);


        //create sound pool for done sounds, use different method depending on if build version is older than android lollipop
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setMaxStreams(2)
                    .setAudioAttributes(audioAttributes)
                    .build();
        }
        else
        {
            soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        }
        doneSound = soundPool.load(getContext(), R.raw.ding, 1);


        //when a task is clicked, turn green and set task to completed, or if delete mode is enabled; delete the task
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                if(modelArrayList.get(position).getTaskDone() == false)
                {
                    modelArrayList.get(position).setTaskDone(true);

                    //play done sound
                    soundPool.play(doneSound, 1, 1, 0, 0, 1);

                    checkIfAllTasksDone();
                }
                else if(modelArrayList.get(position).getTaskDone() == true)
                {
                    modelArrayList.get(position).setTaskDone(false);
                }

                setTaskColours(parent);

                saveData();

            }
        });

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
        editor.putString("task list", json);
        editor.apply();

    }
    //load tasks from local storage
    private void loadData() {

        //get instance of sharedPreferences
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("shared preferences", Context.MODE_PRIVATE);

        Gson gson = new Gson();

        //get tasks from sharedPreferences as a string
        String json = sharedPreferences.getString("task list", null);

        Type type = new TypeToken<ArrayList<ListItemModel>>() {}.getType();

        //load string into task array list
        modelArrayList = gson.fromJson(json, type);

        //if no list is stored, create one
        if (modelArrayList == null) {

            modelArrayList = new ArrayList<> ();
        }

        modelArrayList = removeTasksIfNotToday(modelArrayList);


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

    public static void newTask(Context context, String task, Boolean everyday, int dayOfYear, int year) {

        Boolean duplicateTask = false;

        //check if task already exists
        for(int i = 0; i < modelArrayList.size(); i++)
        {
            if(modelArrayList.get(i).getTaskText().equals(task))
            {
                duplicateTask = true;

                Toast.makeText(context, "Task already exists", Toast.LENGTH_LONG).show();
            }
        }

        if (duplicateTask == false)
        {
            modelArrayList.add(new ListItemModel(task, false, everyday, dayOfYear, year));
        }

        saveData();

    }
    public static void newTaskFromTomorrow(ListItemModel task)
    {

        String newTaskName = task.getTaskText();

        Boolean duplicateTask = false;

        //check if new task is the same as any current tasks
        for(int i = 0; i < modelArrayList.size(); i++)
        {
            if(modelArrayList.get(i).getTaskText().equals(newTaskName))
            {
                duplicateTask = true;
            }
        }
        if(duplicateTask == false)
        {
            modelArrayList.add(task);
        }

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
    //loop through task array list and put tasks that match todays date into a new array list, return new array list
    public ArrayList<ListItemModel> removeTasksIfNotToday(ArrayList<ListItemModel> taskArrayList) {

        //get todays date
        int day = Calendar.getInstance().get(Calendar.DAY_OF_YEAR) ;
        int year = Calendar.getInstance().get(Calendar.YEAR);

        ArrayList<ListItemModel> newTaskArrayList = new ArrayList<>();

        //Toast.makeText(getContext(), Integer.toString(day), Toast.LENGTH_LONG).show();

        //Toast.makeText(getContext(), taskArrayList.get(0).getTaskText() + Integer.toString(taskArrayList.get(0).getDayOfYear()), Toast.LENGTH_LONG).show();

        //loop through all tasks in today task list
        for(int i = 0; i < taskArrayList.size(); i++)
        {

            int taskDay = taskArrayList.get(i).getDayOfYear();
            int taskYear = taskArrayList.get(i).getYear();

            //if task is not for todays date, remove it from list
            if(taskYear == year)
            {
                if(taskDay == day)
                {
                    newTaskArrayList.add(taskArrayList.get(i));
                }
            }

        }
        return newTaskArrayList;
    }



}