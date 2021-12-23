package com.example.tugas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import com.example.tugas.model.DataOperasiSistem;

public class MainActivity extends AppCompatActivity {
    ArrayList<DataOperasiSistem> dataOperasiSistems = new ArrayList();
    protected final String urlAPI = "https://ewinsutriandi.github.io/mockapi/operating_system.json";
    JSONObject dataOperasiSistem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getDataOperatingSistem();
    }

    void setupListview() {
        ListView listView = findViewById(R.id.listView);
        CustomAdapter adapter = new CustomAdapter(this, dataOperasiSistems);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(onItemClick);
    }

    private AdapterView.OnItemClickListener onItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            DataOperasiSistem fSELECTED = dataOperasiSistems.get(position);
            Toast.makeText(MainActivity.this, fSELECTED.getWeb(), Toast.LENGTH_LONG).show();
            toLink(fSELECTED);
        }
    };


    void getDataOperatingSistem() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, urlAPI, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dataOperasiSistem = response;
                        refresView();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("erorr", error.toString());
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

    void refresView() {
        Iterator<String> key = dataOperasiSistem.keys();
        while(key.hasNext()) {
            String nameOperasiSistem = key.next();
            try {
                JSONObject data = dataOperasiSistem.getJSONObject(nameOperasiSistem);
                String lates_version = data.getString("latest_version");
                String description = data.getString("description");
                String logo_url= data.getString("logo_url");
                String developer= data.getString( "developer");
                String website= data.getString("website");
                String source_model= data.getString("source_model");

                dataOperasiSistems.add(new DataOperasiSistem(nameOperasiSistem,description, lates_version, logo_url, developer, website, source_model));
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }

        setupListview();
    }

    void toLink(DataOperasiSistem data) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse(data.getWeb()));
        startActivity(intent);
    }
    }