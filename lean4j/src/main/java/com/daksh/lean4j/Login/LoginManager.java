package com.daksh.lean4j.Login;

import android.content.Context;
import android.content.Intent;

import com.daksh.lean4j.Login.Constants.Scopes;
import com.daksh.lean4j.Login.Interfaces.ExceptionHandler;
import com.daksh.lean4j.Login.Interfaces.LoginListener;


public class LoginManager {

    /**
     * Tag for logging
     */
    private static final String TAG = LoginManager.class.getSimpleName();

    /**
     * Context received from the calling activity
     */
    private Context context;

    /**
     * The interface is called when the activity receives a response from the Login process
     * #Mandatory to pass through to the SDK
     */
    private static LoginListener loginListener;

    /**
     * The interface is called when the login module throws an exception caused by various reasons due which
     * the code cannot continue function.
     * #Optional | Recommended
     */
    private static ExceptionHandler exceptionHandler;

    /**
     * The Callback URL defined in LeanTesting API configuration when a new app is registered.
     * The call back URL in Lean4J and LeanTesting.com should be same for the SDK
     * to identify successful completion of Step 1
     * #Mandatory
     */
    private static String strCallbackUrl;

    /**
     * An array of Scopes which are used to define access level of the account granted to the
     * application.
     * #Optional | Recommended | Defaults to 'READ' only if not provided
     */
    private static Scopes[] scope;

    /**
     * package level constructor
     */
    LoginManager() {
        //empty constructor
    }

    /**
     * Public constructor for user to pass Activity context
     * @param context
     */
    public LoginManager(Context context) {
        this.context = context;
    }

    /**
     * #Mandatory
     * Receives the interface to be called when login process ends.
     * @param listener A callback interface
     */
    public void setLoginListener(LoginListener listener) {
        loginListener = listener;
    }

    /**
     * Optional | Recommended though to handle code in a better fashion
     * The method receives a reference of the interface to be executed when an exception is thrown
     * @param listener
     */
    public void setExceptionHandler(ExceptionHandler listener) {
        exceptionHandler = listener;
    }

    /**
     * #Mandatory
     * The Callback URL defined in LeanTesting API configuration when a new app is registered.
     * The call back URL in Lean4J and LeanTesting.com should be same for the SDK
     * to identify successful completion of Step 1
     */
    public void setUrlCallBack(String strCallbackUrl) {
        this.strCallbackUrl = strCallbackUrl;
    }

    /**
     * PackageLevel Access only to hinder use by user
     * A method to return the callback URL set by the user
     * @return A string which has the call back URL
     */
    String getCallbackUrl() {
        return strCallbackUrl;
    }

    /**
     * PackageLevel Access only to hinder use by user
     * Returns the reference of the interface to be called when a result is retrieved
     * from the Login Process
     * @return The Loginlistener
     */
    LoginListener getLoginListener() {
        return loginListener;
    }

    /**
     * The method returns a reference of the interface to be executed when an exception is thrown
     * @return The ExceptionHandler
     */
    ExceptionHandler getExceptionHandler() {
        return exceptionHandler;
    }

    /**
     * The method executes the Login procedure by calling activity/ies.
     * Call after setting up all mandatory / optional parameters
     */
    public void executeInActivity() {
        Intent intent = new Intent(context, LeanAuthorize.class);
        context.startActivity(intent);
    }

    /**
     * An array of Scopes which are used to define access level of the account granted to the
     * application.
     * #Optional | Recommended | Defaults to 'READ' only if not provided
     * @param scopes An array of Scopes
     */
    public void setScope(Scopes[] scopes) {
        scope = scopes;
    }

    /**
     * A method to return the user set scope for the application
     * @return Scope
     */
    Scopes[] getScope() {
        return scope;
    }

    /**
     * The method initiates the login procedure by calling the Tumblr APIs in a different dialog Fragment
     * which hosts a WebView.
     * @param fragmentManager The support fragment manager from the calling activity / application
     */
//    public void initiateInDialog(FragmentManager fragmentManager) {
//        Instantiate Dialog Fragment
//        LoglrFragment loglrFragment = new LoglrFragment();
//        Show the dialogFragment
//        loglrFragment.show(fragmentManager, LoglrFragment.class.getSimpleName());
//    }
}
