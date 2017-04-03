package com.mvp.crimewatch.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by ajayshrestha on 3/18/17.
 */

public class DialogUtils {

    private static final String POSITIVE_BUTTON = "SEARCH";
    private static final String NEGATIVE_BUTTON = "CANCEL";

    /**
     * ----- use to forcefully display or hide the keyboard -----
     */
    public void showOrHideKeyboardForcefully(Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    /**
     * Hide the Keyboard
     *
     * @param mContext
     */
    public void hideKeyboard(Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

    }

    public void showCrimeSearchDialog(Context mContext, View dialogView,
                                      DialogInterface.OnClickListener positiveBtnCallback, DialogInterface.OnClickListener negativeBtnCallback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setView(dialogView);
        builder.setPositiveButton(POSITIVE_BUTTON, positiveBtnCallback);
        builder.setNegativeButton(NEGATIVE_BUTTON, negativeBtnCallback);
        builder.setCancelable(false);
        builder.show();
        showOrHideKeyboardForcefully(mContext);
    }
}
