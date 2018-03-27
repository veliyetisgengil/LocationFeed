package my.gpsTracking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import my.gpsTracking.R;

public class MainActivity extends AppCompatActivity {
    private Button log_out,search,friend;
    private EditText editText;
    TextView text1,text2;
    String getUrl="http://locationfeed.info/webservice/login/getLocation.php";
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.search);
        search = (Button)findViewById(R.id.search_btn);
      /*  log_out = (Button) findViewById(R.id.logout);*/
        friend =(Button) findViewById(R.id.friend);
        text1 = (TextView) findViewById(R.id.textView3);
        text2 = (TextView) findViewById(R.id.textView);

        requestQueue = Volley.newRequestQueue(getApplicationContext());


        friend.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        startActivity(new Intent(getApplicationContext(),addFriend.class));

    }
});
       /* log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }
        });*/

        search.setOnClickListener(new View.OnClickListener(){

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
                                String a =   editText.getText().toString();
                                String b = student.getString("deviceID");
                                if(a.equals(b)){
                                    Intent en = new Intent(getApplicationContext(), MapsActivity.class);

                                    String enlem = student.getString("enlem");
                                    String boylam = student.getString("boylam");
                                    en.putExtra("enlem",enlem);
                                    en.putExtra("boylam",boylam);
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


                // startActivity(new Intent(getApplicationContext(),MapsActivity.class));
            }
        });


    }
}
