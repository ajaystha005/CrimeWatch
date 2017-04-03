package com.mvp.crimewatch;

import android.app.Application;

import com.mvp.crimewatch.component.AppComponent;
import com.mvp.crimewatch.component.DaggerAppComponent;
import com.mvp.crimewatch.module.DatabaseModule;
import com.mvp.crimewatch.model.CrimeWatchService;
import com.mvp.crimewatch.module.CrimeWatchModule;
import com.mvp.crimewatch.module.UtilModule;

import rx.Scheduler;
import rx.schedulers.Schedulers;

/**
 * Created by ajayshrestha on 3/18/17.
 */

public class CrimeWatchApplication extends Application {

    private CrimeWatchService mCrimeWatchService;
    private Scheduler mDefaultScheduler;

    private static CrimeWatchApplication INSTANCE;

    private AppComponent mComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;

        mComponent = DaggerAppComponent.builder()
                .utilModule(new UtilModule())
                .crimeWatchModule(new CrimeWatchModule(this))
                .databaseModule(new DatabaseModule(this))
                .build();

    }

    public AppComponent getDaggerComponent() {
        return mComponent;
    }

    public static CrimeWatchApplication get() {
        return INSTANCE;
    }

    public CrimeWatchService getCrimeWatchService() {
        if (null == mCrimeWatchService) {
            mCrimeWatchService = new CrimeWatchService.Factory().create();
        }

        return mCrimeWatchService;
    }

    public Scheduler getDefaultScheduler() {
        if (null == mDefaultScheduler) {
            mDefaultScheduler = Schedulers.io();
        }

        return mDefaultScheduler;
    }

}
