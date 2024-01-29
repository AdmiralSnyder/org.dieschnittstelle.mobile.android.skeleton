package org.dieschnittstelle.mobile.android.skeleton.pages;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import org.dieschnittstelle.mobile.android.skeleton.R;
import org.dieschnittstelle.mobile.android.skeleton.data.FirebaseDb;
import org.dieschnittstelle.mobile.android.skeleton.data.Storage;
import org.dieschnittstelle.mobile.android.skeleton.databinding.ActivityLoginBinding;
import org.dieschnittstelle.mobile.android.skeleton.models.Login;
import org.dieschnittstelle.mobile.android.skeleton.viewmodels.LoginViewModel;

public class LoginActivity extends ActivityBase<LoginViewModel>
{
    private ActivityLoginBinding itemBinding;
    @Override
    protected Class<LoginViewModel> getViewModelClass() { return LoginViewModel.class; }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        itemBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        itemBinding.setActivity(this);
        var vm = getViewModel();
        itemBinding.setVM(vm);

        This = this;

        InitDataBase();

        label_inputErrorsPassword = findViewById(R.id.label_inputErrorsPassword);
        label_inputErrorsLogin = findViewById(R.id.label_inputErrorsLogin);
        label_loginErrors = findViewById(R.id.label_loginErrors);

        // TODO rausnehmen für die Abgabe
//        Login.CurrentLogin.setName("DemoUser@MAD.edu");
//        Login.CurrentLogin.setPassword("123456");

        button_login = findViewById(R.id.button_login);

        View.OnFocusChangeListener onFocusChangeListener = (view, hasFocus) ->
        {
            if (hasFocus) return;
            checkLoginStuff();
        };
        var updateLoginButtonStateTextWatcher = new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void afterTextChanged(Editable s)
            {
                label_loginErrors.setVisibility(View.INVISIBLE);
                label_inputErrorsPassword.setVisibility(View.INVISIBLE);
                label_inputErrorsLogin.setVisibility(View.INVISIBLE);
                button_login.setEnabled(vm.getCanLogin());
            }
        };


        edit_login = findViewById(R.id.edit_login);
        edit_login.setOnFocusChangeListener(onFocusChangeListener);
        edit_login.addTextChangedListener(updateLoginButtonStateTextWatcher);

        edit_password = findViewById(R.id.edit_password);
        edit_password.setOnFocusChangeListener(onFocusChangeListener);
        edit_password.addTextChangedListener(updateLoginButtonStateTextWatcher);

        LoginPB = findViewById(R.id.progressBar);
        FirebaseDb.CheckConnectionAsync(connected ->
        {
            if (connected)
            {
                button_login.setEnabled(vm.getCanLogin());
            }
            else
            {
                startActivity(new Intent(This, TodoOverviewActivity.class));
            }
        });

        button_login.setEnabled(vm.getCanLogin());


        checkLoginStuff();
    }

    private void checkLoginStuff()
    {
        var vm = getViewModel();
        if (!vm.getIsChanged()) return;
        label_loginErrors.setVisibility(View.INVISIBLE);
        String pwdError = Login.ValidatePassword(vm.getPassword());
        if (pwdError != null)
        {
            label_inputErrorsPassword.setText(pwdError);
            label_inputErrorsPassword.setVisibility(View.VISIBLE);
        }
        else
        {
            label_inputErrorsPassword.setText("ERROR LOGGING IN");
            label_inputErrorsPassword.setVisibility(View.INVISIBLE);
        }
        String loginError = Login.ValidateLogin(vm.getName());
        if (loginError != null)
        {
            label_inputErrorsLogin.setText(loginError);
            label_inputErrorsLogin.setVisibility(View.VISIBLE);
        }
        else
        {
            label_inputErrorsLogin.setText("ERROR LOGGING IN");
            label_inputErrorsLogin.setVisibility(View.INVISIBLE);
        }
    }

    EditText edit_login;
    EditText edit_password;

    LoginActivity This;
    Button button_login;
    TextView label_inputErrorsPassword;
    TextView label_inputErrorsLogin;
    TextView label_loginErrors;
    ProgressBar LoginPB;

    @Override
    protected void onViewModelInit(LoginViewModel loginViewModel)
    {
        super.onViewModelInit(loginViewModel);
    }
    public void button_login_onClick(View v)
    {
        if (Login.CurrentLogin.IsOkay())
        {
            label_loginErrors.setVisibility(View.INVISIBLE);
            LoginPB.setVisibility(View.VISIBLE);
            new Thread(() -> FirebaseDb.TryLogin(Login.CurrentLogin.getName(), Login.CurrentLogin.getPassword(), loggedIn ->
            {
                if (loggedIn)
                {
                    Storage.Sync(() ->
                    {
                        startActivity(new Intent(This, TodoOverviewActivity.class));
                    });
                }
                else
                {
                    label_loginErrors.setVisibility(View.VISIBLE);
                }
                LoginPB.setVisibility(View.GONE);
            })).start();
        }
        else
        {
            label_loginErrors.setVisibility(View.VISIBLE);
        }
    }

    // TODO im onDestroy die Db schließen oder besser im onStart?
    // siehe https://developer.android.com/guide/components/activitie
    // s/activity-lifecycle
    private void InitDataBase() { Storage.Init(this); }
}
