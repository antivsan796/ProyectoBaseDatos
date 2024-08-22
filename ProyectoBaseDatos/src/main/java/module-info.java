module proyecto.proyectobasedatos {
    requires java.sql;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;

    opens proyecto.proyectobasedatos to javafx.fxml;
    exports proyecto.proyectobasedatos;
}
