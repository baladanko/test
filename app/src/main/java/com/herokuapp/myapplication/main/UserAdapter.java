package com.herokuapp.myapplication.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.herokuapp.myapplication.R;
import com.herokuapp.myapplication.entity.User;

import java.util.ArrayList;

/**
 * Created by nesstar on 15.08.2015.
 */
public class UserAdapter extends BaseAdapter {
    LayoutInflater mLayoutInflater;

    ArrayList<Integer> mListSectionPos;

    ArrayList<User> mListUserItems;

    // context object
    Context mContext;

    public UserAdapter(Context context, ArrayList<User> mListUserItems) {
        // Log.d("TEST1");
        this.mContext = context;
        this.mListUserItems = mListUserItems;

        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mListUserItems.size();
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public Object getItem(int position) {
        return mListUserItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mListUserItems.get(position).getUserId();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        holder = new ViewHolder();
        int type = getItemViewType(position);
        convertView = mLayoutInflater.inflate(R.layout.list_item, null);
        holder.tvName = (TextView) convertView.findViewById(R.id.tv_Name);
        holder.tvSurname = (TextView) convertView.findViewById(R.id.tv_Surname);
        holder.tvName.setText(mListUserItems.get(position).getName());
        holder.tvSurname.setText(mListUserItems.get(position).getSurname());
        convertView.setTag(holder);
        return convertView;
    }

    public static class ViewHolder {
        public TextView tvName, tvSurname;

    }
}
