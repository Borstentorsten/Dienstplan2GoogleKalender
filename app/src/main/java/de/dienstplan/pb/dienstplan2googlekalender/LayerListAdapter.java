package de.dienstplan.pb.dienstplan2googlekalender;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.text.Layout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
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
import de.dienstplan.pb.dienstplan2googlekalender.databinding.GriditemLayerBinding;
/**
 * Created by Paule on 15.11.2017.
 */

public class LayerListAdapter extends ArrayAdapter<Layer> implements View.OnClickListener, TimePickerDialog.OnTimeSetListener {

    Activity context;
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
        Layer layer = getItem(i);

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        GriditemLayerBinding binding = DataBindingUtil.getBinding(convertView);
        if(binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.griditem_layer, viewGroup, false);
        }
        binding.setLayer(layer);
        binding.executePendingBindings();

        View retView = binding.getRoot();
        TextView textStart = retView.findViewById(R.id.editLayerBegin);
        TextView textEnd = retView.findViewById(R.id.editLayerEnd);
        Button deleteButton = retView.findViewById(R.id.buttonDeleteLayer);

        textStart.setOnClickListener(this);
        textStart.setTag(layer);
        textEnd.setOnClickListener(this);
        textEnd.setTag(layer);
        deleteButton.setOnClickListener(this);
        deleteButton.setTag(layer);
        return retView;
    }

    public void addNew() {
        newEntry = new Layer();
        newEntry.setName("Name");
        newEntry.setStart(new GregorianCalendar(1,1,1,7,0));
        newEntry.setEnd(new GregorianCalendar(1,1,1,12,0));
        newEntry.setNewLayer(true);
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

    private void doPickTime(Calendar initialTime) {
        int hour = 0;
        int min = 0;
        if(initialTime != null) {
            hour = initialTime.get(Calendar.HOUR_OF_DAY);
            min = initialTime.get(Calendar.MINUTE);
        }
        TimePickerDialog dlg = new TimePickerDialog(context, this,
                hour, min, true);
        dlg.show();
    }

    private void deleteLayer() {
        remove(currentLayerForTimePicker);
        listView.invalidateViews();
    }

    @Override
    public void onClick(View v) {
        Layer layer = (Layer)v.getTag();
        currentLayerForTimePicker = layer;
        switch (v.getId()) {
            case R.id.editLayerBegin:
                isEditingStart = true;
                doPickTime(layer.getStart());
                break;
            case R.id.editLayerEnd:
                isEditingStart = false;
                doPickTime(layer.getEnd());
                break;
            case R.id.buttonDeleteLayer:
                deleteLayer();
                break;
        }
    }
}
