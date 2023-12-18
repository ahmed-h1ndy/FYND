package com.ahmed.fynd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ahmed.fynd.databinding.ActivityCartBinding;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    ActivityCartBinding b;
    CartAdapter adapter;
    ArrayList<Product> products;
    FyndDatabase db;
    CurrentUser c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        db = new FyndDatabase(this);
        c = db.get_current_user();
        fill_products();
        adapter = new CartAdapter(this,products);
        b.cartRecycler.setAdapter(adapter);
        b.cartRecycler.setLayoutManager(new LinearLayoutManager(this));

        b.cartOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_order_total();
            }
        });

        b.cartClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete_all_cart_products();
            }
        });

        b.cartGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(i);
            }
        });

    }

    private void show_order_total() {
        Dialog d = new Dialog(this);
        d.setContentView(R.layout.order_confirm);

        Button confirm_button = d.findViewById(R.id.final_order_button);
        TextView total_fee = d.findViewById(R.id.total_fee_value);

        int total = 0,price,quantity;
        for(int i =0;i<products.size();i++){

            price = Integer.parseInt(products.get(i).getPrice());
            quantity = Integer.parseInt(products.get(i).getQuantity());
            total+=(price*quantity);
        }
        total_fee.setText(String.valueOf(total));
        confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order();
                delete_all_cart_products();
            }
        });
        d.show();
    }

    private void order() {
        int total = 0,price,quantity;
        for(int i =0;i<products.size();i++){
            price = Integer.parseInt(products.get(i).getPrice());
            quantity = Integer.parseInt(products.get(i).getQuantity());
            total+=(price*quantity);
            edit_product(products.get(i),quantity);
            Log.i("order_quantity", String.valueOf(quantity));
        }
        Log.i("order_total", String.valueOf(total));
        Order o = new Order(-1,String.valueOf(total),"","",c.getEmail());
        db.confirm_order(o);
    }

    private void delete_all_cart_products() {

        Product p;
        int quantity=0,sales=0;
        String product_name;
        for(int i =0;i<products.size();i++){
            product_name = products.get(i).getName();
            CartProduct cp = new CartProduct(product_name,c.getEmail(),"-1");
            db.delete_cart_product(cp);
        }
        products.clear();
        adapter.notifyDataSetChanged();
    }

    private Product get_product(String name){
        for(int i = 0;i<products.size();i++){
            if(products.get(i).getName().equals(name)){
                return products.get(i);
            }
        }
        return null;
    }

    private void edit_product(Product p, int sales){

        int quantity = Integer.parseInt(p.getQuantity())-sales;
        sales += Integer.parseInt(p.getSales());
        p.setQuantity(String.valueOf(quantity));
        p.setSales(String.valueOf(sales));
        db.edit_product(p);
    }

    private void fill_products() {
        CurrentUser c = db.get_current_user();
        products = db.get_cart_products(c.getEmail());
    }
}