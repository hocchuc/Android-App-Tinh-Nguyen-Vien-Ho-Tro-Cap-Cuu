package com.emc.emergency.Adapter;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.emc.emergency.Fragment.fragment_accident_page;
import com.emc.emergency.Fragment.fragment_medical_info_page;
import com.emc.emergency.R;
import com.emc.emergency.model.Accident;
import com.emc.emergency.model.Medical_Info;
import com.emc.emergency.model.MessageEvent;
import com.emc.emergency.utils.SystemUtils;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link } and makes a call to the
 * specified {@link fragment_accident_page.OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyMedicalInfoRecyclerViewAdapter extends RecyclerView.Adapter<MyMedicalInfoRecyclerViewAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<Medical_Info> mValues;
    private final fragment_medical_info_page.OnListFragmentInteractionListener mListener;

    public MyMedicalInfoRecyclerViewAdapter(Context context, ArrayList<Medical_Info> items, fragment_medical_info_page.OnListFragmentInteractionListener listener) {
        this.context = context;
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_list_medical_info, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.txtNameMI.setText(mValues.get(position).getName_MI());
        holder.txtTypeMI.setText(mValues.get(position).getType_MI().toString());
        holder.txDesMI.setText(mValues.get(position).getDescriptionMI());
        holder.imgMI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public  View mView;
        public Medical_Info mItem;
        public final TextView txtNameMI;
        public final TextView txtTypeMI;
        public final TextView txDesMI;
        public final ImageView imgMI;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            txtNameMI = (TextView) view.findViewById(R.id.txtNameMI);
            txDesMI = (TextView) view.findViewById(R.id.txtDesMI);
            txtTypeMI = (TextView) view.findViewById(R.id.txtTypeMI);
            imgMI = (ImageView) view.findViewById(R.id.imageButtonMI);
        }
    }
}
