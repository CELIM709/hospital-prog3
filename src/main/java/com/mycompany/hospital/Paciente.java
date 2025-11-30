package com.mycompany.hospital;

import java.util.ArrayList;
import java.util.List;

public class Paciente extends Persona {
    private int edad;
    private float altura;
    private float peso;     // El atributo que agregamos
    private String genero;
    
    // Referencia al historial de citas (Relación 1 a muchos)
    // En memoria, esto guarda referencias a los objetos Cita
    private List<Cita> historial; 

    // Constructor
    public Paciente(String nombre, String apellido, int cedula, String telefono, 
                    int edad, float altura, float peso, String genero) {
        // 1. Enviamos los datos comunes al padre
        super(nombre, apellido, cedula, telefono);
        
        // 2. Asignamos los datos propios
        this.edad = edad;
        this.altura = altura;
        this.peso = peso;
        this.genero = genero;
        
        // 3. ¡IMPORTANTE EN POO! Inicializamos la lista vacía.
        // Si no haces esto, historial será null y getHistorial().add() fallará.
        this.historial = new ArrayList<>();
    }

    // Getters y Setters
    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }

    public float getAltura() { return altura; }
    public void setAltura(float altura) { this.altura = altura; }

    public float getPeso() { return peso; }
    public void setPeso(float peso) { this.peso = peso; }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }

    // Retorna la lista viva para que se puedan agregar citas o consultarlas
    public List<Cita> getHistorial() {
        return historial;
    }

    // Opcional: Un setter por si necesitas cargar un historial completo desde archivo
    public void setHistorial(List<Cita> historial) {
        this.historial = historial;
    }
}