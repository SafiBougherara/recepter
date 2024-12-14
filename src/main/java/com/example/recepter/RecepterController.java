package com.example.recepter;

import Models.Client;
import bdd.FactureManager;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import bdd.ServiceManager;
import bdd.ClientManager;
import Models.Service;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;
import tools.Generator;

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

    //serviceChoice
    @FXML
    private ChoiceBox serviceChoice;

    //lists
    @FXML
    private ListView serviceList;
    @FXML
    private ListView temp_serviceList;
    @FXML
    private ListView clientList;
    @FXML
    private TextField temp_sum;

    @FXML
    private ListView<String> info_entrepriseList;


    ObservableList<Service> items = FXCollections.observableArrayList();
    ObservableList<Client> items2 = FXCollections.observableArrayList();
    ObservableList<Service> items3 = FXCollections.observableArrayList();

    @FXML
    public void initialize(){
        //lier items à la listeView au démarrage
        serviceList.setItems(items);
        clientList.setItems(items2);

        //vider les listes au demarrage
        temp_sum.setText("total : 0.0 €");


        //charger les services initiaux (optionnel)
        loadServices();
        loadClients();
        //load_info_entreprise();
    }

    private boolean checkFieldsOnRecept() {
        if (clientChoice.getSelectionModel().isEmpty() && !serviceChoice.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez selectionner un client");
            alert.showAndWait();
        }
        if (serviceChoice.getSelectionModel().isEmpty() && !clientChoice.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez selectionner un service");
            alert.showAndWait();
        }
        if (clientChoice.getSelectionModel().isEmpty() && serviceChoice.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez selectionner un client et un service");
            alert.showAndWait();
        }
        if (!clientChoice.getSelectionModel().isEmpty() && !serviceChoice.getSelectionModel().isEmpty()) {
            return true;
        }else {
            return false;
        }

    }

    @FXML
    public void load_client_facture(Event e){
        loadClients();
        loadServices();
        clientChoice.setItems(items2);
        serviceChoice.setItems(items);
    }

    @FXML
    public void add_service_on_recept(ActionEvent event){



        if (this.checkFieldsOnRecept()) {
            // on récupeère les valeurs de la choiceBox et on l'add dans item3
            Service service = (Service) serviceChoice.getSelectionModel().getSelectedItem();
            items3.add(service);
            //tem.out.println(items3);

            // on l'add dans le listview temporaire
            temp_serviceList.setItems(items3);
            Double amount = Generator.SumAmmount(this.items3);
            temp_sum.setText("total : " + amount + " €");
        }


    }

    @FXML
    public void add_service(ActionEvent event){
        String serviceName = this.nameService.getText();
        String priceInput = this.priceService.getText();

        if (serviceName.isEmpty() || priceInput.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs");
            alert.showAndWait();
        } else {
            try {
                double price = Double.parseDouble(priceInput);
                ServiceManager sm = new ServiceManager();
                if(sm.addService(serviceName, price)){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur");
                    alert.setHeaderText(null);
                    alert.setContentText("Service non-ajouté, probleme de base de données");
                    alert.showAndWait();
                }else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information");
                    alert.setHeaderText(null);
                    alert.setContentText("Service ajouté avec succes");
                    alert.showAndWait();
                }
                this.loadServices();
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Veuillez entrer un prix valide");
                alert.showAndWait();
            }
        }
    }

    private void loadServices(){
        this.items.clear();

        try {
            ServiceManager sm = new ServiceManager();
            ResultSet rs = sm.getServices();


            while (rs.next()) {
                String name = rs.getString("name");
                double price = rs.getDouble("amount");
                int id = rs.getInt("id");
                Service service = new Service(name, price, id);
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
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs avec des informations valides");
            alert.showAndWait();
        } else if (!email.matches("^(.+)@(.+)$")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez entrer une adresse email valide");
            alert.showAndWait();
        } else {
            ClientManager cm = new ClientManager();
            cm.addClient(brand, firstname, name, siret, adresse, email);
            this.loadClients();
        }
    }

    public void remove_selected_client(ActionEvent event){

        Client client = (Client) clientList.getSelectionModel().getSelectedItem();
        if (client != null) {
            System.out.println(client.getId());
            ClientManager cm = new ClientManager();
            cm.removeClient(client.getId());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Client supprimé avec succes");
            alert.showAndWait();
            this.loadClients();
        }
    }
    public void remove_selected_service(ActionEvent event){

        Service service = (Service) serviceList.getSelectionModel().getSelectedItem();
        if (service != null) {
            System.out.println(service.getId());
            ServiceManager sm = new ServiceManager();
            sm.removeService(service.getId());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Service supprimé avec succes");
            alert.showAndWait();
            this.loadServices();
        }
    }


    private void loadClients(){
        this.items2.clear();

        try {
            ClientManager cm = new ClientManager();
            ResultSet rs = cm.getClients();


            while (rs.next()) {
                String name = rs.getString("name");
                String brand = rs.getString("brand");
                int id = rs.getInt("id");
                String siret = rs.getString("siret");
                String adresse = rs.getString("adresse");
                String email = rs.getString("mail");
                Client client = new Client(id, brand, name, siret, adresse, email);
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

        if (brand.isEmpty() || siret.isEmpty() || adresse.isEmpty() || email.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs");
            alert.showAndWait();
        }else{

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


                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setHeaderText(null);
                alert.setContentText("Informations ajoutées avec succes");
                alert.showAndWait();
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Impossible d'ajouter les informations");
                alert.showAndWait();
                e.printStackTrace();
            }

        }
    }

    @FXML
    public void clean_added_services(ActionEvent event) {
        this.items3.clear();
        temp_sum.setText("total : 0.0 €");
    }

    public void generate_facture_pdf() {
        Double amount = Generator.SumAmmount(this.items3);
        System.out.println(amount);

        FactureManager fm = new FactureManager();
        int num_facture = (int) (Math.random() * 1000);
        boolean status = false;
        Client client = (Client) clientChoice.getSelectionModel().getSelectedItem();
        int client_id = client.getId();

        System.out.println(client_id);
        fm.addFacture(num_facture, status, amount, client_id);
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