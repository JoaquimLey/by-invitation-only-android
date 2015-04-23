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

package com.joaquimley.byinvitationonly.util;

import android.app.ActionBar;
import android.util.Log;

import com.joaquimley.byinvitationonly.model.Session;

/**
 * Simple UI modifications util class
 */

public class CustomUi {

    private static final String TAG = CustomUi.class.getSimpleName();

    /**
     * Clears the action bar label and sets a custom icon
     *
     * @param actionBar self explanatory
     * @param iconId self explanatory
     */
    public static void simplifyActionBay(ActionBar actionBar, int iconId){
        if(actionBar == null){
            Log.e(TAG, "simplifyActionBay(): Action bar is null");
            return;
        }
        actionBar.setTitle("");
        actionBar.setIcon(iconId);
    }


    /**
     * Creates menu and sub-menu items
     */
//    public void buildMenu(MenuIcon) {
//        // Create Button to attach to Menu
//        mMenuIcon = new ImageView(this); // Create an icon
//        mMenuIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_cross_2));
//        FloatingActionButton actionButton = new FloatingActionButton.Builder(this)
//                .setContentView(mMenuIcon)
//                .setPosition(FloatingActionButton.POSITION_BOTTOM_RIGHT)
//                .build();
//
//        // Custom sub-menu
//        int actionMenuContentSize = getResources().getDimensionPixelSize(R.dimen.sub_action_button_size);
//        int actionMenuContentMargin = getResources().getDimensionPixelSize(R.dimen.sub_action_button_content_margin);
//        SubActionButton.Builder lCSubBuilder = new SubActionButton.Builder(this);
//
//        FrameLayout.LayoutParams actionMenuContentParams = new FrameLayout
//                .LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
//
//        actionMenuContentParams.setMargins(actionMenuContentMargin, actionMenuContentMargin, actionMenuContentMargin,
//                actionMenuContentMargin);
//        lCSubBuilder.setLayoutParams(actionMenuContentParams);
//
//        // Set custom layout params
//        FrameLayout.LayoutParams subButtonParams = new FrameLayout.LayoutParams(actionMenuContentSize, actionMenuContentSize);
//        lCSubBuilder.setLayoutParams(subButtonParams);
//
//        // Create menu Buttons
//        ImageView homeIcon = new ImageView(this);
//        ImageView infoIcon = new ImageView(this);
//        ImageView starIcon = new ImageView(this);
//
//        homeIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_list));
//        infoIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_info));
//        starIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_star));
//
//        SubActionButton menuSubBtnHome = lCSubBuilder.setContentView(homeIcon).build();
//        SubActionButton menuSubBtnInfo = lCSubBuilder.setContentView(infoIcon).build();
//        SubActionButton menuSubBtnMisc = lCSubBuilder.setContentView(starIcon).build();
//
//        // Button Action
////        menuSubBtnHome.setOnClickListener(this);
////        menuSubBtnInfo.setOnClickListener(this);
////        menuSubBtnMisc.setOnClickListener(this);
//
//        menuSubBtnHome.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                closeMenu();
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentHolder,
//                        new HomeFragment(), HomeFragment.class.getSimpleName())
//                        .commit();
//            }
//        });
//
//        menuSubBtnInfo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                closeMenu();
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentHolder,
//                        new InformationFragment(), InformationFragment.class.getSimpleName())
//                        .commit();
//            }
//        });
//
//        menuSubBtnMisc.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                closeMenu();
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentHolder,
//                        new TicketFragment(), TicketFragment.class.getSimpleName())
//                        .commit();
//            }
//        });
//
//        // Create menu with MenuButton + SubItemsButtons
//        mActionMenu = new FloatingActionMenu.Builder(this)
//                .addSubActionView(menuSubBtnHome)
//                .addSubActionView(menuSubBtnInfo)
//                .addSubActionView(menuSubBtnMisc)
//                .attachTo(actionButton)
//                .build();
//
//        // Listen menu open and close events to animate the button content view
//        mActionMenu.setStateChangeListener(this);
//        mMenuVisibility = true;
//    }

//    private void closeMenu() {
//        if(mActionMenu != null && mActionMenu.isOpen()){
//            mActionMenu.close(true);
//        }
//    }

    /**
     * Transforms a Date object into a string to be shown on a List
     *
     * @param session self explanatory
     * @return date with the following format HHh - dd/MOT
     */
    public static String getSessionListDay(Session session){
        return session.getDate() + " | " + session.getStartHour() + "h " + session.getEndHour() + "h";
    }

    /**
     * Converts a month number into a 3-Letter format/name
     *
     * @param month number
     * @return the month name (abbreviated)
     */
    public static String getMonthName(int month){
        String[] monthNames = {"Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez"};
        return monthNames[month];
    }
}
