package com.example.talabatend;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
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

public class LoginActivity extends AppCompatActivity {

    private EditText phone_numlog,pass_log;
    private Button log_btn;
    private ProgressDialog loadingbar;
    private String parentdbname="Users";
    private CheckBox checkBoxRememberme;
    private TextView adminlink,notadmin;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        log_btn = (Button) findViewById(R.id.login_btn);

        phone_numlog = (EditText) findViewById(R.id.login_phonenum);
        pass_log = (EditText) findViewById(R.id.login_password);
        adminlink = (TextView) findViewById(R.id.admin_panel_link);
        notadmin = (TextView) findViewById(R.id.not_admin_panel_link);

        loadingbar = new ProgressDialog(this);
        checkBoxRememberme = (CheckBox) findViewById(R.id.remeber_me_chkb);
        Paper.init(this);


        log_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginuser();

            }
        });
        adminlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                log_btn.setText("Login Admin");
                adminlink.setVisibility(View.INVISIBLE);

                notadmin.setVisibility(View.VISIBLE);
                parentdbname = "Admins";


            }
        });
        notadmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                log_btn.setText("Login");
                adminlink.setVisibility(View.VISIBLE);

                notadmin.setVisibility(View.INVISIBLE);
                parentdbname = "Users";


            }
        });
    }

            private void loginuser()
            {
                String phone = phone_numlog.getText().toString();
                String pass = pass_log.getText().toString();
               if (TextUtils.isEmpty(phone))
               {

                    Toast.makeText(LoginActivity.this, "please enter phone number", Toast.LENGTH_SHORT).show();
               }
               else if (TextUtils.isEmpty(pass))
               {
                    Toast.makeText(LoginActivity.this, "please enter password", Toast.LENGTH_SHORT).show();
               }
               else
               {
                   loadingbar.setTitle("Login Account");
                   loadingbar.setMessage("please wait ");
                   loadingbar.setCanceledOnTouchOutside(false);
                   loadingbar.show();


                   AllowaccesstoAcc(phone,pass);

               }


            }

            private void AllowaccesstoAcc(final String phone,final String pass)
            {
                if (checkBoxRememberme.isChecked())
                {
                    Paper.book().write(Prevalent.userphoneKey,phone);
                    Paper.book().write(Prevalent.userpasswordKey,pass);
                }

                final DatabaseReference RootRef;
                RootRef = FirebaseDatabase.getInstance().getReference();
                RootRef.addListenerForSingleValueEvent(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        if (snapshot.child(parentdbname).child(phone).exists())
                        {
                            Users userdata = snapshot.child(parentdbname).child(phone).getValue(Users.class);
                                if (userdata.getPhone().equals(phone))
                                {
                                    if (userdata.getPassword().equals(pass))
                                    {
                                        if (parentdbname.equals("Admins"))
                                        {
                                            Toast.makeText(LoginActivity.this, "Welcome admin " + phone + ", you Logged in successfully", Toast.LENGTH_SHORT).show();
                                            loadingbar.dismiss();
                                            Intent intent = new Intent(LoginActivity.this,AdminCategoryActivity.class);
                                            startActivity(intent);
                                        } else if (parentdbname.equals("Users"))
                                        {
                                            Toast.makeText(LoginActivity.this, "Welcome user, Logged in successfully", Toast.LENGTH_SHORT).show();
                                            loadingbar.dismiss();
                                            Intent intent = new Intent(LoginActivity.this,HomeActivity.class);

                                            Prevalent.currentonlineuser = userdata;


                                            startActivity(intent);
                                        }
                                    }
                                    else
                                    {
                                        loadingbar.dismiss();
                                        Toast.makeText(LoginActivity.this, "Wrong Password!", Toast.LENGTH_SHORT).show();
                                    }

                                }


                        }
                        else
                        {
                            Toast.makeText(LoginActivity.this, "acc with this"+phone+"Doesn't exist", Toast.LENGTH_SHORT).show();
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
