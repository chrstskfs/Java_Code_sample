package com.example.pc.tabtest;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

import model.Machine;
import model.MySharedPreferences;
import model.ServerIpGetter;


public class ChangeMachineLocation extends ActionBarActivity implements View.OnClickListener {


    private Spinner mySpinner;
    private EditText location;
    private Button okButton;

    private ProgressBar myBar;
    private AsyncTasChangeMachineLocation myAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_machine_location);

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();



        // Specify that the Home/Up button should not be enabled, since there is no hierarchical
        // parent.
        actionBar.setHomeButtonEnabled(true);

        this.mySpinner = (Spinner)findViewById(R.id.spinner2);
        this.location = (EditText)findViewById(R.id.newChangedLocation);
        this.okButton = (Button)findViewById(R.id.regNewLocationButton);
        okButton.setOnClickListener(this);

        ArrayList<String> machinesInSpinner = new ArrayList<String>();
        for(Machine m : MainActivity.machineList){
            machinesInSpinner.add(m.getFromNumber());
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, machinesInSpinner);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(dataAdapter);

        this.myBar = (ProgressBar)findViewById(R.id.progressbar_for_change_location);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_change_machine_location, menu);
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
    public void onClick(View v) {
        myAsyncTask = new AsyncTasChangeMachineLocation();
        myAsyncTask.execute(String.valueOf(mySpinner.getSelectedItem()), location.getText().toString());

    }

    private class AsyncTasChangeMachineLocation extends AsyncTask<String, Integer, String> {

        //Namespace of the Webservice - can be found in WSDL
        private String NAMESPACE = "http://functions.pc.me.com/"; // com.me.pc.functions
        //Webservice URL - WSDL File location
        //private String URL = "http://"+ ServerIpGetter.getInstance(getApplicationContext()).getIp()+":8080/Kypseli_cloud/Kypseli_functions?wsdl";
        private String URL = "http://"+ ServerIpGetter.getInstance(getApplicationContext()).getIp()+"/Kypseli_cloud/Kypseli_functions?wsdl";
        //SOAP Action URI again Namespace + Web method name
        private String SOAP_ACTION = "http://com.me.pc.functions/changeLocation";
        private final String functionName = "changeLocation";

        private String result = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            myBar.setVisibility(View.VISIBLE);
            myBar.setProgress(0);
        }

        @Override
        protected String doInBackground(String... params) {

            //Initialize soap request + add parameters
            SoapObject request = new SoapObject(NAMESPACE, functionName);
            // Property which holds input parameters
            PropertyInfo nameInfo = new PropertyInfo();
            // Set Name
            nameInfo.setName("name");
            // Set Value
            nameInfo.setValue(MySharedPreferences.getInstance(getApplicationContext()).readFtomPrefUserName());
            // Set dataType
            nameInfo.setType(String.class);
            // Add the property to request object
            request.addProperty(nameInfo);

            // Property which holds input parameters
            PropertyInfo pass = new PropertyInfo();
            // Set Name
            pass.setName("password");
            // Set Value
            pass.setValue(MySharedPreferences.getInstance(getApplicationContext()).readFtomPrefPassword());
            // Set dataType
            pass.setType(String.class);
            // Add the property to request object
            request.addProperty(pass);

            // Property which holds input parameters
            PropertyInfo mail = new PropertyInfo();
            // Set Name
            mail.setName("mail");
            // Set Value
            mail.setValue(MySharedPreferences.getInstance(getApplicationContext()).readFtomPrefEmail());
            // Set dataType
            mail.setType(String.class);
            // Add the property to request object
            request.addProperty(mail);

            // Property which holds input parameters
            PropertyInfo number = new PropertyInfo();
            // Set Name
            number.setName("machineNumber");
            // Set Value
            number.setValue(params[0]);
            // Set dataType
            number.setType(String.class);
            // Add the property to request object
            request.addProperty(number);

            // Property which holds input parameters
            PropertyInfo locat = new PropertyInfo();
            // Set Name
            locat.setName("machineLocation");
            // Set Value
            locat.setValue(params[1]);
            // Set dataType
            locat.setType(String.class);
            // Add the property to request object
            request.addProperty(locat);

            publishProgress(20);

            // Create envelope
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            // Set output SOAP object
            envelope.setOutputSoapObject(request);


            // Create HTTP call object
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

            publishProgress(30);

            // Invoke web service
            try {
                androidHttpTransport.call(SOAP_ACTION, envelope);
            } catch (IOException e) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Πρόβλημα σύνδεσης. Προσπαθείστε ξανα", Toast.LENGTH_SHORT).show();
                    }
                });
                return result;
            } catch (XmlPullParserException e) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Πρόβλημα σύνδεσης. Προσπαθείστε ξανα", Toast.LENGTH_SHORT).show();
                    }
                });
                return result;
            }

            publishProgress(80);

            try {
                // Get the response
                SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
                // Assign it to resTxt variable static variable
                this.result = response.toString();

            } catch (SoapFault soapFault) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Πρόβλημα σύνδεσης. Προσπαθείστε ξανα", Toast.LENGTH_SHORT).show();
                    }
                });
                return result;
                //finish();
            }
            publishProgress(100);

            return this.result;
        }

        @Override
        protected void onPostExecute(final String s) {
            super.onPostExecute(s);

            myBar.setProgress(0);
            myBar.setVisibility(View.INVISIBLE);

            if(s.equalsIgnoreCase("ok"))
                finish();
            else{
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                    }
                });
            }

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            if (myBar != null) {
                myBar.setProgress(values[0]);
            }
        }
    }
}
