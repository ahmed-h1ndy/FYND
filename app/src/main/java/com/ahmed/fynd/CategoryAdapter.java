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

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView product_image;
        TextView product_name;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);

            //image = itemView.findViewById(R.id.imageView);
            product_name = itemView.findViewById(R.id.category_name);
            product_image = itemView.findViewById(R.id.category_image);
        }
    }

    Context context;
    ArrayList<Category> categories;
    public CategoryAdapter(Context context, ArrayList<Category> categories){
        this.context=context;
        this.categories=categories;
    }
    @NonNull
    @Override
    public CategoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.category,parent,false);
        return new CategoryAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.MyViewHolder holder, int position) {

        /*
        byte[] image_byte = data.get(position).getImage();
	    Bitmap bmp = BitmapFactory.decodeByteArray(image_byte, 0, image_byte.length);
	    prod_image.setImageBitmap(bmp);
        */
        String name = categories.get(position).getName();
        byte[] img = categories.get(position).getImage();
        Bitmap bmp = BitmapFactory.decodeByteArray(img,0,img.length);

        holder.product_image.setImageBitmap(bmp);
        holder.product_name.setText(name);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }


}



