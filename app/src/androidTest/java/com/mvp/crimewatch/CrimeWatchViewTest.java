package com.mvp.crimewatch;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;

import com.mvp.crimewatch.view.CrimeWatchActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by ajayshrestha on 3/20/17.
 */

@RunWith(AndroidJUnit4.class)
public class CrimeWatchViewTest {

    @Rule
    public ActivityTestRule<CrimeWatchActivity> mActivityRule = new ActivityTestRule<>(CrimeWatchActivity.class);

    private UiDevice mDevice;

    @Before
    public void setup() {
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    }

    @Test
    public void testSearch() {
        onView(withId(R.id.fabLoadMoreData)).perform(click());

        onView(withId(R.id.search)).perform(click());
        onView(withId(R.id.searchText)).perform(typeText("Simple"));
        try {
            new UiObject(new UiSelector().text("SEARCH")).click();

        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }


    }
}
