package modelos;

import java.util.Date;

public class Factura {
    private static int contadorGlobal = 1;

    private int numero;
    private Date fecha;
    private Cita cita;
    private String descripcion;
    private float monto;

    public Factura(Cita cita, String descripcion) {
        this.numero = contadorGlobal++;
        this.fecha = new Date(); 
        this.cita = cita;
        this.descripcion = descripcion;
        this.monto = cita.getMedico().getEspecialidad().getPrecio();
    }

    public int getNumero() {
        return numero;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Cita getCita() {
        return cita;
    }

    public void setCita(Cita cita) {
        this.cita = cita;
        this.monto = cita.getMedico().getEspecialidad().getPrecio();
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public float getMonto() {
        return monto;
    }

    public void setMonto(float monto) {
        this.monto = monto;
    }
}