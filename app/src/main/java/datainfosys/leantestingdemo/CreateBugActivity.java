package datainfosys.leantestingdemo;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import datainfosys.leantestingdemo.Util.AppManager;
import datainfosys.leantestingdemo.Util.BaseActivity;
import datainfosys.leantestingdemo.Util.ConstData;

/**
 * Created by Data on 6/10/2016.
 */
public class CreateBugActivity extends BaseActivity {
    private int projectID;
    private Spinner servitySP, typeSP, reproduceSP, prioritySP, versionSP, sectionSP, statusSP;
    private EditText title, description, expected_result;
    private AppManager manager;
    private Button createBug;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_bug_view);
        if (getIntent() != null) {
            projectID = getIntent().getExtras().getInt("id");
        }
        manager = new AppManager(CreateBugActivity.this);
        getUI();

    }

    private void getUI() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        getSupportActionBar().setTitle("Create Bug");
        servitySP = (Spinner) findViewById(R.id.servity_spinner);
        servitySP.setAdapter(new ArrayAdapter<String>(CreateBugActivity.this, android.R.layout.simple_list_item_1, ConstData.BUG_SERVITY));
        statusSP = (Spinner) findViewById(R.id.status_spinner);
        statusSP.setAdapter(new ArrayAdapter<String>(CreateBugActivity.this, android.R.layout.simple_list_item_1, ConstData.STATUS));
        typeSP = (Spinner) findViewById(R.id.type_spinner);
        typeSP.setAdapter(new ArrayAdapter<String>(CreateBugActivity.this, android.R.layout.simple_list_item_1, ConstData.BUG_TYPE));
        reproduceSP = (Spinner) findViewById(R.id.reproduciblility_spinner);
        reproduceSP.setAdapter(new ArrayAdapter<String>(CreateBugActivity.this, android.R.layout.simple_list_item_1, ConstData.BUG_REPRODUCIBILATY));
        prioritySP = (Spinner) findViewById(R.id.priority_spinner);
        prioritySP.setAdapter(new ArrayAdapter<String>(CreateBugActivity.this, android.R.layout.simple_list_item_1, ConstData.BUG_PRIORITY));
        versionSP = (Spinner) findViewById(R.id.versions_spinner);
        versionSP.setAdapter(new ArrayAdapter<String>(CreateBugActivity.this, android.R.layout.simple_list_item_1, ConstData.PROJECT_VERSION));
        sectionSP = (Spinner) findViewById(R.id.section_spinner);
        sectionSP.setAdapter(new ArrayAdapter<String>(CreateBugActivity.this, android.R.layout.simple_list_item_1, ConstData.BUG_SECTIONS_STRING));
        title = (EditText) findViewById(R.id.title);
        description = (EditText) findViewById(R.id.description);
        expected_result = (EditText) findViewById(R.id.expected_result);
        createBug= (Button) findViewById(R.id.update);
        createBug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createBug();
            }
        });
    }

    public void createBug() {
        JSONObject bug = new JSONObject();
        try {
            bug.put("title", title.getText().toString().trim());
            bug.put("severity_id", servitySP.getSelectedItemPosition() + 1);
            bug.put("status_id", statusSP.getSelectedItemPosition() + 1);
            bug.put("type_id", typeSP.getSelectedItemPosition() + 1);
            bug.put("reproducibility_id", reproduceSP.getSelectedItemPosition() + 1);
            bug.put("priority_id", prioritySP.getSelectedItemPosition() + 1);
            bug.put("description", description.getText().toString().trim());
            bug.put("project_version", 1);
            bug.put("expected_results", expected_result.getText().toString().trim());
            if (ConstData.BUG_SECTIONS.size() > 0) {
                bug.put("project_section_id", ConstData.BUG_SECTIONS.get(sectionSP.getSelectedItemPosition()));
            }
            bug.put("project_version_id",ConstData.VERSIONS_DATA.get(versionSP.getSelectedItemPosition()));
            Log.d("bug", bug.toString());
            createBugNow(bug);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createBugNow(JSONObject object) {
        String urll = ConstData.BASEURL + ConstData.PROJECTS + "/" + projectID + "/" + ConstData.BUGS;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                urll, object,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Projects", response.toString());
                        try {
                            if (response != null) {
                                hideProgress();
                                Toast.makeText(CreateBugActivity.this, "Bug created !!!", Toast.LENGTH_SHORT).show();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        finish();
                                    }
                                }, 500);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Projects", "Error: " + error.getMessage());
                // hide the progress dialog
                hideProgress();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", manager.getAccesToken());
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        showProgress();
        LeanTesting.getInstance().addToRequestQueue(request);
    }
}
