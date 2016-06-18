package com.daksh.lean4j.Login;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.daksh.lean4j.Exceptions.APIKeyException;
import com.daksh.lean4j.Exceptions.UrlCallbackException;
import com.daksh.lean4j.Login.ModelClasses.OAuthTokenModel;
import com.daksh.lean4j.Network.RetroFit;
import com.daksh.lean4j.R;
import com.daksh.lean4j.Storage.AccessToken;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeanAuthorize extends AppCompatActivity implements Callback<OAuthTokenModel> {

    /**
     * Tag for logging
     */
    private final static String TAG = LeanAuthorize.class.getSimpleName();

    /**
     * A webview that is used to show OAuth page to the user where LeanTesting credentials
     * can be verified by the server
     */
    private WebView webView;

    /**
     * A progressDialog that is displayed in interim period while Accesstokens are being retrieved
     */
    private ProgressDialog progressDialog;

    /**
     * A ClientKeys object which holds the ClientKey and ClientSecretKey for the SDK
     */
    private ClientKeys clientKeys;

    /**
     * An object of the LoginManager to retrieve static variables
     */
    private LoginManager loginManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lean_authorize);
        //Instantiating ClientKeys here should have Keys set in them since they are static
        clientKeys = new ClientKeys();
        //instantiate LoginManager
        loginManager = new LoginManager();

        //Test if consumer key was received
        if (TextUtils.isEmpty(clientKeys.getClientKey()))
            throw new APIKeyException();

        //Test if Secret Key was received
        if (TextUtils.isEmpty(clientKeys.getClientSecretKey()))
            throw new APIKeyException();

        //Test if URL Call back was received
        if (TextUtils.isEmpty(loginManager.getCallbackUrl()))
            throw new UrlCallbackException();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //test if LoginListener was registered
        if(loginManager.getLoginListener() != null)
            if(loginManager.getExceptionHandler() == null)
                Log.w(TAG, "Continuing execution without ExceptionHandler. " +
                        "No Exception call backs will be sent. It is recommended to set one.");

        //Prepare Authorization URL to open on WebView. The URL directs the user to the
        //Authorization grant page where the user may choose to login and authorize the application
        final String strAuthorizeUrl = Uri.parse(getString(R.string.leantesting_API_URL) + getString(R.string.OAuth_Authorize))
                .buildUpon()
                //Attach Client ID
                .appendQueryParameter(
                        getString(R.string.OAuth_Client_id),
                        clientKeys.getClientKey()
                )
                //Attach Redirect URL
                .appendQueryParameter(
                        getString(R.string.OAuth_redirect_uri),
                        loginManager.getCallbackUrl()
                )
                //Attach OAuthScopes
                .appendQueryParameter(
                        getString(R.string.OAuth_scope),
                        Arrays.toString(loginManager.getScope()).replace("[", "").replace("]", "").replaceAll(" ", "")
                )
                //Convert to String to form URL
                .toString();
        Log.i(TAG, "Authorization URL : " + strAuthorizeUrl);
        //Load URL
        getWebView().loadUrl(strAuthorizeUrl);
    }

    /**
     * WebView client attached to the WebView on the layout. It monitors URL changes to execute call backs
     * and or capture authorization strings
     */
    private WebViewClient webViewClient = new WebViewClient() {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String strUrl) {
            //Test if URL on page is not empty && if URL is equal to callback URL registered
            if(!TextUtils.isEmpty(strUrl) && strUrl.startsWith(loginManager.getCallbackUrl())) {
                //Parse URL
                Uri uri = Uri.parse(strUrl);
                //get URL Parameters | Temporary code is received here
                String strCode = uri.getEncodedQuery();
                //Test if Code is Empty
                if(!TextUtils.isEmpty(strCode)) {
                    //Split key=value to retrieve value
                    String strClientCode = strCode.split("=")[1];
                    //Load About Black page | Temporary might remove it in future | Still contemplating point
                    getWebView().loadUrl("about:blank");
                    //Display ProgressDialog while Temporary code is exchanged with server for AccessToken
                    progressDialog = ProgressDialog.show(LeanAuthorize.this, null, "Loading...");

                    //proceed with the LoginProcess
                    //The temporary code is exchanged with the LeanTesting server for AccessTokens
                    getAccessTokens(strClientCode);
                }
            }

            return super.shouldOverrideUrlLoading(view, strUrl);
        }
    };

    /**
     * A method to return a reference to the webview used for Authorization
     *
     * @return WebView
     */
    private WebView getWebView() {
        if (webView != null)
            return webView;
        else {
            webView = (WebView) findViewById(R.id.activity_login_webview);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setAppCacheEnabled(true);
            webView.setWebViewClient(webViewClient);
            return webView;
        }
    }

    /**
     * A method to initiate the process to exchange temporary code for the AccessTokens
     * @param strClientCode The temporary code received from leanTesting server post OAuthLogin on webview
     */
    private void getAccessTokens(String strClientCode) {
        //Get Interface
        RetroFit.APIInterface oAuthAccessApi = RetroFit.getServiceHandler(LeanAuthorize.this);

        //Pass params to pass off in Service
        Call<OAuthTokenModel> oAuthCall = oAuthAccessApi.getOAuthToken(
                clientKeys.getClientKey(),
                clientKeys.getClientSecretKey(),
                "authorization_code",
                strClientCode,
                loginManager.getCallbackUrl()
        );
        //Execute ServiceRequest
        oAuthCall.enqueue(LeanAuthorize.this);
    }

    @Override
    public void onResponse(Call<OAuthTokenModel> call, Response<OAuthTokenModel> response) {
        //Remove the progressDialog if shown
        if(progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();

        //Test if data was received from the server
        if(response != null && response.body() != null) {
            //Cast response to OAuthToken Model
            OAuthTokenModel oAuthTokenModel = response.body();

            //Set tokens and bearer types
            AccessToken.setToken(oAuthTokenModel.getAccessToken());
            AccessToken.setTokenType(oAuthTokenModel.getTokenType());
            AccessToken.setTTL(oAuthTokenModel.getExpiresIn());

            //Call LoginCallback listener if registered
            if (loginManager.getLoginListener() != null)
                loginManager.getLoginListener().onSuccess(AccessToken.getInstance());

            //Finish Activity now that LoginProcedure is finished
            finish();
        }
    }

    @Override
    public void onFailure(Call<OAuthTokenModel> call, Throwable t) {
        //If LoginFails | Hide progressDialog
        if(progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();

        //Use ExceptionHandler registered by user here
    }
}