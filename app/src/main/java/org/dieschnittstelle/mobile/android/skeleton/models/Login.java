package org.dieschnittstelle.mobile.android.skeleton.models;
import java.util.regex.Pattern;
public class Login
{
    public static final int PASSWORDLENGTH = 6;

    public static final Login CurrentLogin = new Login();

    private String _Name;
    @com.google.firebase.database.PropertyName("Name")
    public String getName() { return _Name; }
    @com.google.firebase.database.PropertyName("Name")
    public void setName(String email) { _Name = email; }


    private String _Password;
    @com.google.firebase.database.PropertyName("Password")
    public String getPassword() { return _Password; }
    @com.google.firebase.database.PropertyName("Password")
    public void setPassword(String password) { _Password = password; }

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static final Pattern VALID_PASSWORD_REGEX = Pattern.compile("^[0-9][0-9][0-9][0-9][0-9][0-9]$");

    public static String ValidateLogin(String login)
    {
        if (login == null || login.isEmpty()|| login.isBlank())
        {
            return "Login must not be empty.";
        }
        if (!VALID_EMAIL_ADDRESS_REGEX.matcher(login).matches())
        {
            return "Login is not valid email address.";
        }
        return null;
    }

    public static String ValidatePassword(String password)
    {
        if (password == null || password.isEmpty()|| password.isBlank())
        {
            return "Password may not be empty.";
        }
        if (password.length() != PASSWORDLENGTH)
        {
            return "Password needs to have the length " + PASSWORDLENGTH + ".";
        }
        if (!VALID_PASSWORD_REGEX.matcher(password).matches())
        {
            return "Password needs to be numeric";
        }
        return null;
    }

    public boolean IsOkay()
    {
        return ValidatePassword(_Password) == null && ValidateLogin(_Name) == null;
    }
}
