package de.dienstplan.pb.dienstplan2googlekalender;

import android.app.Activity;
import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.logging.ConsoleHandler;

import de.dienstplan.pb.dienstplan2googlekalender.Business.LayerManager;
import de.dienstplan.pb.dienstplan2googlekalender.Model.Layer;

/**
 * Created by Paule on 15.11.2017.
 */

public class LayerListAdapter extends ArrayAdapter<Layer> {

    Activity context;
    ArrayList<Layer> layers;
    LayerManager layerManager;
    Layer newEntry;

    public LayerListAdapter(Activity context, LayerManager layerManager) {
        super(context, 0, layerManager.getLayerList());
        this.context = context;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        if(view == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            try {
                view = inflater.inflate(R.layout.griditem_layer, viewGroup, false);
            }
            catch (Exception exc) {
                int x = 0;
            }
        }

        TextView textView = view.findViewById(R.id.editLayerName);
        Layer layer = getItem(i);
        textView.setText(layer.getName());
        return view;
    }

    public void addNew() {
        newEntry = new Layer();
        newEntry.setName("Name");
        add(newEntry);
    }
}
