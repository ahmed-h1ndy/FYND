package com.ahmed.fynd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.ahmed.fynd.databinding.ActivityCartBinding;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    ActivityCartBinding b;
    CartAdapter adapter;
    ArrayList<Product> products;
    FyndDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        db = new FyndDatabase(this);
        fill_products();
        adapter = new CartAdapter(this,products);
        b.cartRecycler.setAdapter(adapter);
        b.cartRecycler.setLayoutManager(new LinearLayoutManager(this));

    }

    private void fill_products() {
        CurrentUser c = db.get_current_user();
        products = db.get_cart_products(c.getEmail());
    }
}