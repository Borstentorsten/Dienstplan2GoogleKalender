package de.dienstplan.pb.dienstplan2googlekalender;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import de.dienstplan.pb.dienstplan2googlekalender.Business.CalendarManager;
import de.dienstplan.pb.dienstplan2googlekalender.Business.LayerManager;
import de.dienstplan.pb.dienstplan2googlekalender.Business.Settings;
import de.dienstplan.pb.dienstplan2googlekalender.Model.CalendarItem;
import de.dienstplan.pb.dienstplan2googlekalender.Model.Event;
import de.dienstplan.pb.dienstplan2googlekalender.Model.Layer;

public class MainActivity extends AppCompatActivity implements OnMonthChangedListener, OnDateSelectedListener {

    CalendarManager calendarManager;
    CalendarItem calendarItem;
    MaterialCalendarView calendarView;
    LayerDayFormater dayFormater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        calendarManager = new CalendarManager(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        calendarView = findViewById(R.id.calendarView);
        calendarView.setOnMonthChangedListener(this);
        calendarView.setOnDateChangedListener(this);
        if(Static.checkPermissions(this, 0, Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR)) {
            readCalendar();
        }
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
        readCalendar();
    }

    void readCalendar() {
        MaterialCalendarView calendarView = findViewById(R.id.calendarView);
        calendarView.setOnMonthChangedListener(this);
        Settings settings = new Settings(this);
        calendarItem = settings.getCalendarItem();
        if(calendarItem != null) {
            CalendarDay start = calendarView.getCurrentDate();
            CalendarDay end = CalendarDay.from(start.getYear(), start.getMonth(), start.getCalendar().getActualMaximum(Calendar.DAY_OF_MONTH));
            if(start != null && end != null) {
                ArrayList<Event> events =
                        calendarManager.getEvents(calendarItem, start.getCalendar(), end.getCalendar());
                dayFormater = new LayerDayFormater(events, new LayerManager(this));
                calendarView.setDayFormatter(dayFormater);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int callbackId, String permissions[], int[] grantResults) {
        calendarManager = new CalendarManager(this);
        readCalendar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_layers) {
            LayerActivity layerActivity = new LayerActivity();
            Intent intent = new Intent(this, LayerActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.action_settings) {
            SettingsActivity settingsActivity = new SettingsActivity();
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
        if(Static.checkPermissions(this, 0, Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR)) {
            readCalendar();
        }
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull final CalendarDay date, boolean selected) {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Schicht auswählen");
        b.setCancelable(true);
        final LayerManager layerManager = new LayerManager(this);
        final List<Layer> layers = layerManager.getLayerList();
        String[] items = new String[layers.size() + 1];
        items[0] = getString(R.string.layer_no_duty);
        int n = 1;
        for(Layer layer : layers) {
            items[n] = layer.getName();
            n++;
        }
        b.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Layer layer = null;
                if(which == 0) {

                }
                else {
                    layer = layers.get(which - 1);
                }
                Event event = calendarManager.setLayerToCalendar(layer, date, layers, calendarItem);
                //dayFormater.setLayerToDay(date.getDay(), event);
                //calendarView.
                readCalendar();
            }
        });

        b.show();
    }
}
