/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package proyecto.proyectobasedatos;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.*;
import javafx.scene.layout.VBox;
import clases.*;

/**
 * FXML Controller class
 *
 * @author usuario
 */
public class InicioSecionController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private TextField textUsuario;
    
    @FXML
    private PasswordField textContrasenia;

    @FXML
    private ImageView logoImagen;
    @FXML
    private VBox vBoxTitulo;
    
    Connection conexion=App.conn;
    Statement stmt = null;
    ResultSet rs = null;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        Image image = new Image(getClass().getResourceAsStream("/imagenes/images.jpg"));
        // TODO
        logoImagen.setImage(image);
        vBoxTitulo.setStyle("-fx-background-color: #831f1f");

    }    
    
    @FXML
    private void actionIniciarSecion(ActionEvent e){
        String usuarioIntro = textUsuario.getText();
        String contraseniaIntro = textContrasenia.getText();
        
        if (usuarioIntro != null && contraseniaIntro != null && !usuarioIntro.isEmpty() && !contraseniaIntro.isEmpty()) {
            String query = "SELECT * FROM usuarios WHERE usuario = ?";
            
            try (PreparedStatement ps = conexion.prepareStatement(query)) {
                ps.setString(1, usuarioIntro);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String idusuario = String.valueOf(rs.getInt("idusuario"));
                        String usuario = rs.getString("usuario");
                        String contrasenia = rs.getString("contrasenia");
                        String categoria = rs.getString("categoria");

                        if (usuarioIntro.equals(usuario) && contraseniaIntro.equals(contrasenia)) {
                            if (categoria.equals("CLI")) {
                                try {
                                    App.cliente=generarCliente(usuario,idusuario);
                                    App.setRoot("cliente");
                                } catch (IOException ioe) {
                                    ioe.printStackTrace();
                                }   
                            }else{
                                App.empleado = generarEmpleado(usuario,idusuario);
                                try {
                                    App.setRoot("empleado");
                                } catch (IOException ioe) {
                                    ioe.printStackTrace();
                                }   
                            }
                        } else {
                            mostrarAlerta("Contraseña incorrecta", "Te olvidaste de la contraseña :P");
                        }
                    } else {
                        mostrarAlerta("Usuario no encontrado", "El usuario no está registrado.");
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            mostrarAlerta("Campos vacíos", "Llenar todos los campos necesarios");
        }

    }
    
    @FXML
    private void actionCrearCuenta(ActionEvent e) {
        try {
            App.setRoot("crearCuenta");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    
    private Cliente generarCliente(String usuario, String idUsuario){
        String query ="select * from cliente where usuario = ?";
        try (PreparedStatement ps = conexion.prepareStatement(query)) {
                ps.setString(1, usuario);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String nombre = rs.getString("nombre");
                        String apellido = rs.getString("apellido");
                        Cliente cliente = new Cliente(idUsuario,usuario,nombre,apellido);
                        return cliente;
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        return null;
    }
    
    private Empleado generarEmpleado(String usuario, String idUsuario){
        String query ="select * from empleado where correo = ?";
        try (PreparedStatement ps = conexion.prepareStatement(query)) {
                ps.setString(1, usuario);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String cedula = rs.getString("cedula");
                        String rol = rs.getString("rol");
                        String nombre = rs.getString("nombre");
                        String apellido = rs.getString("apellido");
                        Date fechaContratacion = rs.getDate("fechaContratacion");
                        
                        Empleado empleado= new Empleado(idUsuario,cedula,rol,nombre,apellido,fechaContratacion,usuario);
                        return empleado;
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        return null;
    }
    private static void mostrarAlerta(String titulo,String contenido ){
        Alert alerta =new Alert(AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    
    }
}
