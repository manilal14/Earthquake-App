package com.example.mani.earthquake;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = MainActivity.class.getName();
    private  Context mCtx = MainActivity.this;

    public static final String URL = "https://earthquake.usgs.gov/" +
            "fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=3&limit=30";

    List<Earthquake> mEarthquakeList;
    RecyclerView mMainRecycleView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    TextView mErrorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.e(LOG_TAG,"Oncreate is called");

        mEarthquakeList = new ArrayList<>();
        mMainRecycleView = findViewById(R.id.recycle_view);
        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);

        mSwipeRefreshLayout.setColorSchemeColors(Color.argb(0,10,81,89));
        mErrorLayout = findViewById(R.id.error_layout);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchEarthquake();
                mErrorLayout.setVisibility(View.GONE);
            }
        });

        fetchEarthquake();
    }

    private void fetchEarthquake()
    {
        mSwipeRefreshLayout.setRefreshing(true);
        mMainRecycleView.setVisibility(View.GONE);

        Log.e(LOG_TAG,"Fetch Earthquake");


        StringRequest stringRequest = new StringRequest(Request.Method.GET,URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        Log.e(LOG_TAG,"OnResponse");
                        //Log.i(LOG_TAG,response);
                        try {
                            JSONObject baseJsonResponse = new JSONObject(response);
                            JSONArray featureArray = baseJsonResponse.getJSONArray("features");

                            if(featureArray.length() == 0) {
                                mMainRecycleView.setVisibility(View.GONE);
                                mErrorLayout.setVisibility(View.VISIBLE);
                                mErrorLayout.setText("No Earthqauake hit recently");

                                return;


                            }
                            if (featureArray.length() > 0) {

                                for (int i =0;i<featureArray.length();i++){

                                    JSONObject firstFeature = featureArray.getJSONObject(i);

                                    JSONObject properties = firstFeature.getJSONObject("properties");
                                    String time = properties.getString("time");
                                    String mag = properties.getString("mag");
                                    String place = properties.getString("place");

                                    //Log.e(LOG_TAG,mag+" "+place+" "+time);

                                    mEarthquakeList.add(new Earthquake(mag,place,time));
                                }
                            }


                            EarthquakeAdapter adapter = new EarthquakeAdapter(mCtx,mEarthquakeList);
                            mMainRecycleView.setLayoutManager(new LinearLayoutManager(mCtx));
                            mMainRecycleView.setAdapter(adapter);

                            mSwipeRefreshLayout.setRefreshing(false);
                            mMainRecycleView.setVisibility(View.VISIBLE);



                        } catch (JSONException e) {
                            Log.e(LOG_TAG,e.toString());
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(LOG_TAG,"Volley Error : "+error);
                mSwipeRefreshLayout.setRefreshing(false);
                mErrorLayout.setVisibility(View.VISIBLE);

                mErrorLayout.setText(error.getMessage());

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Log.e(LOG_TAG,"Params are send");
                Map<String,String> params = new HashMap<>();

                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy( (5*1000),1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Log.e(LOG_TAG,"String Request is added to the queue");
        MySingleton.getInstance(MainActivity.this).addToRequestQueue(stringRequest);

    }
}
