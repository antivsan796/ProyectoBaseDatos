package proyecto.proyectobasedatos;

import clases.Cliente;
import clases.Empleado;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * JavaFX App
 */
public class App extends Application {
    public static Connection conn;
    private static Scene scene;
    public static Cliente cliente;
    public static Empleado empleado;
    
    public static Connection getConnection() {
        String url = "jdbc:postgresql://aws-0-us-west-1.pooler.supabase.com:6543/postgres";
        String user = "postgres.itliefcisdanaikgbsww";
        String password = "VtRRE8qBRPwQ@$v";

        Connection connection = null;
        
        try{
            connection =DriverManager.getConnection(url,user,password);
            System.out.println("Conexion a la database establecido");
        }catch ( SQLException e){
            System.out.println("Conexion a la database fallo");
            e.printStackTrace();
        }
        
        return connection;
    }

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("inicioSecion"), 640, 480);
        stage.setScene(scene);
        stage.show();
    }
  
     static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        
        while (conn ==null){
            conn= getConnection();
        }
        launch();
    }

}