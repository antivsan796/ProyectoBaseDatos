/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package proyecto.proyectobasedatos;

import clases.Comida;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import static proyecto.proyectobasedatos.App.conn;

public class OrdenarController implements Initializable {
    @FXML
    private TreeTableView<Comida> treeTableView;

    @FXML
    private TreeTableColumn<Comida, String> descripcion;

    @FXML
    private TreeTableColumn<Comida, Double> precio;
    
    @FXML
    private TreeTableColumn<Comida, Void> accionColumna;
    
    @FXML
    private Label idClienteText;
    private String idCliente;
    @FXML
    private Label nombreText;
    private String nombre;
    private String apellido;
    @FXML
    private VBox  vBoxOrden;
    @FXML 
    private TextField totalPagar;

    public void initialize(URL url, ResourceBundle rb) {
        loadDate();
        
        try {
            loadMenuData();
        } catch (SQLException ex) {
            Logger.getLogger(OrdenarController.class.getName()).log(Level.SEVERE, null, ex);
        }
        cargarDatos();
    }    
    
    private void loadMenuData() throws SQLException {
        String sectionQuery = "SELECT * FROM seccionComida";
    
        try (PreparedStatement stmt = conn.prepareStatement(sectionQuery);
             ResultSet rs = stmt.executeQuery()) {

            ObservableList<TreeItem<Comida>> sections = FXCollections.observableArrayList();

            while (rs.next()) {
                String idSeccion = rs.getString("idSeccion");
                String descSeccion = rs.getString("descripcionSeccion");

                TreeItem<Comida> sectionItem = new TreeItem<>(new Comida("Sección: " + descSeccion, 0.0));
                sectionItem.setExpanded(true);

                String comidaQuery = "SELECT * FROM comida WHERE idSeccion = ?";
                try (PreparedStatement ps = conn.prepareStatement(comidaQuery)) {
                    ps.setString(1, idSeccion);
                    
                    try (ResultSet rsComida = ps.executeQuery()) {
                        
                        while (rsComida.next()) {
                            String descComida = rsComida.getString("descripcion");
                            double precio = rsComida.getDouble("precio");
                            
                            TreeItem<Comida> comidaItem = new TreeItem<>(new Comida(descComida, precio));
                            sectionItem.getChildren().add(comidaItem);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                sections.add(sectionItem);
            }

            TreeItem<Comida> root = new TreeItem<>(new Comida("Menu", 0.0));
            root.setExpanded(true);
            root.getChildren().addAll(sections);

            treeTableView.setRoot(root);   
        }
    }
    
    private void cargarDatos(){
        if (App.empleado==null){
            idCliente=App.cliente.getIdCliente();
            nombre=App.cliente.getNombre();
            apellido=App.cliente.getApellido();
        }else if (App.cliente==null){
            idCliente=App.empleado.getIdCliente();
            nombre=App.empleado.getNombre();
            apellido=App.empleado.getApellido();
        }
        idClienteText.setText("id del Cliente: "+idCliente);
        nombreText.setText("Nombres: "+nombre+" "+apellido);
    }
    
    private void loadDate() {
        descripcion.setCellValueFactory(
            (TreeTableColumn.CellDataFeatures<Comida, String> param) -> param.getValue().getValue().descripcionProperty());
        precio.setCellValueFactory(
            (TreeTableColumn.CellDataFeatures<Comida, Double> param) -> param.getValue().getValue().precioProperty().asObject());
        accionColumna.setCellFactory(param -> new TreeTableCell<Comida, Void>() {
            private final Button btn = new Button("Añadir");

            {
                btn.setOnAction(event -> {
                    Comida comida = getTreeTableView().getTreeItem(getIndex()).getValue();
                    String descipcionProducto = String.valueOf(comida.getDescripcion());
                    String precioProducto = String.valueOf(comida.getPrecio()); 
                    Label lb1=new Label ();
                    lb1.setText("1"+"-"+descipcionProducto);
                    totalPagar.setText(String.valueOf(comida.getPrecio()+Double.parseDouble(totalPagar.getText())));
                    vBoxOrden.getChildren().add(lb1);
                    
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTreeTableView().getTreeItem(getIndex()).getParent() == null) {
                    setGraphic(null);
                } else {
                    Comida comida = getTreeTableView().getTreeItem(getIndex()).getValue();
                    if (String.valueOf(comida.getDescripcion()).contains("Sección:")) {
                        setGraphic(null); // No mostrar el botón en las secciones
                    } else {
                        setGraphic(btn); // Mostrar el botón en los elementos de comida
                    }
                }
            }
        });
    }
    
    @FXML
    private void ConfirmarOrden(ActionEvent e){
        String descripcionFinal="";
        String estado="PENDIENTE";
        Double total=Double.parseDouble(totalPagar.getText());
        if(containsLabels(vBoxOrden) != false){
            for(javafx.scene.Node node: vBoxOrden.getChildren()){
                if(node instanceof Label){
                    Label label =(Label)node;
                    descripcionFinal=descripcionFinal+";"+label.getText();
                }
            }
        }
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        String date = now.format(dateFormatter);
        String time = now.format(timeFormatter);

        LocalDate localDate = LocalDate.parse(date, dateFormatter);
        LocalTime localTime = LocalTime.parse(time, timeFormatter);

        Date sqlDate = Date.valueOf(localDate);
        Time sqlTime = Time.valueOf(localTime);
        
        try{
            String query = "INSERT INTO orden (idcliente, fecha, hora, estado, descripcion, preciototal, nombrecliente, apellidocliente) VALUES (?,?,?,?,?,?,?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(query);


            preparedStatement.setLong(1, Long.parseLong(idCliente));
            preparedStatement.setDate(2, sqlDate);
            preparedStatement.setTime(3, sqlTime);
            preparedStatement.setString(4, estado);
            preparedStatement.setString(5, descripcionFinal);
            preparedStatement.setDouble(6, total);
            preparedStatement.setString(7, nombre);
            preparedStatement.setString(8, apellido);

            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println("si se pudo :3");
            if (rowsAffected > 0) {
                System.out.println("Orden creada exitosamente.");
            } else {
                System.out.println("No se pudo crear la orden.");
            }

            String fxml;
            if (App.empleado == null) {
                fxml = "cliente";
            } else {
                fxml = "empleado";
            }
            App.setRoot(fxml);
        
        } catch (SQLException eql) {
            eql.printStackTrace();
        } catch (IOException io) {
            io.printStackTrace();
        }
    }
    
    
    @FXML
    private void CancelarOrden(){
        String fxml;
        if (App.empleado==null){
            fxml="cliente";
        }else {
            fxml="empleado";
        }
        try{
            App.setRoot(fxml);
        }catch(IOException io){
            io.printStackTrace();
        }
    }
    private boolean containsLabels(VBox vBoxOrden) {
            for(javafx.scene.Node node: vBoxOrden.getChildren()){
                if(node instanceof Label){
                    return true;
                }
            }
            return false;
    }
    
    
}
