package de.dienstplan.pb.dienstplan2googlekalender;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.text.Layout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.ConsoleHandler;

import de.dienstplan.pb.dienstplan2googlekalender.Business.LayerManager;
import de.dienstplan.pb.dienstplan2googlekalender.Model.Layer;

/**
 * Created by Paule on 15.11.2017.
 */

public class LayerListAdapter extends ArrayAdapter<Layer> implements View.OnClickListener, TimePickerDialog.OnTimeSetListener {

    Activity context;
    ArrayList<Layer> layers;
    LayerManager layerManager;
    Layer newEntry;
    ListView listView;

    Layer currentLayerForTimePicker;
    boolean isEditingStart;

    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");

    public LayerListAdapter(Activity context, LayerManager layerManager, ListView listView) {
        super(context, 0, layerManager.getLayerList());
        this.context = context;
        this.listView = listView;
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
        TextView textStart = view.findViewById(R.id.editLayerBegin);
        TextView textEnd = view.findViewById(R.id.editLayerEnd);

        Calendar start = layer.getStart();
        Calendar end = layer.getEnd();
        try {
            if (start != null) {
                textStart.setText(dateFormat.format(start.getTime()));
            }
            if (end != null) {
                textEnd.setText(dateFormat.format(end.getTime()));
            }
        } catch (Exception exc) {
            int x = 0;
        }

        textStart.setOnClickListener(this);
        textStart.setTag(layer);
        textEnd.setOnClickListener(this);
        textEnd.setTag(layer);
        return view;
    }

    public void addNew() {
        newEntry = new Layer();
        newEntry.setName("Name");
        add(newEntry);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar date = new GregorianCalendar();
        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
        date.set(Calendar.MINUTE, minute);
        if(isEditingStart) {
            currentLayerForTimePicker.setStart(date);
        }
        else {
            currentLayerForTimePicker.setEnd(date);
        }
        this.listView.invalidateViews();
    }

    @Override
    public void onClick(View v) {
        Layer layer = (Layer)v.getTag();
        currentLayerForTimePicker = layer;
        Calendar date = null;
        switch (v.getId()) {
            case R.id.editLayerBegin:
                date = layer.getStart();
                isEditingStart = true;
                break;
            case R.id.editLayerEnd:
                date = layer.getEnd();
                isEditingStart = false;
                break;
        }
        int hour = 0;
        int min = 0;
        if(date != null) {
            hour = date.get(Calendar.HOUR_OF_DAY);
            min = date.get(Calendar.MINUTE);
        }
        TimePickerDialog dlg = new TimePickerDialog(context, this,
                hour, min, true);
        dlg.show();
    }
}
