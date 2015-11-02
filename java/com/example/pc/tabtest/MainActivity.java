package com.example.pc.tabtest;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;
import model.Machine;
import model.MySharedPreferences;
import model.ServerIpGetter;


public class MainActivity extends ActionBarActivity implements ActionBar.TabListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    // holds the machine list
    static ArrayList<Machine> machineList = new ArrayList<Machine>();

    //
    private static String name = null;
    private static String password = null;
    private static String email = null;


    // test
    WeakReference<Zygaries> ref2Zygaries;
    WeakReference<Xartis> ref2Xartis;

    ProgressBar myBar;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();

        // Specify that the Home/Up button should not be enabled, since there is no hierarchical
        // parent.
        actionBar.setHomeButtonEnabled(false);

        // Specify that we will be displaying tabs in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);


        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // When swiping between different app sections, select the corresponding tab.
                // We can also use ActionBar.Tab#select() to do this if we have a reference to the
                // Tab.
                actionBar.setSelectedNavigationItem(position);
            }


        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by the adapter.
            // Also specify this Activity object, which implements the TabListener interface, as the
            // listener for when this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener( this));
        }

        myBar = (ProgressBar)findViewById(R.id.progressbarSmsForNumber);

        //
        if(savedInstanceState  == null)
            new AsyncTaskGetMachines().execute();


    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void makeActivityAgain(){

        recreate();
        /*
        or :

        finish();
        startActivity(getIntent());
        */
    }


    public void refreshragments() {
        if(ref2Xartis != null && ref2Zygaries != null) {
            //ref2Zygaries.get().setAm(a);
            ref2Zygaries.get().getMachines();
            ref2Xartis.get().makeMarkers();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();

        // ubuntu
        //MySharedPreferences.getInstance(getApplicationContext()).write2PrefUserName("christos");
        //MySharedPreferences.getInstance(getApplicationContext()).write2PrefPassword("christos");
        //MySharedPreferences.getInstance(getApplicationContext()).write2PrefEmail("ak");

        // windows
        MySharedPreferences.getInstance(getApplicationContext()).write2PrefUserName("xristos");
        MySharedPreferences.getInstance(getApplicationContext()).write2PrefPassword("2");
        MySharedPreferences.getInstance(getApplicationContext()).write2PrefEmail("ak");

        this.name = MySharedPreferences.getInstance(getApplicationContext()).readFtomPrefUserName();
        this.password =  MySharedPreferences.getInstance(getApplicationContext()).readFtomPrefPassword();
        this.email = MySharedPreferences.getInstance(getApplicationContext()).readFtomPrefEmail();



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
        if (id == R.id.action_settings) {
            Intent myin = new Intent(this, MySettings.class);
            startActivity(myin);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        Zygaries z;
        Xartis x;
        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            switch (position) {
                case 0:
                    /*
                    z = Zygaries.newInstance(position);
                    ref2Zygaries = new WeakReference<Zygaries>(z);
                    return z;
                    */
                    ref2Zygaries = new WeakReference<Zygaries>(Zygaries.newInstance(position));
                    return ref2Zygaries.get();
                case 1:
                    /*
                    x = Xartis.newInstance(position);
                    ref2Xartis = new WeakReference<Xartis>(x);
                    return x;
                    */
                    ref2Xartis = new WeakReference<Xartis>(Xartis.newInstance(position));
                    return ref2Xartis.get();
                case 2 :
                    return LocationListFragment.newInstance(position);
                default:
                    // The other sections of the app are dummy placeholders.
                    return null;
            }

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }


    /**
     * asyncTask for getting the ;list of active machines
     */

    private class AsyncTaskGetMachines extends AsyncTask<Void, Integer, Void> {


        // http://192.168.1.8:8080/Kypseli_cloud/Kypseli_functions?wsdl
        //Namespace of the Webservice - can be found in WSDL
        private String NAMESPACE = "http://functions.pc.me.com/"; // com.me.pc.functions
        //Webservice URL - WSDL File location
        //private String URL = "http://"+ ServerIpGetter.getInstance(getApplicationContext()).getIp() +":8080/Kypseli_cloud/Kypseli_functions?wsdl";
        private String URL = "http://"+ ServerIpGetter.getInstance(getApplicationContext()).getIp() +"/Kypseli_cloud/Kypseli_functions?wsdl";
        //SOAP Action URI again Namespace + Web method name
        private String SOAP_ACTION = "http://com.me.pc.functions/getSUserMachines";
        private final String functionName = "getSUserMachines";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            myBar.setVisibility(View.VISIBLE);
            myBar.setProgress(0);

        }

        @Override
        protected Void doInBackground(Void... params) {

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

            System.out.println(request);
            publishProgress(10);


            // Create envelope
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);

            //System.out.println("envelope = "+envelope.);

            // Set output SOAP object
            envelope.setOutputSoapObject(request);


            // Create HTTP call object
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.debug = true; // debuging

            // Invoke web service
            try {
                androidHttpTransport.call(SOAP_ACTION, envelope);

                System.out.println("host = "+androidHttpTransport.getHost());
                System.out.println("path = "+androidHttpTransport.getPath());
                System.out.println("port = "+androidHttpTransport.getPort());

                System.out.println("connection host = "+androidHttpTransport.getConnection().getHost());
                System.out.println("connection path = "+androidHttpTransport.getConnection().getPath());
                System.out.println("connection port = "+androidHttpTransport.getConnection().getPort());


                System.out.println("->"+androidHttpTransport.requestDump+"@@@@");
                System.out.println("->"+androidHttpTransport.responseDump+"@@@@");

            } catch (IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Πρόβλημα σύνδεσης. Προσπαθείστε ξανα", Toast.LENGTH_SHORT).show();
                    }
                });
                return null;
            } catch (XmlPullParserException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Πρόβλημα σύνδεσης. Προσπαθείστε ξανα", Toast.LENGTH_SHORT).show();
                    }
                });
                return null;
            }

            publishProgress(40);

            if (envelope.bodyIn instanceof SoapFault) { // SoapFault = FAILURE

                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Πρόβλημα σύνδεσης. SoapFault", Toast.LENGTH_SHORT).show();
                    }
                });
                return null;
            }
            else {
                // Get the response as xml
                SoapObject responseAll = (SoapObject) envelope.bodyIn;
                SoapObject responsetmp;

                machineList.clear();
                ArrayList<HashMap<String, String>> tmpList = new ArrayList<HashMap<String, String>>();
                HashMap<String, String> tmpMap = new HashMap<String, String>();
                Machine tmpMachine;
                for (int i = 0; i < responseAll.getPropertyCount(); i++) {
                    responsetmp = (SoapObject) responseAll.getProperty(i);
                    tmpMachine = new Machine();
                    switch (responsetmp.getPropertyCount()) {
                        case 1:
                            tmpMachine.setFromNumber(responsetmp.getProperty(0).toString());
                            break;
                        case 2:
                            tmpMachine.setFromNumber(responsetmp.getProperty(0).toString());
                            tmpMachine.setLocation(responsetmp.getProperty(1).toString());
                            break;
                        case 3:
                            tmpMachine.setFromNumber(responsetmp.getProperty(0).toString());
                            tmpMachine.setLocation(responsetmp.getProperty(2).toString());
                            tmpMachine.setGpsLocation(responsetmp.getProperty(1).toString()); // ?? giati anapoda ?? --> alphabhtika
                            break;
                        default:
                            ;
                            break;
                    }
                    machineList.add(tmpMachine);
                }

                publishProgress(80);
            }
            return null;
        }

        /*
        updates data
         */
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            publishProgress(100);

            refreshragments();

            myBar.setProgress(0);
            myBar.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            if (myBar != null) {
                myBar.setProgress(values[0]);
            }

        }
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class Zygaries extends Fragment implements AdapterView.OnItemClickListener {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static Zygaries newInstance(int sectionNumber) {
            Zygaries fragment = new Zygaries();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public Zygaries() {
        }

        private View rootView;

        // vars fot the list view
        private ListView lv;
        private final int resource = R.layout.zygaries_list_items;
        private final String[] from = {"number", "location"};
        private final int[] to = {R.id.number_display, R.id.location_display};
        private SimpleAdapter adapter;

        private Button refresh;

        private ArrayList<HashMap<String, String>> am = new ArrayList<HashMap<String, String>> ();

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.zygaries_main, container, false);
            this.rootView = rootView;
            Bundle args = getArguments();


            lv = (ListView)this.rootView.findViewById(R.id.list);
            lv.setOnItemClickListener(this);

           if(! (name == null || name.equalsIgnoreCase("")) )
                getMachines();

            return rootView;
        }

        public void setAm(ArrayList<HashMap<String, String>> am) {
            this.am = am;
        }

        @Override
        public void onResume() {
            super.onResume();

            if (name == null || name.equalsIgnoreCase("")) {
                Intent myIntentA = new Intent(getActivity().getApplicationContext(), NewUserRegistryForm.class);
                startActivity(myIntentA);
            }
        }

        // gets from server
        private void getMachines() {

            am.clear();
            HashMap<String, String> m;
            for (int i = 0; i < machineList.size(); i++){
                m = new HashMap<String, String>();
                m.put("number", "Αριθμός SIM : "+machineList.get(i).getFromNumber().toString());
                m.put("location", "στην τοποθεσία '"+machineList.get(i).getLocation()+"'");
                am.add(m);
            }


            adapter = new SimpleAdapter(getActivity().getApplicationContext(), am  , resource, from, to);
            lv.setAdapter(adapter);

        }

        HashMap<String, String> m;
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            //
            m = (HashMap<String, String>)parent.getAdapter().getItem(position);
            System.out.println("number = "+m.get("number").substring(14));
            System.out.println("location = "+m.get("location").substring(16, m.get("location").length()-1));
            Intent myIn = new Intent(getActivity(), SmsListsAndChart.class);
            myIn.putExtra("numberSelected", m.get("number").substring(14));
            myIn.putExtra("locationOfMachine", m.get("location").substring(16, m.get("location").length()-1));
            startActivity(myIn);

        }
    }


    /**
     * A placeholder fragment containing a simple view. Displays a map
     */
    public static class Xartis extends Fragment implements GoogleMap.OnMarkerClickListener {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static Xartis newInstance(int sectionNumber) {
            Xartis fragment = new Xartis();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public Xartis() {
        }


        MapView mMapView;
        GoogleMap googleMap;

        ArrayList<MarkerOptions> markerList = new ArrayList<MarkerOptions>();
        ArrayList<Machine> mlist;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.xartis_main, container, false);
            Bundle args = getArguments();


            // xartis me markers sthn topo8esia twn zygariwn

            mMapView = (MapView) rootView.findViewById(R.id.mapView);
            mMapView.onCreate(savedInstanceState);

            mMapView.onResume();// needed to get the map to display immediately

            try {
                MapsInitializer.initialize(getActivity().getApplicationContext());
            } catch (Exception e) {
                e.printStackTrace();
            }

            googleMap = mMapView.getMap();

            if(googleMap == null){
                System.out.println("GOOGLE MAP IS NULL !!"); // testing
            }

            // creates markers
            if(!(name == null || name.equalsIgnoreCase("")))
                makeMarkers();

            // click
            googleMap.setOnMarkerClickListener(this);


            // camera position --> map of greece
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(38.46723811266664, 22.675930447876453)).zoom((float) 6.3).build();
            googleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));



            return rootView;
        }

        private void makeMarkers() {

            MarkerOptions marker;
            String [] gpsCoord;
            googleMap.clear();

            mlist = (ArrayList<Machine>)machineList.clone();

            for(Machine m : mlist){
                if(m.getGpsLocation() == null || m.getGpsLocation().equals(""))
                    continue;
                else
                    gpsCoord = m.getGpsLocation().split(";");

                // create marker
                marker = new MarkerOptions().position(
                        new LatLng(Double.parseDouble(gpsCoord[0]), Double.parseDouble(gpsCoord[1]))).title(m.getFromNumber()).snippet(m.getLocation());
                // Changing marker icon
                marker.icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
                // ad to list
                //markerList.add(marker);
                // adding marker
                googleMap.addMarker(marker);
            }

        }


        @Override
        public void onResume() {
            super.onResume();
            mMapView.onResume();
        }

        @Override
        public void onPause() {
            super.onPause();
            mMapView.onPause();
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            mMapView.onDestroy();
        }

        @Override
        public void onLowMemory() {
            super.onLowMemory();
            mMapView.onLowMemory();
        }


        @Override
        public boolean onMarkerClick(Marker marker) {

            System.out.println(""+marker.getTitle());
            Intent myIn = new Intent(getActivity(), SmsListsAndChart.class);
            myIn.putExtra("numberSelected", marker.getTitle());
            myIn.putExtra("locationOfMachine", marker.getSnippet());
            startActivity(myIn);
            return true;
        }
    }


    /*
    fragment that holds the list of all locations
     */

    public static class LocationListFragment extends Fragment implements AdapterView.OnItemClickListener {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static LocationListFragment newInstance(int sectionNumber) {
            LocationListFragment fragment = new LocationListFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public LocationListFragment() {
        }


        private View rootView;

        private ListView lv;
        private final int resource = R.layout.topo8esies_list_items;
        private final int to = R.id.topo8esia_display;
        private ArrayAdapter<String> adapter;
        private final ArrayList<String> topo8esies = new ArrayList<String>();

        private WeakReference<GetMyLocations_AsyncTask> asyncTaskWeakRef;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.topo8esies_main, container, false);
            Bundle args = getArguments();

            this.rootView = rootView;

            GetMyLocations_AsyncTask asyncTask = new GetMyLocations_AsyncTask(this);
            this.asyncTaskWeakRef = new WeakReference<GetMyLocations_AsyncTask >(asyncTask );

            if(! (name == null || name.equalsIgnoreCase("")) )
                asyncTask.execute();

            return rootView;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            /*
            Intent intent1 = new Intent(getActivity(),SmsLocationList.class);
            intent1.putExtra("location", (String)parent.getAdapter().getItem(position));
            startActivity(intent1);
            */
            Intent intent1 = new Intent(getActivity(),CalendarLike.class);
            intent1.putExtra("location", (String)parent.getAdapter().getItem(position));
            startActivity(intent1);

        }


        private class GetMyLocations_AsyncTask extends AsyncTask<String,Void, String> {

            //Namespace of the Webservice - can be found in WSDL
            private String NAMESPACE = "http://functions.pc.me.com/"; // com.me.pc.functions
            //Webservice URL - WSDL File location
            //private String URL = "http://"+   ServerIpGetter.getInstance(getActivity().getApplicationContext()).getIp()+":8080/Kypseli_cloud/Kypseli_functions?wsdl";
            private String URL = "http://"+   ServerIpGetter.getInstance(getActivity().getApplicationContext()).getIp()+"/Kypseli_cloud/Kypseli_functions?wsdl";
            //SOAP Action URI again Namespace + Web method name
            private String SOAP_ACTION = "http://com.me.pc.functions/getUserLocations";
            private final String functionName = "getUserLocations";

            private String result = "";
            private String[] tmp;

            private WeakReference<LocationListFragment> fragmentWeakRef;

            private GetMyLocations_AsyncTask (LocationListFragment fragment) {
                this.fragmentWeakRef = new WeakReference<LocationListFragment>(fragment);
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
                nameInfo.setValue(MySharedPreferences.getInstance(getActivity().getApplicationContext()).readFtomPrefUserName());
                // Set dataType
                nameInfo.setType(String.class);
                // Add the property to request object
                request.addProperty(nameInfo);

                // Property which holds input parameters
                PropertyInfo pass = new PropertyInfo();
                // Set Name
                pass.setName("password");
                // Set Value
                pass.setValue(MySharedPreferences.getInstance(getActivity().getApplicationContext()).readFtomPrefPassword());
                // Set dataType
                pass.setType(String.class);
                // Add the property to request object
                request.addProperty(pass);

                // Property which holds input parameters
                PropertyInfo mail = new PropertyInfo();
                // Set Name
                mail.setName("mail");
                // Set Value
                mail.setValue(MySharedPreferences.getInstance(getActivity().getApplicationContext()).readFtomPrefEmail());
                // Set dataType
                mail.setType(String.class);
                // Add the property to request object
                request.addProperty(mail);

                // Create envelope
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                // Set output SOAP object
                envelope.setOutputSoapObject(request);


                // Create HTTP call object
                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);


                // Invoke web service
                try {
                    androidHttpTransport.call(SOAP_ACTION, envelope);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }

                SoapPrimitive response= null;
                try {
                    // Get the response
                    response = (SoapPrimitive) envelope.getResponse();
                    // Assign it to resTxt variable static variable
                    this.result = response.toString();
                    //System.out.println("locations = "+result);

                } catch (SoapFault soapFault) {
                    soapFault.printStackTrace();
                }catch(NullPointerException ex){
                    ex.printStackTrace();
                }
                if(response == null)
                    result = "";
                else
                    result = response.toString();

                return result;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                tmp = s.split(";");
                lv = (ListView)rootView.findViewById(R.id.listTopo8esies);
                lv.setOnItemClickListener(fragmentWeakRef.get());
                adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), resource, to, tmp);
                lv.setAdapter(adapter);

            }
        }


    }


}
