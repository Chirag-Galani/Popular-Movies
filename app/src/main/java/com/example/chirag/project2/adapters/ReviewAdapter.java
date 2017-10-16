package com.example.chirag.project2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.chirag.project2.R;
import com.example.chirag.project2.model.Review;

import java.util.List;

public class ReviewAdapter extends BaseAdapter {
    Context context;
    private RelativeLayout relativeLayout;
    List<Review> reviewList;

    public ReviewAdapter(Context context, List<Review> reviewList) {
        super();
        this.reviewList = reviewList;
        this.context = context;

    }

    @Override
    public int getCount() {
        return reviewList.size();

    }

    @Override
    public Object getItem(int i) {

        return reviewList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    static class ViewHolder {
        TextView review_title,review_content;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int list_layout = R.layout.list_item_review;
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(list_layout, parent, false);
            viewHolder = new ViewHolder();
            relativeLayout = (RelativeLayout) convertView.findViewById(R.id.frame);
            viewHolder.review_title= (TextView) convertView.findViewById(R.id.textview_review_title);
            viewHolder.review_content= (TextView) convertView.findViewById(R.id.textview_review_content);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.review_title.setText(reviewList.get(position).getAuthor());
        viewHolder.review_content.setText(reviewList.get(position).getContent());
        return convertView;

    }
}

