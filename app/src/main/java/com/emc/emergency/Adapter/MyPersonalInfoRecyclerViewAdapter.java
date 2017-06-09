package com.emc.emergency.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;



import com.emc.emergency.Fragment.fragment_personal_info_page;
import com.emc.emergency.R;
import com.emc.emergency.model.Personal_Infomation;
import java.util.ArrayList;

/**
 * {@link RecyclerView.Adapter} that can display a {@link } and makes a call to the
 * specified {@link fragment_accident_page.OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyPersonalInfoRecyclerViewAdapter extends RecyclerView.Adapter<MyPersonalInfoRecyclerViewAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<Personal_Infomation> mValues;
    private final fragment_personal_info_page.OnListFragmentInteractionListener mListener;

    public MyPersonalInfoRecyclerViewAdapter(Context context, ArrayList<Personal_Infomation> items, fragment_personal_info_page.OnListFragmentInteractionListener listener) {
        this.context = context;
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_list_pi, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.txtNamePI.setText(mValues.get(position).getName_PI());
        holder.txtEmailPI.setText(mValues.get(position).getEmail_PI());
        holder.txtPhonePI.setText((mValues.get(position).getPhone_PI().toString()));
        holder.txtWKPI.setText((mValues.get(position).getWork_location().toString()));
        holder.txtAddressPI.setText(mValues.get(position).getAddress_PI());
        holder.txtBirthdayPI.setText(mValues.get(position).getBirthday());
        holder.txtPID.setText(mValues.get(position).getPersonal_id().toString());
        if(mValues.get(position).getSex__PI()==true){
            holder.radMale.toggle();
        }
        else holder.radFeMale.toggle();
        holder.imgV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


//        RequestOptions options = new RequestOptions()
//                .centerCrop()
//                .placeholder(R.drawable.material_drawer_circle_mask)
//                .error(R.drawable.material_drawer_circle_mask)
//                .priority(Priority.HIGH);
//        Glide.with(context)
//                .load("https://maps.googleapis.com/maps/api/staticmap?zoom=15&size=640x250&maptype=roadmap&markers=color:red%7Clabel:C%7C" + mValues.get(position).getLong_AC() + "," + mValues.get(position).getLat_AC())
//                .apply(options)
//                .into(holder.imageButton);

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
        public Personal_Infomation mItem;

        public final EditText txtNamePI;
        public final EditText txtEmailPI;
        public final EditText txtBirthdayPI;
        public final EditText txtPID;
        public final EditText txtWKPI;
        public final EditText txtPhonePI;
        public final EditText txtAddressPI;
        public final ImageView imgV;

        public final RadioButton radMale;
        public final RadioButton radFeMale;

        public final Button btnEdit;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            txtNamePI = (EditText) view.findViewById(R.id.txtNamePI);
            txtEmailPI= (EditText) view.findViewById(R.id.txtEmailPI);
            txtBirthdayPI= (EditText) view.findViewById(R.id.txtBirthdayPI);
            txtPID= (EditText) view.findViewById(R.id.txtPI_ID);
            txtAddressPI= (EditText) view.findViewById(R.id.txtAddressPI);
            txtWKPI= (EditText) view.findViewById(R.id.txtWorkLocationPI);
            txtPhonePI= (EditText) view.findViewById(R.id.txtPhonePI);
            radFeMale= (RadioButton) view.findViewById(R.id.radFeMale);
            radMale= (RadioButton) view.findViewById(R.id.radMale);
            imgV = (ImageView) view.findViewById(R.id.imageItemHinh);
            btnEdit= (Button) view.findViewById(R.id.btnEditPI);

        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}
