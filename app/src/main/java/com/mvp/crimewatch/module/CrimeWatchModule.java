package com.mvp.crimewatch.module;

import android.content.Context;

import com.mvp.crimewatch.interactor.CrimeWatchInteractor;
import com.mvp.crimewatch.presenter.CrimeWatchPresenter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ajayshrestha on 3/20/17.
 */


/**
 * Dagger Crime Watch Module
 */
@Module
public class CrimeWatchModule {

    private Context mContext;

    public CrimeWatchModule(Context mContext) {
        this.mContext = mContext;
    }

    @Singleton
    @Provides
    public CrimeWatchPresenter provideCrimeWatchPresenter() {
        return new CrimeWatchPresenter(mContext);
    }

    @Singleton
    @Provides
    public CrimeWatchInteractor provideCrimeWatchInteractor() {
        return new CrimeWatchInteractor(mContext);
    }
}
