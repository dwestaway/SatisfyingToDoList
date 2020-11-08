package com.dwestaway.satisfyingtodolist;

import android.content.Context;
import android.os.Bundle;
import com.dwestaway.satisfyingtodolist.ui.main.Fragment_main1;
import com.dwestaway.satisfyingtodolist.ui.main.Fragment_main2;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;

import com.dwestaway.satisfyingtodolist.ui.main.SectionsPagerAdapter;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    public Boolean newTaskViewVisible = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        final TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab);

        //access the ConstraintLayout that holds the new task view and hide it by default
        final ConstraintLayout newTaskLayout =  findViewById(R.id.newTaskLayout);
        newTaskLayout.setVisibility(View.GONE);

        final EditText newTaskEditText = findViewById(R.id.newTask);
        final CheckBox everydayCheckBox = findViewById(R.id.everydayCheckBox);

        //set tab text colours
        tabs.setTabTextColors(getResources().getColor(R.color.colorAccent), getResources().getColor(R.color.textGrey));

        //bottom right of screen button
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(newTaskViewVisible == false)
                {
                    newTaskLayout.setVisibility(View.VISIBLE);

                    newTaskViewVisible = true;
                }
                else if(newTaskViewVisible == true)
                {
                    newTaskLayout.setVisibility(View.GONE);

                    //hide keyboard when newTaskView is hidden
                    hideKeyBoard();

                    newTaskViewVisible = false;

                    String newTaskString = newTaskEditText.getText().toString();

                    Boolean everyday = false;

                    if(everydayCheckBox.isChecked())
                    {
                        everyday = true;

                        everydayCheckBox.toggle();
                    }

                    //get todays date
                    int day = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
                    int year = Calendar.getInstance().get(Calendar.YEAR);


                    //if new task is not empty
                    if(!newTaskString.matches(""))
                    {

                        //if task is set as everyday
                        if(everyday == true)
                        {
                            Fragment_main1.newTask(getApplicationContext(), newTaskString, everyday, day, year);
                            Fragment_main2.newTask(getApplicationContext(), newTaskString, everyday, day + 1, year);
                        }
                        //if "today" tab is selected
                        else if(tabs.getSelectedTabPosition() == 0)
                        {
                            Fragment_main1.newTask(getApplicationContext(), newTaskString, everyday, day, year);
                        }
                        else
                        {
                            Fragment_main2.newTask(getApplicationContext(), newTaskString, everyday, day + 1, year);
                        }

                        newTaskEditText.setText("");

                    }

                }



            }
        });

    }

    public void hideKeyBoard() {
        View view1 = this.getCurrentFocus();
        if(view1 != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
        }
    }


}