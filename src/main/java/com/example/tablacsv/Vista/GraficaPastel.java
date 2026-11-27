package com.example.tablacsv.Vista;

import com.example.tablacsv.Modelo.Cancion;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Construye la grafica de pastel en tonos azules para columnas booleanas
 * y permite abrirla ampliada en una ventana nueva al darle clic.
 */
public class GraficaPastel {

    /**
     * Genera el panel del encabezado booleano con la grafica circular azul
     * a la izquierda y los conteos con porcentaje a la derecha.
     *
     * @param datos     Lista completa de canciones.
     * @param colIdx    Indice de la columna booleana.
     * @param nombreCol Nombre de la columna.
     * @return Contenedor HBox listo para la cabecera de la tabla.
     */
    public HBox crearMiniConDatos(ArrayList<Cancion> datos, int colIdx, String nombreCol) {
        int trues = 0;
        int falses = 0;
        int nulls = 0;

        for (Cancion c : datos) {
            String v = c.getValorTexto(colIdx);
            if (v.equalsIgnoreCase("True")) {
                trues++;
            } else if (v.equalsIgnoreCase("False")) {
                falses++;
            } else {
                nulls++;
            }
        }

        PieChart mini = construir(datos, colIdx, nombreCol, false);
        mini.setPrefSize(82, 82);
        mini.setMinSize(82, 82);
        mini.setMaxSize(82, 82);
        mini.setCursor(Cursor.HAND);

        mini.setOnMouseClicked(e -> {
            PieChart ampliada = construir(datos, colIdx, nombreCol, true);
            VentanaGrafica ventana = new VentanaGrafica();
            ventana.mostrar("Grafica de pastel: " + nombreCol, ampliada);
        });

        int total = Math.max(1, datos.size());
        VBox panelDatos = new VBox(2);
        panelDatos.setAlignment(Pos.CENTER_LEFT);

        Label lblTrue = new Label("true " + trues + " (" + String.format(Locale.US, "%.0f%%", trues * 100.0 / total) + ")");
        lblTrue.setStyle("-fx-font-size: 10px; -fx-text-fill: #1e293b;");

        String falseTexto = falses >= 1000 ? String.format(Locale.US, "%.1fk", falses / 1000.0) : String.valueOf(falses);
        Label lblFalse = new Label("false " + falseTexto + " (" + String.format(Locale.US, "%.0f%%", falses * 100.0 / total) + ")");
        lblFalse.setStyle("-fx-font-size: 10px; -fx-text-fill: #1e293b;");

        Label lblNull = new Label("[null] " + nulls + " (" + String.format(Locale.US, "%.0f%%", nulls * 100.0 / total) + ")");
        lblNull.setStyle("-fx-font-size: 10px; -fx-text-fill: #dc2626;");

        panelDatos.getChildren().addAll(lblTrue, lblFalse, lblNull);

        HBox contenedor = new HBox(6, mini, panelDatos);
        contenedor.setAlignment(Pos.CENTER_LEFT);
        return contenedor;
    }

    /**
     * Cuenta cuantos registros son verdaderos, falsos o vacios y arma el PieChart
     * aplicando tres tonos de azul tanto a las rebanadas como a la leyenda.
     *
     * @param datos     Lista de canciones a contar.
     * @param colIdx    Indice de la columna booleana.
     * @param nombreCol Nombre de la columna.
     * @param grande    true para mostrar leyenda y etiquetas; false para miniatura.
     * @return Objeto PieChart construido.
     */
    public PieChart construir(ArrayList<Cancion> datos, int colIdx, String nombreCol, boolean grande) {
        int trues = 0;
        int falses = 0;
        int nulls = 0;

        for (Cancion c : datos) {
            String v = c.getValorTexto(colIdx);
            if (v.equalsIgnoreCase("True")) {
                trues++;
            } else if (v.equalsIgnoreCase("False")) {
                falses++;
            } else {
                nulls++;
            }
        }

        PieChart.Data datoTrue = new PieChart.Data("true (" + trues + ")", trues);
        PieChart.Data datoFalse = new PieChart.Data("false (" + falses + ")", falses);
        PieChart.Data datoNull = new PieChart.Data("[null] (" + nulls + ")", nulls);

        PieChart pie = new PieChart();
        pie.getData().addAll(datoTrue, datoFalse, datoNull);

        pie.setLabelsVisible(grande);
        pie.setLegendVisible(grande);
        pie.setAnimated(false);

        if (grande) {
            pie.setTitle("Proporcion de " + nombreCol);
        }

        // Cambia los colores base del tema para que coincidan las rebanadas y los puntos de la leyenda
        pie.setStyle("CHART_COLOR_1: #60a5fa; CHART_COLOR_2: #2563eb; CHART_COLOR_3: #1d4ed8;");

        return pie;
    }
}