package com.example.talabatend;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.talabatend.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmFinalOrderActivity extends AppCompatActivity {

    private EditText nameedittext,phoneedittext,addressedittext,cityedittext;
    private Button confirmorderbuttn;
    private String totalamount = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);
        totalamount = getIntent().getStringExtra("Total Price");
        Toast.makeText(this, "Total Price = $", Toast.LENGTH_SHORT).show();


        confirmorderbuttn = (Button) findViewById(R.id.confirm_final_order_btn);
        nameedittext = (EditText) findViewById(R.id.shipment_name);
        phoneedittext = (EditText) findViewById(R.id.shipment_phone);
        addressedittext = (EditText) findViewById(R.id.shipment_address);
        cityedittext = (EditText) findViewById(R.id.shipment_city);

        confirmorderbuttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                check();
            }
        });



    }

    private void check()
    {

        if (TextUtils.isEmpty(nameedittext.getText().toString()))
        {
            Toast.makeText(this, "Provide your name ...", Toast.LENGTH_SHORT).show();

        }
        else if (TextUtils.isEmpty(phoneedittext.getText().toString()))
        {
            Toast.makeText(this, "Provide your phone ...", Toast.LENGTH_SHORT).show();

        }
        else if (TextUtils.isEmpty(addressedittext.getText().toString()))
        {
            Toast.makeText(this, "Provide your address ...", Toast.LENGTH_SHORT).show();

        }
        else if (TextUtils.isEmpty(cityedittext.getText().toString()))
        {
            Toast.makeText(this, "Provide your city ...", Toast.LENGTH_SHORT).show();

        }
        else
        {
            confirmorder();
        }

    }

    private void confirmorder()
    {
        final String savecurrenttime,savecurrentdate;
        String user = Prevalent.currentonlineuser.getPhone();
        Calendar calfordate = Calendar.getInstance();
        SimpleDateFormat currentdata = new SimpleDateFormat("MMM dd, yyyy");
        savecurrentdate = currentdata.format(calfordate.getTime());

        SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss a");
        savecurrenttime = currenttime.format(calfordate.getTime());
        final DatabaseReference orderref = FirebaseDatabase.getInstance().getReference().child("Orders")
                .child(user);


        HashMap<String,Object> ordersmap = new HashMap<>();
        ordersmap.put("totalamount",totalamount);
        ordersmap.put("name",nameedittext.getText().toString());
        ordersmap.put("phone",phoneedittext.getText().toString());
        ordersmap.put("address",addressedittext.getText().toString());
        ordersmap.put("city",cityedittext.getText().toString());

        ordersmap.put("date",savecurrentdate);
        ordersmap.put("time",savecurrenttime);
        ordersmap.put("state","not shipped");


        orderref.updateChildren(ordersmap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if (task.isSuccessful())
                {
                    String user = Prevalent.currentonlineuser.getPhone();
                    FirebaseDatabase.getInstance().getReference().child("Cart List")
                            .child("User View")
                            .child(user)
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(ConfirmFinalOrderActivity.this, "ur order has been confirmed", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(ConfirmFinalOrderActivity.this,HomeActivity.class);
                                        intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                        startActivity(intent);
                                        finish();


                                    }

                                }
                            });



                }

            }
        });


    }
}