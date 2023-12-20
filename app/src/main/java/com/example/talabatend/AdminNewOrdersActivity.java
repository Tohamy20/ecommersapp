package com.example.talabatend;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.talabatend.Model.AdminOrders;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminNewOrdersActivity extends AppCompatActivity
{
    private RecyclerView orderlist;
    private DatabaseReference ordersref;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_orders);
        ordersref = FirebaseDatabase.getInstance().getReference().child("Orders");
        orderlist = findViewById(R.id.orders_list);
        orderlist.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<AdminOrders> options =
                new FirebaseRecyclerOptions.Builder<AdminOrders>()
                        .setQuery(ordersref, AdminOrders.class)
                        .build();


        FirebaseRecyclerAdapter<AdminOrders,AdminOrdersViewHolder> adapter =
                    new FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder>(options) {
                        @Override
                        protected void onBindViewHolder(@NonNull AdminOrdersViewHolder holder, int position, @NonNull AdminOrders model)
                        {

                            holder.username.setText("Name :" + model.getName());
                            holder.userphonenumber.setText("Phone :" + model.getPhone());
                            holder.usertotalprice.setText("total price = $ :" + model.getTotalamount());
                            holder.userdatetime.setText("Date :" + model.getDate() + " " + model.getTime());
                            holder.usershippingaddress.setText("Address :" + model.getAddress() + "," + model.getCity() );



                            holder.showordersbtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String uid = getRef(position).getKey();
                                    Intent intent = new Intent(AdminNewOrdersActivity.this, AdminUserProductsActivity.class);
                                    intent.putExtra("uid",uid);
                                    startActivity(intent);

                                }
                            });





                        }

                        @NonNull
                        @Override
                        public AdminOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                        {
                            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout,parent,false);
                            return new AdminOrdersViewHolder(view);

                        }
                    };

        orderlist.setAdapter(adapter);
        adapter.startListening();
    }

    public static class AdminOrdersViewHolder extends RecyclerView.ViewHolder
    {
        public TextView username,userphonenumber,usertotalprice,userdatetime,usershippingaddress;
        public Button showordersbtn;




        public AdminOrdersViewHolder(@NonNull View itemView)
        {
            super(itemView);
            username = itemView.findViewById(R.id.order_user_name);
            userphonenumber = itemView.findViewById(R.id.order_phone_number);
            usertotalprice = itemView.findViewById(R.id.order_total_price);
            userdatetime = itemView.findViewById(R.id.order_date_time);
            usershippingaddress = itemView.findViewById(R.id.order_address_city);
            showordersbtn = itemView.findViewById(R.id.show_all_products_btn);



        }
    }

}