package com.example.database;

import java.util.Objects;

public class Product {
	
	//just an ID number, to distinguish products with the same name from
	//each other.
	private long id;
	private String productname;
	private int quantity;

	@Override
	public String toString() {
		return "Product{" +
				"id=" + id +
				", productname='" + productname + '\'' +
				", quantity=" + quantity +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Product product = (Product) o;
		return id == product.id &&
				getQuantity() == product.getQuantity() &&
				Objects.equals(productname, product.productname);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, productname, getQuantity());
	}

	public Product() {
		
	}
	
	public Product(int id, String productname, int quantity) {
		this.id = id;
		this.productname = productname;
		this.quantity = quantity;
	}
	
	public Product(String productname, int quantity) {
		this.productname = productname;
		this.quantity = quantity;
	}
	
	public void setID(long id) {
		this.id = id;
	}
	
	public long getID() {
		return this.id;
	}
	
	public void setProductName(String productname) {
		this.productname = productname;
	}
	
	public String getProductName() {
		return this.productname;
	}
	
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public int getQuantity() {
		return this.quantity;
	}

}
