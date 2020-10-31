package com.dwestaway.satisfyingtodolist;

import android.os.Bundle;

import com.dwestaway.satisfyingtodolist.ui.main.Fragment_main1;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.dwestaway.satisfyingtodolist.ui.main.SectionsPagerAdapter;

public class MainActivity extends AppCompatActivity {

    public Boolean delete = false;


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

        final ImageButton deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setBackgroundResource(R.drawable.ic_baseline_delete_forever_24);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(delete == false)
                {
                    delete = true;

                    deleteButton.setBackgroundResource(R.drawable.ic_baseline_delete_24);

                    Toast.makeText(getApplicationContext(), "Press task to delete", Toast.LENGTH_SHORT).show();
                }
                else if (delete == true)
                {
                    delete = false;

                    deleteButton.setBackgroundResource(R.drawable.ic_baseline_delete_forever_24);
                }
            }
        });

        //set tab text colours
        tabs.setTabTextColors(getResources().getColor(R.color.colorAccent), getResources().getColor(R.color.textGrey));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });
    }
}