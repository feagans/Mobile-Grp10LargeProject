package com.test.mylifegoale.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Patterns;

import com.test.mylifegoale.data.LoginRepository;
import com.test.mylifegoale.data.Result;
import com.test.mylifegoale.data.model.LoggedInUser;
import com.test.mylifegoale.R;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(Boolean valid) {

        if (valid) {
            loginResult.setValue(new LoginResult(new LoggedInUserView("ALYSSA")));
        } else {
            loginResult.setValue(new LoginResult(R.string.login_failed));
        }
    }

    public void loginDataChanged(String username, String password) {
        // If username is blank
        if (username == null || username.equals("")){
            loginFormState.setValue(new LoginFormState(R.string.empty_username, null));
        }

        else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return (password != null && !password.equals(""));
//        return password != null && password.trim().length() > 5;
    }
}