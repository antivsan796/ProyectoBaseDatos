/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;



import java.util.Date;

/**
 *
 * @author usuario
 */
public class Empleado {
    private String cedula;
    private String rol;
    private String nombre;
    private String apellido;
    private Date fechaContratacion;
    private String correo;
    private String idCliente;

    public Empleado(String idCliente,String cedula, String rol, String nombre, String apellido, Date fechaContratacion, String correo) {
        this.idCliente = idCliente;
        this.cedula = cedula;
        this.rol = rol;
        this.nombre = nombre;
        this.apellido = apellido;
        this.fechaContratacion = fechaContratacion;
        this.correo = correo;
    }

    public String getCedula() {
        return cedula;
    }

    public String getRol() {
        return rol;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public Date getFechaContratacion() {
        return fechaContratacion;
    }

    public String getCorreo() {
        return correo;
    }

    public String getIdCliente() {
        return idCliente;
    }
}
