package com.example.tablacsv.Vista;

import com.example.tablacsv.Modelo.Cancion;
import javafx.scene.Cursor;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Construye las graficas de barras para las columnas numericas
 * de la tabla y configura el evento de clic para abrirlas en ventana nueva.
 */
public class GraficaBarras {

    /**
     * Genera una grafica pequena para el encabezado de una columna numerica
     * que al recibir clic abre una version grande en una ventana nueva.
     *
     * @param datos     Lista completa de canciones cargadas.
     * @param colIdx    Indice de la columna numerica.
     * @param nombreCol Nombre de la columna para el titulo.
     * @return Componente BarChart listo para incrustarse en la cabecera.
     */
    public BarChart<String, Number> crearMini(ArrayList<Cancion> datos, int colIdx, String nombreCol) {
        BarChart<String, Number> mini = construir(datos, colIdx, nombreCol, false);
        mini.setPrefSize(155, 78);
        mini.setMaxSize(155, 78);
        mini.setCursor(Cursor.HAND);

        mini.setOnMouseClicked(e -> {
            BarChart<String, Number> ampliada = construir(datos, colIdx, nombreCol, true);
            VentanaGrafica ventana = new VentanaGrafica();
            ventana.mostrar("Histograma de frecuencia: " + nombreCol, ampliada);
        });

        return mini;
    }

    /**
     * Calcula los rangos de valores de la columna, cuenta cuantas canciones caen
     * en cada intervalo y arma el objeto BarChart.
     *
     * @param datos     Lista de canciones a analizar.
     * @param colIdx    Indice de la columna numerica.
     * @param nombreCol Nombre de la columna.
     * @param grande    true para mostrar ejes y etiquetas completas; false para version compacta.
     * @return Objeto BarChart configurado.
     */
    public BarChart<String, Number> construir(ArrayList<Cancion> datos, int colIdx, String nombreCol, boolean grande) {
        double min = Double.POSITIVE_INFINITY;
        double max = Double.NEGATIVE_INFINITY;

        for (Cancion c : datos) {
            double v = c.getValorNumerico(colIdx);
            if (v != Double.NEGATIVE_INFINITY) {
                if (v < min) min = v;
                if (v > max) max = v;
            }
        }

        int bins = 10;
        int[] frecuencias = new int[bins];
        double rango = (max - min) == 0 ? 1 : (max - min);

        for (Cancion c : datos) {
            double v = c.getValorNumerico(colIdx);
            if (v != Double.NEGATIVE_INFINITY) {
                int idx = (int) (((v - min) / rango) * (bins - 1));
                if (idx >= 0 && idx < bins) {
                    frecuencias[idx]++;
                }
            }
        }

        CategoryAxis ejeX = new CategoryAxis();
        NumberAxis ejeY = new NumberAxis();
        ejeX.setTickLabelsVisible(grande);
        ejeY.setTickLabelsVisible(grande);
        ejeX.setTickMarkVisible(grande);
        ejeY.setTickMarkVisible(grande);

        if (grande) {
            ejeX.setLabel("Rango de valores (" + nombreCol + ")");
            ejeY.setLabel("Cantidad de canciones");
        }

        BarChart<String, Number> chart = new BarChart<>(ejeX, ejeY);
        chart.setLegendVisible(false);
        chart.setAnimated(false);
        chart.setBarGap(1);
        chart.setCategoryGap(1);
        if (grande) {
            chart.setTitle("Distribucion de " + nombreCol);
        }

        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        for (int i = 0; i < bins; i++) {
            double inicioBin = min + (i * rango / bins);
            String etiqueta = String.format(Locale.US, "%.2f", inicioBin);
            serie.getData().add(new XYChart.Data<>(etiqueta, frecuencias[i]));
        }
        chart.getData().add(serie);

        for (XYChart.Data<String, Number> d : serie.getData()) {
            if (d.getNode() != null) {
                d.getNode().setStyle("-fx-bar-fill: #3b82f6;");
            }
        }

        return chart;
    }
}
