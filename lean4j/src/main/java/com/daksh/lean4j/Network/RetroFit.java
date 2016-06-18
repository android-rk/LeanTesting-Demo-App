package com.daksh.lean4j.Network;

import android.content.Context;

import com.daksh.lean4j.Login.ModelClasses.OAuthTokenModel;
import com.daksh.lean4j.R;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class RetroFit {

    /**
     * API interface in singleton pattern
     */
    private static APIInterface apiInterface = null;

    /**
     * A method which builds the Retrofit library and OkHttp Client.
     * Very resource heavy | hence the singleton pattern
     * @param context Context of calling activity
     * @return Returns API Interface
     */
    public static APIInterface getServiceHandler(Context context) {
        //Test if API interface already exists
        if(apiInterface == null) {
            // Initialize the logging interceptor to log all service requests
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            //Instantiate OkHttp library
            OkHttpClient.Builder httpClient = new OkHttpClient
                    .Builder()
                    .addInterceptor(loggingInterceptor);

            //Instantiate RetroFit Library
            Retrofit.Builder builder =
                    new Retrofit.Builder()
                            //Set up Base URL
                            .baseUrl(context.getString(R.string.leantesting_API_URL))
                            //Set up OKHttP client as HTTP layer
                            .client(httpClient.build())
                            //Use GSON factory to parse server response
                            .addConverterFactory(GsonConverterFactory.create());

            //Build Retrofit
            apiInterface = builder.build().create(APIInterface.class);
            return apiInterface;
        } else
            return apiInterface;
    }

    public interface APIInterface {

        //An API Call used to retrieve access token from LeanTesting post retrieval of temporary code.
        @FormUrlEncoded @POST("/login/oauth/access_token")
        Call<OAuthTokenModel> getOAuthToken(
                @Field("client_id") String strClientId,
                @Field("client_secret") String strClientSecret,
                @Field("grant_type") String strGrantType,
                @Field("code") String strCode,
                @Field("redirect_uri") String strRedirectUrl
        );
    }
}