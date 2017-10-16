package com.example.chirag.project2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.chirag.project2.R;
import com.example.chirag.project2.model.Trailer;

import java.util.List;

public class TrailerAdapter extends BaseAdapter {
    Context context;
    private RelativeLayout relativeLayout;
    List<Trailer> trailerList;

    public TrailerAdapter(Context context, List<Trailer> trailerList) {
        super();
        this.trailerList = trailerList;
        this.context = context;

    }

    @Override
    public int getCount() {
        return trailerList.size();

    }

    @Override
    public Object getItem(int i) {

        return trailerList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    static class ViewHolder {
        TextView name;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int list_layout = R.layout.list_item_trailer;
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(list_layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.textView_review_title);
            relativeLayout = (RelativeLayout) convertView.findViewById(R.id.frame);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.name.setText(trailerList.get(position).getTitle());
        viewHolder.name.setVisibility(View.VISIBLE);
        return convertView;

    }
}

