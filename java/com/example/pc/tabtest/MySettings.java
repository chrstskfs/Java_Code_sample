package com.example.pc.tabtest;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/*
util class for storing user credentials
 */
public class MySettings extends ActionBarActivity implements AdapterView.OnItemClickListener {

    final int resource = R.layout.ry8miseis_list_items;
    final int target = R.id.ry8misi;


    final ArrayList<String> epiloges = new ArrayList<String>();


    private ArrayAdapter<String> myAdaptor;
    {
        epiloges.add("Καταχώρηση νέας ζυγαριάς");
        epiloges.add("Καταχώρηση τοποθέτησης");
        epiloges.add("Αλλαγή τοποθεσίας ζυγαριάς");
    }
    private ListView lview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_settings);


        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        // Specify that the Home/Up button should be enabled
        actionBar.setHomeButtonEnabled(true);

        lview = (ListView)findViewById(R.id.listRy8miseis);
        lview.setOnItemClickListener(this);
        myAdaptor = new ArrayAdapter<String>(getApplicationContext(), resource, target, epiloges);
        lview.setAdapter(myAdaptor);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.home) {
            Intent intent = NavUtils.getParentActivityIntent(this);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            NavUtils.navigateUpTo(this, intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position){
            case 0:
                Intent intent1 = new Intent(getApplicationContext(), RegisterMachineActivity.class);
                startActivity(intent1);

                break;
            case 1 :
                Intent intent2 = new Intent(getApplicationContext(), RegisterNewInstallment.class);
                startActivity(intent2);
                break;
            case 2:
                Intent intent3 = new Intent(getApplicationContext(), ChangeMachineLocation.class);
                startActivity(intent3);
                break;
            default :
                ;
                break;
        }
    }
}
