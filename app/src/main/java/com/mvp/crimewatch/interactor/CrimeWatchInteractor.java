package com.mvp.crimewatch.interactor;

import android.content.Context;
import android.util.Log;

import com.mvp.crimewatch.CrimeWatchApplication;
import com.mvp.crimewatch.database.CrimeDatabaseHelper;
import com.mvp.crimewatch.model.Crime;
import com.mvp.crimewatch.utils.RxUtil;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by ajayshrestha on 3/17/17.
 */

/**
 * This interactor is used to interact with other components in the app
 */
public class CrimeWatchInteractor {
    private final String TAG = CrimeWatchInteractor.class.getSimpleName();

    @Inject
    CrimeDatabaseHelper databaseHelper;

    public CrimeWatchInteractor(Context mContext) {
        ((CrimeWatchApplication) mContext).getDaggerComponent().inject(this);
    }

    public Observable<List<Crime>> getCrimes() {
        return RxUtil.makeObservable(getCallable());
    }

    public List<Crime> getAllMajorCrimes() {
        try {
            return databaseHelper.getAllMajorCrime();
        } catch (SQLException ex) {
            Log.e(TAG, "exception in getting crimes" + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }


    private Callable<List<Crime>> getCallable() {
        return new Callable() {
            public List<Crime> call() {
                return getAllMajorCrimes();
            }
        };

    }

    /**
     * Save all crimes in DB
     *
     * @param crimeList
     */
    public void saveAllCrimes(List<Crime> crimeList) {
        try {
            databaseHelper.saveAllCrimes(crimeList);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}
