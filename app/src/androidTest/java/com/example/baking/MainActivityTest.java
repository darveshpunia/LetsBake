package com.example.baking;

import android.view.Gravity;
import android.view.View;

import com.example.baking.ui.main.MainActivity;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.InstrumentationRegistry;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import junit.framework.AssertionFailedError;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerMatchers.isClosed;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
  @Rule
  public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

  @Test
  public void testAdapterClick(){
    //testing activities that none is crashing
    for (int i=0;i<4;i++) {
      //clicking recycler view child item
      onView(ViewMatchers.withId(R.id.recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(i, clickChildViewWithId(R.id.layout_steps)));
      // testing UI components, 7 is a random number, taken since 7 there are at least 7 rows for every recipe
      for (int j=0;j<7;j++) {
        onView(ViewMatchers.withId(R.id.recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(j, click()));
        try {
          // checking if exo player is displayed
          onView(withId(R.id.video_view)).check(matches(isDisplayed()));
        } catch (AssertionFailedError e) {
          //if exo-player not visible then 'no video' text should be visible
          onView(withId(R.id.tv_no_video)).check(matches(isDisplayed()));
        }
      }
      // closing that activity
      closeActivity();
    }
  }

  public void closeActivity(){
    onView(isRoot()).perform(ViewActions.pressBack());
  }

  public static ViewAction clickChildViewWithId(final int id) {
    return new ViewAction() {
      @Override
      public Matcher<View> getConstraints() {
        return null;
      }

      @Override
      public String getDescription() {
        return "Click on a child view with specified id.";
      }

      @Override
      public void perform(UiController uiController, View view) {
        View v = view.findViewById(id);
        v.performClick();
      }
    };
  }

}
