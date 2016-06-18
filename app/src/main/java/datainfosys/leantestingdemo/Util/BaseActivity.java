package datainfosys.leantestingdemo.Util;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Data on 6/9/2016.
 */
public class BaseActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog=new ProgressDialog(BaseActivity.this);
    }
    public void showProgress()
    {
        progressDialog.setMessage("loading ...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
    public void hideProgress()
    {
        if(progressDialog!=null)
        {
            progressDialog.dismiss();
        }
    }
}
