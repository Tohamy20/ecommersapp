package com.example.talabatend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.talabatend.Model.product;
import com.example.talabatend.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity {



private ImageView productimage;
private TextView productprice,procuctdescription,productname;
private String productid = "",quantityyy ="1",state = "Normal";

private Button addtocartbutton,quantityaddtocartbutton;
private int quantity = 1;






    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        productid = getIntent().getStringExtra("pid");

        quantityaddtocartbutton = (Button)findViewById(R.id.details_quantity_to_cart_btn);
        addtocartbutton = (Button)findViewById(R.id.details_add_to_cart_btn);
        productprice = (TextView) findViewById(R.id.product_price_details);

        productname = (TextView) findViewById(R.id.product_name_details);
        procuctdescription = (TextView) findViewById(R.id.product_description_details);
        productimage = (ImageView) findViewById(R.id.product_image_details);

            getproductdetails(productid);

            quantityaddtocartbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    quantity++;
                    quantityyy = Integer.toString(quantity);

                }
            });
            addtocartbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)

                {
                    addingtocartlist();
                    if (state.equals("Order placed") || state.equals("Order shipped"))
                    {
                        Toast.makeText(ProductDetailsActivity.this, "u can buy more product once ur order is comfirmed", Toast.LENGTH_LONG).show();


                    }
                    else
                    {
                        addingtocartlist();
                    }


                }
            });

    }

    @Override
    protected void onStart() {
        super.onStart();
        checkordersstate();

    }

    private void addingtocartlist()
    {

        String savecurrenttime,savecurrentdate;
        Calendar calfordate = Calendar.getInstance();
        SimpleDateFormat currentdata = new SimpleDateFormat("MMM dd, yyyy");
        savecurrentdate = currentdata.format(calfordate.getTime());

        SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss a");
        savecurrenttime = currenttime.format(calfordate.getTime());
        final DatabaseReference cartlistrefrence = FirebaseDatabase.getInstance().getReference().child("Cart List");
        final HashMap<String,Object> cartmap=new HashMap<>();
        cartmap.put("pid",productid);
        cartmap.put("pname",productname.getText().toString());
        cartmap.put("price",productprice.getText().toString());
        cartmap.put("date",savecurrentdate);
        cartmap.put("time",savecurrenttime);
        cartmap.put("quantity",quantityyy);
        cartmap.put("discount","");

        cartlistrefrence.child("User View").child(Prevalent.currentonlineuser.getPhone())
                .child("Products").child(productid)
                .updateChildren(cartmap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            cartlistrefrence.child("Admin View").child(Prevalent.currentonlineuser.getPhone())
                                    .child("Products").child(productid)
                                    .updateChildren(cartmap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {

                                            if (task.isSuccessful())
                                            {
                                                Toast.makeText(ProductDetailsActivity.this, "Added to cart list", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(ProductDetailsActivity.this,HomeActivity.class);
                                                startActivity(intent);

                                            }
                                        }
                                    });

                        }
                    }
                });



    }

    private void getproductdetails(String productid)

    {

        DatabaseReference productsref = FirebaseDatabase.getInstance().getReference().child("Products");
        productsref.child(productid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if (snapshot.exists())
                {
                    product product = snapshot.getValue(com.example.talabatend.Model.product.class);
                    productname.setText(product.getPname());
                    productprice.setText(product.getPrice());
                    procuctdescription.setText(product.getDescription());
                    Picasso.get().load(product.getImage()).into(productimage);



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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


                    if (shipingstate.equals("Sshipped"))
                    {

                        state = "Order shipped";


                    }
                    else if (shipingstate.equals("not shipped"))
                    {

                        state = "Order placed";


                    }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }




}