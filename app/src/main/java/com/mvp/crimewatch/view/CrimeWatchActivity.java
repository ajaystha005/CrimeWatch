package com.mvp.crimewatch.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mvp.crimewatch.CrimeWatchApplication;
import com.mvp.crimewatch.R;
import com.mvp.crimewatch.constant.NetworkConstant;
import com.mvp.crimewatch.databinding.LayoutCrimeWatchActivityBinding;
import com.mvp.crimewatch.model.Crime;
import com.mvp.crimewatch.presenter.CrimeWatchPresenter;
import com.mvp.crimewatch.utils.DialogUtils;
import com.mvp.crimewatch.utils.ViewUtils;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by ajayshrestha on 3/17/17.
 */

public class CrimeWatchActivity extends AppCompatActivity implements CrimeWatchView, OnMapReadyCallback, View.OnClickListener {

    private final String TAG = CrimeWatchActivity.class.getSimpleName();

    @Inject
    CrimeWatchPresenter mPresenter;

    private GoogleMap mGoogleMap;

    private LayoutCrimeWatchActivityBinding mBinding;

    @Inject
    DialogUtils mDialogUtils;

    private Snackbar mSnackbar;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((CrimeWatchApplication) getApplication()).getDaggerComponent().inject(this);
        mBinding = DataBindingUtil.setContentView(this, R.layout.layout_crime_watch_activity);

        setContentView(mBinding.getRoot());


        mPresenter.onAttachView(this);

        setSupportActionBar(mBinding.toolbar);
        setTitle(R.string.app_title);


        initMap();
        initViewListener();
    }

    /**
     * Initalize the listener
     */
    private void initViewListener() {
        mBinding.fabLoadMoreData.setOnClickListener(this);
    }

    /**
     * Initalize the Google map
     */
    private void initMap() {
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);
    }

    @Override
    public void populateCrimeData(List<Crime> crimeList, Map<String, Integer> lookupRank) {
        if (null == mGoogleMap)
            throw new RuntimeException(getString(R.string.google_map_error));

        if (crimeList == null || crimeList.size() == 0) return;

        mGoogleMap.clear();

        plotMarkerInMapsForCrime(crimeList, lookupRank);

    }

    @Override
    public void showMessage(String message) {
        ViewUtils.showToastMessage(CrimeWatchActivity.this, message);
    }

    @Override
    public void showLoadingProgress() {
        showProgressDialog(getString(R.string.loading));
        mBinding.fabLoadMoreData.setEnabled(false);
    }

    @Override
    public void hideLoadingProgress() {
        hideProgressDialog();
        mBinding.fabLoadMoreData.setEnabled(true);
    }

    @Override
    public void searchActivated() {
        showSearchSnackBar(getString(R.string.clear_search));
    }


    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mGoogleMap = googleMap;
        //Load the crime data if empty. We can fetch data from database too
        if (mPresenter.isCrimeListEmpty())
            mPresenter.loadNextCrimeData(NetworkConstant.MAX_DATA_FETCH);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDetachView();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabLoadMoreData:
                String max = mBinding.maxFetch.getText().toString();
                if (!max.equals("") && Integer.parseInt(max) > 0) {
                    mPresenter.loadNextCrimeData(Integer.valueOf(max));
                } else {
                    mBinding.maxFetch.setError("Must be more than 0");
                }
                break;
        }
    }

    /**
     * Display the search dialog
     */
    private void showSearchDialog() {
        final View dialogView = LayoutInflater.from(CrimeWatchActivity.this).inflate(R.layout.layout_search_crime, null);
        mDialogUtils.showCrimeSearchDialog(CrimeWatchActivity.this, dialogView, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText editText = (EditText) dialogView.findViewById(R.id.searchText);
                mPresenter.searchCrime(editText.getText().toString());
                mDialogUtils.hideKeyboard(CrimeWatchActivity.this);
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mDialogUtils.hideKeyboard(CrimeWatchActivity.this);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.load_more_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.search) {
            showSearchDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Plot all the crime in maps based on their ranking
     *
     * @param crimeList
     * @param crimeRank
     */
    private void plotMarkerInMapsForCrime(List<Crime> crimeList, Map<String, Integer> crimeRank) {
        for (Crime crime : crimeList) {
            try {
                plotCrime(crime, crimeRank.get(crime.getCpdDistrict()));
            } catch (NullPointerException npx) {
                Log.e(TAG, "crime is empty");
            }
        }
        Crime last = crimeList.get(crimeList.size() - 1);
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(last.getLatLng(last.getXCoordinate(), last.getYCoordinate()), 10));
    }


    /**
     * Show the snackbar to notify search results
     *
     * @param message
     */
    private void showSearchSnackBar(String message) {
        mSnackbar = Snackbar.make(mBinding.coordinateLayout, message, Snackbar.LENGTH_INDEFINITE);

        mSnackbar.setAction(getString(R.string.clear), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSnackbar.dismiss();
                mPresenter.resetSearchResult();
            }
        });
        mSnackbar.setActionTextColor(Color.RED);
        mSnackbar.show();
    }


    /**
     * Show loading dialog
     *
     * @param message
     */
    private void showProgressDialog(String message) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(CrimeWatchActivity.this);
            mProgressDialog.setMessage(message);
            mProgressDialog.setCancelable(false);
        }
        mProgressDialog.show();
    }

    /**
     * Hide progress dialog
     */
    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }


    /**
     * Plot the crime based on crime ranking
     *
     * @param crime
     * @param rank
     */
    private void plotCrime(Crime crime, int rank) {
        int color;
        switch (rank) {
            case 1:
                color = R.color.first;
                break;
            case 2:
                color = R.color.second;
                break;
            case 3:
                color = R.color.third;
                break;
            case 4:
                color = R.color.fourth;
                break;
            case 5:
                color = R.color.fifth;
                break;
            case 6:
                color = R.color.sixth;
                break;
            case 7:
                color = R.color.seventh;
                break;
            case 8:
                color = R.color.eighth;
                break;
            case 9:
                color = R.color.nineth;
                break;
            case 10:
                color = R.color.tenth;
                break;

            default:
                color = R.color.default_rank;
                break;
        }
        addMarker(crime, color);
    }

    /**
     * add the marker crime in Google map
     *
     * @param crime
     * @param color
     */
    private void addMarker(Crime crime, int color) {
        mGoogleMap.addMarker(new MarkerOptions().position(crime.getLatLng(crime.getXCoordinate(), crime.getYCoordinate()))
                .icon(BitmapDescriptorFactory.fromBitmap(ViewUtils.getMarkerBitmap(color, 80, 80)))
                .title(crime.getCrimeDescription())
                .snippet("District:" + crime.getCpdDistrict() + ", Date Occurred: " + crime.getDateOccurred()));
    }
}

