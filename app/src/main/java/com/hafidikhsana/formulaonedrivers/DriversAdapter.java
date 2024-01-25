package com.hafidikhsana.formulaonedrivers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DriversAdapter extends RecyclerView.Adapter<DriversAdapter.DriverViewHolder> {

    private List<Drivers> drivers;
    private OnClickListener onClickListener;

    public DriversAdapter(List<Drivers> drivers) {
        this.drivers = drivers;
    }

    @NonNull
    @Override
    public DriverViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_list_item, parent, false);
        return new DriverViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DriverViewHolder holder, int position) {
        holder.bind(drivers.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickListener != null) {
                    onClickListener.onClick(position, drivers.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return drivers.size();
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(int position, Drivers model);
    }

    static class DriverViewHolder extends RecyclerView.ViewHolder {

        private TextView driverName;
        private TextView driverNumber;
        private TextView driverAcronym;
        private TextView driverTeam;

        public DriverViewHolder(@NonNull View itemView) {
            super(itemView);
            driverName = itemView.findViewById(R.id.driver_name);
            driverNumber = itemView.findViewById(R.id.driver_number);
            driverAcronym = itemView.findViewById(R.id.driver_acronym);
            driverTeam = itemView.findViewById(R.id.driver_team);
        }

        public void bind(Drivers drivers) {
            driverName.setText(drivers.getFullName());
            String number = Integer.toString(drivers.getDriverNumber());
            driverNumber.setText(number);
            driverTeam.setText(drivers.getTeamName());
            String acronym = "(" + drivers.getNameAcronym() + ")";
            driverAcronym.setText(acronym);
        }
    }
}
