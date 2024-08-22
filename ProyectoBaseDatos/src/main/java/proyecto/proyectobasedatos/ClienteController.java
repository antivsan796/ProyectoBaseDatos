/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package proyecto.proyectobasedatos;

import clases.Cliente;
import clases.Orden;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author usuario
 */
public class ClienteController implements Initializable {
    private Cliente cliente;
    Connection conexion=App.conn;
    
    @FXML
    private ListView<Orden> listaOrdenes;
    @FXML
    private Label nombreText;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cliente=App.cliente;
        CargarOrdenes();
        
        // TODO
    }    
    
    private void CargarOrdenes(){
        nombreText.setText(cliente.getNombre()+ " "+ cliente.getApellido());
        String query = "select * from orden order by fecha DESC,hora DESC";
        try (PreparedStatement ps = conexion.prepareStatement(query);
         ResultSet rs = ps.executeQuery()) {
            ObservableList<Orden> ordenItems= FXCollections.observableArrayList();
            while (rs.next()){
                String idCliente = String.valueOf(rs.getInt("idcliente"));
                
                if (cliente.getIdCliente().equals(idCliente)){
                    String nombre =cliente.getNombre();
                    String apellido= cliente.getApellido();
                    String fecha = rs.getDate("fecha").toString();
                    String hora = rs.getTime("hora").toString();
                    String estado = rs.getString("estado");
                    String descripcion = rs.getString("descripcion");
                    double precioTotal = rs.getDouble("precioTotal");
                    Orden orden= new Orden(idCliente,nombre,apellido,fecha, hora, estado,descripcion, precioTotal);
                    ordenItems.add(orden);
                }
            }
            listaOrdenes.setItems(ordenItems);
            listaOrdenes.setCellFactory(param -> new ListCell<Orden>() {
            @Override
            protected void updateItem(Orden item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.toString());
                }
            }
            });
            
            listaOrdenes.setOnMouseClicked((MouseEvent event) -> {
                if (event.getClickCount() == 2) {
                    Orden selectedOrder = listaOrdenes.getSelectionModel().getSelectedItem();
                    if (selectedOrder != null) {
                        mostrarDetallesOrden(selectedOrder);
                    }
                }
            });
        }catch (SQLException ex) {
        ex.printStackTrace();
        }
    }
    private void mostrarDetallesOrden(Orden orden) {
        Stage detailStage = new Stage();
        VBox detailBox = new VBox();
        HBox hBox = new HBox();
        String descripcion ="";
        if (orden.getDescripcion().contains(";")){
            String[] descripcionLista=orden.getDescripcion().split(";");
            for (String cadena :descripcionLista){
                descripcion = descripcion+ "\n"+cadena;
            }
        }else{
            descripcion=orden.getDescripcion();
        }
         
        hBox.getChildren().addAll(
                new javafx.scene.control.Label("ID Cliente: " + orden.getIdCliente()),
                new javafx.scene.control.Label("       "+"Fecha: " + orden.getFecha()+ " "+orden.getHora())
        );
        detailBox.getChildren().addAll(
                hBox,
                new javafx.scene.control.Label("Nombre: " + orden.getNombre()+" "+ orden.getApellido()),
                new javafx.scene.control.Label("Estado: " + orden.getEstado()),
                new javafx.scene.control.Label("Descripción: " +"\n"+ descripcion),
                new javafx.scene.control.Label("Precio Total: $" + orden.getPrecioTotal())
        );

        Scene detailScene = new Scene(detailBox, 300, 200);
        detailStage.setScene(detailScene);
        detailStage.show();
    }
    
    @FXML
    private void cargarPaginaOrdenes(ActionEvent e){
        try {
            App.setRoot("ordenar");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

}
