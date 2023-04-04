package Adapter;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.aliferous.mujtheatrebooking.R;
import com.aliferous.mujtheatrebooking.ShowTicketActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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



        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        assert firebaseUser != null;
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child("Bookings");
        DatabaseReference booking_reference = FirebaseDatabase.getInstance().getReference("Bookings");

        String DateTime = ""+ticket_item.getDate()+" - "+ticket_item.getTime()+"";

        holder.ticket_bookingid.setText(ticket_item.getId());
        holder.ticket_name.setText(ticket_item.getName());
        holder.ticket_datetime.setText(DateTime);

        int id = Integer.parseInt(ticket_item.getId());


        holder.ticket_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.ticket_layout_main.setVisibility(View.GONE);
                holder.ticket_layout_delete.setVisibility(View.VISIBLE);
            }
        });

        holder.ticket_nodelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.ticket_layout_main.setVisibility(View.VISIBLE);
                holder.ticket_layout_delete.setVisibility(View.GONE);
            }
        });

        holder.ticket_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ShowTicketActivity.class);
                intent.putExtra("BookingID", id);
                mContext.startActivity(intent);
            }
        });



        holder.ticket_yesdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        int noORequests = (int) dataSnapshot.getChildrenCount();
                        String id = ticket_item.getId();

                            if (dataSnapshot.hasChild(""+id)){
                                String checkID = dataSnapshot.child(""+id).child("Id").getValue().toString();
                                if (checkID.equals(id)){
                                    reference.child(""+id).child("Id").removeValue();
                                    mUsers.clear();
                                }
                            }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });

            }
        });



    }
    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView ticket_bookingid,ticket_name,ticket_datetime, ticket_deletetv;
        public Button ticket_delete,ticket_yesdelete,ticket_nodelete,ticket_open;

        public ConstraintLayout ticket_layout_main, ticket_layout_delete;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ticket_bookingid = itemView.findViewById(R.id.item_booking);
            ticket_name = itemView.findViewById(R.id.item_name);
            ticket_datetime = itemView.findViewById(R.id.item_datetime);
            ticket_delete = itemView.findViewById(R.id.item_delete);
            ticket_deletetv = itemView.findViewById(R.id.item_tv_suredelete);
            ticket_nodelete = itemView.findViewById(R.id.item_tv_nodelete);
            ticket_yesdelete = itemView.findViewById(R.id.item_tv_yesdelete);
            ticket_open = itemView.findViewById(R.id.item_open);

            ticket_layout_delete = itemView.findViewById(R.id.item_constraint_delete);
            ticket_layout_main = itemView.findViewById(R.id.item_constraint_main);


        }
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

}