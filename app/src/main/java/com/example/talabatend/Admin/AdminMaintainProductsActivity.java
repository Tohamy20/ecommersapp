package com.example.talabatend.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.talabatend.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminMaintainProductsActivity extends AppCompatActivity
{

    private Button applychangesbtn,deletebtn;
    private EditText name,price,description;
    private ImageView imageView;
    private String productid = "";
    private DatabaseReference productsref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maintain_products);
        productid = getIntent().getStringExtra("pid");
        productsref= FirebaseDatabase.getInstance().getReference().child("Products").child(productid);





        applychangesbtn = findViewById(R.id.apply_cahnges_maintain_btn);
        deletebtn = findViewById(R.id.delete_product_maintain_btn);
        name = findViewById(R.id.product_name_maintain);
        price = findViewById(R.id.product_price_maintain);
        description = findViewById(R.id.product_description_maintain);
        imageView = findViewById(R.id.product_image_maintain);

        displayspecificproductinfo();



        applychangesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applychanges();

            }
        });

        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                deletethisproduct();

            }
        });


    }




    private void deletethisproduct()

    {

        productsref.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                Intent intent = new Intent(AdminMaintainProductsActivity.this, AdminCategoryActivity.class);
                startActivity(intent);
                finish();
                Toast.makeText(AdminMaintainProductsActivity.this, "the product is deleted successfully", Toast.LENGTH_SHORT).show();


            }
        });

    }

    private void applychanges()
    {
        String pName = name.getText().toString();
        String pPrice = price.getText().toString();
        String pDescribtion = description.getText().toString();
        if (pName.equals(""))
        {
            Toast.makeText(this, "write name ....", Toast.LENGTH_SHORT).show();

        }
        else if (pPrice.equals(""))
        {
            Toast.makeText(this, "write price ....", Toast.LENGTH_SHORT).show();

        }
        else if (pDescribtion.equals(""))
        {
            Toast.makeText(this, "write description ....", Toast.LENGTH_SHORT).show();

        }
        else
        {
            HashMap<String, Object> productmap = new HashMap<>();
            productmap.put("pid",productid);
            productmap.put("description",pDescribtion);
            productmap.put("price",pPrice);
            productmap.put("pname",pName);
            productsref.updateChildren(productmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {

                    if (task.isSuccessful())
                    {
                        Toast.makeText(AdminMaintainProductsActivity.this, "changes applied", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(AdminMaintainProductsActivity.this,AdminCategoryActivity.class);
                        startActivity(intent);
                        finish();

                    }

                }
            });

        }

    }

    private void displayspecificproductinfo()
    {
        productsref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if (snapshot.exists())
                {
                    String pName = snapshot.child("pname").getValue().toString();
                    String pPrice = snapshot.child("price").getValue().toString();
                    String pDescription = snapshot.child("description").getValue().toString();
                    String pImage = snapshot.child("image").getValue().toString();



                    name.setText(pName);
                    price.setText(pPrice);
                    description.setText(pDescription);
                    Picasso.get().load(pImage).into(imageView);






                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });



    }


}