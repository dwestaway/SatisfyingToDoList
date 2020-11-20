package com.dwestaway.satisfyingtodolist;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import com.dwestaway.satisfyingtodolist.ui.main.Fragment_main1;
import com.dwestaway.satisfyingtodolist.ui.main.Fragment_main2;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.loader.content.AsyncTaskLoader;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;

import com.dwestaway.satisfyingtodolist.ui.main.SectionsPagerAdapter;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    public Boolean newTaskViewVisible = false;

    AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        final TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        final FloatingActionButton fab = findViewById(R.id.fab);

        //access the ConstraintLayout that holds the new task view and hide it by default
        final ConstraintLayout newTaskLayout =  findViewById(R.id.newTaskLayout);
        newTaskLayout.setVisibility(View.GONE);

        final EditText newTaskEditText = findViewById(R.id.newTask);
        final CheckBox everydayCheckBox = findViewById(R.id.everydayCheckBox);

        //set tab text colours
        tabs.setTabTextColors(getResources().getColor(R.color.colorAccent), getResources().getColor(R.color.textGrey));


        //initalize admob banner ad
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        final AdRequest adRequest = new AdRequest.Builder().build();


        //bottom right of screen button
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(newTaskViewVisible == false)
                {
                    newTaskLayout.setVisibility(View.VISIBLE);

                    newTaskViewVisible = true;

                    //hide ad when new task view is shown
                    mAdView.destroy();
                    mAdView.setVisibility(View.GONE);

                    fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_keyboard_arrow_down_24));
                }
                else if(newTaskViewVisible == true)
                {
                    newTaskLayout.setVisibility(View.GONE);

                    //hide keyboard when newTaskView is hidden
                    hideKeyBoard();
                    dismissKeyboard(MainActivity.this);

                    newTaskViewVisible = false;

                    //load and show ad when new task view is hidden
                    mAdView.loadAd(adRequest);
                    mAdView.setVisibility(View.VISIBLE);

                    fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_add_24));

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

                        int tomorrow = day + 1;

                        //if task is set as everyday
                        if(everyday == true)
                        {
                            Fragment_main1.newTask(getApplicationContext(), newTaskString, everyday, day, year);
                            Fragment_main2.newTask(getApplicationContext(), newTaskString, everyday, tomorrow, year);
                        }
                        else if (everyday == false)
                        {
                            //if "today" tab is selected
                            if(tabs.getSelectedTabPosition() == 0)
                            {
                                Fragment_main1.newTask(getApplicationContext(), newTaskString, everyday, day, year);
                            }
                            else if(tabs.getSelectedTabPosition() == 1)
                            {
                                Fragment_main2.newTask(getApplicationContext(), newTaskString, everyday, tomorrow, year);
                            }
                        }


                        newTaskEditText.setText("");

                    }

                }



            }
        });

        //change fab button icon to a plus when text is entered for new task
        newTaskEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_add_24));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



    }



    public void hideKeyBoard() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow((null == getCurrentFocus()) ? null : getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    //second hide keyboard method because first does not always work
    public void dismissKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != activity.getCurrentFocus())
            imm.hideSoftInputFromWindow(activity.getCurrentFocus()
                    .getApplicationWindowToken(), 0);
    }


}