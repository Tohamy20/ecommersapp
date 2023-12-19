package com.example.talabatend;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.talabatend.Model.product;
import com.example.talabatend.Prevalent.Prevalent;
import com.example.talabatend.databinding.ActivityHomeBinding;
import com.example.talabatend.viewHolder.productviewholder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomeBinding binding;
    private DatabaseReference productsref;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        productsref = FirebaseDatabase.getInstance().getReference().child("Products");

        setSupportActionBar(binding.appBarHome.toolbar);
        binding.appBarHome.fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                startActivity(intent);

            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_cart, R.id.nav_orders,R.id.nav_categories,R.id.nav_Settings,R.id.nav_log_out)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        View headerview = navigationView.getHeaderView(0);
        TextView usernametextview = headerview.findViewById(R.id.user_profile_name);
        CircleImageView profileimageview = headerview.findViewById(R.id.user_profile_image);
        usernametextview.setText(Prevalent.currentonlineuser.getName());


        Picasso.get().load(Prevalent.currentonlineuser.getImage()).placeholder(R.drawable.profile).into(profileimageview);

        recyclerView = findViewById(R.id.recycle_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);



    }

    @Override
    protected void onStart() {

        super.onStart();
        FirebaseRecyclerOptions<product> options = new FirebaseRecyclerOptions.Builder<product>()
                .setQuery(productsref,product.class).build();


        FirebaseRecyclerAdapter<product , productviewholder> adapter
                = new FirebaseRecyclerAdapter<product, productviewholder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull productviewholder holder, int position, @NonNull product model)

            {

                holder.txtproductname.setText(model.getPname());

                holder.txtproductdescription.setText(model.getDescription());
                holder.txtproductprice.setText( "Price = " +model.getPrice() + "$");
                Picasso.get().load(model.getImage()).into(holder.imageView);
                holder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)

                    {
                        Intent intent = new Intent(HomeActivity.this , ProductDetailsActivity.class);
                        intent.putExtra("pid",model.getPid());
                        startActivity(intent);


                    }
                });





            }

            @NonNull
            @Override
            public productviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout,parent,false);
                productviewholder holder = new productviewholder(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}