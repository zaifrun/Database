package com.example.database;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

public class DataBase extends SQLiteOpenHelper {

	//normally put it to 1, but I changed something
	//from version 1 to 2.
	
	//This is the version of your database
	//Make sure that if you changed any tables
	//then you need to increase the version AND MAKE
	//sure you handle any changes in the onUpgrade method in this class
	private static final int DATABASE_VERSION = 2;
	
	//The name of the database file
	private static final String DATABASE_NAME = "productDB.db";
	
	//We will have one table, called "products"
	private static final String TABLE_PRODUCTS = "products";
	
	//We will have 3 columns in this table
	private static final String COLUMN_ID = "id";
	private static final String COLUMN_PRODUCTNAME = "productname";
	private static final String COLUMN_QUANTITY = "quantity";
	
	//constructor - will simply call our super class
	public DataBase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		//Creating the table and putting the column_ID to the 
		//primary key (this makes sure it will autoincrement)
		String CREATE_PRODUCTS_TABLE = "CREATE TABLE " +
	             TABLE_PRODUCTS + "("
	             + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_PRODUCTNAME 
	             + " TEXT," + COLUMN_QUANTITY + " INTEGER" + ")";
	      db.execSQL(CREATE_PRODUCTS_TABLE);
	}

	//This is not the best way to handle database upgrades, but it
	//is simple. The drawback here is that if a user downloads an update
	//to our app and the database version has changed in that version,
	//then the effect will be to lose all the current data and
	//simply recreate the database from scratch. A better choice
	//is to in the common case that the new database has any additional
	//columns is simply to Add the columns to the database.
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
		onCreate(db);
	}


	public ArrayList<Product> getAllProducts()
	{
		ArrayList<Product> list = new ArrayList<>();

		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor;
		cursor = db.rawQuery("SELECT * FROM "+TABLE_PRODUCTS+" ORDER BY id ASC",null);
		while (cursor.moveToNext())
		{
			int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
			int quantity = cursor.getInt(cursor.getColumnIndex(COLUMN_QUANTITY));
			String name = cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCTNAME));
			Product p = new Product(id,name,quantity);
			list.add(p);
		}
		cursor.close();
		db.close();

		return list;
	}

	//Method for adding a product to our database.
	public void addProduct(Product product) {

		//This create a dictionary (hashmap), where we can 
		//put pairs of (key,values)
        ContentValues values = new ContentValues();
        
        //putting two things in the dictionary.
        values.put(COLUMN_PRODUCTNAME, product.getProductName());
        values.put(COLUMN_QUANTITY, product.getQuantity());
 
        //create a writeable databse
        SQLiteDatabase db = getWritableDatabase();
        
        //inserting our new data into the database
        //dont worry about the second "null" parameter!
        long id = db.insert(TABLE_PRODUCTS, null, values);
        product.setID(id);
        db.close(); //remember to always close our database
	}




	//Find a specific product in our database and
	//return it (or null if it does not exists)
	public Product findProduct(String productname) {
		//making a select from where sql statement
		//notice: the \ character in order to escape the " character
		String query = "Select * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_PRODUCTNAME + " =  \"" + productname + "\"";
		
		SQLiteDatabase db = getWritableDatabase();
		
		//make our query to the database
		Cursor cursor = db.rawQuery(query, null);
		
		Product product = new Product();
		
		//do we get a row?
		//notice we simply move to the first row
		//here, so if two products have exactly the same
		//name, then we will simply choose the first one.
		//To get ALL products you need to modify the select statement
		//AND use a while loop like the one below.
		//You now also need to make an arrayList of products.
	/*	while (cursor.moveToNext()) {
			product.setID(cursor.getInt(0));
		}*/
		if (cursor.moveToFirst()) {
			//cursor.moveToFirst();
			//get the parameters. NOTICE THE DATATYPES AND THE ORDER
			//OF THE PARAMETERS CORRESPONDS TO THE ORDER OF THE PARAMETERS
			//IN WHICH THE DATABASE WAS CREATED!!!!!
			product.setID(cursor.getInt(0));
			product.setProductName(cursor.getString(1));
			product.setQuantity(cursor.getInt(2));
			//close cursor to save memory
			cursor.close();
		} else {
			//we could not find a match, so return null
			product = null;
		}
		db.close(); //close database
		return product;
	}
	
	//delete the product with the given name
	//returns the product deleted - null if not found
	public Product deleteProduct(String productname) {
		

		String query = "Select * FROM " + TABLE_PRODUCTS + " WHERE " + 
						COLUMN_PRODUCTNAME + " =  \"" + productname + "\"";
		SQLiteDatabase db = getWritableDatabase();
		//make a SQL query
		Cursor cursor = db.rawQuery(query, null);
		
		Product product = null;
		
		//did we find a product?
		if (cursor.moveToFirst()) {
			int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
			int quantity = cursor.getInt(cursor.getColumnIndex(COLUMN_QUANTITY));
			String name = cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCTNAME));
			product = new Product(id,name,quantity);

			//delete products where the columns_id matches the 
			//id for this product name
			//NOTICE THE ? MARK WILL BE REPLACED BY OUR ID NUMBER
			db.delete(TABLE_PRODUCTS, COLUMN_ID + " = ?",
		            new String[] { String.valueOf(id) });
			cursor.close();
		}
		db.close(); //close the database
		return product;
	}

}
