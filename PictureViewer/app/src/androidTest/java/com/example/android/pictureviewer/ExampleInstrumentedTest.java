package com.example.android.pictureviewer;

import android.content.Context;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.webkit.URLUtil;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.URL;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.example.android.pictureviewer", appContext.getPackageName());
    }

    @Test
    public void photo_fetch_valid() throws Exception {
        String page = "3";
        String pref = "Nature";
        URL photosUrl = QueryUtils.buildUrl(page, pref);
        assertTrue("Construced URL for fecthing data is not valid", URLUtil.isValidUrl(photosUrl.toString()));
        List<Picture> pics = QueryUtils.extractPictures(photosUrl.toString());
        assertTrue("No data is fetched from the 500px API", pics != null && pics.size() != 0);
        if (pics != null && pics.size() > 0) {
            for (Picture pic : pics) {
                String imageUrl = pic.getImageUrl();
                assertTrue("The fetched photo has no valid URL", URLUtil.isValidUrl(imageUrl));
            }
        }
    }

    @Test
    public void inner_class_exists() throws Exception {
        Class[] innerClassesForAdapter = PictureAdapter.class.getDeclaredClasses();
        assertEquals("There should be 1 Inner class inside the contract class", 1, innerClassesForAdapter.length);
    }

    @Test
    public void inner_class_type_correct() throws Exception {
        Class[] innerClasses = PictureAdapter.class.getDeclaredClasses();
        assertEquals("Cannot find inner class to complete unit test", 1, innerClasses.length);

        Class entryClass = innerClasses[0];
        assertTrue("Inner class should implement the BaseColumns interface", RecyclerView.ViewHolder.class.isAssignableFrom(entryClass));


    }


}
