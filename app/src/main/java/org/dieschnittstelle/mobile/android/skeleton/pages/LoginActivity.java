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

import org.dieschnittstelle.mobile.android.skeleton.R;
import org.dieschnittstelle.mobile.android.skeleton.data.FirebaseDb;
import org.dieschnittstelle.mobile.android.skeleton.data.Storage;
import org.dieschnittstelle.mobile.android.skeleton.models.Login;

public class LoginActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        This = this;

        InitDataBase();

        label_inputErrorsPassword = findViewById(R.id.label_inputErrorsPassword);
        label_inputErrorsLogin = findViewById(R.id.label_inputErrorsLogin);
        label_loginErrors = findViewById(R.id.label_loginErrors);

        // TODO rausnehmen für die Abgabe
        Login.CurrentLogin.setName("DemoUser@MAD.edu");
        Login.CurrentLogin.setPassword("123456");

        edit_login = findViewById(R.id.edit_login);
        edit_login.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void afterTextChanged(Editable s) {
                label_loginErrors.setVisibility(View.INVISIBLE);
                String error = Login.ValidateLogin(s.toString());
                if (error != null)
                {
                    label_inputErrorsLogin.setText(error);
                    label_inputErrorsLogin.setVisibility(View.VISIBLE);
                }
                else
                {
                    label_inputErrorsLogin.setText("ERROR LOGGING IN");
                    label_inputErrorsLogin.setVisibility(View.INVISIBLE);
                }
            }
        });

        edit_password = findViewById(R.id.edit_password);

        edit_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void afterTextChanged(Editable s) {
                label_loginErrors.setVisibility(View.INVISIBLE);
                String error = Login.ValidatePassword(s.toString());
                if (error != null)
                {
                    label_inputErrorsPassword.setText(error);
                    label_inputErrorsPassword.setVisibility(View.VISIBLE);
                }
                else
                {
                    label_inputErrorsPassword.setText("ERROR LOGGING IN");
                    label_inputErrorsPassword.setVisibility(View.INVISIBLE);
                }
            }
        });


        edit_login.setText(Login.CurrentLogin.getName());
        edit_password.setText(Login.CurrentLogin.getPassword());

        button_login = findViewById(R.id.button_login);

        LoginPB = findViewById(R.id.progressBar);
        FirebaseDb.CheckConnectionAsync(connected ->
        {
            if (connected)
            {
                button_login.setEnabled(true);
                button_login.setOnClickListener(this::button_login_onClick);
            }
            else
            {
                startActivity(new Intent(This, TodoOverviewActivity.class));
            }
        });
    }
    EditText edit_login;
    EditText edit_password;

    LoginActivity This;
    Button button_login;
    TextView label_inputErrorsPassword;
    TextView label_inputErrorsLogin;
    TextView label_loginErrors;
    ProgressBar LoginPB;

    private void button_login_onClick(View v)
    {
        Login.CurrentLogin.setName(edit_login.getText().toString());
        Login.CurrentLogin.setPassword(edit_password.getText().toString());

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
