/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;

/**
 *
 * @author usuario
 */
public class Cliente {
    private String idCliente;
    private String usuario;
    private String nombre;
    private String apellido;

    public String getUsuario() {
        return usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public Cliente(String idCliente,String usuario, String nombre, String apellido) {
        this.idCliente=idCliente;
        this.usuario = usuario;
        this.nombre = nombre;
        this.apellido = apellido;
    }

    public String getIdCliente() {
        return idCliente;
    }
}
