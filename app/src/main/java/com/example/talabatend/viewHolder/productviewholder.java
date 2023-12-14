package com.example.talabatend.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.talabatend.R;
import com.example.talabatend.interfaces.itemclicklistner;

public class productviewholder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtproductname,txtproductdescription,txtproductprice;
    public ImageView imageView;
    public itemclicklistner listner;

    public productviewholder(@NonNull View itemView) {
        super(itemView);
        imageView = (ImageView) itemView.findViewById(R.id.product_image);
        txtproductname = (TextView) itemView.findViewById(R.id.product_name);
        txtproductdescription = (TextView) itemView.findViewById(R.id.product_description);
        txtproductprice = (TextView) itemView.findViewById(R.id.product_price);

    }
    public void setitemclicklistner(itemclicklistner    listner )
    {
        this.listner = listner;


    }


    @Override
    public void onClick(View v)
    {

        listner.onClick(v,getAdapterPosition(),false);


    }
}
