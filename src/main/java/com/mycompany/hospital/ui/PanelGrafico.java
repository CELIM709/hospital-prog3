package com.mycompany.hospital.ui;

import java.awt.*;
import java.util.Map;
import javax.swing.JPanel;

public class PanelGrafico extends JPanel {

    private Map<String, Integer> datos;

    public void setDatos(Map<String, Integer> datos) {
        this.datos = datos;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Limpia el fondo

        if (datos == null || datos.isEmpty()) {
            g.drawString("No hay datos para mostrar", 20, 20);
            return;
        }

        int anchoPanel = getWidth();
        int altoPanel = getHeight();
        int margen = 50;

        int anchoUtil = anchoPanel - (2 * margen);
        int altoUtil = altoPanel - (2 * margen);

        int cantidadBarras = datos.size();
        int anchoBarra = anchoUtil / cantidadBarras;

        int valorMaximo = 0;
        for (Integer valor : datos.values()) {
            if (valor > valorMaximo) {
                valorMaximo = valor;
            }
        }

        if (valorMaximo == 0) {
            valorMaximo = 1;
        }

        int x = margen;

        Color[] colores = {Color.BLUE, Color.RED, Color.GREEN, Color.ORANGE, Color.MAGENTA};
        int colorIndex = 0;

        for (Map.Entry<String, Integer> entrada : datos.entrySet()) {
            String especialidad = entrada.getKey();
            int cantidad = entrada.getValue();

            int alturaBarra = (cantidad * altoUtil) / valorMaximo;

            int y = (altoPanel - margen) - alturaBarra;

            g.setColor(colores[colorIndex % colores.length]);
            g.fillRect(x, y, anchoBarra - 10, alturaBarra);

            g.setColor(Color.BLACK);
            g.drawRect(x, y, anchoBarra - 10, alturaBarra);

            g.drawString(String.valueOf(cantidad), x + (anchoBarra / 3), y - 5);

            String nombreCorto = especialidad.length() > 10 ? especialidad.substring(0, 8) + ".." : especialidad;
            g.drawString(nombreCorto, x, altoPanel - margen + 15);

            x += anchoBarra;
            colorIndex++;
        }

        g.drawLine(margen, altoPanel - margen, anchoPanel - margen, altoPanel - margen); // Eje X
        g.drawLine(margen, margen, margen, altoPanel - margen); // Eje Y
    }
}
