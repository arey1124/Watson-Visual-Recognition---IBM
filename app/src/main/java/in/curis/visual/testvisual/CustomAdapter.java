package in.curis.visual.testvisual;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import at.grabner.circleprogress.CircleProgressView;

/**
 * Created by arihant on 17/07/17.
 */

public class CustomAdapter extends ArrayAdapter<ItemData> implements View.OnClickListener {

    private ArrayList<ItemData> dataSet;
    Context mContext;

    TextView item_name;
    CircleProgressView circleProgressView;

    public CustomAdapter(ArrayList<ItemData> data, Context context) {
        super(context, R.layout.data_item, data);
        this.dataSet = data;
        this.mContext=context;

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ItemData item = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view

        final View result;

        convertView=null;

        if (convertView == null) {


            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.data_item, parent, false);

            item_name = (TextView)convertView.findViewById(R.id.item_name);
            circleProgressView = (CircleProgressView)convertView.findViewById(R.id.circleview);

            item_name.setText(dataSet.get(position).getName());
            circleProgressView.setShowTextWhileSpinning(true); // Show/hide text in spinning mode
            circleProgressView.setText("Loading...");
            circleProgressView.setSeekModeEnabled(false);
            circleProgressView.setValueAnimated(dataSet.get(position).getPercent());
            result=convertView;

        } else {
            result=convertView;
        }
        return result;
    }
}
