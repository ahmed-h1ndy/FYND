package com.ahmed.fynd;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.MyViewHolder>{

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView id, email, price, feedback, rating;
        EditText feedback_input, rating_input;
        Button rate_button;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            id = itemView.findViewById(R.id.order_id);
            email = itemView.findViewById(R.id.order_email);
            price = itemView.findViewById(R.id.order_price);
            feedback = itemView.findViewById(R.id.order_feedback);
            rating = itemView.findViewById(R.id.order_rating);
            feedback_input = itemView.findViewById(R.id.order_feedback_value);
            rating_input = itemView.findViewById(R.id.order_rating_value);
            rate_button = itemView.findViewById(R.id.order_rate_button);
        }
    }

    Context context;
    ArrayList<Order> orders;
    FyndDatabase db;
    User u;
    public OrdersAdapter(Context context, ArrayList<Order> orders){
        this.context=context;
        this.orders=orders;
        db = new FyndDatabase(context);
        u = db.get_user(db.get_current_user().getEmail());
    }
    @NonNull
    @Override
    public OrdersAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.order_view,parent,false);
        return new OrdersAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersAdapter.MyViewHolder holder, int position) {

        int id = orders.get(position).getId();
        String email = orders.get(position).getUserEmail();
        String price = orders.get(position).getPrice();
        String feedback = orders.get(position).getFeedback();
        String rating = orders.get(position).getRating();

        final String[] feedback_input = new String[1];
        final String[] rating_input = new String[1];

        holder.id.setText("ID: "+String.valueOf(id));
        holder.email.setText("Mail: "+email);
        holder.price.setText("Price: "+price+"$");
        holder.feedback.setText("Feedback: "+feedback);
        holder.rating.setText("Rating: "+rating+"/5");

        if(u.getAdmin().equals("y")){
            holder.feedback_input.setVisibility(View.GONE);
            holder.rating_input.setVisibility(View.GONE);
            holder.rate_button.setVisibility(View.GONE);
        }
        else{

            if(!feedback.isEmpty()){
                holder.feedback_input.setVisibility(View.GONE);
                holder.rating_input.setVisibility(View.GONE);
                holder.rate_button.setVisibility(View.GONE);
            }
            else {
                holder.rate_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        feedback_input[0] = holder.feedback_input.getText().toString();
                        rating_input[0] = holder.rating_input.getText().toString();

                        if(feedback_input[0].isEmpty()||rating_input[0].isEmpty()){
                            Toast.makeText(context, "feedback or rating is empty",Toast.LENGTH_SHORT).show();
                        }
                        else{
                        holder.feedback.setText(feedback_input[0]);
                        holder.rating.setText(rating_input[0]);

                        holder.feedback_input.setVisibility(View.GONE);
                        holder.rating_input.setVisibility(View.GONE);
                        holder.rate_button.setVisibility(View.GONE);

                        Order o = new Order(id, price, rating_input[0], feedback_input[0], email);
                        db.edit_order(o);
                        }
                    }
                });
            }
        }



    }

    @Override
    public int getItemCount() {
        return orders.size();
    }
}
