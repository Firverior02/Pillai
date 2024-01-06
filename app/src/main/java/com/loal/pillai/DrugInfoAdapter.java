package com.loal.pillai;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DrugInfoAdapter extends RecyclerView.Adapter<DrugInfoAdapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private List<String> titles;
    private List<Boolean> selectedOptions;

    /**
     * Adapter for different information on drugs
     * @param context The current context
     * @param titles Titles for information types
     * @param selectedOptions Which information has been selected
     */
    public DrugInfoAdapter(Context context, List<String> titles, List<Boolean> selectedOptions) {
        this.layoutInflater = LayoutInflater.from(context);
        this.titles = titles;
        this.selectedOptions = selectedOptions;
    }

    /**
     * Store the filtered list based on the selected options
     * @param titles Titles to be saved
     * @param selectedOptions Options to be saved
     */
    public void setFilteredList(ArrayList<String> titles, ArrayList<Boolean> selectedOptions) {
        this.titles = titles;
        this.selectedOptions = selectedOptions;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view holder (instance of a drug info card) is created
        View newView = layoutInflater.inflate(R.layout.drug_info_card, parent, false);
        return new ViewHolder(newView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Set title of each card
        String title = titles.get(position);
        holder.info_label.setText(title);

        // Set on click action
        holder.card_background.setOnClickListener(view -> {
            // Toggle selected state
            selectedOptions.set(position, !selectedOptions.get(position));

            // Set correct color
            int color;
            if(selectedOptions.get(position)) {
                color = ContextCompat.getColor(holder.itemView.getContext(), R.color.selected);
            } else {
                color = ContextCompat.getColor(holder.itemView.getContext(), R.color.deselected);
            }

            holder.card_background.setCardBackgroundColor(color);
        });
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    // Class to describe the contents of the view holder (drug info card)
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView info_label;
        CardView card_background;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Find elements
            info_label = itemView.findViewById(R.id.cardText);
            card_background = itemView.findViewById(R.id.cardBackground);
        }
    }
}
