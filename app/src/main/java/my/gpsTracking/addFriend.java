package my.gpsTracking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

import java.util.HashMap;
import java.util.Map;



public class addFriend extends AppCompatActivity {
    private Button add_btn,onayButon,gosterButon;
    private EditText editText;
    String getUrl="http://locationfeed.info/webservice/login/getUser.php";
    String insertUrl = "http://locationfeed.info/webservice/login/insertRelation.php";
    RequestQueue requestQueue;
   static String deviceID;
  static   String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        add_btn = (Button)findViewById(R.id.addfriend);
        onayButon = (Button)findViewById(R.id.onay);
        gosterButon = (Button)findViewById(R.id.mevcut);
        editText=(EditText) findViewById(R.id.add);
        requestQueue = Volley.newRequestQueue(getApplicationContext());

onayButon.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
         startActivity(new Intent(getApplicationContext(),onayFriend.class));

    }
});
gosterButon.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
         startActivity(new Intent(getApplicationContext(),eklenenFriend.class));

    }
});

        add_btn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,getUrl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response.toString());
                try {

                    JSONArray students = response.getJSONArray("students");
                    for (int i = 0; i < students.length(); i++) {
                        JSONObject student = students.getJSONObject(i);
                      final  String a =   editText.getText().toString();
                        String b = student.getString("deviceID");
/*ARKADAS KONTROL*/
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,getUrl, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                System.out.println(response.toString());
                                try {

                                    JSONArray students = response.getJSONArray("students");
                                    for (int i = 0; i < students.length(); i++) {
                                        JSONObject student = students.getJSONObject(i);


                                        deviceID=student.getString("deviceID");
                                        user = student.getString("mydeviceID");


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

                        if(a.equals(b) && deviceID!=a && user!=null){

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
                                    parameters.put("deviceID", a);
                                    parameters.put("mydeviceID",LoginActivity.userid);
                                    parameters.put("onay","0");
                                    return parameters;
                                }
                            };
                            requestQueue.add(request);

                            Toast.makeText(getApplicationContext(), "" + "İstek Gönderildi... " , Toast.LENGTH_SHORT).show();
                            return;

                        }else{
                            Toast.makeText(getApplicationContext(), "" + "Kullanıcı Adı Aranıyor... "  , Toast.LENGTH_SHORT).show();

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


        // startActivity(new Intent(getApplicationContext(),MapsActivity.class));
    }

});
    }
}
