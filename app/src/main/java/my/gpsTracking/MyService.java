package my.gpsTracking;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MyService extends Service {
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    private LocationListener listener;
    private LocationManager locationManager;
    RequestQueue requestQueue ;
    String insertUrl = "http://locationfeed.info/webservice/login/insertLocation.php";
    int time;


    @Override
    public void onCreate() {
        //imei no get *****************************
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        final String a= telephonyManager.getDeviceId();
        //imei no get *****************************



        //System.out.println("**********************************************************"+df);

        requestQueue= Volley.newRequestQueue(getApplicationContext());
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //tarihh *************************
                DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                final String date = df.format(Calendar.getInstance().getTime());
                //tarihh *************************
                Intent i = new Intent("location_update");
                i.putExtra("coordinates",location.getLongitude()+" "+location.getLatitude());
                sendBroadcast(i);
                final   double lat =  location.getLatitude();
                final double lng =    location.getLongitude();

                final String latitu = " "+location.getLatitude()+"" ;
                final String longi = " "+location.getLongitude()+"" ;
                final String id=LoginActivity.userid;
                time++;
                if(time==50) {
                    time=0;
                    StringRequest request = new StringRequest(Request.Method.POST, insertUrl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            System.out.println(response.toString());
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {

                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> parameters = new HashMap<String, String>();
                            parameters.put("id",id);
                            parameters.put("deviceID", a);
                            parameters.put("enlem", latitu);
                            parameters.put("boylam", longi);
                            parameters.put("tarih", date);
                            return parameters;
                        }
                    };
                    requestQueue.add(request);
                }
            }


            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }


        };
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        try {
            locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, listener);
        } catch (SecurityException e) {
            System.out.println(e);
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(locationManager != null){
            //noinspection MissingPermission
            locationManager.removeUpdates(listener);
        }
    }

}
