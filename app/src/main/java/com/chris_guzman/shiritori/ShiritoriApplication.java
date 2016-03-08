package com.chris_guzman.shiritori;

import android.app.Application;

import com.chris_guzman.shiritori.Data.CouchbaseDM;

/**
 * Copyright (c) 2015 OrderUp. All rights reserved.
 */
public class ShiritoriApplication extends Application {
    private CouchbaseDM couchbaseDM;

    @Override
    public void onCreate() {
        super.onCreate();
        couchbaseDM = CouchbaseDM.init(this);
    }
}
