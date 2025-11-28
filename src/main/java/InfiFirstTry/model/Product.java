package InfiFirstTry.model;

public class Product {
    //All in Database

    /**Unique ID for each product*/
    private int idProduct;
    /**Name of the product*/
    private String name;
    /**Price of the product*/
    private double price;
    private boolean deleted;

    /**Nedded for JSON Serialization*/
    public Product() {}

    @Override
    public String toString() {
        return "Product{" +
                "idProduct=" + idProduct +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", deleted=" + deleted +
                '}';
    }

    /**Standard Constructor for Initializing all variables*/
    Product(int idProduct, String name, double price, boolean deleted) {
        this.idProduct = idProduct;
        this.name = name;
        this.price = price;
        deleted = false;
    }






    //Standard Getters and Setters

    /**Standard Getter for the ID of the product*/
    public int getIdProduct() {
        return idProduct;
    }
    /**Standard Getter for the name of the product*/
    public String getName() {
        return name;
    }
    /**Standard Getter for the price of the product*/
    public double getPrice() {
        return price;
    }
    public boolean isDeleted() { return deleted; }
    public void setDeleted(boolean deleted) { this.deleted = deleted; }
}
