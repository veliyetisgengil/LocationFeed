package my.gpsTracking;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import my.gpsTracking.R;

public class eklenenFriend extends AppCompatActivity {


    String getUrl = "http://locationfeed.info/webservice/login/getUserFriend.php";
    RequestQueue requestQueue;
    String deviceID;
    String getUrl2 = "http://locationfeed.info/webservice/login/getLocation.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eklenen_friend);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        final ListView listview = (ListView) findViewById(R.id.eklenen);

        final ArrayList<String> kis = new ArrayList<String>();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, kis);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, getUrl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response.toString());
                try {

                    JSONArray students = response.getJSONArray("students");
                    for (int i = 0; i < students.length(); i++) {
                        JSONObject student = students.getJSONObject(i);

                        deviceID = student.getString("deviceID");
                        String id = LoginActivity.userid;

                        if (id.equals(deviceID)) {
                            String user = student.getString("mydeviceID");

                            kis.add(user);
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
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {

                AlertDialog.Builder builder = new AlertDialog.Builder(eklenenFriend.this);
                builder.setTitle("LocationFeed");
                builder.setMessage("Kullanıcı konumuna gidilsin mi?");
                builder.setNegativeButton("İPTAL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        //İptal butonuna basılınca yapılacaklar.Sadece kapanması isteniyorsa boş bırakılacak

                    }
                });


                builder.setPositiveButton("GİT", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Tamam butonuna basılınca yapılacaklar


                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, getUrl2, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                System.out.println(response.toString());
                                try {

                                    JSONArray students = response.getJSONArray("students");
                                    for (int i = 0; i < students.length(); i++) {
                                        JSONObject student = students.getJSONObject(i);
                                        String a = kis.get(position);
                                        String b = student.getString("id");
                                        if (a.equals(b)) {
                                            Intent en = new Intent(getApplicationContext(), MapsActivity.class);

                                            String enlem = student.getString("enlem");
                                            String boylam = student.getString("boylam");
                                            en.putExtra("enlem", enlem);
                                            en.putExtra("boylam", boylam);
                                            startActivity(en);


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


                        Toast.makeText(getApplicationContext(), "" + "GİDİLİYOR... ", Toast.LENGTH_SHORT).show();

                    }
                });


                builder.show();

            }
        });

    }
}
