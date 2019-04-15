package edu.iastate.cs309.jr2.catchthecacheandroid;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import edu.iastate.cs309.jr2.catchthecacheandroid.models.user_models.UserQuestionRequest;
import edu.iastate.cs309.jr2.catchthecacheandroid.models.user_models.UserQuestionResponse;

public class ForgotPasswordActivity_Username extends AppCompatActivity {
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    // UI references.

    private RequestQueue queue;
    private Gson gson;
    private getQuestionTask mForgotTask;
    private View mUsernameFormView;
    private View mProgressView;
    private EditText mUsernameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_username);
        queue = Volley.newRequestQueue(getApplicationContext());
        gson = new Gson();
        mUsernameFormView = findViewById(R.id.user_question_form);
        mUsernameText = findViewById(R.id.forgot_pass_username_field);
        mProgressView = findViewById(R.id.register_progress);
        final Button mRegisterButton = (Button) findViewById(R.id.get_question_button);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRetrieveQuestion();
            }
        });
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid username, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptRetrieveQuestion() {

        if (!isUsernameValid(mUsernameText.getText().toString())) {
            mUsernameText.setError("Please enter a username");
            mUsernameText.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mForgotTask = new getQuestionTask(mUsernameText.getText().toString());
            mForgotTask.execute((Void) null);
        }
    }


    /**
     * Shows the progress UI and hides the login form.
     * @param show true to show the progress bar, false to hid it.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mUsernameFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mUsernameFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mUsernameFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.GONE : View.VISIBLE);
            mUsernameFormView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * Checks if the username is valid.
     * @param username username to be checked
     * @return true if username is valid and false if it is not
     */
    private boolean isUsernameValid(String username) {
        //https://stackoverflow.com/questions/12018245/regular-expression-to-validate-username
        return username.matches("^(?=.{3,}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$");
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class getQuestionTask extends AsyncTask<Void, Void, Boolean> {
        private String username;

        /**
         * Constructor for security question retrieval task.
         * @param name username to get the question for
         */
        getQuestionTask(String name) {
            username = name;
        }

        /**
         * The background task to perform the obtainment of the security question.
         * @param params
         * @return true if successful, false otherwise
         */
        @Override
        protected Boolean doInBackground(Void... params) {
            JSONObject jsonData;
            try {
                jsonData = new JSONObject(gson.toJson(new UserQuestionRequest(username)));
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,getString(R.string.access_url) + "login/forgot", jsonData,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                showProgress(false);
                                UserQuestionResponse respJson = gson.fromJson(response.toString(), UserQuestionResponse.class);
                                if ( respJson.getQuestion() != null && respJson.getQuestion().length() != 0) {
                                    Intent intent = new Intent(getApplicationContext(), ForgotPasswordActivity_Answer.class);
                                    intent.putExtra("Question", respJson.getQuestion());
                                    intent.putExtra("Username", username);
                                    startActivity(intent);
                                } else {
                                    mUsernameText.setError("Username not found.");
                                    mUsernameText.requestFocus();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showProgress(false);
                        mUsernameText.setError("An problem arose while getting question");
                        Log.d("REQUESTRESPONSE", error.networkResponse.toString());
                    }
                });

                queue.add(jsonObjectRequest);
            } catch (JSONException e) {
                return false;
            }
            return true;
        }

        /**
         * Method called when task is cancelled.
         */
        @Override
        protected void onCancelled() {
            mForgotTask = null;
            showProgress(false);
        }
    }
}
