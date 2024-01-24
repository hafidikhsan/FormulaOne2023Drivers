package com.hafidikhsana.formulaonedrivers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.List;

public class DriverListAdapter extends ArrayAdapter<Drivers> {

    public DriverListAdapter(Context context, List<Drivers> drivers) {
        super(context, 0, drivers);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_list_item, parent, false);
        }

        Drivers driver = getItem(position);

        TextView driverName = convertView.findViewById(R.id.driver_name);
        TextView driverTeam = convertView.findViewById(R.id.driver_team);
        TextView driverAcronym = convertView.findViewById(R.id.driver_acronym);

        driverName.setText(driver.getFullName());
        driverTeam.setText(driver.getTeamName());
        driverAcronym.setText(driver.getNameAcronym());

        return convertView;
    }
}
