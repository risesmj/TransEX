package com.univali.transex;

import android.app.Application;

import io.realm.Realm;

public class InitApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        //Realm.deleteRealm(Realm.getDefaultConfiguration());
    }
}
