package com.joaquimley.byinvitationonly.util;

import android.content.Context;
import android.preference.EditTextPreference;
import android.util.AttributeSet;

public class MyEditTextPreferences extends EditTextPreference {

    public MyEditTextPreferences(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult){
        super.onDialogClosed(positiveResult);
        if(positiveResult)
            setSummary(getSummary());
    }

    @Override
    public CharSequence getSummary(){
        return getText().isEmpty() ? super.getSummary() : getText();
    }
}
