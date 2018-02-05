package de.dienstplan.pb.dienstplan2googlekalender.Model;

import android.databinding.ObservableField;
import android.text.Editable;
import android.text.TextWatcher;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import de.dienstplan.pb.dienstplan2googlekalender.Business.LayerManager;

/**
 * Created by Paule on 23.11.2017.
 */

public class Layer {

    private String name;
    private boolean isNewLayer;
    private Calendar start;
    private Calendar end;
    public TextWatcher nameChangedWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            name = s.toString();
        }
    };

    public void Layer() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Calendar getStart() {
        return start;
    }

    public void setStart(Calendar start) {
        this.start = start;
    }

    public Calendar getEnd() {
        return end;
    }

    public void setEnd(Calendar end) {
        this.end = end;
    }

    public String getStartString() {
        return LayerManager.TimeFormater.format(start.getTime());
    }

    public String getEndString() {
        return LayerManager.TimeFormater.format(end.getTime());
    }

    public boolean isNewLayer() {
        return isNewLayer;
    }

    public void setNewLayer(boolean newLayer) {
        isNewLayer = newLayer;
    }
}
