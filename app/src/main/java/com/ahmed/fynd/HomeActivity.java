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
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ahmed.fynd.databinding.ActivityHomeBinding;

import java.util.ArrayList;
import java.util.Locale;

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
    SpeechRecognizer speechRecognizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        db = new FyndDatabase(this);
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);








        CurrentUser c = db.get_current_user();
        User u = db.get_user(c.getEmail());
        if(u.getAdmin().equals("n")){
            b.addProductButton.setVisibility(View.GONE);
            b.addCategoryButton.setVisibility(View.GONE);
            b.homeEditCategoryButton.setVisibility(View.GONE);
            b.homeEditProductButton.setVisibility(View.GONE);
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
        b.homeAppbar.chartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ChartActivity.class);
                startActivity(i);
            }
        });
        b.homeAppbar.logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentUser c = db.get_current_user();
                c.setRemember("n");
                db.set_current_user(c);
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
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
                startSpeechRecognition();
            }
        });

        b.homeAppbar.previousOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), OrdersActivity.class);
                startActivity(i);
            }
        });

        b.homeSearchBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        b.homeEditProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_product();
            }
        });

        b.homeEditCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_category();
            }
        });

    }


    private void startSpeechRecognition() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "start speaking");

        // Start speech recognition
        //speechRecognizer.startListening(intent);
        startActivityForResult(intent, 100);
    }


    protected void onDestroy() {
        super.onDestroy();
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
    }



    private void populate_products_with_search(String search) {

        ArrayList<Product> p = db.get_all_products();
        ArrayList<Product> result = new ArrayList<Product>();
        for(int i =0;i<p.size();i++){
            if(p.get(i).getName().contains(search)){
                result.add(p.get(i));
            }
        }
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

        if (requestCode == 100&&resultCode==RESULT_OK) {
            String search_query = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0);
            populate_products_with_search(search_query);
            b.homeSearchText.setText(search_query);
        } else {
            if (resultCode == RESULT_OK && data != null) {
                Uri selected_image = data.getData();
                added_image = HelperFunctions.convertGalleryImageToByteArray(selected_image, getApplicationContext());

                Bitmap bmp = BitmapFactory.decodeByteArray(added_image, 0, added_image.length);
                dialog_image.setImageBitmap(bmp);
            }
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
        Button delete_product_button = d.findViewById(R.id.delete_product_button);

        delete_product_button.setVisibility(View.GONE);

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
        Button delete_category_button = d.findViewById(R.id.delete_category_button);

        delete_category_button.setVisibility(View.GONE);

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



























    public void edit_product(){

        d = new Dialog(this);
        d.setContentView(R.layout.forgot_password_popup);

        TextView name = d.findViewById(R.id.question);
        EditText name_value = d.findViewById(R.id.answer);
        Button edit = d.findViewById(R.id.recover_button);

        name.setText("Enter product name");
        edit.setText("Edit");

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean found=false;
                for(int i =0;i<products.size();i++){
                    if(products.get(i).getName().equals(name_value.getText().toString())){
                        found = true;


                        d.setContentView(R.layout.add_product);


                        dialog_image = d.findViewById(R.id.add_product_image);
                        EditText product_name = d.findViewById(R.id.add_product_name);
                        EditText product_category = d.findViewById(R.id.add_product_category);
                        EditText product_price = d.findViewById(R.id.add_product_price);
                        EditText product_quantity = d.findViewById(R.id.add_product_quantity);
                        Button edit_product_button = d.findViewById(R.id.confirm_product_button);
                        Button delete_product_button = d.findViewById(R.id.delete_product_button);

                        byte[] img = products.get(i).getImage();
                        Bitmap bmp = BitmapFactory.decodeByteArray(img,0,img.length);
                        dialog_image.setImageBitmap(bmp);

                        product_name.setText(products.get(i).getName());
                        product_category.setText(products.get(i).getCategory());
                        product_price.setText(products.get(i).getPrice());
                        product_quantity.setText(products.get(i).getQuantity());
                        edit_product_button.setText("Edit Product");

                        product_name.setEnabled(false);

                        dialog_image.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(intent,3);
                            }
                        });

                        int finalI1 = i;
                        int finalI2 = i;
                        edit_product_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String name = product_name.getText().toString();
                                String price = product_price.getText().toString();
                                String category = product_category.getText().toString();
                                String quantity = product_quantity.getText().toString();
                                added_image = products.get(finalI2).getImage();
                                if(price.isEmpty()||category.isEmpty()||quantity.isEmpty()){
                                    Toast.makeText(getApplicationContext(),"one of the fields are empty!",Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Product p = new Product(name,category,price,quantity,added_image,"0");
                                    db.edit_product(p);
                                    products.remove(finalI1);
                                    products.add(p);
                                    products_adapter.notifyDataSetChanged();
                                }
                                d.dismiss();
                            }
                        });

                        int finalI = i;
                        delete_product_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                db.delete_product(products.get(finalI).getName());
                                products.remove(finalI);
                                products_adapter.notifyDataSetChanged();
                            }
                        });


                    }
                }
                if(!found){
                    Toast.makeText(getApplicationContext(), "Invalid name",Toast.LENGTH_SHORT).show();
                }
            }
        });

        d.show();
    }

    public void edit_category(){


        d = new Dialog(this);
        d.setContentView(R.layout.forgot_password_popup);

        TextView name = d.findViewById(R.id.question);
        EditText name_value = d.findViewById(R.id.answer);
        Button edit = d.findViewById(R.id.recover_button);

        name.setText("Enter Category name");
        edit.setText("Edit");

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean found=false;
                for(int i =0;i<categories.size();i++){
                    if(categories.get(i).getName().equals(name_value.getText().toString())){
                        found = true;


                        d.setContentView(R.layout.add_category);


                        dialog_image = d.findViewById(R.id.add_category_image);
                        EditText category_name = d.findViewById(R.id.add_category_name);
                        Button edit_category_button = d.findViewById(R.id.confirm_category_button);
                        Button delete_category_button = d.findViewById(R.id.delete_category_button);

                        byte[] img = categories.get(i).getImage();
                        Bitmap bmp = BitmapFactory.decodeByteArray(img,0,img.length);
                        dialog_image.setImageBitmap(bmp);

                        category_name.setText(categories.get(i).getName());

                        edit_category_button.setText("Edit Category");

                        category_name.setEnabled(false);

                        dialog_image.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(intent,3);
                            }
                        });

                        int finalI1 = i;
                        edit_category_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String name = category_name.getText().toString();
                                added_image = categories.get(finalI1).getImage();
                                if(name.isEmpty()||added_image==null){
                                    Toast.makeText(getApplicationContext(),"one of the fields are empty!",Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Category c = new Category(name, added_image);
                                    db.edit_category(c);
                                    categories.remove(finalI1);
                                    categories.add(c);
                                    categories_adapter.notifyDataSetChanged();
                                }
                                d.dismiss();
                            }
                        });

                        int finalI = i;
                        delete_category_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                db.delete_category(categories.get(finalI).getName());
                                categories.remove(finalI);
                                categories_adapter.notifyDataSetChanged();
                            }
                        });


                    }
                }
                if(!found){
                    Toast.makeText(getApplicationContext(), "Invalid name",Toast.LENGTH_SHORT).show();
                }
            }
        });

        d.show();
    }
}