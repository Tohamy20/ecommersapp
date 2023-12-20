package com.example.talabatend.Admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.talabatend.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminaddnewProductActivity extends AppCompatActivity {

    private String categoryName,Description,price,pname,saveCurrentDate,saveCurrentTime;
    private Button Addnewproduct;
    private ImageView productImage;
    private EditText productname,productdescription,productprice;
    private static final int GalleryPick=1;
    private Uri imageuri;
    private String productRandomKey,downloadImageUrl;
    private StorageReference productImagesref;
    private DatabaseReference productsref;
    private ProgressDialog loadingbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminaddnew_product);



        categoryName = getIntent().getExtras().get("category").toString();
        productImagesref = FirebaseStorage.getInstance().getReference().child("Product Images");
        productsref = FirebaseDatabase.getInstance().getReference().child("Products");

        Addnewproduct = (Button) findViewById(R.id.add_new_product);
        productImage = (ImageView) findViewById(R.id.select_product_image);
        productname = (EditText) findViewById(R.id.product_name);
        productdescription = (EditText) findViewById(R.id.product_description);
        productprice = (EditText) findViewById(R.id.product_price);
        loadingbar = new ProgressDialog(this);


        productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openGallery();
            }
        });
        Addnewproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validateproductdata();
            }
        });

    }

    private void openGallery()
    {
        Intent galleryintent = new Intent();
        galleryintent.setAction(Intent.ACTION_GET_CONTENT);
        galleryintent.setType("image/*");
        startActivityForResult(galleryintent,GalleryPick);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GalleryPick && resultCode == RESULT_OK && data!=null)
        {

            imageuri = data.getData();
            productImage.setImageURI(imageuri);

        }
    }
    private void validateproductdata()
    {
    Description = productdescription.getText().toString();
    price       = productprice.getText().toString();
    pname       = productname.getText().toString();

        if (imageuri==null)
        {
            Toast.makeText(this, "Product image is mandatory", Toast.LENGTH_SHORT).show();


        }
        else if (TextUtils.isEmpty(Description))
        {
            Toast.makeText(this, "please write a description", Toast.LENGTH_SHORT).show();

        }
        else if (TextUtils.isEmpty(price))
        {
            Toast.makeText(this, "please write a Price", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(pname))
        {
            Toast.makeText(this, "please write a Name", Toast.LENGTH_SHORT).show();

        }
        else
        {
            storeproductinfo();
        }


    }

    private void storeproductinfo()
    {

        loadingbar.setTitle("Add new  product");
        loadingbar.setMessage("please wait ");
        loadingbar.setCanceledOnTouchOutside(false);
        loadingbar.show();

        Calendar calender = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd,yyyy");
        saveCurrentDate =currentDate.format(calender.getTime());


        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime =currentTime.format(calender.getTime());

        productRandomKey = saveCurrentDate + saveCurrentTime;


        StorageReference filepath =productImagesref.child(imageuri.getLastPathSegment()+ productRandomKey+".jpg");

        final UploadTask uploadTask= filepath.putFile(imageuri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                String message = e.toString();
                Toast.makeText(AdminaddnewProductActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                loadingbar.dismiss();


            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Toast.makeText(AdminaddnewProductActivity.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful())
                        {
                            throw task.getException();

                        }
                        downloadImageUrl = filepath.getDownloadUrl().toString();
                        return filepath.getDownloadUrl();

                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task)
                    {
                        if (task.isSuccessful())
                        {
                            downloadImageUrl = task.getResult().toString();
                            Toast.makeText(AdminaddnewProductActivity.this, "getting image url successfully", Toast.LENGTH_SHORT).show();

                            saveProductinfoindaTabase();
                        }
                    }
                });

            }
        });

    }

    private void saveProductinfoindaTabase()
    {
        HashMap<String, Object> productmap = new HashMap<>();
        productmap.put("pid",productRandomKey);
        productmap.put("date",saveCurrentDate);
        productmap.put("time",saveCurrentTime);
        productmap.put("description",Description);
        productmap.put("image",downloadImageUrl);
        productmap.put("category",categoryName);
        productmap.put("price",price);
        productmap.put("pname",pname);


        productsref.child(productRandomKey).updateChildren(productmap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            Intent intent = new Intent(AdminaddnewProductActivity.this, AdminCategoryActivity.class);
                            startActivity(intent);
                            loadingbar.dismiss();
                            Toast.makeText(AdminaddnewProductActivity.this, "Product added successfully.....", Toast.LENGTH_SHORT).show();

                        }
                        else
                        {
                            loadingbar.dismiss();
                            String message =task.getException().toString();
                            Toast.makeText(AdminaddnewProductActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();

                        }
                    }
                });


    }
}