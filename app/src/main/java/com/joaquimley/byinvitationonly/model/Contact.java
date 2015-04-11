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
