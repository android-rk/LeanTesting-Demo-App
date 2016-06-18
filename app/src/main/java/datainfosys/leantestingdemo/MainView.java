package datainfosys.leantestingdemo;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import datainfosys.leantestingdemo.Adapters.ProjectAdapter;
import datainfosys.leantestingdemo.Util.AppManager;
import datainfosys.leantestingdemo.Util.BaseActivity;
import datainfosys.leantestingdemo.Util.ConstData;
import datainfosys.leantestingdemo.Util.ProjectsPoja;

/**
 * Created by Data on 6/9/2016.
 */
public class MainView extends BaseActivity {
    private ListView listProjects;
    private ProjectAdapter adapter;
    private List<ProjectsPoja> listData = new ArrayList<>();
    private String url;
    private AppManager manager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_list);
        manager = new AppManager(MainView.this);
        url = ConstData.BASEURL + ConstData.PROJECTS + ConstData.ENDPOINT + manager.getAccesToken();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        getSupportActionBar().setTitle("Projects");
        listProjects = (ListView) findViewById(R.id.listProjects);
        adapter = new ProjectAdapter(MainView.this, R.layout.projects_view, listData);
        listProjects.setAdapter(adapter);
        listProjects.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainView.this, BugActivity.class);
                intent.putExtra("id", listData.get(position).getId());
                startActivity(intent);
            }
        });
        getProjects(false);
    }

    public void getProjects(final boolean clear) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Projects", response.toString());
                        try {
                            if (response != null) {
                                if (clear) {
                                    listData.clear();
                                }
                                JSONArray projectArray = response.getJSONArray("projects");
                                for (int i = 0; i < projectArray.length(); i++) {
                                    JSONObject project = projectArray.getJSONObject(i);
                                    ProjectsPoja poja = new ProjectsPoja();
                                    poja.setId(project.getInt("id"));
                                    poja.setName(project.getString("name"));
                                    poja.setOwnerId(project.getInt("owner_id"));
                                    poja.setCreatedAt(project.getString("created_at"));
                                    listData.add(poja);
                                }
                                hideProgress();
                                adapter.notifyDataSetChanged();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        addProject();
        return super.onOptionsItemSelected(item);
    }

    public void addProject() {
        final AppCompatDialog dialog = new AppCompatDialog(MainView.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.add_project);
        dialog.getWindow().setLayout(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        final EditText edit = (EditText) dialog.findViewById(R.id.name);
        Button add = (Button) dialog.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    showProgress();
                    String urll = ConstData.BASEURL + ConstData.PROJECTS;
                    JSONObject object = new JSONObject();
                    object.put("name", edit.getText().toString());
                    Log.d("object", object.toString());
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                            urll, object,
                            new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.d("Projects", response.toString());
                                    try {
                                        if (response != null) {
                                            hideProgress();
                                            getProjects(true);
                                            dialog.dismiss();
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        Button cancel = (Button) dialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

}
