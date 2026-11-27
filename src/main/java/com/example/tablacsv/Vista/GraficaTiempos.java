package com.example.tablacsv.Vista;

import com.example.tablacsv.Modelo.ResultadoOrdenamiento;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

/**
 * Panel inferior encargado de graficar los tiempos de ejecucion de los
 * metodos de ordenamiento y mostrar su listado formateado a 2 decimales.
 */
public class GraficaTiempos extends HBox {

    private BarChart<String, Number> grafica;
    private TextArea txtResumen;
    private ArrayList<ResultadoOrdenamiento> ultimosResultados;
    private String ultimaColumna;

    /**
     * Inicializa el panel con la grafica de barras a la izquierda, el cuadro
     * de texto con los tiempos a la derecha y el evento de clic para ampliar.
     */
    public GraficaTiempos() {
        this.ultimosResultados = new ArrayList<>();
        this.ultimaColumna = "";

        setSpacing(15);
        setPadding(new Insets(10, 18, 10, 18));
        setStyle("-fx-background-color: #ffffff;");

        grafica = construirBarChart(new ArrayList<>(), "");
        HBox.setHgrow(grafica, Priority.ALWAYS);

        grafica.setCursor(Cursor.HAND);
        grafica.setOnMouseClicked(e -> {
            BarChart<String, Number> ampliada = construirBarChart(ultimosResultados, ultimaColumna);
            VentanaGrafica ventana = new VentanaGrafica();
            ventana.mostrar("Comparacion de tiempos de ejecucion (ms)", ampliada);
        });

        VBox panelDerecho = new VBox(6);
        panelDerecho.setPrefWidth(330);

        Label lblTitulo = new Label("Tiempos registrados:");
        lblTitulo.setStyle("-fx-font-weight: bold; -fx-text-fill: #0f172a;");

        txtResumen = new TextArea();
        txtResumen.setEditable(false);
        txtResumen.setStyle("-fx-font-family: 'Consolas'; -fx-font-size: 13px;");
        VBox.setVgrow(txtResumen, Priority.ALWAYS);

        panelDerecho.getChildren().addAll(lblTitulo, txtResumen);
        getChildren().addAll(grafica, panelDerecho);
    }

    /**
     * Construye una grafica de barras comparando los milisegundos de cada algoritmo.
     *
     * @param resultados Lista de resultados obtenidos en la prueba.
     * @param columna    Nombre de la columna ordenada.
     * @return Objeto BarChart configurado.
     */
    private BarChart<String, Number> construirBarChart(ArrayList<ResultadoOrdenamiento> resultados, String columna) {
        CategoryAxis ejeX = new CategoryAxis();
        ejeX.setLabel("Algoritmo de Ordenamiento");

        NumberAxis ejeY = new NumberAxis();
        ejeY.setLabel("Tiempo de ejecucion (ms)");

        BarChart<String, Number> chart = new BarChart<>(ejeX, ejeY);
        chart.setTitle(columna.isEmpty()
                ? "Rendimiento de Algoritmos (ms)"
                : "Tiempos de Ordenamiento - Columna: " + columna);
        chart.setLegendVisible(false);
        chart.setAnimated(false);

        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        for (ResultadoOrdenamiento r : resultados) {
            serie.getData().add(new XYChart.Data<>(r.getNombreMetodo(), r.getTiempoMs()));
        }
        chart.getData().add(serie);

        return chart;
    }

    /**
     * Actualiza la grafica de barras y el area de texto con los nuevos tiempos
     * cortados a 2 decimales.
     *
     * @param resultados Lista con los tiempos medidos de cada algoritmo.
     * @param columna    Nombre de la columna que se ordeno.
     */
    public void actualizar(ArrayList<ResultadoOrdenamiento> resultados, String columna) {
        this.ultimosResultados = resultados;
        this.ultimaColumna = columna;

        grafica.getData().clear();
        grafica.setTitle("Tiempos de Ordenamiento - Columna: " + columna);

        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        StringBuilder sb = new StringBuilder();
        sb.append("Columna: ").append(columna).append("\n");
        sb.append("----------------------------------\n");

        for (ResultadoOrdenamiento r : resultados) {
            serie.getData().add(new XYChart.Data<>(r.getNombreMetodo(), r.getTiempoMs()));
            sb.append(String.format("%-22s : %s\n", r.getNombreMetodo(), r.getTiempoFormateado()));
        }

        grafica.getData().add(serie);
        txtResumen.setText(sb.toString());
    }
}