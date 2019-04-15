package com.example.parseweatherdata_1textview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class MainActivity extends AppCompatActivity {
    private TextView mTextViewResult;
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextViewResult = findViewById(R.id.result_textView);                                           // use 1 textView
        Button parse_button = findViewById(R.id.parse_button);                                          // and 1 button

        mQueue = Volley.newRequestQueue(this);

        parse_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jsonParse();
            }
        });
    }

    private void jsonParse(){
        String url = "http://api.openweathermap.org/data/2.5/weather?q=athens,gr&appid=1bc595f73d94c0a78f5aedf47f0c6cb4&units=metric";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            //              *** for city ***
                            String city = response.getString("name");

                            //              *** for country ***
                            JSONObject sys_object = response.getJSONObject("sys");
                            String country = sys_object.getString("country");

                            //              *** for temperature ***
                            JSONObject main_object = response.getJSONObject("main");
                            String temp = String.valueOf(main_object.getDouble("temp"));

                            //              *** for description ***
                            JSONArray weather_array = response.getJSONArray("weather");
                            JSONObject weatherObj = weather_array.getJSONObject(0);                         // 0 for 1st element in array
                            String description = weatherObj.getString("description");

                            mTextViewResult.append(city+", "+country+"\n"+temp+" C\n"+"\""+description+"\"\n\n");          // print C for Celsius scale
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }
}