package com.example.talabatend;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.talabatend.Model.Users;
import com.example.talabatend.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private Button joinNowButton,loginButton;
    private ProgressDialog loadingbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        joinNowButton = (Button)findViewById(R.id.main_join_now_btn);
        loginButton = (Button) findViewById(R.id.main_login_btn);
        loadingbar = new ProgressDialog(this);

        Paper.init(this);



        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
            }


        });

        joinNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);

            }
        });
            String userphonekey = Paper.book().read(Prevalent.userphoneKey);
            String userpasskey = Paper.book().read(Prevalent.userpasswordKey);

        if (userphonekey != "" && userpasskey != "")
        {
            if (!TextUtils.isEmpty(userphonekey) &&!TextUtils.isEmpty(userpasskey) )
            {
                Allowaccessmain(userphonekey,userpasskey);

                loadingbar.setTitle("Already Logged In");
                loadingbar.setMessage("please wait ");
                loadingbar.setCanceledOnTouchOutside(false);
                loadingbar.show();

            }

        }

    }

    private void Allowaccessmain(final String phone,final String pass)
    {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if (snapshot.child("Users").child(phone).exists())
                {
                    Users userdata = snapshot.child("Users").child(phone).getValue(Users.class);
                    if (userdata.getPhone().equals(phone))
                    {
                        if (userdata.getPassword().equals(pass))
                        {
                            Toast.makeText(MainActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                            loadingbar.dismiss();
                                Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                                Prevalent.currentonlineuser = userdata;
                            startActivity(intent);


                        }
                        else
                        {
                            loadingbar.dismiss();
                            Toast.makeText(MainActivity.this, "Wrong Password!", Toast.LENGTH_SHORT).show();
                        }

                    }


                }
                else
                {
                    Toast.makeText(MainActivity.this, "acc with this"+phone+"Doesn't exist", Toast.LENGTH_SHORT).show();
                    loadingbar.dismiss();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });
    }

}