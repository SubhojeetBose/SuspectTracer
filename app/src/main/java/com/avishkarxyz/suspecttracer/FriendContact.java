package com.avishkarxyz.suspecttracer;

/**
 * Created by Ishaan on 8/27/2017.
 */

//Contact Class....for making object for database

public class FriendContact {

    String Name;
    String Android_id;


    public FriendContact(String name, String android_id) {
        Name = name;
        Android_id = android_id;
    }

    public String getName() {
        return Name;
    }

    public String getAndroid_id() {
        return Android_id;
    }
}
