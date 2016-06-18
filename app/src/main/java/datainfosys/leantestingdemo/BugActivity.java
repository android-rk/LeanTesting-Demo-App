package datainfosys.leantestingdemo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import datainfosys.leantestingdemo.Adapters.BugaAdapter;
import datainfosys.leantestingdemo.Util.AppManager;
import datainfosys.leantestingdemo.Util.BaseActivity;
import datainfosys.leantestingdemo.Util.BugsPojo;
import datainfosys.leantestingdemo.Util.ConstData;

/**
 * Created by Data on 6/9/2016.
 */
public class BugActivity extends BaseActivity {
    private ListView listBugs;
    private String url_, sectionUrl = null;
    private AppManager manager;
    private int projectID;
    private BugaAdapter adapter;
    private List<BugsPojo> listData = new ArrayList<>();
    private SwipeRefreshLayout refreshLayout;
    private Spinner numberSP;
    private ArrayList<String> pages = new ArrayList<>();
    private ArrayAdapter<String> numberSPAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bug_list);
        if (getIntent() != null) {
            projectID = getIntent().getExtras().getInt("id");
        }
        numberSPAdapter = new ArrayAdapter<String>(BugActivity.this, android.R.layout.simple_list_item_1, pages);
        setContentView(R.layout.bug_list);
        manager = new AppManager(BugActivity.this);
        url_ = ConstData.BASEURL + ConstData.PROJECTS + "/" + projectID + "/" + ConstData.BUGS + ConstData.ENDPOINT + manager.getAccesToken();
        Log.d("url", url_);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        getSupportActionBar().setTitle("Bugs");
        listBugs = (ListView) findViewById(R.id.listBugs);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                 refresh data here
                loadBugs(url_);
            }
        });
        numberSP = (Spinner) findViewById(R.id.pageSp);
        numberSP.setAdapter(numberSPAdapter);
        adapter = new BugaAdapter(BugActivity.this, R.layout.bugs_view, listData);
        listBugs.setAdapter(adapter);
        listBugs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(BugActivity.this, Bug2Activity.class);
                intent.putExtra("id", listData.get(position).getId());
                startActivity(intent);
            }
        });
        loadBugs(url_);
        sectionUrl = ConstData.BASEURL + ConstData.PROJECTS + "/" + projectID + "/" + ConstData.SECTIONS + ConstData.ENDPOINT + manager.getAccesToken();
        getProjectSections(sectionUrl, true);
        String versionUrl = ConstData.BASEURL + ConstData.PROJECTS + "/" + projectID + "/" + ConstData.VERSIONS + ConstData.ENDPOINT + manager.getAccesToken();
        getProjectVersions(versionUrl);

        numberSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int page = position + 1;
                url_ = ConstData.BASEURL + ConstData.PROJECTS + "/" + projectID + "/" + ConstData.BUGS + ConstData.ENDPOINT + manager.getAccesToken() + ConstData.PAGE + page;
                loadBugs(url_);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(BugActivity.this, CreateBugActivity.class);
        intent.putExtra("id", projectID);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

    public void loadBugs(String url) {
        refreshLayout.setRefreshing(true);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Projects", response.toString());
                        listData.clear();
                        try {
                            if (response != null) {
                                JSONArray bugsArray = response.getJSONArray("bugs");
                                for (int i = 0; i < bugsArray.length(); i++) {
                                    JSONObject bug = bugsArray.getJSONObject(i);
                                    BugsPojo poja = new BugsPojo();
                                    poja.setTitle(bug.getString("title"));
                                    poja.setId(bug.getInt("id"));
                                    poja.setDescription(bug.getString("description"));
                                    poja.setStatusId(bug.getInt("status_id"));
                                    poja.setCreatedAt(bug.getString("created_at"));
                                    listData.add(poja);
                                }
                                JSONObject meta = response.getJSONObject("meta");
                                JSONObject pagination = meta.getJSONObject("pagination");
                                int totalPages = pagination.getInt("total_pages");
                                pages.clear();
                                for (int k = 1; k <= totalPages; k++) {
                                    pages.add(k + "");
                                }
                                numberSPAdapter.notifyDataSetChanged();
                                numberSP.setSelection(pagination.getInt("current_page") - 1);
                                refreshLayout.setRefreshing(false);
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
                refreshLayout.setRefreshing(false);
            }
        });
        LeanTesting.getInstance().addToRequestQueue(request);
    }

    public void getProjectSections(String url, final boolean shudClear) {
        refreshLayout.setRefreshing(true);
        JsonObjectRequest request1 = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Projects", response.toString());
                        try {
                            if (response != null) {
                                JSONArray sectionsArray = response.getJSONArray("sections");
                                if (shudClear) {
                                    ConstData.BUG_SECTIONS.clear();
                                    ConstData.BUG_SECTIONS_STRING.clear();
                                }
                                for (int i = 0; i < sectionsArray.length(); i++) {
                                    JSONObject section = sectionsArray.getJSONObject(i);
//                                    SectionData data = new SectionData();
//                                    data.setId(section.getInt("id"));
//                                    data.setName(section.getString("name"));
//                                    ConstData.BUG_SECTIONS.add(data);
                                    ConstData.BUG_SECTIONS.add(section.getInt("id"));
                                    ConstData.BUG_SECTIONS_STRING.add(section.getString("name"));
                                }
                                JSONObject meta = response.getJSONObject("meta");
                                JSONObject pagination = meta.getJSONObject("pagination");
                                if (pagination.has("links")) {
                                    JSONObject links = pagination.getJSONObject("links");
                                    if (links.has("next")) {
                                        sectionUrl = links.getString("next");
                                        getProjectSections(sectionUrl, false);
                                    }
                                    Log.d("sections", ConstData.BUG_SECTIONS.size() + "");
                                }
                            }
                            refreshLayout.setRefreshing(false);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Projects", "Error: " + error.getMessage());
                refreshLayout.setRefreshing(false);
                // hide the progress dialog
            }
        });
        LeanTesting.getInstance().addToRequestQueue(request1);
    }

    public void getProjectVersions(String url) {
        refreshLayout.setRefreshing(true);
        JsonObjectRequest request1 = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Projects", response.toString());
                        try {
                            if (response != null) {
                                ConstData.VERSIONS_DATA.clear();
                                ConstData.PROJECT_VERSION.clear();
                                JSONArray sectionsArray = response.getJSONArray("versions");
                                for (int i = 0; i < sectionsArray.length(); i++) {
                                    JSONObject version = sectionsArray.getJSONObject(i);
                                    ConstData.VERSIONS_DATA.add(version.getInt("id"));
                                    ConstData.PROJECT_VERSION.add(version.getString("number"));
                                }
                            }
                            refreshLayout.setRefreshing(false);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Projects", "Error: " + error.getMessage());
                refreshLayout.setRefreshing(false);
                // hide the progress dialog
            }
        });
        LeanTesting.getInstance().addToRequestQueue(request1);
    }
}
