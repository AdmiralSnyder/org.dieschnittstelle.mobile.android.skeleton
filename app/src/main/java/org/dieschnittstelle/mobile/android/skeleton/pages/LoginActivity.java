package org.dieschnittstelle.mobile.android.skeleton.pages;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.dieschnittstelle.mobile.android.skeleton.R;
import org.dieschnittstelle.mobile.android.skeleton.data.Db;
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

        Login.CurrentLogin.setName("DemoUser");
        Login.CurrentLogin.setPassword("123456");


        edit_login = findViewById(R.id.edit_login);
        edit_password = findViewById(R.id.edit_password);

        edit_login.setText(Login.CurrentLogin.getName());
        edit_password.setText(Login.CurrentLogin.getPassword());

        button_login = findViewById(R.id.button_login);
        label_inputErrors = findViewById(R.id.label_inputErrors);

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
    TextView label_inputErrors;

    private void button_login_onClick(View v)
    {
        Login.CurrentLogin.setName(edit_login.getText().toString());
        Login.CurrentLogin.setPassword(edit_password.getText().toString());

        if (Login.CurrentLogin.IsOkay())
        {
            FirebaseDb.TryLogin(Login.CurrentLogin.getName(), Login.CurrentLogin.getPassword(), loggedIn ->
            {
                if (loggedIn)
                {
                    FirebaseDb.CheckTodos(hasTodos ->
                    {
                        if (hasTodos)
                        {
                            Db.Instance.ClearDbObjsAsync();
                            FirebaseDb.GetTodos(todos ->
                            {
                                Db.Instance.SetDbObjsAsync(todos);
                                Intent detailViewIntent = new Intent(This, TodoOverviewActivity.class);
                                startActivity(detailViewIntent);
                            });
                        }
                        else
                        {
                            Intent detailViewIntent = new Intent(This, TodoOverviewActivity.class);
                            startActivity(detailViewIntent);
                        }
                    });
                }
                else
                {
                    label_inputErrors.setVisibility(View.VISIBLE);
                }
            });
        }
        else
        {
            label_inputErrors.setVisibility(View.VISIBLE);
        }
    }

    // TODO im onDestroy die Db schließen oder besser im onStart?
    // siehe https://developer.android.com/guide/components/activitie
    // s/activity-lifecycle
    private void InitDataBase() { Storage.Init(this); }
}
