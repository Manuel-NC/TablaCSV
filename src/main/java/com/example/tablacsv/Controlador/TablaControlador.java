package com.example.tablacsv.Controlador;

import com.example.tablacsv.Modelo.Cancion;
import com.example.tablacsv.Modelo.LectorCSV;
import com.example.tablacsv.Modelo.Ordenamientos;
import com.example.tablacsv.Modelo.ResultadoOrdenamiento;
import com.example.tablacsv.Vista.TablaGUI;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.util.ArrayList;

/**
 * Controlador que conecta la interfaz grafica con la carga del CSV
 * y la ejecucion de los algoritmos de ordenamiento en segundo plano.
 */
public class TablaControlador {

    private ArrayList<Cancion> datosOriginales;
    private TablaGUI vista;
    private Ordenamientos ordenamientos;
    private Cancion infoColumnas;

    /**
     * Carga el dataset completo desde los recursos e instancia los objetos
     * necesarios para ordenar.
     *
     * @param vista Instancia de la interfaz grafica principal.
     */
    public TablaControlador(TablaGUI vista) {
        this.vista = vista;
        this.ordenamientos = new Ordenamientos();
        this.infoColumnas = new Cancion();
        LectorCSV lector = new LectorCSV();
        this.datosOriginales = lector.cargarCSV("/com/example/tablacsv/billboard_hot_100_spotify_audio_features_enriched.csv");
    }

    /**
     * Construye la ventana inicial con los datos cargados y enlaza los botones.
     *
     * @param stage Escenario principal de JavaFX.
     */
    public void iniciar(Stage stage) {
        vista.construirInterfaz(stage, datosOriginales);
        registrarEventos();
    }

    /**
     * Asocia las acciones de clic a los botones de ordenar y restaurar.
     */
    private void registrarEventos() {
        vista.getBtnOrdenar().setOnAction(e -> ejecutarOrdenamiento());
        vista.getBtnRestaurar().setOnAction(e -> vista.restaurarTabla(datosOriginales));
    }

    /**
     * Toma la columna, el metodo y la dimension elegida por el usuario, ejecuta
     * los ordenamientos en un hilo secundario para no trabar la ventana y
     * actualiza la grafica y la tabla al terminar.
     */
    private void ejecutarOrdenamiento() {
        int colIdx = vista.getComboColumna().getSelectionModel().getSelectedIndex();
        String nombreCol = infoColumnas.getColumnas()[colIdx];
        String metodoElegido = vista.getComboMetodo().getValue();
        int limite = vista.getComboLimiteFilas().getValue();

        ArrayList<Cancion> subconjunto = new ArrayList<>();
        int max = Math.min(limite, datosOriginales.size());
        for (int i = 0; i < max; i++) {
            subconjunto.add(datosOriginales.get(i));
        }

        vista.getBtnOrdenar().setDisable(true);
        vista.getLblEstado().setText("Ordenando " + max + " registros...");

        Thread hilo = new Thread(() -> {
            ArrayList<ResultadoOrdenamiento> resultados = new ArrayList<>();

            if (metodoElegido.equals("Todos los metodos") || metodoElegido.equals("QuickSort")) {
                resultados.add(ordenamientos.ejecutarQuickSort(subconjunto, colIdx));
            }
            if (metodoElegido.equals("Todos los metodos") || metodoElegido.equals("Shell Sort")) {
                resultados.add(ordenamientos.ejecutarShellSort(subconjunto, colIdx));
            }
            if (metodoElegido.equals("Todos los metodos") || metodoElegido.equals("Seleccion Directa")) {
                resultados.add(ordenamientos.ejecutarSeleccionDirecta(subconjunto, colIdx));
            }
            if (metodoElegido.equals("Todos los metodos") || metodoElegido.equals("MergeSort")) {
                resultados.add(ordenamientos.ejecutarMergeSort(subconjunto, colIdx));
            }
            if (metodoElegido.equals("Todos los metodos") || metodoElegido.equals("Arrays.sort()")) {
                resultados.add(ordenamientos.ejecutarArraysSort(subconjunto, colIdx));
            }
            if (metodoElegido.equals("Todos los metodos") || metodoElegido.equals("Arrays.parallelSort()")) {
                resultados.add(ordenamientos.ejecutarParallelSort(subconjunto, colIdx));
            }

            Platform.runLater(() -> {
                vista.actualizarResultados(resultados, nombreCol);
                vista.getBtnOrdenar().setDisable(false);
                vista.getLblEstado().setText("Completado (" + max + " filas ordenadas)");
            });
        });
        hilo.start();
    }
}
