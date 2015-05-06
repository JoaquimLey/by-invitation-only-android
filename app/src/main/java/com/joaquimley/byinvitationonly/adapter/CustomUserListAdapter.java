/*
 * GNU GENERAL PUBLIC LICENSE
 *                        Version 3, 29 June 2007
 *
 *     Copyright (c) 2015 Joaquim Ley <me@joaquimley.com>
 *
 *     This program is free software; you can redistribute it and/or
 *     modify it under the terms of the GNU General Public License
 *     as published by the Free Software Foundation; either version 2
 *     of the License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program; if not, write to the Free Software
 *     Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package com.joaquimley.byinvitationonly.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
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

    private static final String TAG = CustomUserListAdapter.class.getSimpleName();
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
        }

        holder = new CustomHolder();
        holder.listName = (TextView) convertView.findViewById(R.id.tv_participant_name);
        holder.listEmail = (TextView) convertView.findViewById(R.id.tv_participant_email);
        holder.listDescription = (TextView) convertView.findViewById(R.id.tv_participant_description);
        holder.listPhoto = (ImageView) convertView.findViewById(R.id.iv_participant_pic);
        convertView.setTag(holder);

        // Getting user data for row
        User user = mItems.get(position);
        if(user == null){
            Log.e(TAG, "user is null");
            return convertView;
        }
        // Title
        holder.listName.setText(user.getName());
        // Email
        holder.listEmail.setText(user.getEmail());
        // Description
        holder.listDescription.setText(user.getDescription());
        // Photo
        if (user.getPhotoBase64() == null || user.getPhotoBase64().isEmpty()) {
            Picasso.with(mActivity).load(R.drawable.image_placeholder).into(holder.listPhoto);
        } else {
            Picasso.with(mActivity).load(user.getPhotoBase64())
                    .placeholder(R.drawable.image_placeholder)
                    .error(R.drawable.image_placeholder_error)
                    .transform(new ImageCircleTransform())
                    .into(holder.listPhoto);
        }
        return convertView;
    }

    /**
     * Private holder class
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

    public void setItems(List<User> items) {
        mItems = items;
    }
}
