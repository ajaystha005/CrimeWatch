package com.mvp.crimewatch.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.mvp.crimewatch.constant.DbConstant;
import com.mvp.crimewatch.model.Crime;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by ajayshrestha on 3/17/17.
 */

/**
 * Database to save crime data
 */

public class CrimeDatabaseHelper extends OrmLiteSqliteOpenHelper {
    private final String TAG = CrimeDatabaseHelper.class.getSimpleName();


    private Dao<Crime, Long> mCrimeDao;

    public CrimeDatabaseHelper(Context mContext) {
        super(mContext, DbConstant.DB_NAME, null, DbConstant.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            Log.i("Create Table", "let's go");
            TableUtils.createTable(connectionSource, Crime.class);
        } catch (SQLException sqlException) {
            Log.e(TAG, "Can't create database");
            throw new RuntimeException(sqlException);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }

    /**
     * Get all Major Crime
     *
     * @return
     * @throws SQLException
     */
    public List<Crime> getAllMajorCrime() throws SQLException {
        return getDao(Crime.class).queryForAll();
    }

    /**
     * Save all crime
     *
     * @param crimeList
     * @throws Exception
     */
    public void saveAllCrimes(final List<Crime> crimeList) throws Exception {
        getCrimeDao().callBatchTasks(new Callable<Crime>() {
            @Override
            public Crime call() throws Exception {
                for (Crime crime : crimeList) {
                    getCrimeDao().createOrUpdate(crime);
                }
                return null;
            }
        });
    }

    /**
     * Get crime DAO
     *
     * @return
     * @throws SQLException
     */
    public Dao<Crime, Long> getCrimeDao() throws SQLException {
        if (mCrimeDao == null) {
            mCrimeDao = getDao(Crime.class);
        }

        return mCrimeDao;
    }


}
