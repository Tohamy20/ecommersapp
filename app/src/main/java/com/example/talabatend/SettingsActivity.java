package com.example.talabatend;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.talabatend.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;


public class SettingsActivity extends AppCompatActivity {

    private CircleImageView profileimageviewbtn;
    private EditText fullnameedittxt,userphoneedittxt,addressedittext,passwordedittxt;
    private TextView profilechangetxtbtn,closetxtbtn,saveTextbutton;
    private Uri imageuri;
    private String myuri = "";
    private StorageTask uploadtask;
    private StorageReference storageprofilepictureref;
    private String checker = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        profileimageviewbtn = (CircleImageView) findViewById(R.id.settings_profile_image);
        fullnameedittxt = (EditText) findViewById(R.id.settings_full_name);
        userphoneedittxt = (EditText) findViewById(R.id.settings_phone_number);
        addressedittext = (EditText) findViewById(R.id.settings_full_address);
        passwordedittxt = (EditText) findViewById(R.id.settings_new_pass);
        profilechangetxtbtn = (TextView) findViewById(R.id.profile_image_change_btn);
        saveTextbutton = (TextView) findViewById(R.id.update_account_settings_btn);
        closetxtbtn = (TextView) findViewById(R.id.close_settings_btn);


        userinfodisplay(profileimageviewbtn,fullnameedittxt,userphoneedittxt,addressedittext,passwordedittxt);
        closetxtbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                finish();

            }
        });
        saveTextbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                if (checker.equals("clicked"))
                {
                    userinfosaved();

                }
                else
                {
                    updateonlyuserinfo();

                }
            }
        });


        profilechangetxtbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checker = "clicked";



            }
        });
    }

    private void updateonlyuserinfo()
    {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");
        HashMap<String,Object> usermap=new HashMap<>();
        usermap.put("name",fullnameedittxt.getText().toString());
        usermap.put("address",addressedittext.getText().toString());
        usermap.put("phoneOrder",userphoneedittxt.getText().toString());
        usermap.put("password",passwordedittxt.getText().toString());
        ref.child(Prevalent.currentonlineuser.getPhone()).updateChildren(usermap);
        ref.child(Prevalent.currentonlineuser.getPassword()).updateChildren(usermap);



        startActivity(new Intent(SettingsActivity.this,HomeActivity.class));
        Toast.makeText(SettingsActivity.this, "profile info updated", Toast.LENGTH_SHORT).show();

        finish();
    }


    private void userinfosaved()
    {
        if (TextUtils.isEmpty(fullnameedittxt.getText().toString()))
        {

            Toast.makeText(this, "please enter name ", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(addressedittext.getText().toString()))
        {

            Toast.makeText(this, "please enter address ", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(userphoneedittxt.getText().toString()))
        {

            Toast.makeText(this, "please enter phone ", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(userphoneedittxt.getText().toString()))
        {

            Toast.makeText(this, "please enter phone ", Toast.LENGTH_SHORT).show();
        }


    }




    private void userinfodisplay(CircleImageView profileimageviewbtn, EditText fullnameedittxt, EditText userphoneedittxt, EditText addressedittext, EditText passwordedittxt)
    {

        DatabaseReference userref = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentonlineuser.getPhone());
        userref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {

                if (snapshot.exists())
                {

                    if (snapshot.child("image").exists())
                    {
                        String image =snapshot.child("image").getValue().toString();
                        String name =snapshot.child("name").getValue().toString();
                        String phone =snapshot.child("phone").getValue().toString();
                        String address =snapshot.child("address").getValue().toString();
                        String pass = snapshot.child("password").getValue().toString();

                        Picasso.get().load(image).into(profileimageviewbtn);
                        fullnameedittxt.setText(name);
                        userphoneedittxt.setText(phone);
                        addressedittext.setText(address);
                        passwordedittxt.setText(pass);

                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}