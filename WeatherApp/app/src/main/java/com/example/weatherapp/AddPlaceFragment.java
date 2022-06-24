package com.example.weatherapp;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.ListView;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.weatherapp.Database.DBManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddPlaceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class AddPlaceFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    List<Address> listAddress;
    SearchView searchView;
    ListView listView;
    public static int dem = 0;
    ArrayList<String> list;
    ArrayAdapter<String > adapter;

    public AddPlaceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddPlaceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddPlaceFragment newInstance(String param1, String param2) {
        AddPlaceFragment fragment = new AddPlaceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_place, container, false);

        searchView = (SearchView) view.findViewById(R.id.search);
        listView = (ListView) view.findViewById(R.id.listview);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                /*if(list.contains(query)){
                    Log.e("fuck","you");

                }else{
                    Toast.makeText(view.getContext(), "No Match found",Toast.LENGTH_LONG).show();
                }*/
                new AddressTask(view,listView).execute(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                new AddressTask(view,listView).execute(newText);
                return false;
            }
        });
        return view;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

class AddressTask extends AsyncTask<String,Void,String>{
    View view;
    ListView listView;
    public AddressTask(View _v, ListView _l){
        listView = _l;
        view = _v;
    }
    @Override
    protected void onPreExecute(){
        ListView listView = (ListView) view.findViewById(R.id.listview);
    }
    protected String doInBackground(String... args){
        String response = HttpRequest.excuteGet("https://geocode.maps.co/search?q="+args[0]+"&limit=1");
        if(response == null) {Log.e("response", "không có giá trị");}
        else{
            Log.e("response",response);
        }
        return response;
    }
    @Override
    protected void onPostExecute(String result){
        try {
            ArrayAdapter<String > adapter;
            ArrayList<String> list = new ArrayList<String>();
           JSONArray arr = new JSONArray(result);
           for(int i = 0; i<arr.length();i++){
               list.add(arr.getJSONObject(i).getString("display_name"));
           }
            adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_1,list);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    try {
                        String name = arr.getJSONObject(i).getString("display_name");
                        double lat = arr.getJSONObject(i).getDouble("lat");
                        double lon = arr.getJSONObject(i).getDouble("lon");
                        Address newAdd = new Address(name,lat,lon);
                        MainActivity.app.dbManager.add(newAdd);
                        List<Address> test = MainActivity.app.dbManager.getAll();
                        Log.e("database",test.get(0).getName());

                    }catch(Exception e){
                        Log.e("error",e.toString());
                    }
                }
            });
        }catch (Exception e){
            Log.e("error",e.toString());
        }
    }
}