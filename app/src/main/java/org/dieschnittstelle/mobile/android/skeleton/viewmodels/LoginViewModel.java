package org.dieschnittstelle.mobile.android.skeleton.viewmodels;
import androidx.lifecycle.MutableLiveData;

import org.dieschnittstelle.mobile.android.skeleton.models.Login;
public class LoginViewModel extends ViewModelBase
{
    private String Password;
    public void setPassword(String password)
    {
        Password = password;
        Login.CurrentLogin.setPassword(password);
        isChanged = true;
    }
    public String getPassword() { return Login.CurrentLogin.getPassword(); }

    private String Name;
    public void setName(String name)
    {
        Name = name;
        Login.CurrentLogin.setName(name);
        isChanged = true;
    }

    private boolean isChanged;

    public boolean getIsChanged(){return isChanged;}

    public String getName() { return Login.CurrentLogin.getName(); }
    public boolean getCanLogin()
    {
        return Login.ValidateLogin(getName()) == null && Login.ValidatePassword(getPassword()) == null;
    }

    // Binding gegen MutableLiveData funktioniert anscheinend nicht.
}
