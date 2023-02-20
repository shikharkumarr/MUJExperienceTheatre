package Adapter;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aliferous.mujtheatrebooking.R;

import java.util.List;

import Model.Ticket_Model;

public class Ticket_Adapter extends RecyclerView.Adapter<Ticket_Adapter.ViewHolder>{
    private Context mContext;
    private List<Ticket_Model> mUsers;



    public Ticket_Adapter(Context mContext, List<Ticket_Model> mUsers, boolean b) {
        this.mUsers = mUsers;
        this.mContext = mContext;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.ticket_item,parent,false);
        return new Ticket_Adapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {


        final Ticket_Model ticket_item = mUsers.get(position);
        Log.i(TAG, "onBindViewHolder: "+position);


        holder.ticket_bookingid.setText(ticket_item.getId());
        holder.ticket_name.setText(ticket_item.getName());
        holder.ticket_datetime.setText(ticket_item.getDate());
//holder.onclick etc



    }
    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView ticket_bookingid,ticket_name,ticket_datetime;
        public Button ticket_delete;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ticket_bookingid = itemView.findViewById(R.id.item_booking);
            ticket_name = itemView.findViewById(R.id.item_name);
            ticket_datetime = itemView.findViewById(R.id.item_datetime);
            ticket_delete = itemView.findViewById(R.id.item_delete);


        }
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

}