package com.example.tablacsv.Vista;

import com.example.tablacsv.Modelo.Cancion;
import com.example.tablacsv.Modelo.ResultadoOrdenamiento;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/**
 * Ventana principal de la aplicacion. Arma la barra de controles superior,
 * la tabla de 33 columnas estilo Kaggle y conecta el panel de tiempos.
 */
public class TablaGUI {

    private TableView<Cancion> tabla;
    private GraficaTiempos panelRendimiento;
    private Cancion infoColumnas;

    private ComboBox<String> comboColumna;
    private ComboBox<String> comboMetodo;
    private ComboBox<Integer> comboLimiteFilas;
    private Button btnOrdenar;
    private Button btnRestaurar;
    private Label lblEstado;

    /**
     * Inicializa la referencia base de columnas de Cancion.
     */
    public TablaGUI() {
        this.infoColumnas = new Cancion();
    }

    /**
     * Construye toda la ventana principal y la muestra en pantalla.
     *
     * @param stage          Escenario principal de JavaFX.
     * @param datosCompletos Lista completa de canciones cargadas desde el CSV.
     */
    public void construirInterfaz(Stage stage, ArrayList<Cancion> datosCompletos) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f8fafc;");

        root.setTop(crearBarraControles());

        SplitPane splitCentral = new SplitPane();
        splitCentral.setOrientation(Orientation.VERTICAL);
        splitCentral.setDividerPositions(0.65);

        tabla = crearTablaKaggle(datosCompletos);
        panelRendimiento = new GraficaTiempos();

        splitCentral.getItems().addAll(tabla, panelRendimiento);
        root.setCenter(splitCentral);

        Scene scene = new Scene(root, 1360, 820);
        stage.setTitle("Billboard Hot 100 & Spotify Audio Features - Analisis de Ordenamiento");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Crea la barra superior con los selectores de columna, algoritmo,
     * cantidad de filas y botones de accion.
     *
     * @return Contenedor HBox con todos los controles superiores.
     */
    private HBox crearBarraControles() {
        HBox barra = new HBox(12);
        barra.setPadding(new Insets(12, 18, 12, 18));
        barra.setAlignment(Pos.CENTER_LEFT);
        barra.setStyle("-fx-background-color: #ffffff; -fx-border-color: #e2e8f0; -fx-border-width: 0 0 1 0;");

        Label lblCol = new Label("Columna:");
        lblCol.setStyle("-fx-font-weight: bold;");
        comboColumna = new ComboBox<>();
        for (String col : infoColumnas.getColumnas()) {
            comboColumna.getItems().add(col);
        }
        comboColumna.setValue("danceability");

        Label lblMet = new Label("Metodo:");
        lblMet.setStyle("-fx-font-weight: bold;");
        comboMetodo = new ComboBox<>();
        comboMetodo.getItems().addAll(
                "Todos los metodos",
                "QuickSort",
                "Shell Sort",
                "Seleccion Directa",
                "MergeSort",
                "Arrays.sort()",
                "Arrays.parallelSort()"
        );
        comboMetodo.setValue("Todos los metodos");

        Label lblDim = new Label("Dimension (filas):");
        lblDim.setStyle("-fx-font-weight: bold;");
        comboLimiteFilas = new ComboBox<>();
        comboLimiteFilas.getItems().addAll(1000, 5000, 10000, 20000, 32503);
        comboLimiteFilas.setValue(32503);

        btnOrdenar = new Button("Ordenar y Medir");
        btnOrdenar.setStyle("-fx-background-color: #2563eb; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 6 14; -fx-cursor: hand;");

        btnRestaurar = new Button("Datos Originales");
        btnRestaurar.setStyle("-fx-background-color: #e2e8f0; -fx-text-fill: #1e293b; -fx-font-weight: bold; -fx-padding: 6 12; -fx-cursor: hand;");

        lblEstado = new Label("Listo (32,503 registros cargados)");
        lblEstado.setStyle("-fx-text-fill: #475569; -fx-font-weight: bold;");

        barra.getChildren().addAll(lblCol, comboColumna, lblMet, comboMetodo, lblDim, comboLimiteFilas, btnOrdenar, btnRestaurar, lblEstado);
        return barra;
    }

