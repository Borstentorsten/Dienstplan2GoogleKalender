package de.dienstplan.pb.dienstplan2googlekalender;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import de.dienstplan.pb.dienstplan2googlekalender.Business.LayerManager;
import de.dienstplan.pb.dienstplan2googlekalender.Model.Layer;

public class LayerActivity extends AppCompatActivity {
    LayerListAdapter layerListAdapter;
    LayerManager layerManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.buttonAddLayer);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layerListAdapter.addNew();
                ListView gridView = findViewById(R.id.layerGridView);
                gridView.invalidateViews();
            }
        });

        ListView layerGridView = findViewById(R.id.layerGridView);
        layerManager = new LayerManager(this);
        layerListAdapter = new LayerListAdapter(this, layerManager, layerGridView);
        layerGridView.setAdapter(layerListAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_layer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_layer_cancel) {
            finish();
        }
        else {
            saveLayerList();
            finish();
        }
        return true;
    }


    public void saveLayerList() {
        int count = layerListAdapter.getCount();
        for(int pos = 0; pos < count; pos++) {
            Layer layer = layerListAdapter.getItem(pos);
            layerManager.addLayer(layer);
        }
    }
}
