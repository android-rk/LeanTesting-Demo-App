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
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import datainfosys.leantestingdemo.Util.AppManager;
import datainfosys.leantestingdemo.Util.BaseActivity;
import datainfosys.leantestingdemo.Util.BugViewPojo;
import datainfosys.leantestingdemo.Util.ConstData;

/**
 * Created by Data on 6/10/2016.
 */
public class Bug2Activity extends BaseActivity {
    private int bugId;
    private Spinner servitySP, typeSP, reproduceSP, prioritySP, versionSP, sectionSP, statusSP;
    private EditText title, description, expected_result;
    private AppManager manager;
    private BugViewPojo viewPojo = new BugViewPojo();
    private String url;
    private Button deleteBug,updateBug;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_bug_view);
        if (getIntent() != null) {
            bugId = getIntent().getExtras().getInt("id");
        }
        getUI();
        manager = new AppManager(Bug2Activity.this);
        url = ConstData.BASEURL + ConstData.BUGS + "/" + bugId + ConstData.ENDPOINT + manager.getAccesToken();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Projects", response.toString());
                        try {
                            if (response != null) {
                                viewPojo.setTitle(response.getString("title"));
                                viewPojo.setCreatedAt(response.getString("created_at"));
                                viewPojo.setId(response.getInt("id"));
                                viewPojo.setDescription(response.getString("description"));
                                viewPojo.setSectionId(response.getInt("project_section_id"));
                                viewPojo.setTypeId(response.getInt("type_id"));
                                viewPojo.setStatusId(response.getInt("status_id"));
                                viewPojo.setServityId(response.getInt("severity_id"));
                                viewPojo.setProject_version_id(response.getInt("project_version_id"));
                                viewPojo.setReproduceId(response.getInt("reproducibility_id"));
                                viewPojo.setExpectedResults(response.getString("expected_results"));
                                setUI();
                                hideProgress();
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
        });
        showProgress();
        LeanTesting.getInstance().addToRequestQueue(request);
    }

    private void getUI() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        getSupportActionBar().setTitle("Bug Detail");
        servitySP = (Spinner) findViewById(R.id.servity_spinner);
        servitySP.setAdapter(new ArrayAdapter<String>(Bug2Activity.this, android.R.layout.simple_list_item_1, ConstData.BUG_SERVITY));
        statusSP = (Spinner) findViewById(R.id.status_spinner);
        statusSP.setAdapter(new ArrayAdapter<String>(Bug2Activity.this, android.R.layout.simple_list_item_1, ConstData.STATUS));
        typeSP = (Spinner) findViewById(R.id.type_spinner);
        typeSP.setAdapter(new ArrayAdapter<String>(Bug2Activity.this, android.R.layout.simple_list_item_1, ConstData.BUG_TYPE));
        reproduceSP = (Spinner) findViewById(R.id.reproduciblility_spinner);
        reproduceSP.setAdapter(new ArrayAdapter<String>(Bug2Activity.this, android.R.layout.simple_list_item_1, ConstData.BUG_REPRODUCIBILATY));
        prioritySP = (Spinner) findViewById(R.id.priority_spinner);
        prioritySP.setAdapter(new ArrayAdapter<String>(Bug2Activity.this, android.R.layout.simple_list_item_1, ConstData.BUG_PRIORITY));
        versionSP = (Spinner) findViewById(R.id.versions_spinner);
        versionSP.setAdapter(new ArrayAdapter<String>(Bug2Activity.this, android.R.layout.simple_list_item_1, ConstData.PROJECT_VERSION));
        sectionSP = (Spinner) findViewById(R.id.section_spinner);
        sectionSP.setAdapter(new ArrayAdapter<String>(Bug2Activity.this, android.R.layout.simple_list_item_1, ConstData.BUG_SECTIONS_STRING));
        title = (EditText) findViewById(R.id.title);
        deleteBug= (Button) findViewById(R.id.delete);
        updateBug= (Button) findViewById(R.id.update);
        updateBug.setText("Update");
        updateBug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateBug();
            }
        });
        deleteBug.setVisibility(View.VISIBLE);
        deleteBug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteBug();
            }
        });
        description = (EditText) findViewById(R.id.description);
        expected_result = (EditText) findViewById(R.id.expected_result);
    }

    public void setUI() {
        title.setText(viewPojo.getTitle());
        title.setEnabled(false);
        servitySP.setSelection(viewPojo.getServityId() - 1, true);
        servitySP.setEnabled(false);
        statusSP.setSelection(viewPojo.getStatusId() - 1, true);
        typeSP.setSelection(viewPojo.getTypeId() - 1, true);
        typeSP.setEnabled(false);
        reproduceSP.setSelection(viewPojo.getReproduceId() - 1, true);
        reproduceSP.setEnabled(false);
        sectionSP.setSelection(ConstData.BUG_SECTIONS.indexOf(viewPojo.getSectionId()), true);
        sectionSP.setEnabled(false);
        versionSP.setSelection(ConstData.VERSIONS_DATA.indexOf(viewPojo.getProject_version_id()), true);
        versionSP.setEnabled(false);
        description.setText(viewPojo.getDescription());
        description.setEnabled(false);
        expected_result.setText(viewPojo.getExpectedResults());
        expected_result.setEnabled(false);
    }
    private void deleteBug() {
        String deleteUrl = ConstData.BASEURL + ConstData.BUGS + "/" + bugId + ConstData.ENDPOINT + manager.getAccesToken();
        final StringRequest request = new StringRequest(Request.Method.DELETE,
                deleteUrl,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("Projects", response.toString());
                        try {
                            if (response != null) {
                                hideProgress();
                                Toast.makeText(Bug2Activity.this, "Bug deleted !!!", Toast.LENGTH_SHORT).show();
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
                NetworkResponse networkResponse=error.networkResponse;
                if(networkResponse.statusCode==204)
                {
                    Toast.makeText(Bug2Activity.this, "Bug deleted !!!", Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 500);
                }
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
    public void updateBug() {
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
            updateBugNow(bug);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateBugNow(JSONObject object) {
        String urll = ConstData.BASEURL + ConstData.BUGS + "/" + bugId;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT,
                urll, object,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Projects", response.toString());
                        try {
                            if (response != null) {
                                hideProgress();
                                Toast.makeText(Bug2Activity.this, "Bug Updated!!!", Toast.LENGTH_SHORT).show();
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
