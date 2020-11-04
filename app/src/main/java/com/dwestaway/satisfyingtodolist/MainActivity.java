package com.dwestaway.satisfyingtodolist;

import android.content.Context;
import android.os.Bundle;

import com.dwestaway.satisfyingtodolist.ui.main.Fragment_main1;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.dwestaway.satisfyingtodolist.ui.main.SectionsPagerAdapter;

public class MainActivity extends AppCompatActivity {

    public Boolean newTaskViewVisible = false;

    public int currentTab = 0;

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

        //set tab text colours
        tabs.setTabTextColors(getResources().getColor(R.color.colorAccent), getResources().getColor(R.color.textGrey));

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

                    hideKeyBoard();

                    newTaskViewVisible = false;

                    String newTaskString = newTaskEditText.getText().toString();

                    //if new task is not empty
                    if(!newTaskString.matches(""))
                    {

                        //if "today" tab is selected
                        if(tabs.getSelectedTabPosition() == 0)
                        {
                            //send task to fragment
                            Fragment_main1.newTask(newTaskString);
                        }
                        else
                        {
                            //add new task to tomorrow tab
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