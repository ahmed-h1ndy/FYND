package com.ahmed.fynd;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView product_image;
        TextView product_name;
        TextView product_price;
        ImageView product_add_to_cart;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);

            //image = itemView.findViewById(R.id.imageView);
            product_name = itemView.findViewById(R.id.product_name);
            product_image = itemView.findViewById(R.id.product_image);
            product_price = itemView.findViewById(R.id.product_price);
            product_add_to_cart = itemView.findViewById(R.id.add_product_to_cart_button);
        }
    }
    FyndDatabase db;
    Context context;
    ArrayList<Product> products;
    CurrentUser u;
    public ProductAdapter(Context context, ArrayList<Product> products){
        this.context=context;
        this.products=products;
        this.db = new FyndDatabase(context);
        u = db.get_current_user();
    }
    @NonNull
    @Override
    public ProductAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.product,parent,false);
        return new ProductAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.MyViewHolder holder, int position) {

        /*
        byte[] image_byte = data.get(position).getImage();
	    Bitmap bmp = BitmapFactory.decodeByteArray(image_byte, 0, image_byte.length);
	    prod_image.setImageBitmap(bmp);
        */
        String name = products.get(position).getName();
        String price = products.get(position).getPrice();
        byte[] img = products.get(position).getImage();
        Bitmap bmp = BitmapFactory.decodeByteArray(img,0,img.length);

        holder.product_image.setImageBitmap(bmp);
        holder.product_name.setText(name);
        holder.product_price.setText(price);

        holder.product_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.add_to_cart(u.getEmail(),name,"1");
                holder.product_add_to_cart.setEnabled(false);
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

}








