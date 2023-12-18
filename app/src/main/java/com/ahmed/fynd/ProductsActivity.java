package com.ahmed.fynd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ahmed.fynd.databinding.ActivityLoginBinding;
import com.ahmed.fynd.databinding.ActivityProductsBinding;

import java.util.ArrayList;

public class ProductsActivity extends AppCompatActivity {

    ActivityProductsBinding b;
    FyndDatabase db;
    ProductAdapter products_adapter;
    ArrayList<Product> products;
    String category;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        b = ActivityProductsBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        db = new FyndDatabase(this);
        Intent i = getIntent();
        category = i.getStringExtra("category");
        fill_products();

        products_adapter = new ProductAdapter(this,products);
        b.productsRecycler.setAdapter(products_adapter);
        b.productsRecycler.setLayoutManager(new GridLayoutManager(this,2));

        b.goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(i);
            }
        });

        b.productCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), CartActivity.class);
                startActivity(i);
            }
        });
    }

    private void fill_products() {
        ArrayList<Product> p = db.get_all_products();
        products = new ArrayList<Product>();
        for(int i =0;i<p.size();i++){
            if(p.get(i).getCategory().equals(category)){
                products.add(p.get(i));
            }
        }
    }
}