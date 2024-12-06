package com.example.recepter;

import Models.Client;
import com.fasterxml.jackson.core.type.TypeReference;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import bdd.ServiceBdd;
import bdd.ClientBdd;
import Models.Service;
import javafx.collections.ObservableList;
import javax.swing.JOptionPane;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import java.util.HashMap;
import java.util.List;

public class RecepterController {


    //service
    @FXML
    private TextField nameService;
    @FXML
    private TextField priceService;


    //client
    @FXML
    private TextField nameClient;
    @FXML
    private TextField brand;
    @FXML
    private TextField firstname;
    @FXML
    private TextField name;
    @FXML
    private TextField siret;
    @FXML
    private TextArea adresse;
    @FXML
    private TextField email;


    //info_entreprise
    @FXML
    private TextField brandE;
    @FXML
    private TextField siretE;
    @FXML
    private TextArea adresseE;
    @FXML
    private TextField emailE;
    @FXML
    private ListView info_entreprise;


    //client_choice
    @FXML
    private ChoiceBox clientChoice;

    //lists
    @FXML
    private ListView serviceList;
    @FXML
    private ListView clientList;

    @FXML
    private ListView<String> info_entrepriseList;


    ObservableList<Service> items = FXCollections.observableArrayList();
    ObservableList<Client> items2 = FXCollections.observableArrayList();

    @FXML
    public void initialize(){
        //lier items à la listeView au démarrage
        serviceList.setItems(items);
        clientList.setItems(items2);
        clientChoice.setItems(items2);

        //charger les services initiaux (optionnel)
        loadServices();
        loadClients();
        //load_info_entreprise();
    }



    @FXML
    public void add_service(ActionEvent event){
        String serviceName = this.nameService.getText();
        String priceInput = this.priceService.getText();

        if (serviceName.isEmpty() || priceInput.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Veuillez remplir tous les champs", "Erreur", JOptionPane.ERROR_MESSAGE);
        } else {
            try {
                double price = Double.parseDouble(priceInput);
                ServiceBdd sm = new ServiceBdd();
                if(sm.addService(serviceName, price)){
                    JOptionPane.showMessageDialog(null, "Service non-ajouté, probleme de base de données", "Confirmation", JOptionPane.ERROR_MESSAGE);
                }else {
                    JOptionPane.showMessageDialog(null, "Service ajouteé", "Confirmation", JOptionPane.INFORMATION_MESSAGE);
                }
                this.loadServices();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Veuillez entrer un prix valide", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void loadServices(){
        this.items.clear();

        try {
            ServiceBdd sm = new ServiceBdd();
            ResultSet rs = sm.getServices();


            while (rs.next()) {
                String name = rs.getString("name");
                double price = rs.getDouble("amount");
                Service service = new Service(name, price);
                this.items.add(service);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }



    @FXML
    public void add_client(ActionEvent event) throws SQLException {
        String brand = this.brand.getText();
        String firstname = this.firstname.getText();
        String name = this.name.getText();
        String siret = this.siret.getText();
        String adresse = this.adresse.getText();
        String email = this.email.getText();

        if (brand.isEmpty() || firstname.isEmpty() || name.isEmpty() || siret.isEmpty() || adresse.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Veuillez remplir tous les champs avec des informations valides", "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
        } else if (!email.matches("^(.+)@(.+)$")) {
            JOptionPane.showMessageDialog(null, "Veuillez entrer une adresse email valide", "Erreur d'email", JOptionPane.ERROR_MESSAGE);
        } else {
            ClientBdd cm = new ClientBdd();
            cm.addClient(brand, firstname, name, siret, adresse, email);
            this.loadClients();
        }
    }


    private void loadClients(){
        this.items2.clear();

        try {
            ClientBdd cm = new ClientBdd();
            ResultSet rs = cm.getClients();


            while (rs.next()) {
                String name = rs.getString("name");
                String brand = rs.getString("brand");
                Client client = new Client(brand, name);
                this.items2.add(client);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    @FXML
    public void add_info_entreprise(ActionEvent event) {
        // Récupérer les valeurs des champs
        String brand = brandE.getText();
        String siret = siretE.getText();
        String adresse = adresseE.getText();
        String email = emailE.getText();

        // Nouvelle entrée à ajouter (sans infosSupplementaires)
        Map<String, Object> newEntry = new HashMap<>();
        newEntry.put("brand", brand);
        newEntry.put("siret", siret);
        newEntry.put("adresse", adresse);
        newEntry.put("email", email);

        // Spécifier le chemin du fichier JSON
        String filePath = "src/main/resources/json/informations_entreprise.json";

        try {
            // Créer un ObjectMapper pour gérer le JSON
            ObjectMapper objectMapper = new ObjectMapper();

            // Créer une liste contenant uniquement la nouvelle entrée
            List<Map<String, Object>> dataList = new ArrayList<>();
            dataList.add(newEntry);

            // Sauvegarder la nouvelle liste dans le fichier JSON, en remplaçant son contenu
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), dataList);

            JOptionPane.showMessageDialog(null, "Informations ajoutées", "Confirmation", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Impossible d'ajouter les informations", "Erreur", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }


/*
    @FXML
    public void load_info_entreprise() {
        // Spécifier le chemin du fichier JSON
        String filePath = "src/main/resources/json/informations_entreprise.json";

        try {
            // Créer un ObjectMapper pour gérer le JSON
            ObjectMapper objectMapper = new ObjectMapper();

            // Lire le fichier JSON et le convertir en liste de Map
            File file = new File(filePath);
            List<Map<String, Object>> dataList;

            if (file.exists()) {
                dataList = objectMapper.readValue(file, new TypeReference<List<Map<String, Object>>>() {});
            } else {
                dataList = new ArrayList<>();
            }

            // Récupérer les informations de chaque entrée et les ajouter à la ListView
            ObservableList<String> displayList = FXCollections.observableArrayList();

            for (Map<String, Object> entry : dataList) {
                String brand = (String) entry.get("brand");
                String siret = (String) entry.get("siret");
                String adresse = (String) entry.get("adresse");
                String email = (String) entry.get("email");

                // Format des informations à afficher dans la ListView
                String displayInfo = "Brand: " + brand + ", Siret: " + siret + ", Adresse: " + adresse + ", Email: " + email;
                displayList.add(displayInfo);
            }

            // Affecter la liste observable à la ListView
            info_entreprise.setItems(displayList);

            System.out.println("Les informations de l'entreprise ont été chargées avec succès.");

        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du fichier JSON : " + e.getMessage());
            e.printStackTrace();
        }
    }
*/

}