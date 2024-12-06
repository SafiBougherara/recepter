package tools;

import Models.Service;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import javafx.beans.Observable;
import javafx.collections.ObservableList;

public abstract class Generator {
    public static void generatePDF() {
        try {
            // Créer un fichier PDF

            //String name = client_name + "_" + facture_id + "_" + date + ".pdf";
            //String path = "src\\main\\resources\\pdf\\" + name;

            String path = "src\\main\\resources\\pdf\\test.pdf";
            PdfWriter writer = new PdfWriter(path);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Ajouter du texte
            document.add(new Paragraph("Hello, PDF!"));
            document.add(new Paragraph("Ce document a été généré avec iText."));
            float[] columnWidths = { 200F, 200F };
            Table table = new Table(columnWidths);

            // Fermer le document
            document.close();
            System.out.println("PDF créé avec succès !");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static double SumAmmount(ObservableList<Service> services) {

        double sum = 0.0;
        for (Service service : services) {
            sum += service.getPrice();
        }
        return sum;
    }
}
