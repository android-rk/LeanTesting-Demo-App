package datainfosys.leantestingdemo.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import datainfosys.leantestingdemo.R;
import datainfosys.leantestingdemo.Util.BugsPojo;
import datainfosys.leantestingdemo.Util.ConstData;

/**
 * Created by Data on 6/9/2016.
 */
public class BugaAdapter extends ArrayAdapter<BugsPojo> {
    private int resource;
    private Context context;
    private List<BugsPojo> listData;

    public BugaAdapter(Context context, int resource, List<BugsPojo> listData) {
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
        TextView status = (TextView) v.findViewById(R.id.status_id);
        BugsPojo poja = listData.get(position);
        projectName.setText(poja.getTitle());
        createdAt.setText(poja.getCreatedAt());
        status.setText(ConstData.STATUS[poja.getStatusId()-1]);
        return v;
    }
}
