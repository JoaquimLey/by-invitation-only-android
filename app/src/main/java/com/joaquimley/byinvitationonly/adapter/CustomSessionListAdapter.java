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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.joaquimley.byinvitationonly.R;
import com.joaquimley.byinvitationonly.interfaces.FavoriteChangeListener;
import com.joaquimley.byinvitationonly.model.Session;
import com.joaquimley.byinvitationonly.util.CustomUi;
import com.joaquimley.byinvitationonly.util.ImageCircleTransform;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Custom Adapter to populate list with:
 * Image; Title; Subtitle; Description; Date;
 */

public class CustomSessionListAdapter extends BaseAdapter {

    private Activity mActivity;
    private LayoutInflater mInflater;
    private List<Session> mItems;
    private final FavoriteChangeListener mListener;


    public CustomSessionListAdapter(Activity activity, List<Session> items, FavoriteChangeListener listener) {
        mActivity = activity;
        mItems = items;
        mListener = listener;
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
    public Session getItem(int location) {
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
            convertView = inflater.inflate(R.layout.custom_list_row, parent, false);

            holder = new CustomHolder();
            holder.listTitle = (TextView) convertView.findViewById(R.id.title);
            holder.listSubtitle = (TextView) convertView.findViewById(R.id.speaker);
            holder.listDate = (TextView) convertView.findViewById(R.id.date);
            holder.listDescription = (TextView) convertView.findViewById(R.id.description);
            holder.listImage = (ImageView) convertView.findViewById(R.id.image);
            holder.favorite = (CheckBox) convertView.findViewById(R.id.favorite);

            convertView.setTag(holder);

        } else {
            holder = (CustomHolder) convertView.getTag();
        }

        // Getting artist data for row
        Session session = mItems.get(position);
        // Title
        holder.listTitle.setText(session.getTitle());
        // Speaker
        holder.listSubtitle.setText(session.getPresenter());
        // Date
        holder.listDate.setText(CustomUi.getSessionListDay(session));
        // Bio
        holder.listDescription.setText(session.getAbstract());
        // Image
        if (session.getImageUrl() == null || session.getImageUrl().isEmpty()) {
            Picasso.with(mActivity).load(R.drawable.image_placeholder).into(holder.listImage);
        } else {
            Picasso.with(mActivity).load(session.getImageUrl())
                    .placeholder(R.drawable.image_placeholder)
                    .error(R.drawable.image_placeholder_error)
                    .transform(new ImageCircleTransform())
                    .into(holder.listImage);
        }
        holder.favorite.setTag(session);
        // Favorite
        if (!session.isBookmarked()) {
            holder.favorite.setChecked(false);
        } else {
            holder.favorite.setChecked(true);
        }

        holder.favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCheckBoxClick(position, (Session) v.getTag());
            }
        });

        return convertView;
    }

    /**
     * Private view holder class
     */
    private static class CustomHolder {
        ImageView listImage;
        TextView listTitle;
        TextView listSubtitle;
        TextView listDescription;
        TextView listDate;
        CheckBox favorite;
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

    public List<Session> getItems() {
        return mItems;
    }

    public void setArtistItems(List<Session> items) {
        mItems = items;
    }
}