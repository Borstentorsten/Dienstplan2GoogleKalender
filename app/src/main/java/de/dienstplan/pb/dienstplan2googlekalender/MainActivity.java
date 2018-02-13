package de.dienstplan.pb.dienstplan2googlekalender;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import de.dienstplan.pb.dienstplan2googlekalender.Business.CalendarManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(Static.checkPermissions(this, 0, Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR)) {
            readCalendar();
        }

        MaterialCalendarView calendarView = findViewById(R.id.calendarView);
        calendarView.setDayFormatter(new LayerDayFormater());
    }

    void readCalendar() {
        CalendarManager calendarManager = new CalendarManager(this);
        calendarManager.getAvailableCalendars();
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
}
