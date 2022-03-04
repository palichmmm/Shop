import inter.Manufacturer;

public class Product {
    private static int count = 1;
    private int id;
    private String nameProduct;
    private double price;
    private Manufacturer manufacturer;

    public Product(String nameProduct, double price, Manufacturer manufacturer) {
        id = count++;
        this.nameProduct = nameProduct + id;
        this.price = price;
        this.manufacturer = manufacturer;
    }

    public int getId() {
        return id;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public double getPrice() {
        return price;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    @Override
    public String toString() {
        return id + ". - " + nameProduct + " (" + manufacturer + ") Цена: " + price;
    }
}
