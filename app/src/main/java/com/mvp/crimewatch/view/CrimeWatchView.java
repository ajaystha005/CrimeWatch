package com.mvp.crimewatch.view;

import com.mvp.crimewatch.model.Crime;

import java.util.List;
import java.util.Map;

/**
 * Created by ajayshrestha on 3/17/17.
 */

/**
 * This is View interface to handle all the interaction by Presenter
 */
public interface CrimeWatchView extends CWView {

    /**
     * Populate all the Crime data once load from the server
     *
     * @param crimeList
     * @param lookUpRank
     */
    void populateCrimeData(List<Crime> crimeList, Map<String, Integer> lookUpRank);


    /**
     * Display message to the user
     *
     * @param message
     */
    void showMessage(String message);


    /**
     * Notify user about loading data
     */
    void showLoadingProgress();

    /**
     * Hide the notifications
     */
    void hideLoadingProgress();

    /**
     * Notify user that Search result is currently displayed in Map
     */
    void searchActivated();
}
