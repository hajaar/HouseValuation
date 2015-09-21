package com.houseevaluation.kartikn.housevaluation;

/**
 * Created by kartikn on 21-09-2015.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class YearlyLedgerDetailsAdapter extends RecyclerView.Adapter<YearlyLedgerDetailsAdapter.ViewHolder> {
    private String[] mDataset;

    // Provide a suitable constructor (depends on the kind of dataset)
    public YearlyLedgerDetailsAdapter(String[] myDataset) {
        mDataset = myDataset;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public YearlyLedgerDetailsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                    int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_yearly_ledger_details, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final String yearlyLedger = mDataset[position].trim();
        String[] temp = mDataset[position].split(",");
        if (temp.length > 1) {
            holder.txtYear.setText(temp[1].trim());
            holder.txtPrincipal.setText(temp[2].trim());
            holder.txtInterest.setText(temp[3].trim());
            holder.txtRentalStatus.setText(temp[4].trim());
            holder.txtRent.setText(temp[5].trim());
            holder.txtTax.setText(temp[6].trim());
            holder.txtOutflow.setText(temp[7].trim());
            holder.txtNotation.setText(temp[8].trim());
        }


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView txtID;
        public TextView txtYear;
        public TextView txtPrincipal;
        public TextView txtInterest;
        public TextView txtRent;
        public TextView txtTax;
        public TextView txtOutflow;
        public TextView txtRentalStatus;
        public TextView txtNotation;

        public ViewHolder(View v) {
            super(v);
            txtYear = (TextView) v.findViewById(R.id.yearly_ledger_year);
            txtPrincipal = (TextView) v.findViewById(R.id.yearly_ledger_principal);
            txtInterest = (TextView) v.findViewById(R.id.yearly_ledger_interest);
            txtRent = (TextView) v.findViewById(R.id.yearly_ledger_rent);
            txtTax = (TextView) v.findViewById(R.id.yearly_ledger_tax);
            txtOutflow = (TextView) v.findViewById(R.id.yearly_ledger_outflow);
            txtRentalStatus = (TextView) v.findViewById(R.id.yearly_ledger_status);
            txtNotation = (TextView) v.findViewById(R.id.yearly_ledger_notation);


        }
    }


}
