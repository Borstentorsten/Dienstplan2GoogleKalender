package de.dienstplan.pb.dienstplan2googlekalender.Business;

import android.app.Activity;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import de.dienstplan.pb.dienstplan2googlekalender.Model.Layer;

/**
 * Created by Paule on 23.11.2017.
 */

public class LayerManager {

    static final String TimeFormatString = "HH:mm";

    SharedPreferences preferences;
    public LayerManager(Activity activity) {
        init(activity);
    }

    public void init(Activity activity) {
        preferences = activity.getSharedPreferences("Layer", 0);
    }



    public ArrayList<Layer> getLayerList() {
        ArrayList<Layer> retList = new ArrayList<>();
        Map<String, ?> layerMap = preferences.getAll();
        for (Map.Entry<String, ?> item: layerMap.entrySet()) {
            try {
                Layer layer = parsePrefString(item.getValue().toString());
                layer.setName(item.getKey());
                retList.add(layer);
            }
            catch (Exception exc) {
                // ToDo: Delete the item
            }
        }
        return retList;
    }

    static Layer parsePrefString(String preferences) throws Exception {
        String[] startEndItems = preferences.split(";");
        if(startEndItems.length != 2)
            throw new Exception("Ungültige Anzahl an Items");


        Layer layer = new Layer();
        boolean start = true;
        for (String item: startEndItems) {
            SimpleDateFormat format = new SimpleDateFormat(TimeFormatString);
            Calendar calendar = new GregorianCalendar();
            Date date = format.parse(item);
            calendar.setTime(date);

            if(start) {
                layer.setStart(calendar);
                start = false;
            }
            else {
                layer.setEnd(calendar);
            }
        }
        return layer;
    }

    static String createPrefStringByLayer(Layer layer) {
        Calendar start = layer.getStart();
        Calendar end = layer.getEnd();
        SimpleDateFormat dateFormat = new SimpleDateFormat(TimeFormatString);
        return String.format("{0};{1}", dateFormat.format(start), dateFormat.format(end));
    }

    public boolean addLayer(Layer layer) {
        String layerString = createPrefStringByLayer(layer);
        preferences.edit().putString(layer.getName(), layerString);
        return preferences.edit().commit();
    }
}
