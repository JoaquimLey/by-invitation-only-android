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

package com.joaquimley.byinvitationonly.model;

import java.util.ArrayList;

/**
 * Model for Favorites
 */

public class Favorite {

    private ArrayList<Talk> mFavorites;

    // TODO: create method to get the most recent favourite

    public ArrayList<Talk> getFavorites() {
        return mFavorites;
    }

    public void setFavorites(ArrayList<Talk> favorites) {
        mFavorites = favorites;
    }
}
