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

        removeTaskIfNotTomorrow(modelArrayList);
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

    public void removeTaskIfNotTomorrow(ArrayList<ListItemModel> taskArrayList) {


        //get tomorrows date
        int day = Calendar.getInstance().get(Calendar.DAY_OF_YEAR) + 1;
        int year = Calendar.getInstance().get(Calendar.YEAR);

        //loop through all tasks in tomorrow task list
        for(int i = 0; i < taskArrayList.size(); i++)
        {

            int taskDay = taskArrayList.get(i).getDayOfYear();
            int taskYear = taskArrayList.get(i).getYear();

            //if task is for today, send to today list and remove from tomorrow list
            if((taskDay == day - 1) && (taskYear == year))
            {
                //when sending any task to today list, change the date day by minus 1 so the date is correct and will work with date check in today fragment
                taskArrayList.get(i).setDayOfYear(taskArrayList.get(i).getDayOfYear() - 1);

                //if task is not an every day task; move it to today fragment list and remove from tomorrow list
                if(taskArrayList.get(i).getEveryday() == false)
                {
                    Fragment_main1.newTaskFromTomorrow(taskArrayList.get(i));
                    taskArrayList.remove(i);
                }
                //if task is every day task; move it to today fragment list and keep it in tomorrow list
                else if (taskArrayList.get(i).getEveryday() == true)
                {
                    Fragment_main1.newTaskFromTomorrow(taskArrayList.get(i));
                }
            }
            //if task isnt for tomorrow, remove it
            else if(taskDay != day || taskYear != year)
            {
                taskArrayList.remove(i);
            }


        }
    }
}