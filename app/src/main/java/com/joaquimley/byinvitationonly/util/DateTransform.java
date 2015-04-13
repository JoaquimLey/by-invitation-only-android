/*
 * Copyright (c) 2015 Joaquim Ley
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

/**
 * Helper class to return minutes, hours, days, years from the Date.class and Month names
 */

public class DateTransform {

//    public static final int MINUTES = 1000 * 60;
//    public static final int HOURS = MINUTES * 60;
//    public static final int DAYS = HOURS * 24;
//    public static final int YEARS = DAYS * 365;
//
//    public static int getMinutes(long timeInLong){
//        return Math.round(timeInLong / MINUTES);
//    }
//
//    public static int getHours(long timeInLong){
//        return Math.round(timeInLong / HOURS);
//    }
//
//    public static int getDays(long timeInLong){
//        return Math.round(timeInLong / DAYS);
//    }
//
//    public static int getYears(long timeInLong){
//        return Math.round(timeInLong / YEARS);
//    }

    public static String getMonthName(int month){
        String[] monthNames = {"Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez"};
        return monthNames[month];
    }

}
