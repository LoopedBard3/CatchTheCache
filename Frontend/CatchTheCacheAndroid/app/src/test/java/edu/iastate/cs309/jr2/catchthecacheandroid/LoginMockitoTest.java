package edu.iastate.cs309.jr2.catchthecacheandroid;

import android.os.AsyncTask;
import android.widget.TextView;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;


public class LoginMockitoTest {
    //Tests created by Parker Bibus
    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Test
    public void userPassValidTestExeTrue_returnsTrue() {
        //This creates a Mock Object of the class that we have not fully implemented
        LoginActivity.UserLoginTask bypass = mock(LoginActivity.UserLoginTask.class);
        TextView view = mock(TextView.class);
        LoginActivity act = spy(new LoginActivity());

        //Parameters for the test function
        String username = "Micheal12";
        String password = "Obiw0n12";

        when(bypass.execute()).thenReturn(new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                return true;
            }
        });
        doNothing().when(act).showProgress(true);
        Assert.assertTrue(act.attemptLogin(username, password, view));
    }

    @Test
    public void userPassNotValidTest_returnsFalse() {
        //This creates a Mock Object of the class that we have not fully implemented
        LoginActivity.UserLoginTask bypass = mock(LoginActivity.UserLoginTask.class);
        TextView view = mock(TextView.class);
        LoginActivity act = spy(new LoginActivity());

        //Parameters for the test function
        String username = "Miche";
        String password = "Obiw";

        when(bypass.execute()).thenReturn(new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                return false;
            }
        });
        doNothing().when(act).showProgress(true);
        Assert.assertFalse(act.attemptLogin(username, password, view));
    }

    @Test
    public void userValidNotPassTestExeFalse_returnsFalse(){
        //This creates a Mock Object of the class that we have not fully implemented
        LoginActivity.UserLoginTask bypass = mock(LoginActivity.UserLoginTask.class);
        TextView view = mock(TextView.class);
        LoginActivity act = spy(new LoginActivity());

        //Parameters for the test function
        String username = "Micheal12";
        String password = "Obi";

        when(bypass.execute()).thenReturn(new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                return false;
            }
        });
        doNothing().when(act).showProgress(true);
        Assert.assertFalse(act.attemptLogin(username, password, view));
    }

    @Test
    public void userPassNotValidTestExeTrue_returnsFalse(){
        //This creates a Mock Object of the class that we have not fully implemented
        LoginActivity.UserLoginTask bypass = mock(LoginActivity.UserLoginTask.class);
        TextView view = mock(TextView.class);
        LoginActivity act = spy(new LoginActivity());

        //Parameters for the test function
        String username = "Mic2";
        String password = "Obi";

        when(bypass.execute()).thenReturn(new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                return true;
            }
        });
        doNothing().when(act).showProgress(true);
        Assert.assertFalse(act.attemptLogin(username, password, view));
    }

}
