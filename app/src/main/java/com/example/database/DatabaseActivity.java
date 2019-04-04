package com.example.database;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class DatabaseActivity extends Activity {

	TextView idView;
	EditText productBox;
	EditText quantityBox;
	ArrayAdapter<Product> adapter;


	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_database);
        idView = findViewById(R.id.productID);
        productBox = findViewById(R.id.productName);
        quantityBox = findViewById(R.id.productQuantity);

        DataBase db = new DataBase(this);
		adapter = new ArrayAdapter<Product>(this,
				android.R.layout.simple_list_item_1,db.getAllProducts());

		final ListView listView = findViewById(R.id.productList);

		//only allow one element to be selected at the same time
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		listView.setAdapter(adapter);
	}

	//onClickListener for the "New" product button
	//to create a new product
    public void newProduct (View view) {
	   DataBase dbHandler = new DataBase(this);
	
	   int quantity = 
          Integer.parseInt(quantityBox.getText().toString());
	
	   Product product = new Product();
	   product.setProductName(productBox.getText().toString());
	   product.setQuantity(quantity);
	   
	   //add the product to the database.
	   dbHandler.addProduct(product);
	   productBox.setText(""); //reset the box
	   quantityBox.setText(""); //reset the box
	   adapter.add(product);

  }

  public void lookupProduct (View view) {
	   DataBase dbHandler = new DataBase(this);
	
	   Product product = 
           dbHandler.findProduct(productBox.getText().toString());

	   if (product != null) {
		   idView.setText(String.valueOf(product.getID()));
		   
          quantityBox.setText(String.valueOf(product.getQuantity()));
       } else {
	         idView.setText(getString(R.string.no_match_found));
       }        	
   }

   public void removeProduct (View view) {
	    DataBase dbHandler = new DataBase(this);
	
	     Product p = dbHandler.deleteProduct(productBox.getText().toString());

	     //did we find something that matches
	     if (p!=null)
	     {
	     	 Log.d("product_removed",p.toString());
		     idView.setText(getString(R.string.record_deleted));
		     productBox.setText("");
		     quantityBox.setText("");
		     int pos = adapter.getPosition(p);
		     Log.d("item_position","pos : "+pos);
		     adapter.remove(p);
		     adapter.notifyDataSetChanged();
	     }
         else
	          idView.setText(R.string.no_match_found);
   	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.database, menu);
		return true;
	}

}