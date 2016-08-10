package com.herokuapp.myapplication.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.herokuapp.myapplication.R;
import com.herokuapp.myapplication.dialoginfo.UserInfoActivity;
import com.herokuapp.myapplication.entity.User;
import com.herokuapp.myapplication.tests.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    JSONArray mResponse;
    ArrayList<User> mItems;
    UserAdapter mAdapter;
    ListView mListView;
    private int selected;

    final String ALARM_CHOOSE = "NOTHING TO SHOW, CHOOSE USER! ";
    final String FIELD_NAME = "name";
    final String FIELD_SURNAME = "surname";
    final String FIELD_INFO = "info";
    final String FIELD_IMEI = "imei";
    final String FIELD_MESSAGE= "message";
    final String FIELD_CREATED_AT = "created_at";
    final String FIELD_ID = "id";
    final public static String FIELD_USER = "user";
    final int NOTHING_SELECTED = -1;

    Button btnFetch,btnDetalis;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        selected =NOTHING_SELECTED;
        btnFetch = (Button) findViewById(R.id.btnFetch);
        btnDetalis = (Button) findViewById(R.id.btnDetails);
        btnFetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeListRequest();
            }
        });

        btnDetalis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selected==NOTHING_SELECTED){
                    Toast.makeText(
                            MainActivity.this,
                            ALARM_CHOOSE
                            ,
                            Toast.LENGTH_LONG
                    ).show();
                }else{
                    makeDetailsRequest(((User) mAdapter.getItem(selected)).getUserId());
                }

            }
        });


        if(checkFirstLoad()){
            makeFirstRequest();
        }
    }

    boolean checkFirstLoad(){
        SharedPreferences preference = getSharedPreferences("test_app", MODE_PRIVATE);
        return preference.getBoolean("first_load", true);
    }
    void setFirstLoad(boolean serverResponse){
        SharedPreferences preference = getSharedPreferences("test_app", MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        editor.putBoolean("first_load", !serverResponse);
        editor.commit();
    }

    void recreateUserList() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    initializeListUsers();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pullListUsers();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        thread.start();

    }
    void pullListUsers() {
        if (mItems!=null) {
            setListAdaptor();
        }
    }

    public void initializeListUsers() {
        if (mResponse != null) {
            mListView = (ListView) findViewById(R.id.hero_list_view);
            mItems = new ArrayList<User>();
            for (int i = 0; i < mResponse.length(); i++) {
                User user = new User();
                try {
                    user.setName(mResponse.getJSONObject(i).getString(FIELD_NAME));
                    user.setUserId(mResponse.getJSONObject(i).getInt(FIELD_ID));
                    user.setSurname(mResponse.getJSONObject(i).getString(FIELD_SURNAME));
                } catch (JSONException e) {
                    Log.e(e.toString());
                    e.printStackTrace();
                }
                mItems.add(user);
            }

        }

    }

    void makeListRequest() {
        RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest stringRequest = new JsonArrayRequest("https://obscure-shelf-31484.herokuapp.com/users.json", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                mResponse = response;
                try {
                    recreateUserList();
                }catch (Exception e){
                    Log.e(e);
                    Toast.makeText(
                            MainActivity.this,
                            // "NO RESPONSE FROM SERVER! ",
                            "NOTHING TO SHOW, CHOOSE ENEMIES! ",
                            Toast.LENGTH_LONG
                    ).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.e("mylogs", "ERRRORRRR " + error.toString());
            }
        });
        //Log.d("mylogs", "ON CREATE3");
        stringRequest.setShouldCache(false);
        if (isNetworkAvailable()){
            mRequestQueue.add(stringRequest);
        }else{
            Toast.makeText(
                    MainActivity.this,
                    "NO INTERNET CONNECTION! ",
                    Toast.LENGTH_LONG
            ).show();
        }

    }

    void makeDetailsRequest(int id) {
        RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext());

        JsonObjectRequest stringRequest = new JsonObjectRequest("https://obscure-shelf-31484.herokuapp.com/users/"+id+".json",null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    User user = new User();
                    try {
                        user.setName(response.getString(FIELD_NAME));
                        user.setUserId(response.getInt(FIELD_ID));
                        user.setSurname(response.getString(FIELD_SURNAME));
                        user.setInfo(response.getString(FIELD_INFO));
                        user.setCreated_at(response.getString(FIELD_CREATED_AT));
                        Intent intent = new Intent(MainActivity.this, UserInfoActivity.class);
                        intent.putExtra(FIELD_USER,user);
                        MainActivity.this.startActivity(intent);
                    } catch (JSONException e) {
                        Log.e(e.toString());
                        e.printStackTrace();
                    }

                }catch (Exception e){
                    Log.e(e);
                    Toast.makeText(
                            MainActivity.this,
                            // "NO RESPONSE FROM SERVER! ",
                            "NOTHING TO SHOW, CHOOSE ENEMIES! ",
                            Toast.LENGTH_LONG
                    ).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("mylogs", "ERRRORRRR " + error.toString());
            }
        });
        //Log.d("mylogs", "ON CREATE3");
        stringRequest.setShouldCache(false);
        if (isNetworkAvailable()){
            mRequestQueue.add(stringRequest);
        }else{
            Toast.makeText(
                    MainActivity.this,
                    "NO INTERNET CONNECTION! ",
                    Toast.LENGTH_LONG
            ).show();
        }

    }

    private int mStatusCode = 0;
    void makeFirstRequest() {
        RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        ;
        JSONObject params = new JSONObject();
        try {
            params.put(FIELD_IMEI, telephonyManager.getDeviceId());
            params.put(FIELD_MESSAGE, "hello world");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final JSONObject requests = new JSONObject();
        try {
            requests.put("upload", params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST,"https://obscure-shelf-31484.herokuapp.com/uploads",  new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("mylogs", "1 "+response );
                Log.d("mylogs", "2 "+String.valueOf(mStatusCode));

                try {

                 /* проверить ответ  */
                 /* а пока что false  */
                    setFirstLoad(true);


                }catch (Exception e){
                    Log.e(e);
                    Toast.makeText(
                            MainActivity.this,
                            // "NO RESPONSE FROM SERVER! ",
                            "NOTHING TO SHOW, CHOOSE ENEMIES! ",
                            Toast.LENGTH_LONG
                    ).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("mylogs", "ERRRORRRR " + error.toString());
            }
        }){
            @Override
            public byte[] getBody() throws AuthFailureError {
                return requests.toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                if (response != null) {
                    mStatusCode = response.statusCode;
                }
                return super.parseNetworkResponse(response);
            }
        };
        //Log.d("mylogs", "ON CREATE3");
        stringRequest.setShouldCache(false);
        if (isNetworkAvailable()){
            mRequestQueue.add(stringRequest);
        }else{
            Toast.makeText(
                    MainActivity.this,
                    "NO INTERNET CONNECTION! ",
                    Toast.LENGTH_LONG
            ).show();
        }

    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void setListAdaptor() {
        mAdapter = new UserAdapter(this, mItems);
        if(mListView!=null){
            mListView.setAdapter(mAdapter);
            mListView.setDividerHeight(1);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,long arg3) {
                    for (int j = 0; j < parent.getChildCount(); j++)
                        if(j!=position)
                        parent.getChildAt(j).setSelected(false);
                    selected = position;
                    view.setSelected(!view.isSelected());


                }
            });
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        }
    }
}
