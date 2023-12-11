package com.ahmed.fynd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class FyndDatabase extends SQLiteOpenHelper {
    private static final int VERSION=1;
    private static final String DATABASE_NAME="FYND.db";

    private static final String TABLE_USER="user";
    private static final String TABLE_PRODUCT="product";
    private static final String TABLE_CATEGORY="category";
    private static final String TABLE_ORDER="orders";
    private static final String TABLE_ORDER_PRODUCTS="order_products";
    private static final String TABLE_CART_PRODUCTS="cart_products";
    private static final String TABLE_CURRENT_USER="current_user";

    private static final String USER_NAME="name";
    private static final String USER_EMAIL="email";
    private static final String USER_PASSWORD="password";
    private static final String USER_BIRTHDATE="birthdate";
    private static final String USER_RECOVERY_QUESTION="recovery_question";
    private static final String USER_RECOVERY_ANSWER="recovery_answer";
    private static final String USER_ADMIN="admin";

    private static final String PRODUCT_NAME="name";
    private static final String PRODUCT_CATEGORY_NAME="category";
    private static final String PRODUCT_PRICE="price";
    private static final String PRODUCT_QUANTITY="quantity";
    private static final String PRODUCT_IMAGE_BYTE="image";
    private static final String PRODUCT_SALES="sales";

    private static final String CATEGORY_NAME="name";
    private static final String CATEGORY_IMAGE_BYTE="image";

    private static final String ORDER_ID="id";
    private static final String ORDER_PRICE="price";
    private static final String ORDER_LOCATION="location";
    private static final String ORDER_RATING="rating";
    private static final String ORDER_FEEDBACK="feedback";
    private static final String ORDER_EMAIL="email";

    private static final String CART_PRODUCTS_USER_EMAIL="email";
    private static final String CART_PRODUCTS_PRODUCT_NAME="product_name";
    private static final String CART_PRODUCTS_QUANTITY="product_quantity";

    private static final String ORDER_PRODUCTS_ORDER_ID="order_id";
    private static final String ORDER_PRODUCTS_PRODUCT_NAME="product_name";
    private static final String ORDER_PRODUCTS_QUANTITY="product_quantity";

    private static final String CURRENT_USER_EMAIL="email";
    private static final String CURRENT_USER_REMEMBER="remember";


    String CREATE_USER_TABLE="CREATE TABLE "+TABLE_USER+"("
            +USER_EMAIL+" TEXT PRIMARY KEY,"
            +USER_NAME+" TEXT,"
            +USER_PASSWORD+" TEXT,"
            +USER_BIRTHDATE+" TEXT,"
            +USER_RECOVERY_QUESTION+" TEXT,"
            +USER_RECOVERY_ANSWER+" TEXT,"
            +USER_ADMIN+" TEXT"+")";

    String CREATE_PRODUCT_TABLE="CREATE TABLE "+TABLE_PRODUCT+"("
            +PRODUCT_NAME+" TEXT PRIMARY KEY,"
            +PRODUCT_CATEGORY_NAME+" TEXT,"
            +PRODUCT_PRICE+" TEXT,"
            +PRODUCT_IMAGE_BYTE+" BLOB,"
            +PRODUCT_QUANTITY+" TEXT,"
            +PRODUCT_SALES+" TEXT"+")";

    String CREATE_CATEGORY_TABLE="CREATE TABLE "+TABLE_CATEGORY+"("
            +CATEGORY_NAME+" TEXT PRIMARY KEY,"
            +CATEGORY_IMAGE_BYTE+" BLOB"+")";

    String CREATE_ORDERS_TABLE="CREATE TABLE "+TABLE_ORDER+"("
            +ORDER_ID+" TEXT PRIMARY KEY,"
            +ORDER_PRICE+" TEXT,"
            +ORDER_LOCATION+" TEXT,"
            +ORDER_RATING+" TEXT,"
            +ORDER_FEEDBACK+" TEXT,"
            +ORDER_EMAIL+" TEXT"+")";

    String CREATE_CART_PRODUCTS_TABLE="CREATE TABLE "+TABLE_CART_PRODUCTS+"("
            +CART_PRODUCTS_USER_EMAIL+" TEXT NOT NULL,"
            +CART_PRODUCTS_PRODUCT_NAME+" TEXT NOT NULL,"
            +CART_PRODUCTS_QUANTITY+" TEXT,"
            +"PRIMARY KEY"+"("+CART_PRODUCTS_QUANTITY+","+CART_PRODUCTS_PRODUCT_NAME+")"
            +")";


    String CREATE_ORDER_PRODUCTS_TABLE="CREATE TABLE "+TABLE_ORDER_PRODUCTS+"("
            +ORDER_PRODUCTS_ORDER_ID+" TEXT PRIMARY KEY,"
            +ORDER_PRODUCTS_PRODUCT_NAME+" TEXT,"
            +ORDER_PRODUCTS_QUANTITY+" TEXT"+")";

    String CREATE_CURRENT_USER_TABLE="CREATE TABLE "+TABLE_CURRENT_USER+"("
            +CURRENT_USER_EMAIL+" TEXT PRIMARY KEY,"
            +CURRENT_USER_REMEMBER+" TEXT"+")";


    public FyndDatabase(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_PRODUCT_TABLE);
        db.execSQL(CREATE_ORDERS_TABLE);
        db.execSQL(CREATE_CATEGORY_TABLE);
        db.execSQL(CREATE_ORDER_PRODUCTS_TABLE);
        db.execSQL(CREATE_CART_PRODUCTS_TABLE);
        db.execSQL(CREATE_CURRENT_USER_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_PRODUCT);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_ORDER);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_CART_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_ORDER_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_CURRENT_USER);
        onCreate(db);
    }

    public Cursor fetch_table(String table_name)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        String FETCH_TABLE="SELECT * FROM "+table_name;
        Cursor values;
        values=db.rawQuery(FETCH_TABLE,null);
        values.moveToFirst();
        db.close();
        return values;
    }

    public boolean this_user_exists(String email)
    {
        Cursor values=fetch_table(TABLE_USER);
        String db_emails;
        if(values.moveToFirst())
        {
            do
            {
                db_emails=values.getString(0);
                if(db_emails.equals(email))
                    return true;

            }   while(values.moveToNext());

        }
        return false;
    }

    public boolean register_a_user(User user)
    {
        if(this_user_exists(user.getEmail()))
            return false;
        else
        {
            SQLiteDatabase db=this.getWritableDatabase();
            ContentValues values=new ContentValues();
            values.put(USER_NAME,user.getName());
            values.put(USER_PASSWORD,user.getPassword());
            values.put(USER_EMAIL, user.getEmail());
            values.put(USER_BIRTHDATE, user.getBirthDate());
            values.put(USER_RECOVERY_QUESTION, user.getRecoveryQuestion());
            values.put(USER_RECOVERY_ANSWER, user.getRecoveryAnswer());
            values.put(USER_ADMIN,user.getAdmin());
            long success = db.insert(TABLE_USER,null,values);
            db.close();
            if(success==-1){
                return false;
            }
            return true;
        }
    }

    public boolean login(String email,String password)
    {
        Cursor values=fetch_table(TABLE_USER);
        String db_emails,db_passwords;
        if(values.moveToFirst())
        {
            do
            {
                db_emails=values.getString(0);
                db_passwords=values.getString(2);
                if(email.equals(db_emails)&&password.equals(db_passwords))
                {
                    return true;
                }

            }while(values.moveToNext());
        }
        return false;
    }

    public ArrayList<Product> get_all_products()
    {
        ArrayList products_list=new ArrayList<Product>();
        String PRODUCT_NAME;
        String PRODUCT_CATEGORY;
        byte[] PRODUCT_IMAGE;
        String PRODUCT_PRICE;
        String PRODUCT_QUANTITY;
        String PRODUCT_SALES;
        Cursor values=fetch_table(TABLE_PRODUCT);

        if(values.moveToFirst())
        {
            do
            {
                PRODUCT_NAME=values.getString(0);
                PRODUCT_CATEGORY=values.getString(1);
                PRODUCT_PRICE=values.getString(2);
                PRODUCT_IMAGE=values.getBlob(3);
                PRODUCT_QUANTITY=values.getString(4);
                PRODUCT_SALES=values.getString(5);
                Product product=new Product(PRODUCT_NAME,PRODUCT_CATEGORY,PRODUCT_PRICE,PRODUCT_QUANTITY,PRODUCT_IMAGE,PRODUCT_SALES);
                products_list.add(product);
            }while(values.moveToNext());
        }
        return products_list;
    }

    public ArrayList<Product> get_cart_products(String userEmail)
    {
        ArrayList products_list=new ArrayList<Product>();

        String EMAIL;

        String PRODUCT_NAME;
        String PRODUCT_CATEGORY;
        byte[] PRODUCT_IMAGE;
        String PRODUCT_PRICE;
        String PRODUCT_QUANTITY;
        String PRODUCT_SALES;

        Cursor cart=fetch_table(TABLE_CART_PRODUCTS);

        if(cart.moveToFirst())
        {
            do
            {
                //email name quantity, price image
                EMAIL = cart.getString(0);
                if(EMAIL.equals(userEmail)) {

                    PRODUCT_NAME = cart.getString(1);
                    PRODUCT_QUANTITY = cart.getString(2);

                    Cursor values = fetch_instances(PRODUCT_NAME,TABLE_PRODUCT, FyndDatabase.PRODUCT_NAME);
                    if(values.moveToFirst()) {
                        PRODUCT_CATEGORY = values.getString(1);
                        PRODUCT_PRICE = values.getString(2);
                        PRODUCT_IMAGE = values.getBlob(3);
                        PRODUCT_SALES = values.getString(5);

                        Product product = new Product(PRODUCT_NAME, PRODUCT_CATEGORY, PRODUCT_PRICE, PRODUCT_QUANTITY, PRODUCT_IMAGE, PRODUCT_SALES);
                        products_list.add(product);
                    }
                }
            }while(cart.moveToNext());
        }
        return products_list;
    }

    public ArrayList<Order> get_all_orders()
    {
        ArrayList orders_list=new ArrayList<Order>();
        String ORDER_ID;
        String ORDER_PRICE;
        String ORDER_LOCATION;
        String ORDER_RATING;
        String ORDER_FEEDBACK;
        String ORDER_EMAIL;
        Cursor values=fetch_table(TABLE_ORDER);

        if(values.moveToFirst())
        {
            do
            {
                ORDER_ID=values.getString(0);
                ORDER_PRICE=values.getString(1);
                ORDER_LOCATION=values.getString(2);
                ORDER_RATING=values.getString(3);
                ORDER_FEEDBACK=values.getString(4);
                ORDER_EMAIL=values.getString(5);
                Order order=new Order(ORDER_ID,ORDER_PRICE,ORDER_LOCATION,ORDER_RATING,ORDER_FEEDBACK,ORDER_EMAIL);
                orders_list.add(order);
            }while(values.moveToNext());
        }
        return orders_list;
    }



    public ArrayList<Category> get_all_categories()
    {
        ArrayList categories_list=new ArrayList<Category>();
        String CATEGORY_NAME;
        byte[] CATEGORY_IMAGE;
        Cursor values=fetch_table(TABLE_CATEGORY);

        if(values.moveToFirst())
        {
            do
            {
                CATEGORY_NAME=values.getString(0);
                CATEGORY_IMAGE=values.getBlob(1);
                Category category=new Category(CATEGORY_NAME,CATEGORY_IMAGE);
                categories_list.add(category);
            }while(values.moveToNext());
        }
        return categories_list;
    }

    public Cursor fetch_instances(String value, String table_name, String primary_key)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        String FETCH_INSTANCES="SELECT * FROM "+table_name+" WHERE "+primary_key+" LIKE ?";
        Cursor instances;
        instances=db.rawQuery(FETCH_INSTANCES,new String[]{value});
        return instances;

    }

    public Product search_product(String product_name)
    {
        Cursor values=fetch_instances(product_name,TABLE_PRODUCT,PRODUCT_NAME);
        values.moveToFirst();
        String PRODUCT_NAME=values.getString(0);
        String PRODUCT_CATEGORY=values.getString(1);
        String PRODUCT_PRICE=values.getString(2);
        byte[] PRODUCT_IMAGE=values.getBlob(3);
        String PRODUCT_QUANTITY=values.getString(4);
        String PRODUCT_SALES=values.getString(5);
        Product product=new Product(PRODUCT_NAME,PRODUCT_CATEGORY,PRODUCT_PRICE,PRODUCT_QUANTITY,PRODUCT_IMAGE,PRODUCT_SALES);
        return product;
    }

    public void set_current_user(CurrentUser user){

        SQLiteDatabase db = this.getWritableDatabase();
        delete_table(TABLE_CURRENT_USER);

        ContentValues values=new ContentValues();
        values.put(CURRENT_USER_EMAIL,user.getEmail());
        values.put(CURRENT_USER_REMEMBER,user.getRemember());
        db.insert(TABLE_CURRENT_USER,null,values);
        db.close();
    }
    public void delete_table(String table_name){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ table_name);
    }
    public User get_remembered_user(){

        Cursor current_values=fetch_instances("y",TABLE_CURRENT_USER,CURRENT_USER_REMEMBER);
        current_values.moveToFirst();
        String CURRENT_USER_EMAIL=current_values.getString(0);

        User user = get_user(CURRENT_USER_EMAIL);
        return user;

    }
    public User get_user(String email){

        Cursor user_values = fetch_instances(email, TABLE_USER, USER_EMAIL);
        user_values.moveToFirst();
        String USER_EMAIL=user_values.getString(0);
        String USER_NAME=user_values.getString(1);
        String USER_PASSWORD=user_values.getString(2);
        String USER_BIRTHDATE=user_values.getString(3);
        String USER_RECOVERY_QUESTION=user_values.getString(4);
        String USER_RECOVERY_ANSWER=user_values.getString(5);
        String USER_ADMIN=user_values.getString(6);

        User user=new User(USER_EMAIL,USER_NAME,USER_PASSWORD,USER_BIRTHDATE, USER_RECOVERY_QUESTION,USER_RECOVERY_ANSWER,USER_ADMIN);
        return user;
    }

    public CurrentUser get_current_user(){

        Cursor user_values = fetch_table(TABLE_CURRENT_USER);
        user_values.moveToFirst();

        String USER_EMAIL=user_values.getString(0);
        String USER_REMEMBER=user_values.getString(1);

        CurrentUser current_user=new CurrentUser(USER_EMAIL,USER_REMEMBER);
        return current_user;
    }


    public boolean add_to_cart(String email, String product_name, String quantity)
    {
        try{
            SQLiteDatabase db=this.getWritableDatabase();
            ContentValues values=new ContentValues();
            values.put(CART_PRODUCTS_USER_EMAIL,email);
            values.put(CART_PRODUCTS_PRODUCT_NAME,product_name);
            values.put(CART_PRODUCTS_QUANTITY,quantity);

            db.insert(TABLE_CART_PRODUCTS,null,values);
            db.close();
        }
        catch(Exception e){
            return false;
        }
        return true;
    }


    public void edit_cart_product(String email, String product, String quantity)
    {
        int q = Integer.parseInt(quantity);
        SQLiteDatabase db=this.getWritableDatabase();

        if(q==0){
            delete_cart_product(email,product);
        }
        else {
            ContentValues values = new ContentValues();
            values.put(CART_PRODUCTS_QUANTITY, quantity);
            String whereClause = CART_PRODUCTS_USER_EMAIL+" = ? AND +"+CART_PRODUCTS_PRODUCT_NAME+" = ?";
            String[] whereArgs = {email,product};
            db.update(TABLE_CART_PRODUCTS, values, whereClause, whereArgs);
        }


    }

    public void delete_instance(String value, String table_name, String column_name)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(table_name,column_name+" like ?",new String[]{value});
    }


    public void delete_cart_product(String email,String product_name){
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = CART_PRODUCTS_USER_EMAIL+" = ? AND "+CART_PRODUCTS_PRODUCT_NAME +" = ?";
        String[] whereArgs = {email, product_name};
        db.delete(TABLE_CART_PRODUCTS, whereClause, whereArgs);
    }

    public void add_product(Product p)
    {
            SQLiteDatabase db=this.getWritableDatabase();
            ContentValues values=new ContentValues();
            values.put(PRODUCT_NAME,p.getName());
            values.put(PRODUCT_CATEGORY_NAME,p.getCategory());
            values.put(PRODUCT_PRICE,p.getPrice());
            values.put(PRODUCT_QUANTITY,p.getQuantity());
            values.put(PRODUCT_IMAGE_BYTE,p.getImage());
            values.put(PRODUCT_SALES,p.getSales());
            db.insert(TABLE_PRODUCT,null,values);
            db.close();
    }

    public void add_category(Category c)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(CATEGORY_NAME,c.getName());
        values.put(CATEGORY_IMAGE_BYTE,c.getImage());
        db.insert(TABLE_CATEGORY,null,values);
        db.close();
    }

    public void edit_product(String name, String category, String price, String quantity, byte[] image, String sales)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PRODUCT_NAME, name);
        values.put(PRODUCT_CATEGORY_NAME, category);
        values.put(PRODUCT_PRICE, price);
        values.put(PRODUCT_QUANTITY, quantity);
        values.put(PRODUCT_IMAGE_BYTE, image);
        values.put(PRODUCT_SALES,sales);
        db.update(TABLE_PRODUCT, values, PRODUCT_NAME + " like ?", new String[]{name});
    }

    public void edit_category(String name, byte[] image)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CATEGORY_NAME, name);
        values.put(CATEGORY_IMAGE_BYTE, image);
        db.update(TABLE_CATEGORY, values, CATEGORY_NAME + " like ?", new String[]{name});
    }

    public void delete_product(String product)
    {
        delete_instance(product, TABLE_PRODUCT, PRODUCT_NAME);
    }

    public void delete_category(String category)
    {
        delete_instance(category, TABLE_CATEGORY, CATEGORY_NAME);
    }


    public boolean confirm_order(String price, String location, String rating, String feedback, String email, Product[] products, int[] quantities)
    {
            SQLiteDatabase db=this.getWritableDatabase();
            ContentValues values=new ContentValues();

            int id = get_highest_order_id()+1;
            values.put(ORDER_ID,id);
            values.put(ORDER_PRICE,price);
            values.put(ORDER_LOCATION,location);
            values.put(ORDER_RATING,rating);
            values.put(ORDER_FEEDBACK,feedback);
            values.put(ORDER_EMAIL,email);
            db.insert(TABLE_ORDER,null,values);
            db.close();

            for(int i = 0;i< products.length;i++){
                add_order_products(products[i].getName(), String.valueOf(quantities[i]),String.valueOf(id));
            }
            return true;
    }

    public boolean add_order_products(String product, String quantity, String order_id){

        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();

        values.put(ORDER_PRODUCTS_PRODUCT_NAME,product);
        values.put(ORDER_PRODUCTS_QUANTITY,quantity);
        values.put(ORDER_PRODUCTS_ORDER_ID,order_id);
        db.insert(TABLE_ORDER_PRODUCTS,null,values);
        db.close();
        return true;
    }
    public int get_highest_order_id()
    {
        ArrayList<Order> order_list=get_all_orders();
        int highest=-1;
        for(int i=0;i<order_list.size();i++)
        {
            if(Integer.parseInt(order_list.get(i).getId())>highest)
                highest=Integer.parseInt(order_list.get(i).getId());
        }
        return highest;
    }



    public ArrayList<Product> top_selling_products(){

        ArrayList products_list=new ArrayList<Product>();
        String PRODUCT_NAME;
        String PRODUCT_CATEGORY;
        byte[] PRODUCT_IMAGE;
        String PRODUCT_PRICE;
        String PRODUCT_QUANTITY;
        String PRODUCT_SALES;

        SQLiteDatabase db=this.getReadableDatabase();
        String FETCH_TABLE="SELECT * FROM "+TABLE_PRODUCT+" ORDER BY "+this.PRODUCT_SALES+" DESC";
        Cursor values;
        values=db.rawQuery(FETCH_TABLE,null);
        values.moveToFirst();
        db.close();

        if(values.moveToFirst())
        {
            do
            {
                PRODUCT_NAME=values.getString(0);
                PRODUCT_CATEGORY=values.getString(1);
                PRODUCT_PRICE=values.getString(2);
                PRODUCT_IMAGE=values.getBlob(3);
                PRODUCT_QUANTITY=values.getString(4);
                PRODUCT_SALES=values.getString(5);
                Product product=new Product(PRODUCT_NAME,PRODUCT_CATEGORY,PRODUCT_PRICE,PRODUCT_QUANTITY,PRODUCT_IMAGE,PRODUCT_SALES);
                products_list.add(product);
            }while(values.moveToNext());
        }
        return products_list;
    }

    // for debugging only
    public void delete_all_databases(){
        delete_table(TABLE_CURRENT_USER);
        delete_table(TABLE_USER);
        delete_table(TABLE_PRODUCT);
        delete_table(TABLE_ORDER_PRODUCTS);
        delete_table(TABLE_CART_PRODUCTS);
        delete_table(TABLE_CATEGORY);
        delete_table(TABLE_ORDER);
    }

}










