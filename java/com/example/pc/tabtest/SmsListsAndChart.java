package com.example.pc.tabtest;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.provider.Telephony;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import model.MySharedPreferences;
import model.ServerIpGetter;
import model.Sms_data;
import model.UpdateChartInterface;


public class SmsListsAndChart extends ActionBarActivity implements ActionBar.TabListener, UpdateChartInterface{

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

    private static String number;
    private static String location;
    private static ArrayList<Sms_data> myListSms = new ArrayList<Sms_data>();
    //private static final ArrayList<HashMap<String, String>> am = new ArrayList<HashMap<String, String>>();

    private WeakReference<SmsChart> chartRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_lists_and_chart);


        Intent int1 = getIntent();
        this.number = (String)int1.getSerializableExtra("numberSelected");
        this.location = (String)int1.getSerializableExtra("locationOfMachine");


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        // Specify that the Home/Up button should not be enabled, since there is no hierarchical
        // parent.
        actionBar.setHomeButtonEnabled(true);

        // Specify that we will be displaying tabs in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);



        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }


        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener( this));
        }

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sms_lists_and_chart, menu);
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
        if (id == R.id.home) {
            Intent intent = NavUtils.getParentActivityIntent(this);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            NavUtils.navigateUpTo(this, intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        mViewPager.setCurrentItem(tab.getPosition());

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void updateChart(ArrayList<Sms_data> a) {

        if(chartRef != null)
            chartRef.get().refresh();

    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        SmsChart tmp;
        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.


            switch (position) {
                case 0:
                    return ListSms.newInstance(0);
                case 1:
                    chartRef = new WeakReference<SmsChart>(SmsChart.newInstance(1));
                    return chartRef.get();
                default:
                    return null;

            }

        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section2_1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2_2).toUpperCase(l);
            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */

    public static class ListSms extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static ListSms newInstance(int sectionNumber) {
            ListSms fragment = new ListSms();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public ListSms() {
        }

        /*
        the interface to update the chart
         */
        public WeakReference<UpdateChartInterface> actionCall;


        // data list
        private ListView lv;
        private final int resource = R.layout.sms_for_number_list_items;
        private final String[] from = {"date", "weight", "temperature", "humidity"};
        private final int[] to = {R.id.date_display, R.id.weight_display, R.id.temperature_display, R.id.humidity_display};
        private SimpleAdapter adapter;

        private View rootView;
        private ProgressBar myBar;
        private WeakReference<GetSmsForNumber_AsyncTask> asyncTaskWeakRef;


        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            try {
                // This makes sure that the container activity has implemented
                // the callback interface. If not, it throws an exception
                actionCall = new WeakReference<UpdateChartInterface>((UpdateChartInterface) activity);
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString() + " must implement IActionListener.");
            }
        }

        @Override
        public void onDetach() {
            super.onDetach();
            actionCall = null;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_sms_for_number_lists, container, false);
            Bundle args = getArguments();
            this.rootView = rootView;

            GetSmsForNumber_AsyncTask asyncTask = new GetSmsForNumber_AsyncTask(this);
            this.asyncTaskWeakRef = new WeakReference<GetSmsForNumber_AsyncTask >(asyncTask );
            setRetainInstance(true);

            asyncTask.execute();

            return rootView;
        }


        private class GetSmsForNumber_AsyncTask extends AsyncTask<String, Integer, ArrayList<Sms_data>> {


            private String NAMESPACE = "http://functions.pc.me.com/"; // com.me.pc.functions
            //Webservice URL - WSDL File location
            //private String URL = "http://"+ ServerIpGetter.getInstance(getActivity().getApplicationContext()).getIp() +":8080/Kypseli_cloud/Kypseli_functions?wsdl";
            private String URL = "http://"+ ServerIpGetter.getInstance(getActivity().getApplicationContext()).getIp() +"/Kypseli_cloud/Kypseli_functions?wsdl";
            //SOAP Action URI again Namespace + Web method name
            private String SOAP_ACTION = "http://com.me.pc.functions/getSmsForMachine";
            private final String functionName = "getSmsForMachine";

            private WeakReference<ListSms> fragmentWeakRef;

            private GetSmsForNumber_AsyncTask(ListSms fragment) {
                this.fragmentWeakRef = new WeakReference<ListSms>(fragment);
            }

            ProgressBar bar;
            private String result;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                // progressBar
                //bar = (ProgressBar)rootView.findViewById(R.id.progressbar);
                //bar.setVisibility(View.VISIBLE);
                //bar.setProgress(0);

            }

            GregorianCalendar cal;
            ArrayList<Sms_data> tmpList;
            @Override
            protected ArrayList<Sms_data> doInBackground(String... params) {

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

                // Property which holds input parameters
                PropertyInfo machineNumber = new PropertyInfo();
                // Set Name
                machineNumber.setName("machineNumber");
                // Set Value
                machineNumber.setValue(number);
                // Set dataType
                machineNumber.setType(String.class);
                // Add the property to request object
                request.addProperty(machineNumber);

                // Property which holds input parameters
                PropertyInfo machineLocation = new PropertyInfo();
                // Set Name
                machineLocation.setName("machineLocation");
                // Set Value
                machineLocation.setValue(location);
                // Set dataType
                machineLocation.setType(String.class);
                // Add the property to request object
                request.addProperty(machineLocation);

                publishProgress(10);



                // Create envelope
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                // Set output SOAP object
                envelope.setOutputSoapObject(request);


                // Create HTTP call object
                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
                //androidHttpTransport.debug = true; // debuging

                publishProgress(30);

                // Invoke web service
                try {
                    androidHttpTransport.call(SOAP_ACTION, envelope);
                } catch (IOException e) {
                    e.printStackTrace();
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getActivity().getApplicationContext(), "Πρόβλημα σύνδεσης. Προσπαθείστε ξανα", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return tmpList;
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getActivity().getApplicationContext(), "Πρόβλημα σύνδεσης. Προσπαθείστε ξανα", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return tmpList;
                }

                publishProgress(40);

                if (envelope.bodyIn instanceof SoapFault) { // SoapFault = FAILURE

                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getActivity().getApplicationContext(), "Πρόβλημα σύνδεσης. Soap", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return tmpList;

                }
                else {
                    // Get the response as xml
                    SoapObject responseAll = (SoapObject) envelope.bodyIn;
                    SoapObject responsetmp;

                    publishProgress(80);

                    tmpList = new ArrayList<Sms_data>();
                    Sms_data tmpData;
                    for (int i = 0; i < responseAll.getPropertyCount(); i++) {
                        responsetmp = (SoapObject) responseAll.getProperty(i);
                        tmpData = new Sms_data();
                        tmpData.setDateSend(responsetmp.getProperty("dateSend").toString());
                        tmpData.setWeigth(Float.parseFloat(responsetmp.getProperty("weigth").toString()));
                        tmpData.setTemperature(Float.parseFloat(responsetmp.getProperty("temperature").toString()));
                        tmpData.setHumidity(Float.parseFloat(responsetmp.getProperty("humidity").toString()));
                        tmpList.add(tmpData);
                    }

                    publishProgress(100);
                    SmsListsAndChart.myListSms = tmpList;
                }
                return tmpList;
            }

            @Override
            protected void onPostExecute(ArrayList<Sms_data> sms_datas) {
                super.onPostExecute(sms_datas);

                //bar.setProgress(0);
                //bar.setVisibility(View.INVISIBLE);

                ArrayList<HashMap<String, String>> am = new ArrayList<HashMap<String, String>>();
                if(sms_datas != null) {
                    HashMap<String, String> m;
                    for (int i = 0; i < sms_datas.size(); i++) {
                        m = new HashMap<String, String>();
                        m.put("date", " " + sms_datas.get(i).getDateSend().toString() + " : ");
                        m.put("weight", "βάρος " + sms_datas.get(i).getWeigth() + " κιλά, ");
                        m.put("temperature", "θερμοκρασία  " + sms_datas.get(i).getTemperature() + " οC, ");
                        m.put("humidity", "και υγρασία " + sms_datas.get(i).getHumidity() + " %");
                        am.add(m);
                    }
                }

                lv = (ListView)rootView.findViewById(R.id.listsmsformachine);
                adapter = new SimpleAdapter(getActivity().getApplicationContext(), am  , resource, from, to);
                lv.setAdapter(adapter);

                // I want from here to call SmsChart.createChart(sms_datas)
                actionCall.get().updateChart(sms_datas);
                SmsListsAndChart.myListSms = sms_datas;
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);

                if (this.bar != null) {
                    bar.setProgress(values[0]);
                }
            }

        }

    }

    /**
     * A placeholder fragment containing a view. Chart view
     */
    public static class SmsChart extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";


        // vars chart
        private   GraphicalView myChart;

        private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();

        private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();


        private  XYSeries shmeiaVarous;
        private  XYSeries shmeiaTemp;
        private  XYSeries shmeiaHum;


        private XYSeriesRenderer varosRenderer;
        private XYSeriesRenderer tempRenderer;
        private XYSeriesRenderer humRenderer;

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static SmsChart newInstance(int sectionNumber) {
            SmsChart fragment = new SmsChart();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public SmsChart() {

        }

        View rootView;



        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_sms_chart, container, false);
            Bundle args = getArguments();

            this.rootView = rootView;

            LinearLayout layout = (LinearLayout) this.rootView.findViewById(R.id.chart);
            if (myChart == null) {
                initChart();
                addSampleData();
                myChart = ChartFactory.getCubeLineChartView(getActivity().getApplicationContext(), mDataset, mRenderer, 0.3f);
                layout.addView(myChart);
            } else {
                myChart.repaint();
            }


            return rootView;
        }

        private void initChart() {
            shmeiaVarous = new XYSeries("Βάρος");
            shmeiaTemp = new XYSeries("Θερμοκρασια");
            shmeiaHum = new XYSeries("Υγρασία");


            mDataset.addSeries(shmeiaVarous);
            mDataset.addSeries(shmeiaTemp);
            mDataset.addSeries(shmeiaHum);

            varosRenderer = new XYSeriesRenderer();
            varosRenderer.setColor(Color.RED);
            //varosRenderer.setFillPoints(true);
            varosRenderer.setLineWidth(3);
            //varosRenderer.setDisplayChartValues(true);



            mRenderer.addSeriesRenderer(varosRenderer);

            tempRenderer = new XYSeriesRenderer();
            tempRenderer.setColor(Color.CYAN);
            tempRenderer.setFillPoints(true);
            tempRenderer.setLineWidth(3);
            //tempRenderer.setDisplayChartValues(true);
            mRenderer.addSeriesRenderer(tempRenderer);


            humRenderer = new XYSeriesRenderer();
            humRenderer.setColor(Color.GREEN);
            humRenderer.setFillPoints(true);
            humRenderer.setLineWidth(3);
            mRenderer.addSeriesRenderer(humRenderer);

            mRenderer.setChartTitle("Εξέλιξη των τελευταίων 20 ημερών");
            mRenderer.setZoomEnabled(true);
        }

        int times = 0;

        private void addSampleData() {

            if(myListSms == null)
                times = 0;
            else {
                times = (myListSms.size() > 20) ? 20 : myListSms.size();
                System.out.println("size = "+myListSms.size());
            }

            /*
            for(int i = 0 ; i < times; i++){
                shmeiaVarous.add(i, myListSms.get(i).getWeigth());
                shmeiaTemp.add(i, myListSms.get(i).getTemperature());
                shmeiaHum.add(i, myListSms.get(i).getHumidity());
            }
            */
            times--;
            for(int i = 0 ; i <= times; i++){
                shmeiaVarous.add(i, myListSms.get(times-i).getWeigth());
                shmeiaTemp.add(i, myListSms.get(times-i).getTemperature());
                shmeiaHum.add(i, myListSms.get(times-i).getHumidity());
            }

        }

        public void refresh() {
            System.out.println("refresh is called!!. size = "+myListSms.size());
            shmeiaVarous.clear();
            shmeiaTemp.clear();
            shmeiaHum.clear();
            addSampleData();

        }
    }
}
