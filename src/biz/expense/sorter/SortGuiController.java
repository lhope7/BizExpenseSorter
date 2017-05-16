/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biz.expense.sorter;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

/**
 *
 * @author LeesMac
 */
public class SortGuiController implements Initializable {

    @FXML
    private Label outputLabel;

    @FXML
    private Button addSite;

    @FXML
    private Button browseBtn;

    @FXML
    private TextArea addedTF;

    @FXML
    private Tab sortTab;

    @FXML
    private Tab sitesTab;

    @FXML
    private Button rmSiteBtn;

    @FXML
    protected TextField addSiteTF;

    @FXML
    private Button addToBtn;

    @FXML
    public ListView siteToAdd;

    @FXML
    protected ListView siteList;

    @FXML
    protected ListView listView;

    @FXML
    protected TextArea addedTA;

    @FXML
    private Button rmFromBt;

    @FXML
    private Button sortAndSave;

    @FXML
    private Button saveBtn;

    @FXML
    private TextField sourceTF;

    @FXML
    private TextField destinationTF;

    private String source;

    private String destination;

    private File reportFile;

    public ArrayList listOfSites = new ArrayList();

    private final FileChooser fc = new FileChooser();

    @FXML
    private void handleButtonActionBrowse(ActionEvent event) {
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(".csv files (*.csv)", "*.csv");
        fc.getExtensionFilters().add(extFilter);
        reportFile = fc.showOpenDialog(null);
        source = reportFile.getAbsolutePath();
        sourceTF.setText(source);

    }

    @FXML
    private void handleButtonActionSave(ActionEvent event) {

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(".csv files (*.csv)", "*.csv");
        fc.getExtensionFilters().add(extFilter);

        //Show save file dialog
        File file = fc.showSaveDialog(null);

        if (file != null) {
            destination = file.getAbsolutePath();
            destinationTF.setText(destination);
        }

    }

    @FXML
    private void handleButtonActionSortAndSave(ActionEvent event) {
        //SortCSV s = new SortCSV();
        SortAndSave sas = new SortAndSave();

       // s.sortAndSave(getSiteList(), source, destination);
        sas.sas(getSiteList(), source, destination);
    }

    @FXML
    private void handleButtonActionAddSite(ActionEvent event) {
        String site = addSiteTF.getText();
        siteToAdd.getItems().add(site);
        try {
            storeSite(site);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleButtonActionAddToBtn(ActionEvent event) {
        Object o = siteToAdd.getFocusModel().getFocusedItem();
        if (o == null) {

        } else {
            siteList.getItems().add(o);
            siteToAdd.getItems().remove(o);
        }
    }

    @FXML
    private void handleButtonActionRmFromBtn(ActionEvent event) {
        Object o = siteList.getFocusModel().getFocusedItem();
        if (o == null) {
        } else {
            siteList.getItems().remove(o);
            siteToAdd.getItems().add(o);
        }
    }

    @FXML
    private void handleButtonActionRmSiteBtn(ActionEvent event) {
        Object o = siteToAdd.getFocusModel().getFocusedItem();
        siteToAdd.getItems().remove(o);
        try {
            removeSite(o.toString());
        } catch (Exception e) {
            outputLabel.setText(e.getCause().toString());
        }
    }

    // This is what is called when adding a site to the site list. I think this works
    public void storeSite(String site) throws Exception {

        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection("jdbc:sqlite:/Users/Shared/test.db");
        Statement stat = conn.createStatement();
        stat.executeUpdate("create table if not exists Location (name);");
        PreparedStatement prep = conn.prepareStatement(
                "insert into Location values (?);");

        prep.setString(1, site);
        prep.addBatch();

        conn.setAutoCommit(false);
        prep.executeBatch();
        conn.setAutoCommit(true);
        conn.close();
    }

    public void removeSite(String site) throws Exception {
        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection("jdbc:sqlite:/Users/Shared/test.db");
        Statement stat = conn.createStatement();
        PreparedStatement prep = conn.prepareStatement(
                "delete from Location where name = ?;");

        prep.setString(1, site);
        prep.addBatch();

        conn.setAutoCommit(false);
        prep.executeBatch();
        conn.setAutoCommit(true);
        conn.close();
    }

    // This bit works in the IDE, but not from the .jar on the desktop. I think this is 
    // where the problem is.
    public void populateSites() throws Exception {
        File file = new File("/Users/Shared/test.db");

        if (file.exists()) {

            Class.forName("org.sqlite.JDBC");
            // I assume the file path here needs some finesse
            Connection conn = DriverManager.getConnection("jdbc:sqlite:/Users/Shared/test.db");
            Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery("select * from Location;");
            while (rs.next()) {
                siteToAdd.getItems().add(rs.getString("name"));
                //System.out.println("name = " + rs.getString("name"));
            }
            rs.close();
            conn.close();
        }
        else{
            outputLabel.setText("file doesn't exist");
        }
        
    }

    @FXML
    public ArrayList getSiteList() {
        for (int i = 0; i < siteList.getItems().size(); i++) {
            listOfSites.add(siteList.getItems().get(i));
        }
        //System.out.println(listOfSites);
        return listOfSites;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            populateSites();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
