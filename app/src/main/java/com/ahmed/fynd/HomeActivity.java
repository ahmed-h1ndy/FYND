package com.ahmed.fynd;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ahmed.fynd.databinding.ActivityHomeBinding;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    ActivityHomeBinding b;
    FyndDatabase db;
    byte[] added_image;
    CategoryAdapter categories_adapter;
    ProductAdapter products_adapter;
    ArrayList<Category> categories;
    ArrayList<Product> products;
    Dialog d;
    ImageButton dialog_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        db = new FyndDatabase(this);

        CurrentUser c = db.get_current_user();
        User u = db.get_user(c.getEmail());
        if(u.getAdmin().equals("n")){
            b.addProductButton.setVisibility(View.GONE);
            b.addCategoryButton.setVisibility(View.GONE);
        }

        fill_categories();
        fill_products();

        categories_adapter = new CategoryAdapter(this,categories);
        products_adapter = new ProductAdapter(this,products);

        b.homeCategories.setAdapter(categories_adapter);
        b.homeProducts.setAdapter(products_adapter);

        b.homeCategories.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        b.homeProducts.setLayoutManager(new GridLayoutManager(this,2));

        b.addCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_category();
            }
        });

        b.addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_product();
            }
        });

        b.homeAppbar.goToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),CartActivity.class);
                startActivity(i);
            }
        });

        b.homeSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String search = b.homeSearchText.getText().toString();
                populate_products_with_search(search);
            }
        });

        b.homeSearchText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                String search = b.homeSearchText.getText().toString();
                populate_products_with_search(search);
                return false;
            }
        });


        b.homeSearchVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentUser c = db.get_current_user();
                c.setRemember("n");
                db.set_current_user(c);
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
            }
        });
    }

    private void populate_products_with_search(String search) {



        ArrayList<Product> p = db.get_all_products();
        ArrayList<Product> result = new ArrayList<Product>();
        Log.i("ffff","in the function");
        for(int i =0;i<p.size();i++){
            Log.i("ffff","in the loop");
            if(p.get(i).getName().contains(search)){
                Log.i("ffff","added");
                result.add(p.get(i));
            }
        }
        Log.i("ffff","out of loop");
        products.clear();

        for(int i = 0;i<result.size();i++){
            products.add(result.get(i));
        }
        products_adapter.notifyDataSetChanged();
    }

    private void fill_products() {
        products = db.get_all_products();
    }

    private void fill_categories() {
        categories = db.get_all_categories();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK&&data!=null){
            Uri selected_image = data.getData();
            added_image = HelperFunctions.convertGalleryImageToByteArray(selected_image,getApplicationContext());

            Bitmap bmp = BitmapFactory.decodeByteArray(added_image,0,added_image.length);
            dialog_image.setImageBitmap(bmp);
        }
    }

    public void add_product(){


        d = new Dialog(this);
        d.setContentView(R.layout.add_product);

        dialog_image = d.findViewById(R.id.add_product_image);
        EditText product_name = d.findViewById(R.id.add_product_name);
        EditText product_category = d.findViewById(R.id.add_product_category);
        EditText product_price = d.findViewById(R.id.add_product_price);
        EditText product_quantity = d.findViewById(R.id.add_product_quantity);
        Button add_product_button = d.findViewById(R.id.confirm_product_button);

        dialog_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,3);
            }
        });



        add_product_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = product_name.getText().toString();
                String price = product_price.getText().toString();
                String category = product_category.getText().toString();
                String quantity = product_quantity.getText().toString();
                if(name.isEmpty()||price.isEmpty()||category.isEmpty()||quantity.isEmpty()||added_image==null){
                    Toast.makeText(getApplicationContext(),"one of the fields are empty!",Toast.LENGTH_SHORT).show();
                }
                else{
                    Product p = new Product(name,category,price,quantity,added_image,"0");
                    db.add_product(p);
                    products.add(p);
                    products_adapter.notifyDataSetChanged();
                }
                d.dismiss();
            }
        });
        d.show();
    }

    public void add_category(){


        d = new Dialog(this);
        d.setContentView(R.layout.add_category);

        dialog_image = d.findViewById(R.id.add_category_image);
        EditText category_name = d.findViewById(R.id.add_category_name);
        Button add_category_button = d.findViewById(R.id.confirm_category_button);

        dialog_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,3);


            }
        });



        add_category_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = category_name.getText().toString();
                if(name.isEmpty()||added_image==null){
                    Toast.makeText(getApplicationContext(),"one of the fields are empty!",Toast.LENGTH_SHORT).show();
                }
                else{
                    Category c = new Category(name,added_image);
                    db.add_category(c);
                    categories.add(c);
                    categories_adapter.notifyDataSetChanged();
                }
                d.dismiss();
            }
        });
        d.show();
    }
}