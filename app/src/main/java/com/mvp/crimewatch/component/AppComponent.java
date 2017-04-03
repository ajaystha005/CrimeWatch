package com.mvp.crimewatch.component;

import com.mvp.crimewatch.module.DatabaseModule;
import com.mvp.crimewatch.interactor.CrimeWatchInteractor;
import com.mvp.crimewatch.module.CrimeWatchModule;
import com.mvp.crimewatch.presenter.CrimeWatchPresenter;
import com.mvp.crimewatch.module.UtilModule;
import com.mvp.crimewatch.view.CrimeWatchActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by ajayshrestha on 3/20/17.
 */
@Singleton
@Component(modules = {UtilModule.class, CrimeWatchModule.class, DatabaseModule.class})
public interface AppComponent {

    void inject(CrimeWatchInteractor crimeWatchInteractor);

    void inject(CrimeWatchActivity crimeWatchActivity);

    void inject(CrimeWatchPresenter crimeWatchPresenter);
}
