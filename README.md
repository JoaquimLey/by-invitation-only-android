# By Invitation Only

## Part of "**School Source Code**" project
+info: [joaquimley.com/school](http://www.joaquimley.com/school)

Motivation
----------
Academic project for my Computer Science course.  
Simple Android app where the users can see all the info related to an event and do some networking with other participants.

Features
--------
* Event schedule with all relevant info.
* Give feedback on the sessions.
* Bookmark your own sessions to organize a personal event agenda.
* See other participants information.
* Contact other participants (networking).
* Create your personal profile for the event and share with other participants.

Future Releases
---------------
* In-app chat with other participants (using Firebase)


Technology
----------
* [Firebase](https://www.firebase.com/) as backend cloud service
* [Google Support Libraries](https://developer.android.com/tools/support-library/features.html) for material-like design on pre-Lollipop

Dependencies
---------
* [robotium-solo](https://code.google.com/p/robotium/)
* [firebase-client-android](https://www.firebase.com/docs/android/quickstart.html)
* [appcompat-v7](http://android-developers.blogspot.pt/2014/10/appcompat-v21-material-design-for-pre.html)
* [recyclerview-v7](https://developer.android.com/tools/support-library/features.html)
* [opencsv](http://opencsv.sourceforge.net/)
* [picasso](http://square.github.io/picasso/)
* [pullrefreshlayout](https://github.com/baoyongzhang/android-PullRefreshLayout)

Automated Tests
---------------
Using [Robotium](https://code.google.com/p/robotium/) framework, every feature should be tested simulating user behaviour.  
All tests can be found [here](../master/app/src/androidTest/java/com/joaquimley/byinvitationonly) (javadocs currently in portuguese).  
*I will update this section soon (implment new and improve already written tests).*

Contribution guidelines
-----------------------
[Git cheat sheet](http://tinyurl.com/mslxyyt) **by** [Ryan Amaral](https://github.com/ryanamaral)

* For each Feature/Module: **Create a new branch (copy from master) with the following syntax:** author/featureName **(i.e.: *ley/navigationDrawerAdapter*)**

* Code review: **Each commit must be clearly explained (keep it short and simple).**

* Testing: **Try to not neglet feature testing (using robotium).**

* Other guidelines: **Always PULL from master before doing any work, doing so keeps the REABASE conflicts to a minimum.**

### Contributors
Joaquim Ley - <me@joaquimley.com>

#License
    GNU GENERAL PUBLIC LICENSE VERSION 2

    Copyright (c) 2015 Joaquim Ley <me@joaquimley.com>

    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public License
    as published by the Free Software Foundation; either version 2
    of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
Read the full license - [GNU GPL](../master/LICENSE.md)

## IMPORTANT
This project is under development, if you are going to use any part of it, please DO READ the License and follow it's guidelines.
