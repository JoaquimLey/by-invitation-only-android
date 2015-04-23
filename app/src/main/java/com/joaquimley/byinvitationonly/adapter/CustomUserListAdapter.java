/*
 * Copyright (c) 2015 Joaquim Ley - www.joaquimley.com
 * All rights reserved.
 *
 * Redistribution, modification or use of source and binary forms are NOT allowed
 * without permission. The name of Joaquim Ley, or joaquimley.com may not be used
 * to endorse products derived without previous authorization.
 * THIS SOFTWARE IS PROVIDED 'AS IS' AND WITHOUT ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 */

package com.joaquimley.byinvitationonly.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.joaquimley.byinvitationonly.R;
import com.joaquimley.byinvitationonly.model.User;
import com.joaquimley.byinvitationonly.util.ImageCircleTransform;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Custom Adapter to populate list with:
 * Image; Title; Subtitle; Description; Date;
 */

public class CustomUserListAdapter extends BaseAdapter {

    private Activity mActivity;
    private LayoutInflater mInflater;
    private List<User> mItems;


    public CustomUserListAdapter(Activity activity, List<User> items) {
        mActivity = activity;
        mItems = items;
        mInflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if (mItems == null) {
            return 0;
        }
        return mItems.size();
    }

    @Override
    public User getItem(int location) {
        return mItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        CustomHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = mActivity.getLayoutInflater();
            convertView = inflater.inflate(R.layout.custom_participant_row, parent, false);

            holder = new CustomHolder();
            holder.listName = (TextView) convertView.findViewById(R.id.tv_participant_name);
            holder.listEmail = (TextView) convertView.findViewById(R.id.tv_participant_email);
            holder.listDescription = (TextView) convertView.findViewById(R.id.tv_participant_description);
            holder.listPhoto = (ImageView) convertView.findViewById(R.id.iv_participant_pic);

        } else {
            holder = (CustomHolder) convertView.getTag();
        }

        // Getting user data for row
        User user = mItems.get(position);
        // Title
        holder.listName.setText(user.getName());
        // Email
        holder.listEmail.setText(user.getEmail());
        // Description
        holder.listDescription.setText(user.getDescription());
        // Photo
        if (user.getPhotoUrl() == null || user.getPhotoUrl().isEmpty()) {
            Picasso.with(mActivity).load(R.drawable.image_placeholder).into(holder.listPhoto);
        } else {
            Picasso.with(mActivity).load(user.getPhotoUrl())
                    .placeholder(R.drawable.image_placeholder)
                    .error(R.drawable.image_placeholder_error)
                    .transform(new ImageCircleTransform())
                    .into(holder.listPhoto);
        }
        return convertView;
    }

    /**
     * Private view holder class
     */
    private static class CustomHolder {
        ImageView listPhoto;
        TextView listName;
        TextView listEmail;
        TextView listDescription;
    }


    public Activity getActivity() {
        return mActivity;
    }

    public void setActivity(Activity activity) {
        mActivity = activity;
    }

    public LayoutInflater getInflater() {
        return mInflater;
    }

    public void setInflater(LayoutInflater inflater) {
        mInflater = inflater;
    }

    public List<User> getItems() {
        return mItems;
    }

    public void setArtistItems(List<User> items) {
        mItems = items;
    }
}
