/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;

/**
 *
 * @author usuario
 */
public class Orden {


    private String idCliente;
    private String nombre;
    private String apellido;
    private String fecha;
    private String hora;
    private String estado;
    private String descripcion;
    private double precioTotal;

    public Orden( String idCliente,String nombre, String apellido, String fecha, String hora, String estado, String descripcion, double precioTotal) {
        this.idCliente = idCliente;
        this.nombre=nombre;
        this.apellido=apellido;
        this.fecha = fecha;
        this.hora = hora;
        this.estado = estado;
        this.descripcion = descripcion;
        this.precioTotal = precioTotal;
    }


    public String getIdCliente() {
        return idCliente;
    }

    public String getFecha() {
        return fecha;
    }

    public String getHora() {
        return hora;
    }

    public String getEstado() {
        return estado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public double getPrecioTotal() {
        return precioTotal;
    }
    
    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }
    @Override
    public String toString() {
        return String.format("Cliente: %s %s\nTotal a pagar: %s\nFecha: %s\nEstado: %s", nombre,apellido, precioTotal, fecha, estado);
    }

}
