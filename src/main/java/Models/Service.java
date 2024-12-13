package Models;

public class Service {

    private String name;
    private double price;

    private int id;

    public Service(String name, double price, int id) {
        this.name = name;
        this.price = price;
        this.id = id;
    }

    public String getName() {return name;}

    public int getId() {return id;}

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return name + " - " + price;
    }


}
