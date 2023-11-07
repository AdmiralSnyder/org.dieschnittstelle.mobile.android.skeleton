package org.dieschnittstelle.mobile.android.skeleton.models;
public class Login
{
    public static final int PASSWORDLENGTH = 6;

    public static final Login CurrentLogin = new Login();
    private String Name;
    public String getName() { return Name; }
    public void setName(String email) { Name = email; }

    private String Password;
    public String getPassword() { return Password; }
    public void setPassword(String password) { Password = password; }

    public boolean IsOkay()
    {
        boolean okay = Name != null && !Name.isEmpty()
            && Password != null
            && Password.length() == PASSWORDLENGTH;

        if (okay)
        {
            for (int i = 0; i < Password.length(); i++)
            {
                var chr = Password.charAt(i);
                if (chr < '0' || chr > '9')
                {
                    return false;
                }
            }
        }
        return okay;
    }
}
