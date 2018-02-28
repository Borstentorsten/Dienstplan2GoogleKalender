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
import java.util.Dictionary;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.dienstplan.pb.dienstplan2googlekalender.Model.Layer;

/**
 * Created by Paule on 23.11.2017.
 */

public class LayerManager {

    static private final String TimeFormatString = "HH:mm";
    static public SimpleDateFormat TimeFormater = new SimpleDateFormat(TimeFormatString);

    SharedPreferences preferences;
    public LayerManager(Activity activity) {
        init(activity);
    }

    public void init(Activity activity) {
        preferences = activity.getSharedPreferences("Layer", 0);
    }


    public HashMap<String, Layer> getLayerDictByName() {
        HashMap<String, Layer> result = new HashMap<>();
        List<Layer> layerList = getLayerList();
        for(Layer layer : layerList) {
            result.put(layer.getName(), layer);
        }
        return result;
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
        if(start != null && end != null) {
            return String.format("%s;%s", dateFormat.format(start.getTime()), dateFormat.format(end.getTime()));
        }
        return null;
    }

    public boolean clearLayer() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        return editor.commit();
    }

    public boolean addLayer(Layer layer) {
        String layerString = createPrefStringByLayer(layer);
        if(layer != null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(layer.getName(), layerString);
            return editor.commit();
        }
        return false;
    }
}
