package Models;

public class Client {

    private String brand;
    private String name;
    private String siret;
    private String adresse;
    private String email;
    private int id;

    public Client(int id, String brand, String name, String siret, String adresse, String email) {

        this.id = id;
        this.siret = siret;
        this.adresse = adresse;
        this.email = email;
        this.brand = brand;
        this.name = name;
    }

    public String getName() {return name;}

    public String getPrice() {
        return brand;
    }

    @Override
    public String toString() {
        return brand + " - " + name;
    }

    public int getId() {
        return this.id;
    }

    public String getBrand() {
        return this.brand;
    }
}
