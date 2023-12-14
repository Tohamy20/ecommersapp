package com.example.talabatend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

     private Button createAccountButton;
    private EditText Inputname,inputephone,inputpassword;
    private ProgressDialog loadingbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        createAccountButton = (Button) findViewById(R.id.register_btn);
        Inputname = (EditText) findViewById(R.id.register_username);
        inputephone = (EditText) findViewById(R.id.register_phonenum);
        inputpassword = (EditText) findViewById(R.id.register_password);
        loadingbar = new ProgressDialog(this);


        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreatAcc();
            }
        });
    }

    private void CreatAcc() {
        String name = Inputname.getText().toString();
        String phone = inputephone.getText().toString();
        String pass = inputpassword.getText().toString();
        if (TextUtils.isEmpty(name))
        {
            Toast.makeText(this,"please enter name",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(phone))
        {
            Toast.makeText(this,"please enter phone number",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(pass))
        {
            Toast.makeText(this,"please enter password",Toast.LENGTH_SHORT).show();
        }
        else
        {

            loadingbar.setTitle("Create Account");
            loadingbar.setMessage("please wait ");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();

            ValidatePhone(name,phone,pass);
        }

    }

    private void ValidatePhone(String name, String phone, String pass)
    {
     final DatabaseReference RootRef;
     RootRef = FirebaseDatabase.getInstance().getReference();
     RootRef.addListenerForSingleValueEvent(new ValueEventListener()
     {
         @Override
         public void onDataChange(@NonNull DataSnapshot snapshot)
         {
             if (!(snapshot.child("Users").child(phone).exists()))
             {
                 HashMap<String,Object> userdatamap = new HashMap<>();
                 userdatamap.put("phone",phone);
                 userdatamap.put("password",pass);
                 userdatamap.put("name",name);
                 RootRef.child("Users").child(phone).updateChildren(userdatamap)
                         .addOnCompleteListener(new OnCompleteListener<Void>()
                         {
                             @Override
                             public void onComplete(@NonNull Task<Void> task)
                             {
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(RegisterActivity.this, "Your acc has been created", Toast.LENGTH_SHORT).show();
                                    loadingbar.dismiss();

                                    Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                                    startActivity(intent);


                                }
                                else
                                {
                                    loadingbar.dismiss();
                                    Toast.makeText(RegisterActivity.this, "unsucess", Toast.LENGTH_SHORT).show();

                                }
                             }
                         });

             }
             else
             {

                 Toast.makeText(RegisterActivity.this,"This"+phone+"Already exist",Toast.LENGTH_SHORT).show();
                 loadingbar.dismiss();
                 Toast.makeText(RegisterActivity.this, "please try again using another phone num", Toast.LENGTH_SHORT).show();
                 Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                 startActivity(intent);

             }
         }

         @Override
         public void onCancelled(@NonNull DatabaseError error) {

         }
     });

    }
}