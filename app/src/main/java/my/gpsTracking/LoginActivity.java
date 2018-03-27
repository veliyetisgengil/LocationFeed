package my.gpsTracking;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import my.gpsTracking.R;


public class LoginActivity extends Activity {

    private EditText email,password;
    private Button sign_in_register;
    private RequestQueue requestQueue;
    private static final String URL = "http://locationfeed.info/webservice/login/user_control.php";
    private StringRequest request;
    Switch onay;
    boolean onay_btn;
    public  static String userid;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText) findViewById(R.id.imei);
        password = (EditText) findViewById(R.id.password);
        sign_in_register = (Button) findViewById(R.id.button);
        onay = (Switch) findViewById(R.id.onay);
        onay.setChecked(false);
        requestQueue = Volley.newRequestQueue(this);

        Intent en = new Intent(getApplicationContext(), Main2Activity.class);
        startActivity(en);


        onay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){

                    onay_btn = true;
                }else{
                    onay_btn = false;
                }
            }
        });
        sign_in_register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

if(onay_btn==true){




                request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try { userid=email.getText().toString();

                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.names().get(0).equals("success")) {
                                Toast.makeText(getApplicationContext(), " " + jsonObject.getString("success"), Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));

                            } else {
                                Toast.makeText(getApplicationContext(), "" + jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> hashMap = new HashMap<String, String>();
                        hashMap.put("deviceID", email.getText().toString());
                        hashMap.put("password", password.getText().toString());

                        return hashMap;
                    }
                };

                requestQueue.add(request);

            }else{
    Toast.makeText(getApplicationContext(), "" + "Konum Paylaşmalısınız." , Toast.LENGTH_SHORT).show();


}
            }
        });


    }


}


