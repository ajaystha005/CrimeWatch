package com.mvp.crimewatch.module;

import com.mvp.crimewatch.utils.DialogUtils;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ajayshrestha on 3/20/17.
 */

@Module
public class UtilModule {

    @Singleton
    @Provides
    public DialogUtils provideDialogModule() {
        return new DialogUtils();
    }
}
