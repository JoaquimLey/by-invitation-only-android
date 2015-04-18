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

/**
 * Model for contact/person
 */
public class Contact {

    private String mName;
    private String mEmail;

    public Contact(){
        // No args constructor
    }

    public Contact(String name, String email) {
        mName = name;
        mEmail = email;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String mEmail) {
        mEmail = mEmail;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "Name" + mName + '\'' +
                "Email='" + mEmail + '\'' +
                '}';
    }
}
