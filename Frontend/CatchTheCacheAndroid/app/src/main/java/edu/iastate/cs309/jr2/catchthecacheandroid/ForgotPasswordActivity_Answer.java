package edu.iastate.cs309.jr2.catchthecacheandroid;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import edu.iastate.cs309.jr2.catchthecacheandroid.models.user_models.UserResetPassRequest;
import edu.iastate.cs309.jr2.catchthecacheandroid.models.user_models.UserResetPassResponse;

public class ForgotPasswordActivity_Answer extends AppCompatActivity {
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    // UI references.

    private RequestQueue queue;
    private Gson gson;
    private submitNewPassTask mForgotTask;
    private View mUserRegisterView;
    private View mProgressView;
    private EditText mPasswordText;
    private EditText mPasswordTextMatch;
    private EditText mAnswerText;
    private String user;
    private String question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_answer);
        question = getIntent().getExtras().getString("Question");
        user = getIntent().getExtras().getString("Username");
        TextView ques = findViewById(R.id.securityQuestion);
        ques.setText(question);

        queue = Volley.newRequestQueue(getApplicationContext());
        gson = new Gson();

        mUserRegisterView = findViewById(R.id.user_register_form);
        mProgressView = findViewById(R.id.register_progress);

        mAnswerText = findViewById(R.id.securityQuestionAnswer);
        mPasswordText = findViewById(R.id.securityQuestionPassword);
        mPasswordTextMatch = findViewById(R.id.securityQuestionPasswordMatch);


        final Button mRegisterButton = (Button) findViewById(R.id.recover_account_button);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptResetPassword();
            }
        });
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid username, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptResetPassword() {
        //TODO: Check for proper password and what not
        if (!isPasswordValid(mPasswordText.getText().toString())) {
            mPasswordText.setError("Please enter a valid password");
            mPasswordText.requestFocus();
        } else if(mAnswerText.getText().toString().length() == 0){
            mAnswerText.setError("Please enter answer");
            mAnswerText.requestFocus();
        } else if(!mPasswordText.getText().toString().equals(mPasswordTextMatch.getText().toString())){
            mPasswordText.setError("Please make sure passwords match");
            mPasswordText.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mForgotTask = new submitNewPassTask(user, mAnswerText.getText().toString(), mPasswordText.getText().toString());
            mForgotTask.execute((Void) null);
        }
    }

    private boolean isPasswordValid(String password) {
        //https://stackoverflow.com/questions/3802192/regexp-java-for-password-validation
        return password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{4,}$");
    }



    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mUserRegisterView.setVisibility(show ? View.GONE : View.VISIBLE);
            mUserRegisterView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mUserRegisterView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mUserRegisterView.setVisibility(show ? View.GONE : View.VISIBLE);
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class submitNewPassTask extends AsyncTask<Void, Void, Boolean> {
        String ans;
        String username;
        String newPass;
        submitNewPassTask(String user, String answer, String newPass) {
            ans = answer;
            username = user;
            this.newPass = newPass;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            JSONObject jsonData;
            try {
                jsonData = new JSONObject(gson.toJson(new UserResetPassRequest(username, newPass, ans)));
                //TODO: Change the request location
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,getString(R.string.access_url), jsonData,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                showProgress(false);
                                UserResetPassResponse respJson = gson.fromJson(response.toString(), UserResetPassResponse.class);
                                if (respJson.getSuccess()) {
                                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                    startActivity(intent);
                                } else {
                                    mAnswerText.setError("Unable to process request");
                                    mAnswerText.requestFocus();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mAnswerText.setError("An problem arose while submitting answer");
                        mAnswerText.requestFocus();
                        showProgress(false);
                    }
                });

                queue.add(jsonObjectRequest);
            } catch (JSONException e) {
                return false;
            }

            return true;
        }


        @Override
        protected void onCancelled() {
            mForgotTask = null;
            showProgress(false);
        }
    }
}
