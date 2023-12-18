package com.ahmed.fynd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.ahmed.fynd.databinding.ActivityHomeBinding;
import com.ahmed.fynd.databinding.ActivityOrdersBinding;

import java.util.ArrayList;

public class OrdersActivity extends AppCompatActivity {

    ActivityOrdersBinding b;
    FyndDatabase db;
    OrdersAdapter adapter;
    ArrayList<Order> orders;
    User u;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        b = ActivityOrdersBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        db = new FyndDatabase(this);
        u = db.get_user(db.get_current_user().getEmail());
        fill_orders_list();

        adapter = new OrdersAdapter(this, orders);
        b.ordersRecycler.setAdapter(adapter);
        b.ordersRecycler.setLayoutManager(new LinearLayoutManager(this));
        b.ordersGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(i);
            }
        });
    }

    private void fill_orders_list() {
        ArrayList<Order> o = db.get_all_orders();
        orders = new ArrayList<>();
        if(!u.getAdmin().equals("y")){
            for(int i= 0;i<o.size();i++){
                Log.i("orders stuff","filling orders loop");
                Log.i("orders stuff","email 1:"+o.get(i).getUserEmail());
                Log.i("orders stuff","email 2:"+u.getEmail());
                if(o.get(i).getUserEmail().equals(u.getEmail())){
                    Log.i("orders stuff","filling orders condition");
                    orders.add(o.get(i));
                }
            }
        }
        else{
            orders = o;
        }
    }
}