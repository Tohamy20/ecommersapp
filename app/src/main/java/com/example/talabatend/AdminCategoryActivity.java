package com.example.talabatend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class AdminCategoryActivity extends AppCompatActivity {


    private ImageView tshirts,sportsshirts,femaledresses,sweathers;
    private ImageView glasses,hatscaps,walletsbagspurses,shoes;
    private ImageView headphoneshandfree,laptops,watches,mobilephones;
    private Button logoutbtn,checkorderbutton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        logoutbtn = (Button)findViewById(R.id.admin_logout_btn);
        checkorderbutton = (Button)findViewById(R.id.check_out_orders_btn);


        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                    Intent intent = new Intent(AdminCategoryActivity.this,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();



            }
        });
        checkorderbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminNewOrdersActivity.class);

                startActivity(intent);
            }
        });



        tshirts = (ImageView)findViewById(R.id.t_shirts);
        sportsshirts = (ImageView)findViewById(R.id.sports_t_shirt);
        femaledresses = (ImageView)findViewById(R.id.female_dresses);
        sweathers = (ImageView)findViewById(R.id.sweather);

        glasses = (ImageView)findViewById(R.id.glasses);
        hatscaps = (ImageView)findViewById(R.id.hats_caps);
        walletsbagspurses = (ImageView)findViewById(R.id.purse_bags_wallets);
        shoes = (ImageView)findViewById(R.id.shoes);

        headphoneshandfree = (ImageView)findViewById(R.id.headphones_handfree);
        laptops = (ImageView)findViewById(R.id.laptop_pc);
        watches = (ImageView)findViewById(R.id.watches);
        mobilephones = (ImageView)findViewById(R.id.mobilephones);

        tshirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AdminCategoryActivity.this,AdminaddnewProductActivity.class);

                intent.putExtra("category","tshirts");
                startActivity(intent);

            }
        });
        sportsshirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminaddnewProductActivity.class);

                intent.putExtra("category","sportsshirts");
                startActivity(intent);
            }
        });
        femaledresses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminaddnewProductActivity.class);

                intent.putExtra("category","femaledresses");
                startActivity(intent);
            }
        });
        sweathers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminaddnewProductActivity.class);

                intent.putExtra("category","sweathers");
                startActivity(intent);
            }
        });
        glasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminaddnewProductActivity.class);

                intent.putExtra("category","glasses");
                startActivity(intent);
            }
        });
        hatscaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminaddnewProductActivity.class);

                intent.putExtra("category","hatscaps");
                startActivity(intent);
            }
        });
        walletsbagspurses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminaddnewProductActivity.class);

                intent.putExtra("category","walletsbagspurses");
                startActivity(intent);
            }
        });
        shoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminaddnewProductActivity.class);

                intent.putExtra("category","glasses");
                startActivity(intent);
            }
        });
        headphoneshandfree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminaddnewProductActivity.class);

                intent.putExtra("category","headphoneshandfree");
                startActivity(intent);
            }
        });
        laptops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminaddnewProductActivity.class);

                intent.putExtra("category","laptops");
                startActivity(intent);
            }
        });
        watches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminaddnewProductActivity.class);

                intent.putExtra("category","watches");
                startActivity(intent);
            }
        });
        mobilephones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminaddnewProductActivity.class);

                intent.putExtra("category","mobilephones");
                startActivity(intent);
            }
        });


    }
}