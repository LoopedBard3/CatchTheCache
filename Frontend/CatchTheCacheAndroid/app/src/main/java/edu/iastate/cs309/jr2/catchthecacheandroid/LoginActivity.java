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
import android.view.View.OnClickListener;
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

import edu.iastate.cs309.jr2.catchthecacheandroid.models.user_models.User;
import edu.iastate.cs309.jr2.catchthecacheandroid.models.user_models.UserLoginRequest;
import edu.iastate.cs309.jr2.catchthecacheandroid.models.user_models.UserLoginResponse;

//Added from chat branch
//End added from chat branch


/**

 * A login screen that offers login via username/password.

 */
public class LoginActivity extends AppCompatActivity {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mUsernameView;
    private EditText mPasswordView;
    private TextView debugText;
    private View mProgressView;
    private View mLoginFormView;
    private RequestQueue queue;
    private Gson gson;
    private UserChecker userChecker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mUsernameView = findViewById(R.id.username);
        queue = Volley.newRequestQueue(getApplicationContext());
        //TODO: Remove debugText and Text Box
        debugText = findViewById(R.id.debugText);
        gson = new Gson();
        mPasswordView = findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin(mUsernameView.getText().toString(), mPasswordView.getText().toString(), mPasswordView);
                    return true;
                }
                return false;
            }
        });

        userChecker = new UserChecker();
        Button mSignInButton = (Button) findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin(mUsernameView.getText().toString(), mPasswordView.getText().toString(), mPasswordView);
            }
        });

        final Button mRegisterButton = (Button) findViewById(R.id.register_account_button);
        mRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                registerBtnPressed();
            }
        });

        final Button mForgotPasswordButton = (Button) findViewById(R.id.forgot_password_button);
        mForgotPasswordButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                forgotPasswordPressed();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }




    /**
     * Attempts to go through the register control flow
     * for registering a new user.
     */
    private void registerBtnPressed(){
        Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
        startActivity(intent);
    }

//    private void configureNextButton(){
//        Button nextbutton = (Button) findViewById(R.id.nextbutton);
//        nextbutton.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent (LoginActivity.this, MainActivity.class));
//            }
//        });
//    }


//    private boolean mayRequestContacts() {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//            return true;
//        }
//        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
//            return true;
//        }
//        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
//            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
//                    .setAction(android.R.string.ok, new View.OnClickListener() {
//                        @Override
//                        @TargetApi(Build.VERSION_CODES.M)
//                        public void onClick(View v) {
//                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
//                        }
//                    });
//        } else {
//            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
//        }
//        return false;
//    }


    /**
     * Attempts to go through the forgot password control
     * flow for users that forgot their password.
     */
    private void forgotPasswordPressed(){
        Intent intent = new Intent(getApplicationContext(), ForgotPasswordActivity_Username.class);
        startActivity(intent);
    }



    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid username, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public boolean attemptLogin(String username, String password, TextView view) {

        boolean cancel = false;
        View focusView = null;
        if(userChecker == null) userChecker = new UserChecker();

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            view.setError(getString(R.string.error_field_required));
            focusView = view;
            cancel = true;
        }else if (!userChecker.isPasswordValid(password)){
            view.setError(getString(R.string.error_invalid_password));
            focusView = view;
            cancel = true;
        }

        // Check for a valid username address.
        if (TextUtils.isEmpty(username)) {
            view.setError(getString(R.string.error_field_required));
            focusView = view;
            cancel = true;
        } else if (!userChecker.isUsernameValid(username)) {
            view.setError(getString(R.string.error_invalid_username));
            focusView = view;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
            return false;
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(username, password);
            mAuthTask.execute((Void) null);
            return true;
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUsername;
        private final String mPassword;

        UserLoginTask(String username, String password) {
            mUsername = username;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            JSONObject jsonData;
            try {
                jsonData = new JSONObject(gson.toJson(new UserLoginRequest(mUsername, mPassword)));
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,getString(R.string.access_url) + "login", jsonData,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                mAuthTask = null;
                                showProgress(false);
                                UserLoginResponse respJson = gson.fromJson(response.toString(), UserLoginResponse.class);
                                if (respJson.getSuccess()) {
                                    debugText.setText(String.format("Successfully Logged in %s", mUsername));
                                    //TODO:Logic for if the user already existed or not and opening next activity
                                        Intent intent = new Intent(getApplicationContext(), CacheListActivity.class);
                                        intent.putExtra("UserObject", new User(mUsername, respJson.getAuthority()));
                                        startActivity(intent);
                                } else {
                                    mPasswordView.setError(getString(R.string.error_incorrect_sign_in));
                                    mPasswordView.requestFocus();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        debugText.setText("Problem getting response");
                        debugText.requestFocus();
                        Log.d("ERROR", "onErrorResponse: " + error.toString());
                        showProgress(false);
                    }
                });

                queue.add(jsonObjectRequest);
            } catch (JSONException e) {
                debugText.setText(e.toString());
                return false;
            }

            return true;
        }


        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

