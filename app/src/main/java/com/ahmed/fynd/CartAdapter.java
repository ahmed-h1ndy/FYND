package com.ahmed.fynd;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView product_image,increase_button,decrease_button;
        TextView product_name;
        TextView product_quantity;
        TextView product_total_price;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);

            //image = itemView.findViewById(R.id.imageView);
            product_name = itemView.findViewById(R.id.cart_product_name);
            product_image = itemView.findViewById(R.id.cart_product_image);
            increase_button = itemView.findViewById(R.id.cart_increase_button);
            decrease_button = itemView.findViewById(R.id.cart_decrease_button);
            product_quantity = itemView.findViewById(R.id.cart_product_amount);
            product_total_price = itemView.findViewById(R.id.cart_product_price);
        }
    }

    Context context;
    ArrayList<Product> products;
    FyndDatabase db;
    public CartAdapter(Context context, ArrayList<Product> products){
        this.context=context;
        this.products=products;
        db = new FyndDatabase(context);
    }
    @NonNull
    @Override
    public CartAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.cart_product,parent,false);
        return new CartAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.MyViewHolder holder, int position) {


        String name = products.get(position).getName();
        byte[] img = products.get(position).getImage();
        Bitmap bmp = BitmapFactory.decodeByteArray(img,0,img.length);
        String quantity = products.get(position).getQuantity();
        int total_price = Integer.parseInt(products.get(position).getPrice())*Integer.parseInt(quantity);

        holder.product_image.setImageBitmap(bmp);
        holder.product_name.setText(name);
        holder.product_quantity.setText(quantity);
        holder.product_total_price.setText(String.valueOf(total_price));

        holder.increase_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp = holder.product_quantity.getText().toString();
                int amount = Integer.parseInt(temp)+1;
                holder.product_quantity.setText(String.valueOf(amount));
                db.edit_cart_product(db.get_current_user().getEmail(),name,String.valueOf(amount));
                amount*=Integer.parseInt(products.get(position).getPrice());
                holder.product_total_price.setText(String.valueOf(amount));
            }
        });
        holder.decrease_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp = holder.product_quantity.getText().toString();
                int amount = Integer.parseInt(temp)-1;
                holder.product_quantity.setText(String.valueOf(amount));
                db.edit_cart_product(db.get_current_user().getEmail(),name,String.valueOf(amount));
                if(amount==0){
                    products.remove(position);
                    notifyDataSetChanged();
                }
                else{
                    amount*=Integer.parseInt(products.get(position).getPrice());
                    holder.product_total_price.setText(String.valueOf(amount));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

}

















