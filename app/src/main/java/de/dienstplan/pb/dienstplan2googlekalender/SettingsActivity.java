package de.dienstplan.pb.dienstplan2googlekalender;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import de.dienstplan.pb.dienstplan2googlekalender.Business.CalendarManager;
import de.dienstplan.pb.dienstplan2googlekalender.Business.Settings;
import de.dienstplan.pb.dienstplan2googlekalender.Model.CalendarItem;

public class SettingsActivity extends AppCompatActivity {

    Settings settings;
    List<CalendarItem> calendarItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        try {
            Toolbar toolbar = findViewById(R.id.toolbarSettings);
            setSupportActionBar(toolbar);
        }
        catch(Exception e) {
            Static.showAlert(this, e.getMessage());
        }
        settings = new Settings(this);

        CalendarItem item = settings.getCalendarItem();

        CalendarManager calendarManager = new CalendarManager(this);
        calendarItems = calendarManager.getAvailableCalendars();

        HashSet<String> accounts = new HashSet<>();
        for(CalendarItem iter : calendarItems) {
            accounts.add(iter.getAccountName());
        }

        Spinner accountSpinner = findViewById(R.id.spinnerAccountPicker);
        ArrayAdapter<String> accountAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        accountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accountAdapter.addAll(accounts);
        accountSpinner.setAdapter(accountAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings_cancel:
                finish();
                break;
            case R.id.action_settings_save:

                finish();
                break;
        }
        return true;
    }
}
