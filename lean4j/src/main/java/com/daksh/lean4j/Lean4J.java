package com.daksh.lean4j;

import android.content.Context;
import android.content.Intent;

import com.daksh.lean4j.Login.LeanAuthorize;

public class Lean4J {

    /**
     * The instance to this class. It is necessary to declare this here to keep access to this class singleton
     */
    private static Lean4J lean4J;

    /**
     * An object of the interface defined on this class. The interface is called
     * when the activity receives a response from the Login process
     */
//    private static LoginListener loginListener;

    /**
     * An object of the interface defined on this class. The interface is called
     * when the activity throws an exception caused by various reasons due which
     * the code cannot continue function.
     */
//    private static ExceptionHandler exceptionHandler;

    /**
     * The LeanTesting Call back URL
     */
    private String strCallbackUrl;

    /**
     * The Consumer Key received when a new app is registered with Tumblr
     * #Mandatory
     */
    private static String CLIENT_KEY;

    /**
     * The Consumer Secret Key received when a new app is registered with Tumblr
     * #Mandatory
     */
    private static String CLIENT_SECRET_KEY;

    private Lean4J() {
        //Empty private constructor to disallow creation of object
    }

    /**
     * A method to return a reference to this class. Since the variable
     * @return
     */
    public static Lean4J getInstance() {
        if(lean4J == null) {
            lean4J = new Lean4J();
            return lean4J;
        } else
            return lean4J;
    }

    /**
     * Receives a reference of the interface to be called when a result is retrieved
     * from the login process
     * @param listener
     */
//    public Lean4J setLoginListener(LoginListener listener) {
//        loginListener = listener;
//        return lean4J;
//    }

    /**
     * Optional | Recommended though to handle code in a better fashion
     * The method receives a reference of the interface to be executed when an exception is thrown
     * @param listener
     */
//    public Lean4J setExceptionHandler(ExceptionHandler listener) {
//        exceptionHandler = listener;
//        return lean4J;
//    }

    /**
     * A call back URL to monitor for login call back
     * Should be same as callback URL registered with Lean4J developer website.
     * @param strCallbackUrl
     * @return
     */
    public Lean4J setUrlCallBack(String strCallbackUrl) {
        this.strCallbackUrl = strCallbackUrl;
        return lean4J;
    }

    /**
     * A method to return the URL call back registered with LeanTesting on the developer dashboard
     * @return strUrl
     */
    String getUrlCallBack() {
        return strCallbackUrl;
    }

    /**
     * A method to provide Lean4J with the Consumer Key which will be used to access LeanTesting APIs.
     * Without it, the app will fail.
     * #MANDATORY
     * @param strClientKey The LeanTesting app consumer Key in String format
     * @return lean4J
     */
    public Lean4J setClientKey(String strClientKey) {
        CLIENT_KEY = strClientKey;
        return lean4J;
    }

    /**
     * A method to provide Lean4J with the Consumer Secret Key which will be used to access Tumblr APIs.
     * Without it, the app will fail.
     * #MANDATORY
     * @param strConsumerSecretKey The Tumblr app consumer Secret Key in String format
     * @return lean4J
     */
    public Lean4J setClientSecretKey(String strConsumerSecretKey) {
        CLIENT_SECRET_KEY = strConsumerSecretKey;
        return lean4J;
    }

    /**
     * A method to get the Consumer Key which will be used to access Tumblr APIs.
     * Without it, the app will fail.
     * #MANDATORY
     * @return CLIENT_KEY
     */
    String getClientKey() {
        return CLIENT_KEY;
    }

    /**
     * A method to get the Client Secret Key which will be used to access Lean4J APIs.
     * Without it, the app will fail.
     * #MANDATORY
     * @return CLIENT_SECRET_KEY
     */
    String getClientSecretKey() {
        return CLIENT_SECRET_KEY;
    }

    /**
     * Returns the reference of the interface to be called when a result is retrieved
     * from the Login Process
     * @return The Loginlistener
     */
//    LoginListener getLoginListener() {
//        return loginListener;
//    }

    /**
     * Optional | Recommended though to handle code in a better fashion
     * The method returns a reference of the interface to be executed when an exception is thrown
     * @return The ExceptionHandler
     */
//    ExceptionHandler getExceptionHandler() {
//        return exceptionHandler;
//    }

    /**
     * The method initiates the login procedure by calling the Tumblr APIs in a different dialog Fragment
     * which hosts a WebView.
     * @param context The context of the calling Activity / Application
     */
    public void initiateInActivity(Context context) {
        Intent intent = new Intent(context, LeanAuthorize.class);
        context.startActivity(intent);
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