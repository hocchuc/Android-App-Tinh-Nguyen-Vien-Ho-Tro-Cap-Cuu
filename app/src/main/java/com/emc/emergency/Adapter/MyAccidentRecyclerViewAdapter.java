package com.emc.emergency.Adapter;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.emc.emergency.Fragment.fragment_accident_page;
import com.emc.emergency.R;
import com.emc.emergency.model.Accident;
import com.emc.emergency.model.MessageEvent;
import com.emc.emergency.utils.SystemUtils;
import com.google.gson.Gson;


import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link } and makes a call to the
 * specified {@link fragment_accident_page.OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyAccidentRecyclerViewAdapter extends RecyclerView.Adapter<MyAccidentRecyclerViewAdapter.ViewHolder> {
    private final Context context;
    private final List<Accident> mValues;
    private final fragment_accident_page.OnListFragmentInteractionListener mListener;

    public MyAccidentRecyclerViewAdapter(Context context, List<Accident> items, fragment_accident_page.OnListFragmentInteractionListener listener) {
        this.context = context;
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_list_accident, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.txtDes.setText(mValues.get(position).getDescription_AC());
        holder.txtStatus.setText(mValues.get(position).getStatus_AC());
//        holder.txtLong_Ac.setText((mValues.get(position).getLong_AC().toString()));
//        holder.txtLat_Ac.setText((mValues.get(position).getLat_AC().toString()));
        holder.txtDate.setText(mValues.get(position).getDate_AC());
        holder.txtAdress.setText(mValues.get(position).getAddress());
        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, mValues.get(position).getDescription_AC(), Toast.LENGTH_SHORT).show();

                EventBus.getDefault().post(new MessageEvent(SystemUtils.TAG_GO_MAP, new Gson().toJson(mValues.get(position))));
            }
        });
        holder.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new MessageEvent(SystemUtils.TAG_GO_MESSAGE, mValues.get(position).getDescription_AC()));
            }
        });
        // request cho glide
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.marker)
                .error(R.drawable.material_drawer_circle_mask)
                .priority(Priority.HIGH);
        Glide.with(context)
                .load("https://maps.googleapis.com/maps/api/staticmap?zoom=15&size=640x250&maptype=roadmap&markers=color:red%7Clabel:C%7C" + mValues.get(position).getLong_AC() + "," + mValues.get(position).getLat_AC())
                .apply(options)
                .into(holder.imageButton);

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
        public final View mView;
        public Accident mItem;
        public final TextView txtDes;
        public final TextView txtStatus;
//        public final TextView txtLong_Ac;
//        public final TextView txtLat_Ac;
        public final TextView txtDate;
        public final ImageButton imageButton;
        public final FloatingActionButton floatingActionButton;
        public final TextView txtAdress;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            txtDes = (TextView) view.findViewById(R.id.txtDes_Ac);
            txtStatus = (TextView) view.findViewById(R.id.txtStatus);
//            txtLong_Ac = (TextView) view.findViewById(R.id.txtLong_Ac);
//            txtLat_Ac = (TextView) view.findViewById(R.id.txtLat_Ac);
            txtDate = (TextView) view.findViewById(R.id.txtDate_Ac);
            imageButton = (ImageButton) view.findViewById(R.id.imageButton);
            floatingActionButton = (FloatingActionButton) view.findViewById(R.id.floatingActionButton);
            txtAdress = (TextView) view.findViewById(R.id.txtAddess_Ac);
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}
