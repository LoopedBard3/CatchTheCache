package edu.iastate.cs309.jr2.catchthecacheandroid;

import android.widget.TextView;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;


public class ChatMockitoTest {
    //Tests created by Anastasia Golter
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Test
    public void CandSendChats() {
        //This creates a Mock Object of the class that we have not fully implemented
        Chat.LoadChat bypass = mock(Chat.LoadChat.class);
        TextView view = mock(TextView.class);
        Chat act = spy(new Chat());

        //Parameters for the test function
        //TODO add some parameters so that we are actually testing something

        //Call function class for the other methods
        when(bypass.isCancelled()).thenReturn(true);
        Assert.assertNull(bypass.doInBackground());

    }
}