    /**
     * Construye el TableView con las 33 columnas del dataset y sus encabezados
     * personalizados con graficas y estadisticas bien alineadas.
     *
     * @param datos Lista de canciones que se mostraran en las filas.
     * @return Componente TableView configurado.
     */
    private TableView<Cancion> crearTablaKaggle(ArrayList<Cancion> datos) {
        TableView<Cancion> tv = new TableView<>();
        tv.setStyle("-fx-background-color: white; -fx-font-size: 12px;");

        String[] nombresCol = infoColumnas.getColumnas();
        for (int i = 0; i < nombresCol.length; i++) {
            int colIdx = i;
            String nombreCol = nombresCol[i];

            TableColumn<Cancion, String> columna = new TableColumn<>();
            columna.setSortable(false);
            double anchoCol = (colIdx == 31) ? 260 : 185;
            columna.setPrefWidth(anchoCol);

            VBox headerBox = construirEncabezadoColumna(datos, colIdx, nombreCol, anchoCol - 16);
            columna.setGraphic(headerBox);
            columna.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getValorTexto(colIdx)));

            tv.getColumns().add(columna);
        }

        tv.setItems(FXCollections.observableArrayList(datos));
        return tv;
    }

    /**
     * Crea el encabezado visual de cada columna mostrando su nombre, tipo de dato
     * y su grafica o resumen correspondiente ajustado al ancho de la columna.
     *
     * @param datos     Lista completa de canciones.
     * @param colIdx    Indice de la columna.
     * @param nombreCol Nombre de la columna.
     * @param ancho     Ancho disponible dentro de la cabecera.
     * @return Contenedor VBox con el encabezado armado.
     */
    private VBox construirEncabezadoColumna(ArrayList<Cancion> datos, int colIdx, String nombreCol, double ancho) {
        VBox box = new VBox(4);
        box.setPadding(new Insets(4, 2, 4, 2));
        box.setAlignment(Pos.TOP_LEFT);
        box.setMinWidth(ancho);
        box.setPrefWidth(ancho);
        box.setMaxWidth(ancho);
        box.setPrefHeight(134);

        boolean[] numericas = infoColumnas.getEsNumerica();
        boolean esFecha = (colIdx == 24 || colIdx == 25);

        String tipoTexto;
        if (colIdx == 7) {
            tipoTexto = "boolean";
        } else if (esFecha) {
            tipoTexto = "date";
        } else if (numericas[colIdx]) {
            tipoTexto = "# double";
        } else {
            tipoTexto = "A string";
        }

        Label lblTitulo = new Label(nombreCol);
        lblTitulo.setStyle("-fx-font-weight: bold; -fx-text-fill: #0f172a; -fx-font-size: 12px;");
        lblTitulo.setMaxWidth(ancho);

        Label lblTipo = new Label(tipoTexto);
        lblTipo.setStyle("-fx-text-fill: #64748b; -fx-font-size: 10px;");
        lblTipo.setMaxWidth(ancho);

        box.getChildren().addAll(lblTitulo, lblTipo);

        if (colIdx == 7) {
            GraficaPastel pastel = new GraficaPastel();
            box.getChildren().add(pastel.crearMiniConDatos(datos, colIdx, nombreCol));
        } else if (esFecha) {
            GraficaBarras barras = new GraficaBarras();
            box.getChildren().add(barras.crearMiniFecha(datos, colIdx, nombreCol));
        } else if (numericas[colIdx]) {
            GraficaBarras barras = new GraficaBarras();
            box.getChildren().add(barras.crearMini(datos, colIdx, nombreCol));
        } else {
            agregarResumenTexto(box, datos, colIdx, ancho);
        }

        return box;
    }

    /**
     * Muestra el conteo de valores unicos en columnas de identificadores largos
     * o los 2 valores principales mas la fila Other con su porcentaje como en Kaggle.
     *
     * @param box    Contenedor del encabezado de la columna.
     * @param datos  Lista completa de canciones.
     * @param colIdx Indice de la columna de texto.
     * @param ancho  Ancho disponible para alinear etiqueta y porcentaje.
     */
    private void agregarResumenTexto(VBox box, ArrayList<Cancion> datos, int colIdx, double ancho) {
        HashMap<String, Integer> conteo = new HashMap<>();
        for (Cancion c : datos) {
            String v = c.getValorTexto(colIdx);
            if (v.isEmpty()) v = "[null]";
            conteo.put(v, conteo.getOrDefault(v, 0) + 1);
        }

        boolean mostrarSoloUnicos = (colIdx == 0 || colIdx == 1 || colIdx == 2 || colIdx == 30 || colIdx == 31);

        if (mostrarSoloUnicos) {
            Label lblUnicos = new Label(conteo.size() + "\nunique values");
            lblUnicos.setStyle("-fx-font-weight: bold; -fx-font-size: 12px; -fx-text-fill: #1e293b; -fx-padding: 14 0 0 18;");
            box.getChildren().add(lblUnicos);
        } else {
            ArrayList<String> claves = new ArrayList<>(conteo.keySet());
            claves.sort((a, b) -> conteo.get(b) - conteo.get(a));

            int mostrados = 0;
            int totalMostrados = 0;
            for (int k = 0; k < Math.min(2, claves.size()); k++) {
                String cat = claves.get(k);
                int cant = conteo.get(cat);
                totalMostrados += cant;
                mostrados++;

                double pct = (cant * 100.0) / datos.size();
                String catCorta = cat.length() > 16 ? cat.substring(0, 16) + "..." : cat;
                box.getChildren().add(crearFilaPorcentaje(catCorta, String.format(Locale.US, "%.0f%%", pct), ancho, cat.equals("[null]")));
            }

            int restantes = conteo.size() - mostrados;
            if (restantes > 0) {
                int cantOtros = datos.size() - totalMostrados;
                double pctOtros = (cantOtros * 100.0) / datos.size();
                box.getChildren().add(crearFilaPorcentaje("Other (" + restantes + ")", String.format(Locale.US, "%.0f%%", pctOtros), ancho, false));
            }
        }
    }

    /**
     * Crea una fila horizontal con el texto a la izquierda y su porcentaje a la derecha.
     *
     * @param etiqueta   Texto de la categoria.
     * @param porcentaje Porcentaje formateado.
     * @param ancho      Ancho total de la fila.
     * @param esNulo     true si la etiqueta es [null] para resaltarla en rojo.
     * @return Contenedor HBox alineado.
     */
    private HBox crearFilaPorcentaje(String etiqueta, String porcentaje, double ancho, boolean esNulo) {
        HBox fila = new HBox();
        fila.setPrefWidth(ancho);
        fila.setMaxWidth(ancho);

        Label lblIzq = new Label(etiqueta);
        lblIzq.setStyle(esNulo ? "-fx-font-size: 10px; -fx-text-fill: #dc2626;" : "-fx-font-size: 10px; -fx-text-fill: #334155;");

        Region espacio = new Region();
        HBox.setHgrow(espacio, Priority.ALWAYS);

        Label lblDer = new Label(porcentaje);
        lblDer.setStyle(esNulo ? "-fx-font-size: 10px; -fx-text-fill: #dc2626;" : "-fx-font-size: 10px; -fx-text-fill: #64748b;");

        fila.getChildren().addAll(lblIzq, espacio, lblDer);
        return fila;
    }

    /**
     * Actualiza la grafica de tiempos y reemplaza las filas de la tabla con
     * los datos ya ordenados.
     *
     * @param resultados Lista de resultados de los algoritmos ejecutados.
     * @param columna    Nombre de la columna que se ordeno.
     */
    public void actualizarResultados(ArrayList<ResultadoOrdenamiento> resultados, String columna) {
        panelRendimiento.actualizar(resultados, columna);
        if (!resultados.isEmpty()) {
            tabla.setItems(FXCollections.observableArrayList(resultados.get(resultados.size() - 1).getDatosOrdenados()));
        }
    }

    /**
     * Vuelve a mostrar en la tabla los registros en su orden original del CSV.
     *
     * @param originales Lista original de canciones sin ordenar.
     */
    public void restaurarTabla(ArrayList<Cancion> originales) {
        tabla.setItems(FXCollections.observableArrayList(originales));
        lblEstado.setText("Mostrando datos originales (" + originales.size() + " filas)");
    }

    /**
     * Devuelve el selector desplegable de columnas.
     *
     * @return ComboBox de nombres de columna.
     */
    public ComboBox<String> getComboColumna() { return comboColumna; }

    /**
     * Devuelve el selector desplegable de algoritmos.
     *
     * @return ComboBox de metodos de ordenamiento.
     */
    public ComboBox<String> getComboMetodo() { return comboMetodo; }

    /**
     * Devuelve el selector de dimension o limite de filas.
     *
     * @return ComboBox con las cantidades de filas disponibles.
     */
    public ComboBox<Integer> getComboLimiteFilas() { return comboLimiteFilas; }

    /**
     * Devuelve el boton principal para iniciar el ordenamiento.
     *
     * @return Boton Ordenar y Medir.
     */
    public Button getBtnOrdenar() { return btnOrdenar; }

    /**
     * Devuelve el boton para restaurar el orden original de los datos.
     *
     * @return Boton Datos Originales.
     */
    public Button getBtnRestaurar() { return btnRestaurar; }

    /**
     * Devuelve la etiqueta de estado de la barra superior.
     *
     * @return Label informativo de estado.
     */
    public Label getLblEstado() { return lblEstado; }
}