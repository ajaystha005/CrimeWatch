package com.mvp.crimewatch.presenter;

import android.content.Context;
import android.util.Log;

import com.mvp.crimewatch.CrimeWatchApplication;
import com.mvp.crimewatch.helper.CrimeProcessor;
import com.mvp.crimewatch.interactor.CrimeWatchInteractor;
import com.mvp.crimewatch.model.Crime;
import com.mvp.crimewatch.view.CrimeWatchView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by ajayshrestha on 3/17/17.
 */

/**
 * This is the Crime Presenter which helps to get data from Model or interactor and help view to display
 */

public class CrimeWatchPresenter implements Presenter<CrimeWatchView> {

    private CrimeWatchView mView;

    @Inject
    CrimeWatchInteractor mInteractor;

    private Subscription mSubscription;


    //Pagination number
    private int mOffSetValue;

    //Total list of crime
    private List<Crime> mCrimeList = new ArrayList<>();

    //Crime frequency based on District
    private Map<String, Integer> mCrimeFrequency = new HashMap<>();

    //Crime Rank based on Frequency
    private Map<String, Integer> mCrimeRank = new HashMap<>();

    //Crime Dictionary based on Crime keywords
    private Map<String, ArrayList<Crime>> mCrimeDictionary = new HashMap<>();

    public CrimeWatchPresenter(Context mContext) {
        ((CrimeWatchApplication) mContext).getDaggerComponent().inject(this);
    }

    @Override
    public void onAttachView(CrimeWatchView view) {
        this.mView = view;
    }

    @Override
    public void onDetachView() {
        this.mView = null;
        this.mInteractor = null;

        if (mSubscription != null) mSubscription.unsubscribe();
    }

    /**
     * Load the crime data from DB. I am not calling this right now
     */
    public void loadCrimeDataFromDb() {
        mView.showLoadingProgress();

        mSubscription = mInteractor.getCrimes().subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.computation())
                .subscribe(new Action1<List<Crime>>() {
                    @Override
                    public void call(List<Crime> crimeList) {

                        //mView.populateCrimeData(crimeList);
                        mView.hideLoadingProgress();
                    }
                });
    }

    /**
     * Save all crime data in local db
     *
     * @param crimeList
     */
    public void saveAllCrimes(List<Crime> crimeList) {
        mInteractor.saveAllCrimes(crimeList);
    }


    /**
     * Load the crime data with pagination
     *
     * @param max
     */
    public void loadNextCrimeData(int max) {

        mView.showLoadingProgress();
        Observable<List<Crime>> observable = CrimeWatchApplication.get().getCrimeWatchService().getMajorCrimes(getQueryMap(max));
        mSubscription = observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(CrimeWatchApplication.get().getDefaultScheduler())
                .subscribe(new Subscriber<List<Crime>>() {
                    @Override
                    public void onCompleted() {
                        mView.hideLoadingProgress();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("Error", e.getMessage());
                        mView.hideLoadingProgress();
                        mView.showMessage("Error while loading Crime data");
                    }

                    @Override
                    public void onNext(List<Crime> crimeList) {
                        if (crimeList != null && crimeList.size() > 0) {
                            mCrimeList.addAll(crimeList);

                            CrimeProcessor.processCrimeData(mCrimeList, mCrimeFrequency, mCrimeDictionary);

                            mCrimeFrequency = CrimeProcessor.sortByCrimeFrequency(mCrimeFrequency);

                            mCrimeRank = CrimeProcessor.getCrimeByRank(mCrimeFrequency);

                            mView.populateCrimeData(mCrimeList, mCrimeRank);

                            mView.showMessage("Success loading Crime Data " + mCrimeList.size());
                        } else {
                            mView.showMessage("Error loading Crime Data");
                        }
                        saveAllCrimes(crimeList);

                        mView.hideLoadingProgress();
                    }
                });

    }


    /**
     * Search Crime data with Keywords. Like Assult, Theift, Simple, or Assult Simple
     *
     * @param keywords
     */
    public void searchCrime(String keywords) {
        String[] words = keywords.split("[^\\w']+");
        ArrayList<Crime> searchResult = new ArrayList<>();

        for (String word : words) {
            if (searchResult.isEmpty() && mCrimeDictionary.containsKey(word)) {
                searchResult.addAll(mCrimeDictionary.get(word));
            } else if (mCrimeDictionary.containsKey(word)) {
                searchResult.retainAll(mCrimeDictionary.get(word));
            } else {
                break;
            }
        }
        if (searchResult.size() > 0) {
            mView.searchActivated();
            mView.populateCrimeData(searchResult, mCrimeRank);
        } else {
            mView.showMessage("No result found !!");
        }


    }

    /**
     * QueryMap for Retrofit Parameters
     *
     * @param max
     * @return
     */
    private Map<String, String> getQueryMap(int max) {
        mOffSetValue += max;

        Map<String, String> queryMap = new LinkedHashMap<>();
        queryMap.put("dateOccurredStart", "01-01-2017");
        queryMap.put("dateOccurredEnd", "01-30-2017");
        queryMap.put("max", max + "");
        queryMap.put("offset", mOffSetValue + "");
        queryMap.put("sort", "dateOccurred");

        return queryMap;
    }

    /**
     * check if Crime list is empty
     *
     * @return
     */
    public boolean isCrimeListEmpty() {
        return mCrimeList.isEmpty();
    }


    /**
     * Reset the search result to display all data
     */
    public void resetSearchResult() {
        mView.populateCrimeData(mCrimeList, mCrimeRank);
        mView.showMessage("Displaying all crime data");
    }

}
