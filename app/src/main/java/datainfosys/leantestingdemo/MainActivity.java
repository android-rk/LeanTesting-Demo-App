package datainfosys.leantestingdemo;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.daksh.lean4j.Login.ClientKeys;
import com.daksh.lean4j.Login.Constants.Scopes;
import com.daksh.lean4j.Login.Interfaces.LoginListener;
import com.daksh.lean4j.Login.LoginManager;
import com.daksh.lean4j.Storage.AccessToken;

import datainfosys.leantestingdemo.Util.AppManager;

public class MainActivity extends AppCompatActivity {
    private ImageView logo;
    private AppManager manager;
    private String TAG = MainActivity.class.getSimpleName();
    private Button loginUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginUser = (Button) findViewById(R.id.loginUser);
        loginUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
        manager = new AppManager(this);
        logo = (ImageView) findViewById(R.id.image);
        ObjectAnimator animator = ObjectAnimator.ofFloat(logo, "rotationY", 0, 360);
        animator.setDuration(3000);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (manager.isLoggedIn()) {
                            startActivity(new Intent(MainActivity.this, MainView.class));
                            finish();
                        } else {
                            loginUser.setVisibility(View.VISIBLE);
                        }
                    }
                }, 300);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.setStartDelay(400);
        animator.start();
    }

    public void loginUser() {
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
                manager.setAccessToken("" + AccessToken.getAccessToken());
                manager.setLoggedIn();
                startActivity(new Intent(MainActivity.this, MainView.class));
                finish();
            }
        });
        loginManager.executeInActivity();
    }
}
