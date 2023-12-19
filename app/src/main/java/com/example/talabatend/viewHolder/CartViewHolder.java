package com.example.talabatend.viewHolder;


import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.talabatend.R;
import com.example.talabatend.interfaces.itemclicklistner;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtproductname,txtproductprice,txtproductquantity;
    private itemclicklistner itemClickListenervr;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        txtproductname = itemView.findViewById(R.id.cart_product_name);
        txtproductprice = itemView.findViewById(R.id.cart_product_prcie);
        txtproductquantity = itemView.findViewById(R.id.cart_product_quantity);


    }


    @Override
    public void onClick(View v)
    {


        itemClickListenervr.onClick(v,getAdapterPosition(),false);




    }

    public void setItemClickListener(itemclicklistner itemClickListener) {
        this.itemClickListenervr = itemClickListener;
    }
}
