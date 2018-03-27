package my.gpsTracking;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import my.gpsTracking.R;

public class onayFriend extends AppCompatActivity {


    RequestQueue requestQueue;
    String getUrl ="http://locationfeed.info/webservice/login/getOnay.php";
    String insertUrl = "http://locationfeed.info/webservice/login/insertRelation1.php";
    String deviceID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onay_friend);
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        final ListView listview=(ListView)findViewById(R.id.list);

        final ArrayList<String> ki=new ArrayList<String>();
        final ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,ki);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,getUrl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response.toString());
                try {

                    JSONArray students = response.getJSONArray("students");
                    for (int i = 0; i < students.length(); i++) {
                        JSONObject student = students.getJSONObject(i);


                        deviceID=student.getString("deviceID");
                        String id=LoginActivity.userid;
                        if(id.equals(deviceID)){

                          final String user = student.getString("mydeviceID");


                            ki.add(user);
                            listview.setAdapter(adapter);

                        }

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.append(error.getMessage());

            }
        });
        requestQueue.add(jsonObjectRequest);









  listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view,final int i, long l) {

            AlertDialog.Builder builder = new AlertDialog.Builder(onayFriend.this);
            builder.setTitle("LocationFeed");
            builder.setMessage("Kullanıcı isteği kabul edilsin mi?");
            builder.setNegativeButton("İPTAL", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    //İptal butonuna basılınca yapılacaklar.Sadece kapanması isteniyorsa boş bırakılacak

                }
            });


            builder.setPositiveButton("TAMAM", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    //Tamam butonuna basılınca yapılacaklar
                    final String onay = ki.get(i);
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
                            parameters.put("deviceID", deviceID);
                            parameters.put("mydeviceID", onay);
                            parameters.put("onay", "1");
                            return parameters;

                        }
                    };
                    requestQueue.add(request);

                }
            });


            builder.show();

        }



});









    }

}
