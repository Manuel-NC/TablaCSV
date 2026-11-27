package com.example.tablacsv;

import com.example.tablacsv.Controlador.TablaControlador;
import com.example.tablacsv.Vista.TablaGUI;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Clase principal que arranca la aplicacion JavaFX siguiendo el patron MVC.
 */
public class Main extends Application {

    /**
     * Instancia la vista y el controlador e inicia la interfaz grafica.
     *
     * @param stage Escenario principal proporcionado por JavaFX.
     */
    @Override
    public void start(Stage stage) {
        TablaGUI vista = new TablaGUI();
        TablaControlador controlador = new TablaControlador(vista);
        controlador.iniciar(stage);
    }

    /**
     * Metodo main de entrada al programa.
     *
     * @param args Argumentos de linea de comandos.
     */
    public static void main(String[] args) {
        launch();
    }
}