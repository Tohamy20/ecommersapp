package com.example.talabatend;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.talabatend.Model.product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProductDetailsActivity extends AppCompatActivity {


//private FloatingActionButton addtocart;
private ImageView productimage;
private TextView productprice,procuctdescription,productname;
private String productid = "";



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        productid = getIntent().getStringExtra("pid");

       // addtocart = (FloatingActionButton) findViewById(R.id.add_product_to_cart);
        productprice = (TextView) findViewById(R.id.product_price_details);
        productname = (TextView) findViewById(R.id.product_name_details);
        procuctdescription = (TextView) findViewById(R.id.product_description_details);
        productimage = (ImageView) findViewById(R.id.product_image_details);

            getproductdetails(productid);

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
}