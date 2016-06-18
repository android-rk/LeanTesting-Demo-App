package datainfosys.leantestingdemo.Util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Data on 6/9/2016.
 */
public class AppManager {
    private SharedPreferences.Editor editor;
    private SharedPreferences preferences;
    public AppManager(Context context)
    {
        preferences=context.getSharedPreferences("leanTestingPrefrence",0);
    }
    public boolean isLoggedIn()
    {
        return preferences.getBoolean("login",false);
    }
    public void setLoggedIn()
    {
        editor=preferences.edit();
        editor.putBoolean("login",true);
        editor.commit();
    }
    public void setAccessToken(String accessToken)
    {
        editor=preferences.edit();
        editor.putString("accessToken",accessToken);
        editor.commit();
    }
    public String getAccesToken()
    {
        return  preferences.getString("accessToken",null);
    }
}
