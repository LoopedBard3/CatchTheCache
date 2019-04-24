package edu.iastate.cs309.jr2.catchthecacheandroid;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
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

import edu.iastate.cs309.jr2.catchthecacheandroid.models.user_models.UserCreateRequest;
import edu.iastate.cs309.jr2.catchthecacheandroid.models.user_models.UserCreateResponse;

/**
 * Activity for users to signup
 */
public class SignUpActivity extends AppCompatActivity {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    private UserRegisterTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mUsernameView;
    private EditText mPasswordView;
    private EditText mPasswordViewMatch;
    private EditText mSecurityQuestion;
    private EditText mSecurityAnswerMain;
    private EditText mSecurityAnswerMatch;
    private View mProgressView;
    private View mRegisterFormView;
    private RequestQueue queue;
    private Gson gson;

    /**
     * Default method for starting the activity.
     * Sets up the Gson json translator, Volley queue,
     * and the user buttons for registering.
     * @author Parker Bibus
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // Set up the login form.
        mUsernameView = findViewById(R.id.username);
        queue = Volley.newRequestQueue(getApplicationContext());
        gson = new Gson();
        mPasswordView = findViewById(R.id.password);
        mPasswordViewMatch = findViewById(R.id.passwordMatch);
        mSecurityQuestion = findViewById(R.id.securityQuestion);
        mSecurityAnswerMain = findViewById(R.id.securityAnswer);
        mSecurityAnswerMatch = findViewById(R.id.securityAnswerCheck);
        mSecurityAnswerMatch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptRegister();
                    return true;
                }
                return false;
            }
        });

        final Button mRegisterButton = (Button) findViewById(R.id.register_account_button);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });


        mRegisterFormView = findViewById(R.id.register_form);
        mProgressView = findViewById(R.id.register_progress);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid username, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptRegister() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);
        mPasswordViewMatch.setError(null);
        mSecurityQuestion.setError(null);
        mSecurityAnswerMain.setError(null);
        mSecurityAnswerMatch.setError(null);



        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();
        String passwordMatch = mPasswordViewMatch.getText().toString();
        String securityQuestion = mSecurityQuestion.getText().toString();
        String securityAnswer = mSecurityAnswerMain.getText().toString();
        String securityAnswerMatch = mSecurityAnswerMatch.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }else if (!isPasswordValid(password)){
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }else if(!doPasswordsMatch(password, passwordMatch)){
            mPasswordViewMatch.setError(getString(R.string.error_password_mismatch));
            focusView = mPasswordViewMatch;
            cancel = true;
        }

        // Check for a valid username address.
        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        } else if (!isUsernameValid(username)) {
            mUsernameView.setError(getString(R.string.error_invalid_username));
            focusView = mUsernameView;
            cancel = true;
        }

        //Check for security question and answer
        if(TextUtils.isEmpty(securityQuestion)){
            mSecurityQuestion.setError("Security Question Required");
            focusView = mSecurityQuestion;
            cancel = true;
        } else if(!securityAnswer.equals(securityAnswerMatch)){
            mSecurityAnswerMain.setError("Security Question Answer Don\'t Match");
            focusView = mSecurityAnswerMain;
            cancel = true;
        }


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new SignUpActivity.UserRegisterTask(username, password, securityQuestion, securityAnswer);
            mAuthTask.execute((Void) null);
        }
    }

    /**
     * Method to check if a specific username is valid.
     * @param username the username to check
     * @return true if the username is valid, false otherwise
     */
    private boolean isUsernameValid(String username) {
        //https://stackoverflow.com/questions/12018245/regular-expression-to-validate-username
        return username.matches("^(?=.{3,}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$");
    }

    /**
     * Method to check if a specific password is valid.
     * @param password the password to check
     * @return true if the username is valid, false otherwise
     */
    private boolean isPasswordValid(String password) {
        //https://stackoverflow.com/questions/3802192/regexp-java-for-password-validation
        return password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{4,}$");
    }

    /**
     * Method to make sure that two strings are the same.
     * @param passwordMain Password to match against
     * @param passwordMatch Password to check
     * @return true if the username is valid, false otherwise
     */
    private boolean doPasswordsMatch(String passwordMain, String passwordMatch){
        return passwordMain.equals(passwordMatch);
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

            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mRegisterFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUsername;
        private final String mPassword;
        private final String mSecQuestion;
        private final String mSecAnswer;

        /**
         * Constructor for the task.
         * @param username the username to register
         * @param passwordMain the password to register
         * @param securityQuestion the security question to register
         * @param securityAnswer the security answer to register
         */
        UserRegisterTask(String username, String passwordMain, String securityQuestion, String securityAnswer) {
            mUsername = username;
            mPassword = passwordMain;
            mSecQuestion = securityQuestion;
            mSecAnswer = securityAnswer;
        }

        /**
         * The process that happens when the task gets called.
         * This consists of sending the data to be used to register
         * a new user.
         * @param params
         * @return true on success and false one everything else
         */
        @Override
        protected Boolean doInBackground(Void... params) {
            final JSONObject jsonData;
            try {
                UserCreateRequest req = new UserCreateRequest();
                req.updateRequest(mUsername, mPassword, mSecQuestion, mSecAnswer);
                jsonData = new JSONObject(gson.toJson(req));
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,getString(R.string.access_url) + "users", jsonData,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                mAuthTask = null;
                                showProgress(false);
                                UserCreateResponse respJson = gson.fromJson(response.toString(), UserCreateResponse.class);
                                Log.d("RESPONSE", String.valueOf(response.toString()));
                                if (respJson.getSuccess()) {
                                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                        startActivity(intent);
                                } else if(!respJson.getValidPass()){
                                    mPasswordView.setError(getString(R.string.error_invalid_password));
                                    mPasswordView.requestFocus();
                                } else if(!respJson.getValidUser()){
                                    mUsernameView.setError("Username is already taken!");
                                    mUsernameView.requestFocus();
                                }else {
                                    mUsernameView.setError("Something went wrong, please try again.");
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("GSON", jsonData.toString());
                        Log.d("ERROR", "onErrorResponse: " + error.toString());
                        mUsernameView.setError("Problem getting response");
                        mUsernameView.requestFocus();
                        showProgress(false);
                    }
                });

                queue.add(jsonObjectRequest);
            } catch (JSONException e) {
                return false;
            }
            return true;
        }


        /**
         * The method that gets called when the task gets cancelled.
         */
        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}
