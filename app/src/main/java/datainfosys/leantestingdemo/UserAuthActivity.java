package datainfosys.leantestingdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.daksh.lean4j.Login.ClientKeys;
import com.daksh.lean4j.Login.Constants.Scopes;
import com.daksh.lean4j.Login.Interfaces.LoginListener;
import com.daksh.lean4j.Login.LoginManager;
import com.daksh.lean4j.Storage.AccessToken;

/**
 * Created by Data on 6/9/2016.
 */
public class UserAuthActivity extends AppCompatActivity {
    private String TAG = UserAuthActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_auth_activity);
    }

    public void getToken(View view) {
        ClientKeys clientKeys = new ClientKeys(
                getString(R.string.leantesting_client_id),
                getString(R.string.leantesting_client_secret)
        );
        //initiate login procedure
        LoginManager loginManager = new LoginManager(this);
        loginManager.setScope(new Scopes[]{Scopes.ADMIN});
        loginManager.setUrlCallBack("http://hoodime.com");
        loginManager.setLoginListener(new LoginListener() {
            @Override
            public void onSuccess(AccessToken accessToken) {
                Log.i(TAG, "AccessToken : " + AccessToken.getAccessToken());
                Log.i(TAG, "AccessToken TTL : " + AccessToken.getTTL());
                Log.i(TAG, "AccessToken TokenType : " + AccessToken.getTokenType());
            }
        });
        loginManager.executeInActivity();
    }

}
