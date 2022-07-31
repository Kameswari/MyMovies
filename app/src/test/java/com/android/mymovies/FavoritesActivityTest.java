package com.android.mymovies;

import static junit.framework.TestCase.assertNotNull;
import static org.robolectric.Shadows.shadowOf;

import android.content.Intent;

import androidx.appcompat.widget.Toolbar;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class FavoritesActivityTest {
    private FavoritesActivity favoritesActivity;

    @Before
    public void setUp() throws Exception {
        favoritesActivity = Robolectric.buildActivity(FavoritesActivity.class)
                .create()
                .resume()
                .get();

        favoritesActivity.initUI();
    }

    @Test
    public void verifyUIElements(){
        Toolbar toolbar = favoritesActivity.findViewById(R.id.toolbar);
        Assert.assertNotNull(toolbar);
        Assert.assertEquals(toolbar.getTitle(), "My Favorites");
    }

    // By clicking on search icon in favorites screen, verify whether search screen is launched or not
    @Test
    public void verifySearchActivityLaunched() {
//        favoritesActivity.findViewById(R.id.search_view).performClick();
        Intent intent = shadowOf(favoritesActivity).peekNextStartedActivity();
        assertNotNull(intent.getComponent());
        Assert.assertEquals(SearchActivity.class.getCanonicalName(), intent.getComponent().getClassName());
    }

    // TODO: Need to write test cases to verify UI for details, search screens
}
