package com.dwestaway.satisfyingtodolist.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

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

        ListItemModel  listItemModel = modelArrayList.get(position);

        text.setText(listItemModel.getTaskText());

        return convertView;
    }


}
