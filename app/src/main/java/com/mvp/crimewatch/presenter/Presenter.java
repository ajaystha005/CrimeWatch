package com.mvp.crimewatch.presenter;

/**
 * Created by ajayshrestha on 3/17/17.
 */

/**
 * This is the Presenter class
 * @param <T>
 */
public interface Presenter<T> {

    void onAttachView(T view);

    void onDetachView();
}
