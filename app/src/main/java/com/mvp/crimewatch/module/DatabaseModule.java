package com.mvp.crimewatch.module;

import android.content.Context;

import com.mvp.crimewatch.database.CrimeDatabaseHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ajayshrestha on 3/20/17.
 */

@Module
public class DatabaseModule {
    private Context mContext;

    public DatabaseModule(Context mContext) {
        this.mContext = mContext;
    }

    @Singleton
    @Provides
    public CrimeDatabaseHelper provideDatabaseModule() {
        return new CrimeDatabaseHelper(mContext);
    }
}
