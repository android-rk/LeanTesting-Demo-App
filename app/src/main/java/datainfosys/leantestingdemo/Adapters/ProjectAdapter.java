package datainfosys.leantestingdemo.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import datainfosys.leantestingdemo.MainView;
import datainfosys.leantestingdemo.R;
import datainfosys.leantestingdemo.Util.ProjectsPoja;

/**
 * Created by Data on 6/9/2016.
 */
public class ProjectAdapter extends ArrayAdapter<ProjectsPoja> {
    private int resource;
    private Context context;
    private List<ProjectsPoja> listData;

    public ProjectAdapter(Context context, int resource, List<ProjectsPoja> listData) {
        super(context, resource, listData);
        this.context = context;
        this.resource = resource;
        this.listData = listData;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            v = LayoutInflater.from(context).inflate(resource, null);
        }
        TextView projectName = (TextView) v.findViewById(R.id.projectName);
        TextView createdAt = (TextView) v.findViewById(R.id.createdAt);
        ProjectsPoja poja = listData.get(position);
        projectName.setText(poja.getName());
        createdAt.setText(poja.getCreatedAt());
        return v;
    }
}
