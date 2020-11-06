package com.dwestaway.satisfyingtodolist.ui.main;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.dwestaway.satisfyingtodolist.ListItemModel;
import com.dwestaway.satisfyingtodolist.R;

import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ListItemModel> modelArrayList;

    public ListAdapter(Context context, ArrayList<ListItemModel> modelArrayList) {
        this.context = context;
        this.modelArrayList = modelArrayList;
    }

    @Override
    public int getCount() {

        return modelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return modelArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = View.inflate(context, R.layout.card, null);
        }

        TextView text = convertView.findViewById(R.id.cardText);

        ConstraintLayout constraintLayout = convertView.findViewById(R.id.constraintLayout);

        ListItemModel listItemModel = modelArrayList.get(position);

        text.setText(listItemModel.getTaskText());

        //if task is complete, set colour to green, this fixes completed tasks being grey when app is opened
        if(listItemModel.getTaskDone() == true)
        {
            constraintLayout.setBackgroundColor(context.getApplicationContext().getResources().getColor(R.color.taskDone));

        }

        return convertView;

    }


}
