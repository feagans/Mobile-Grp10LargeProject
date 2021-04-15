package com.test.mylifegoale.ui.login;

import android.app.Activity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.test.mylifegoale.MyApplication;
import com.test.mylifegoale.R;
import android.util.Log;
import android.app.Application;
import android.widget.EditText;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.test.mylifegoale.data.APIError;
import com.test.mylifegoale.data.APIService;
import com.test.mylifegoale.ui.login.LoginViewModel;
import com.test.mylifegoale.ui.login.LoginViewModelFactory;
import com.test.mylifegoale.view.SplashActivity;

public class LoginActivity extends AppCompatActivity {
    private static LoginActivity mInstance;
    public Retrofit retrofit;
    public APIService.API API;
    public APIService.LoginResponse user;
    private LoginViewModel loginViewModel;
    boolean validUser = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // API
        super.onCreate(savedInstanceState);
        mInstance = this;
        this.retrofit = new Retrofit.Builder()
                .baseUrl("https://letsbuckit.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.API = retrofit.create(APIService.API.class);

        setContentView(R.layout.activity_login);
        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);
        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (!validUser) {
                    showLoginFailed(loginResult.getError());
                }
                if (validUser) {
                    Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
                    startActivity(intent);
                    updateUiWithUser(loginResult.getSuccess());
                    // Complete and destroy login activity once successful
                    finish();
                }
//                setResult(Activity.RESULT_OK);

            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                }
                return false;
            }
        });

        // On login click, verify user's credentials using API.
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("taggy", "ONCLICK!!");
                try {
                    APIService.LoginRequest user = new APIService.LoginRequest(usernameEditText.getText().toString(), passwordEditText.getText().toString());
                    API.login(user).enqueue(new Callback<APIService.LoginResponse>() {
                        @Override
                        public void onResponse(Call<APIService.LoginResponse> call, Response<APIService.LoginResponse> response) {
                            Log.d("MainActivity", "Status Code = " + response.code());
                            LoginActivity.getInstance().user = response.body();

                            // Valid credentials
                            if (response.code() == 200) {
                                validUser = true;
                                Log.d("taggy", "VALID!!");
                            }

                            // Invalid credentials status code = 204
                            else {
                                Log.d("TAGGYTAG", "FAILED!!");
                                // Show error message
                            }

                            loginViewModel.login(validUser);
                        }

                        // Request failed
                        @Override
                        public void onFailure(Call<APIService.LoginResponse> call, Throwable t) {
                            Log.d("TAGGYTAG", "failing!");

                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
                loadingProgressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Log.d("taggy", "ShowloginFail!!");
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    public static synchronized LoginActivity getInstance() {
        LoginActivity loginActivity;
        synchronized (LoginActivity.class) {
            loginActivity = mInstance;
        }
        return loginActivity;
    }
}