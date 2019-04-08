package edu.iastate.cs309.jr2.catchthecacheandroid;

import android.os.AsyncTask;
import android.widget.EditText;

import com.android.volley.RequestQueue;

import org.json.JSONException;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;


//Anastasia's tests for demo 4 sorry they are kind of stupid, I'm not sure if i am doing them right .. actually I don't think my tests do anything yet, but Ill come back to them later

public class CacheAddMockitoTest {

    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Test
    public void cacheAddNotNull() throws JSONException {
        //This creates a Mock Object of the class that we have not fully implemented

        CacheAddActivity test = mock(CacheAddActivity.class);
        CacheAddActivity act = spy(new CacheAddActivity());


        //Parameters for the test function, try adding null values

       String mCacheLat = "";
       String mCacheName = "";
       String mCacheLong = "";


        when(test.cacheValid(mCacheLat, mCacheName, mCacheLong)).thenReturn(false);
        Assert.assertFalse(test.cacheValid(mCacheLat, mCacheName, mCacheName));
        doNothing().when(act).addCache();
    }




    @Test
    public void cacheAddWithinBounds() throws JSONException {
        //This creates a Mock Object of the class that we have not fully implemented

        CacheAddActivity test = mock(CacheAddActivity.class);
        CacheAddActivity act = spy(new CacheAddActivity());

        //Parameters for the test function, try values that are outside of the bounds
        String mCacheLat = "10000";
        String mCacheName = "New";
        String mCacheLong = "10000";


        when(test.cacheValid(mCacheLat, mCacheName, mCacheLong)).thenReturn(false);
        Assert.assertFalse(test.cacheValid(mCacheLat, mCacheName, mCacheLong));
        doNothing().when(act).addCache();
    }

    @Test
    public void cacheAddLatLongNotNumbers() throws JSONException {
        //This creates a Mock Object of the class that we have not fully implemented

        CacheAddActivity test = mock(CacheAddActivity.class);
        CacheAddActivity act = spy(new CacheAddActivity());


        //Parameters for the test function, Longitudes and Latitudes should not contains alphabet characters.
        String mCacheLat = "abc";
        String mCacheName = "New";
        String mCacheLong = "efg";


        when(test.cacheValid(mCacheLat, mCacheName, mCacheLong)).thenReturn(false);
        Assert.assertFalse(test.cacheValid(mCacheLat, mCacheName, mCacheLong));
        doNothing().when(act).addCache();
    }
}
