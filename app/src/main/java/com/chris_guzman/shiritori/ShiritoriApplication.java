package com.chris_guzman.shiritori;

import android.app.Application;
import com.firebase.client.Firebase;

public class ShiritoriApplication extends Application {
    private Firebase firebaseRef;

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
        firebaseRef = new Firebase("https://blistering-heat-7744.firebaseio.com/");
    }

    public Firebase getFirebaseRef() {
        return firebaseRef;
    }
}
