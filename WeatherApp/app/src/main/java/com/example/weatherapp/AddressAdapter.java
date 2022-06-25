package com.example.weatherapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class AddressAdapter extends ArrayAdapter<Address> {
    public ListView listViewAddress;
    private Context context;
    public List<Address> addressList;
    public AddressAdapter(Context context, List<Address> list, ListView _a) {
        super(context, R.layout.activity_list_address, list);
        this.context = context;
        this.addressList = list;
        this.listViewAddress = _a;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_list_address, parent, false);
        ImageView imgDelete = (ImageView) view.findViewById(R.id.imgDelete);
        imgDelete.setTag(position);
        Address address = addressList.get(position);

        imgDelete.setOnClickListener(view1 -> {
            MainActivity.app.dbManager. delete(address.getName());
            List<Address> listAddress = MainActivity.app.dbManager.getAll();
            AddressAdapter adapter1 = new AddressAdapter(view.getContext(),listAddress, listViewAddress);
            listViewAddress.setAdapter(adapter1);
        });
        TextView nameAddress = (TextView) view.findViewById(R.id.addressName);
        nameAddress.setText(address.getName());
        view.setTag(position); // setting the 'row' id to the id of the donation
        return view;
    }
}
