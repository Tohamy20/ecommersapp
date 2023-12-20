package com.example.talabatend;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.talabatend.Model.Cart;
import com.example.talabatend.Prevalent.Prevalent;
import com.example.talabatend.viewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CartActivity extends AppCompatActivity
{


    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button nextprocessbuttn;
    private TextView txttotalamount,txtmsg1;
    private String totalamount = "";

    private int overtotalprice = 0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        totalamount = getIntent().getStringExtra("Total Price");
        Toast.makeText(this, "total price = $" + totalamount, Toast.LENGTH_SHORT).show();
        recyclerView = findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        nextprocessbuttn = (Button) findViewById(R.id.next_process_btn);
        txttotalamount = (TextView) findViewById(R.id.total_price);
        txtmsg1 = (TextView) findViewById(R.id.msg1);




        nextprocessbuttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                txttotalamount.setText( "total Price = $" + String.valueOf(overtotalprice));


                Intent intent = new Intent(CartActivity.this, ConfirmFinalOrderActivity.class);
                intent.putExtra("Total Price",String.valueOf(overtotalprice));
                startActivity(intent);
                finish();


            }
        });



    }

    @Override
    protected void onStart()
    {
        super.onStart();
        checkordersstate();

        String user = Prevalent.currentonlineuser.getPhone();

        final DatabaseReference cartlistref = FirebaseDatabase.getInstance().getReference().child("Cart List").child("User View");
        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>().setQuery(cartlistref.child(user).child("Products"),Cart.class).build();
        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter =
                new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Cart model)
                    {


                        holder.txtproductquantity.setText( "Quantity "+ model.getQuantity());
                        holder.txtproductprice.setText(model.getPrice());
                        holder.txtproductname.setText(model.getPname());

                        int onetypeproductTPrice = ((Integer.valueOf(model.getPrice()))) * Integer.valueOf(model.getQuantity());

                        overtotalprice = overtotalprice + onetypeproductTPrice;

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {
                                CharSequence options[] = new CharSequence[]
                                        {
                                                "Edit",
                                                "Delete"


                                        };

                                AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                                builder.setTitle("Cart options");

                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        if (which == 0)
                                        {
                                            Intent intent = new Intent(CartActivity.this,ProductDetailsActivity.class);
                                            intent.putExtra("pid",model.getPid());
                                            startActivity(intent);


                                        }
                                        if (which==1)
                                        {
                                            String user = Prevalent.currentonlineuser.getPhone();
                                            String pidval = model.getPid();
                                            cartlistref.child(user).child("Products").child(pidval).removeValue()
                                                 .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task)
                                                        {

                                                            if (task.isSuccessful())
                                                            {
                                                                Toast.makeText(CartActivity.this, "deleted successfully", Toast.LENGTH_SHORT).show();
                                                                Intent intent = new Intent(CartActivity.this,HomeActivity.class);


                                                                startActivity(intent);

                                                            }

                                                        }
                                                    });

                                        }




                                    }
                                });
                                builder.show();


                            }
                        });


                    }

                    @NonNull
                    @Override
                    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout,parent,false);
                        CartViewHolder holder = new CartViewHolder(view);
                        return holder;

                    }
                };

                recyclerView.setAdapter(adapter);
                adapter.startListening();
    }

    private void checkordersstate()
    {
        String user = Prevalent.currentonlineuser.getPhone();
        DatabaseReference ordersref;
        ordersref = FirebaseDatabase.getInstance().getReference().child("Orders")
                .child(user);
        ordersref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if (snapshot.exists())
                {

                    String shipingstate = snapshot.child("state").getValue().toString();
                    String username = snapshot.child("name").getValue().toString();
                    if (shipingstate.equals("Sshipped"))
                    {
                        txttotalamount.setText("Dear" +  username + "\n order is shipped successfully " );
                        recyclerView.setVisibility(View.GONE);
                        txtmsg1.setVisibility(View.VISIBLE);
                        txtmsg1.setText("Congrats ur final has been Shipped");


                        nextprocessbuttn.setVisibility(View.GONE);
                        Toast.makeText(CartActivity.this, "u can buy more products once u receive ur first order", Toast.LENGTH_SHORT).show();



                    }
                    else if (shipingstate.equals("not shipped"))
                    {
                        txttotalamount.setText("SHippment state = Not Shipped" );
                        recyclerView.setVisibility(View.GONE);
                        txtmsg1.setVisibility(View.VISIBLE);
                        nextprocessbuttn.setVisibility(View.GONE);
                        Toast.makeText(CartActivity.this, "u can buy more products once u receive ur first order", Toast.LENGTH_SHORT).show();





                    }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }


}