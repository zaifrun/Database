package com.example.database;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class DatabaseActivity extends Activity {

	TextView idView;
	EditText productBox;
	EditText quantityBox;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_database);
        idView = (TextView) findViewById(R.id.productID);
        productBox = (EditText) findViewById(R.id.productName);
        quantityBox = (EditText) findViewById(R.id.productQuantity);
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
  }

  public void lookupProduct (View view) {
	   DataBase dbHandler = new DataBase(this);
	
	   Product product = 
           dbHandler.findProduct(productBox.getText().toString());

	   if (product != null) {
		   idView.setText(String.valueOf(product.getID()));
		   
          quantityBox.setText(String.valueOf(product.getQuantity()));
       } else {
	         idView.setText("No Match Found");
       }        	
   }

   public void removeProduct (View view) {
	    DataBase dbHandler = new DataBase(this);
	
	     boolean result = dbHandler.deleteProduct(productBox.getText().toString());

	     if (result)
	     {
		     idView.setText("Record Deleted");
		     productBox.setText("");
		     quantityBox.setText("");
	     }
         else
	          idView.setText("No Match Found");        	
   	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.database, menu);
		return true;
	}

}