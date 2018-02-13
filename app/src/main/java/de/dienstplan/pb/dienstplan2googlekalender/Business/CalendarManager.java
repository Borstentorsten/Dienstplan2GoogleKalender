package de.dienstplan.pb.dienstplan2googlekalender.Business;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.dienstplan.pb.dienstplan2googlekalender.Model.CalendarItem;
import de.dienstplan.pb.dienstplan2googlekalender.Model.Event;
import de.dienstplan.pb.dienstplan2googlekalender.Model.Layer;
import de.dienstplan.pb.dienstplan2googlekalender.Static;

/**
 * Created by Paule on 06.02.2018.
 */

public class CalendarManager {
    // Projection array. Creating indices for this array instead of doing
    // dynamic lookups improves performance.
    public static final String[] CALENDAR_PROJECTION = new String[]{
            CalendarContract.Calendars._ID,                           // 0
            CalendarContract.Calendars.ACCOUNT_NAME,                  // 1
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,         // 2
            CalendarContract.Calendars.OWNER_ACCOUNT                  // 3
    };

    // The indices for the projection array above.
    private static final int CALENDAR_PROJECTION_ID_INDEX = 0;
    private static final int CALENDAR_PROJECTION_ACCOUNT_NAME_INDEX = 1;
    private static final int CALENDAR_PROJECTION_DISPLAY_NAME_INDEX = 2;
    private static final int CALENDAR_PROJECTION_OWNER_ACCOUNT_INDEX = 3;

    Activity activity;

    public CalendarManager(Activity activity) {
        this.activity = activity;
    }

    public List<CalendarItem> getAvailableCalendars() {
        ArrayList<CalendarItem> retList = new ArrayList<>();
        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        ContentResolver cr = activity.getContentResolver();
        String query = "";
        String[] selectionArgs = null;
        try {
            Cursor cursor = cr.query(uri, CALENDAR_PROJECTION, query, selectionArgs, null);
            while (cursor.moveToNext()) {
                String name = cursor.getString(CALENDAR_PROJECTION_DISPLAY_NAME_INDEX);
                String accountName = cursor.getString(CALENDAR_PROJECTION_ACCOUNT_NAME_INDEX);
                long id = cursor.getLong(CALENDAR_PROJECTION_ID_INDEX);
                CalendarItem item = new CalendarItem();
                item.setAccountName(accountName);
                item.setCalendarName(name);
                item.setCalendarId(id);
                retList.add(item);
                int x = 0;
            }

        }
        catch (SecurityException exc) {
            Static.showAlert(activity, exc.getMessage());
        }

        return retList;
    }

    public void getEvents() {
        ContentResolver cr = activity.getContentResolver();
        try {
        }
        catch (Exception exc) {
            Static.showAlert(activity, exc.getMessage());
        }
    }

    public void setEvent(Event event) {
        ContentResolver cr = activity.getContentResolver();
        ContentValues contentValues = new ContentValues();

        Layer layer = event.getLayer();
        Date layerStart = layer.getStart().getTime();
        Date layerEnd = layer.getEnd().getTime();

        Calendar start = event.getDate();
        start.setTime(layerStart);
        Calendar end = event.getDate();
        if(layerEnd.compareTo(layerStart) < 0) {
            end.add(Calendar.DAY_OF_MONTH, 1);
        }
        end.setTime(layerEnd);

        contentValues.put(CalendarContract.Events.DTSTART, start.getTimeInMillis());
        contentValues.put(CalendarContract.Events.DTEND, end.getTimeInMillis());
        contentValues.put(CalendarContract.Events.TITLE, layer.getName());

        try {
            cr.insert(CalendarContract.Events.CONTENT_URI, contentValues);
        }
        catch (SecurityException exc) {
            Static.showAlert(activity, exc.getMessage());
        }
    }
}