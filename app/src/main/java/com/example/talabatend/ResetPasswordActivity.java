package com.example.talabatend;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.talabatend.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ResetPasswordActivity extends AppCompatActivity
{

    private String check ="";
    private TextView pagetitle,titlequestion;
    private EditText phonenumber,question1,question2;
    private Button verifybtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);


        check =getIntent().getStringExtra("check");


        pagetitle = findViewById(R.id.page_title);
        titlequestion = findViewById(R.id.title_question);
        phonenumber = findViewById(R.id.rest_find_phone_number);
        question1 = findViewById(R.id.question_1);
        question2 = findViewById(R.id.question_2);
        verifybtn = findViewById(R.id.veirfy_btn);






    }

    @Override
    protected void onStart()
    {
        super.onStart();
        phonenumber.setVisibility(View.GONE);

        if (check.equals("settings"))
        {



            pagetitle.setText("Set question");
            titlequestion.setText("please set answers to the fellowing question");

            verifybtn.setText("Set");

            displayperiviousanswers();

            verifybtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {

                    setanswers();


                }
            });

        }
        else if (check.equals("login"))
        {
            phonenumber.setVisibility(View.VISIBLE);
            verifybtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    verifyuser();

                }
            });



        }
    }
    private void setanswers(){
        String answer1 = question1.getText().toString().toLowerCase();
        String answer2 = question2.getText().toString().toLowerCase();
        if (question1.equals("")&& question2.equals(""))
        {
            Toast.makeText(ResetPasswordActivity.this, "please answer alll questions", Toast.LENGTH_SHORT).show();
        }
        else
        {
            String user = Prevalent.currentonlineuser.getPhone();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(user);
            HashMap<String,Object> userdatamap = new HashMap<>();
            userdatamap.put("answer1",answer1);
            userdatamap.put("answer2",answer2);

            ref.child("security question").updateChildren(userdatamap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(ResetPasswordActivity.this, "you have answer security question successully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ResetPasswordActivity.this,HomeActivity.class);
                        startActivity(intent);

                    }

                }
            });

        }
    }
    private void displayperiviousanswers(){


        String user = Prevalent.currentonlineuser.getPhone();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(user);


        ref.child("security question").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if (snapshot.exists())
                {

                    String ans1 = snapshot.child("answer1").getValue().toString();
                    String ans2 = snapshot.child("answer2").getValue().toString();
                    question1.setText(ans1);
                    question2.setText(ans2);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {


            }
        });






    }
    private void verifyuser()
    {

       final String phone = phonenumber.getText().toString();
       final String answer1 = question1.getText().toString().toLowerCase();
       final String answer2 = question2.getText().toString().toLowerCase();
       if (!phone.equals("") && !answer1.equals("") && !answer2.equals(""))
       {


           String user = Prevalent.currentonlineuser.getPhone();
           final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(phone);

           ref.addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot snapshot)
               {
                   if (snapshot.exists())
                   {
                       String mphone = snapshot.child("phone").getValue().toString();
                       if (snapshot.hasChild("security question"))
                       {

                           String ans1 = snapshot.child("security question").child("answer1").getValue().toString();
                           String ans2 = snapshot.child("security question").child("answer2").getValue().toString();

                           if (!ans1.equals(answer1))
                           {
                               Toast.makeText(ResetPasswordActivity.this, "ur answer 1 is incorrect", Toast.LENGTH_SHORT).show();

                           }
                           else  if (!ans2.equals(answer2))
                           {
                               Toast.makeText(ResetPasswordActivity.this, "ur answer 2  is incorrect", Toast.LENGTH_SHORT).show();


                           }
                           else
                           {
                               AlertDialog.Builder builder = new AlertDialog.Builder(ResetPasswordActivity.this);
                               builder.setTitle("new password");

                               final EditText newpassword = new EditText(ResetPasswordActivity.this);
                               newpassword.setHint("Write password here");
                               builder.setView(newpassword);
                               builder.setPositiveButton("change", new DialogInterface.OnClickListener() {
                                   @Override
                                   public void onClick(DialogInterface dialog, int which)
                                   {

                                       if (!newpassword.getText().toString().equals(""))
                                       {
                                           ref.child("password").setValue(newpassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                               @Override
                                               public void onComplete(@NonNull Task<Void> task)
                                               {
                                                   if (task.isSuccessful())
                                                   {
                                                       Toast.makeText(ResetPasswordActivity.this, "password changed successfully", Toast.LENGTH_SHORT).show();
                                                       Intent intent = new Intent(ResetPasswordActivity.this,LoginActivity.class);
                                                       startActivity(intent);



                                                   }

                                               }
                                           });

                                       }

                                   }

                               });

                               builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                   @Override
                                   public void onClick(DialogInterface dialog, int which)
                                   {
                                       dialog.cancel();

                                   }
                               });

                               builder.show();
                           }

                       }
                       else
                       {
                           Toast.makeText(ResetPasswordActivity.this, "this phone doesn't exist", Toast.LENGTH_SHORT).show();

                       }
                   }
                   else
                   {
                       Toast.makeText(ResetPasswordActivity.this, "the phone is incorrect", Toast.LENGTH_SHORT).show();

                   }
               }

               @Override
               public void onCancelled(@NonNull DatabaseError error)
               {

               }
           });
       }
       else
       {
           Toast.makeText(this, "please complete the form", Toast.LENGTH_SHORT).show();
       }





    }

}