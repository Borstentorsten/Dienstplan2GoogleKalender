package de.dienstplan.pb.dienstplan2googlekalender;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.util.ArrayList;
import java.util.Calendar;

import de.dienstplan.pb.dienstplan2googlekalender.Business.CalendarManager;
import de.dienstplan.pb.dienstplan2googlekalender.Business.LayerManager;
import de.dienstplan.pb.dienstplan2googlekalender.Business.Settings;
import de.dienstplan.pb.dienstplan2googlekalender.Model.CalendarItem;
import de.dienstplan.pb.dienstplan2googlekalender.Model.Event;

public class MainActivity extends AppCompatActivity implements OnMonthChangedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MaterialCalendarView calendarView = findViewById(R.id.calendarView);
        calendarView.setOnMonthChangedListener(this);

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
        CalendarManager manager = new CalendarManager(this);
        Settings settings = new Settings(this);
        CalendarItem calendarItem = settings.getCalendarItem();
        if(calendarItem != null) {
            CalendarDay start = calendarView.getCurrentDate();
            CalendarDay end = CalendarDay.from(start.getYear(), start.getMonth(), start.getCalendar().getActualMaximum(Calendar.DAY_OF_MONTH));
            if(start != null && end != null) {
                ArrayList<Event> events =
                        manager.getEvents(calendarItem, start.getCalendar(), end.getCalendar());
                calendarView.setDayFormatter(new LayerDayFormater(events, new LayerManager(this)));
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int callbackId, String permissions[], int[] grantResults) {
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
}
