package com.dwestaway.satisfyingtodolist;

import android.os.Bundle;

import com.dwestaway.satisfyingtodolist.ui.main.Fragment_main1;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.dwestaway.satisfyingtodolist.ui.main.SectionsPagerAdapter;

public class MainActivity extends AppCompatActivity {

    public Boolean newTaskViewVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
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
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                if(newTaskViewVisible == false)
                {
                    newTaskLayout.setVisibility(View.VISIBLE);

                    newTaskViewVisible = true;
                }
                else if(newTaskViewVisible == true)
                {
                    newTaskLayout.setVisibility(View.GONE);

                    newTaskViewVisible = false;

                    String newTaskString = newTaskEditText.getText().toString();

                    //if new task is not empty
                    if(!newTaskString.matches(""))
                    {
                        //send task to fragment
                        Fragment_main1.newTask(newTaskString);

                        newTaskEditText.setText("");
                    }
                }



            }
        });
    }
}