package com.example.tablacsv.Vista;

import com.example.tablacsv.Modelo.Cancion;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Construye las graficas de barras tipo histograma para las columnas numericas
 * y de fecha de la tabla, permitiendo abrirlas en ventana nueva al dar clic.
 */
public class GraficaBarras {

    /**
     * Genera una grafica pequena para el encabezado de una columna numerica
     * con ancho minimo fijo para que no desborde el titulo de la columna.
     *
     * @param datos     Lista completa de canciones cargadas.
     * @param colIdx    Indice de la columna numerica.
     * @param nombreCol Nombre de la columna para el titulo.
     * @return Componente BarChart listo para incrustarse en la cabecera.
     */
    public BarChart<String, Number> crearMini(ArrayList<Cancion> datos, int colIdx, String nombreCol) {
        BarChart<String, Number> mini = construir(datos, colIdx, nombreCol, false);
        mini.setMinSize(150, 78);
        mini.setPrefSize(150, 78);
        mini.setMaxSize(150, 78);
        mini.setCursor(Cursor.HAND);

        mini.setOnMouseClicked(e -> {
            BarChart<String, Number> ampliada = construir(datos, colIdx, nombreCol, true);
            VentanaGrafica ventana = new VentanaGrafica();
            ventana.mostrar("Grafica de barras: " + nombreCol, ampliada);
        });

        return mini;
    }

    /**
     * Genera el histograma pequeno para columnas de fecha (first_week y last_week)
     * mostrando la fecha minima a la izquierda y la maxima a la derecha en la parte inferior.
     *
     * @param datos     Lista completa de canciones cargadas.
     * @param colIdx    Indice de la columna de fecha.
     * @param nombreCol Nombre de la columna.
     * @return Contenedor VBox con el histograma y el rango de fechas debajo.
     */
    public VBox crearMiniFecha(ArrayList<Cancion> datos, int colIdx, String nombreCol) {
        BarChart<String, Number> mini = construirFecha(datos, colIdx, nombreCol, false);
        mini.setMinSize(150, 62);
        mini.setPrefSize(150, 62);
        mini.setMaxSize(150, 62);

        String fechaMin = "";
        String fechaMax = "";
        for (Cancion c : datos) {
            String f = c.getValorTexto(colIdx);
            if (!f.isEmpty()) {
                if (fechaMin.isEmpty() || f.compareTo(fechaMin) < 0) fechaMin = f;
                if (fechaMax.isEmpty() || f.compareTo(fechaMax) > 0) fechaMax = f;
            }
        }

        Label lblMin = new Label(fechaMin);
        lblMin.setStyle("-fx-font-size: 9px; -fx-text-fill: #64748b;");

        Region espacio = new Region();
        HBox.setHgrow(espacio, Priority.ALWAYS);

        Label lblMax = new Label(fechaMax);
        lblMax.setStyle("-fx-font-size: 9px; -fx-text-fill: #64748b;");

        HBox filaFechas = new HBox(lblMin, espacio, lblMax);
        filaFechas.setMinWidth(150);
        filaFechas.setPrefWidth(150);
        filaFechas.setMaxWidth(150);
        filaFechas.setAlignment(Pos.CENTER);

        VBox contenedor = new VBox(1, mini, filaFechas);
        contenedor.setCursor(Cursor.HAND);
        contenedor.setOnMouseClicked(e -> {
            BarChart<String, Number> ampliada = construirFecha(datos, colIdx, nombreCol, true);
            VentanaGrafica ventana = new VentanaGrafica();
            ventana.mostrar("Distribucion por fechas: " + nombreCol, ampliada);
        });

        return contenedor;
    }

    /**
     * Calcula los rangos de valores de una columna numerica, cuenta cuantas canciones
     * caen en cada intervalo y arma el objeto BarChart.
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
            if (v != Double.POSITIVE_INFINITY) {
                if (v < min) min = v;
                if (v > max) max = v;
            }
        }

        int bins = 10;
        int[] frecuencias = new int[bins];
        double rango = (max - min) == 0 ? 1 : (max - min);

        for (Cancion c : datos) {
            double v = c.getValorNumerico(colIdx);
            if (v != Double.POSITIVE_INFINITY) {
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

    /**
     * Convierte las fechas YYYY-MM-DD a dias de epoca para agruparlas en intervalos
     * y dibujar su histograma cronologico.
     *
     * @param datos     Lista de canciones a analizar.
     * @param colIdx    Indice de la columna de fecha.
     * @param nombreCol Nombre de la columna.
     * @param grande    true para mostrar ejes y etiquetas completas; false para version compacta.
     * @return Objeto BarChart configurado con las fechas.
     */
    public BarChart<String, Number> construirFecha(ArrayList<Cancion> datos, int colIdx, String nombreCol, boolean grande) {
        long minDia = Long.MAX_VALUE;
        long maxDia = Long.MIN_VALUE;

        for (Cancion c : datos) {
            String f = c.getValorTexto(colIdx);
            if (!f.isEmpty()) {
                try {
                    long dia = LocalDate.parse(f).toEpochDay();
                    if (dia < minDia) minDia = dia;
                    if (dia > maxDia) maxDia = dia;
                } catch (Exception ignored) {
                }
            }
        }

        int bins = 15;
        int[] frecuencias = new int[bins];
        long rango = (maxDia - minDia) == 0 ? 1 : (maxDia - minDia);

        for (Cancion c : datos) {
            String f = c.getValorTexto(colIdx);
            if (!f.isEmpty()) {
                try {
                    long dia = LocalDate.parse(f).toEpochDay();
                    int idx = (int) (((double) (dia - minDia) / rango) * (bins - 1));
                    if (idx >= 0 && idx < bins) {
                        frecuencias[idx]++;
                    }
                } catch (Exception ignored) {
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
            ejeX.setLabel("Periodo (" + nombreCol + ")");
            ejeY.setLabel("Cantidad de canciones");
        }

        BarChart<String, Number> chart = new BarChart<>(ejeX, ejeY);
        chart.setLegendVisible(false);
        chart.setAnimated(false);
        chart.setBarGap(1);
        chart.setCategoryGap(1);
        if (grande) {
            chart.setTitle("Distribucion temporal de " + nombreCol);
        }

        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        for (int i = 0; i < bins; i++) {
            long diaBin = minDia + (i * rango / bins);
            String etiqueta = LocalDate.ofEpochDay(diaBin).toString();
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