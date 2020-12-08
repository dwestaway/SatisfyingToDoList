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
import java.util.Calendar;

public class Fragment_main2 extends Fragment {

    private ListView listView;

    private static ArrayList<ListItemModel> modelArrayList;

    public static ListAdapter listAdapter;

    public static MainActivity mainActivity;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        listView = getView().findViewById(R.id.listView);

        //loadData();

        //get main activity to access variables from it
        mainActivity = (MainActivity) getActivity();

        saveData();

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

                saveData();

                return true;
            }
        });

    }

    //save tasks into local storage
    private static void saveData() {

        //get instance of sharedPreferences
        SharedPreferences sharedPreferences = mainActivity.getSharedPreferences("shared preferences2", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();

        //save the task arraylist as a string
        String json = gson.toJson(modelArrayList);

        //save the string to sharedPreferences
        editor.putString("task list tomorrow", json);
        editor.apply();
    }
    //load tasks from local storage
    public  static void loadData(SharedPreferences sharedPreferences) {

        //get instance of sharedPreferences
        //SharedPreferences sharedPreferences = mainActivity.getSharedPreferences("shared preferences2", Context.MODE_PRIVATE);

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

        modelArrayList = removeTaskIfNotTomorrow(modelArrayList);
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

        listAdapter.notifyDataSetChanged();

        saveData();

    }

    public static ArrayList<ListItemModel> removeTaskIfNotTomorrow(ArrayList<ListItemModel> taskArrayList) {


        //get tomorrows date
        int today = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
        int tomorrow = Calendar.getInstance().get(Calendar.DAY_OF_YEAR) + 1;
        int year = Calendar.getInstance().get(Calendar.YEAR);

        ArrayList<ListItemModel> newTaskArrayList = new ArrayList<>();

        //loop through all tasks in tomorrow task list
        for (int i = 0; i < taskArrayList.size(); i++)
        {

            ListItemModel task = taskArrayList.get(i);

            int taskDay = task.getDayOfYear();
            int taskYear = task.getYear();

            //if task is everyday, add it to new arraylist and send to todays tasks
            if(task.getEveryday() == true)
            {
                //task.setDayOfYear(tomorrow);

                newTaskArrayList.add(task);

                task.setDayOfYear(today);

                Fragment_main1.newTaskFromTomorrow(task);
            }
            else if (taskDay == tomorrow && taskYear == year)
            {
                newTaskArrayList.add(task);

            }
            else if (taskDay == today && taskYear == year)
            {
                Fragment_main1.newTaskFromTomorrow(task);

            }
        }

        //return updated task list without any out of date tasks
        return newTaskArrayList;
    }
}