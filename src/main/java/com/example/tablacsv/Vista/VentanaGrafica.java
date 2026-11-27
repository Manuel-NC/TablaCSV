package com.example.tablacsv.Vista;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.Chart;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Clase encargada de abrir cualquier grafica en una ventana nueva
 * cuando el usuario hace clic sobre ella para verla con mayor detalle.
 */
public class VentanaGrafica {

    /**
     * Crea un nuevo escenario de JavaFX, coloca la grafica recibida adentro
     * y la muestra en pantalla.
     *
     * @param tituloVentana Titulo que aparecera en la barra superior de la ventana.
     * @param grafica       Componente Chart de JavaFX que se mostrara ampliado.
     */
    public void mostrar(String tituloVentana, Chart grafica) {
        Stage ventanaNueva = new Stage();
        ventanaNueva.setTitle(tituloVentana);

        StackPane contenedor = new StackPane(grafica);
        contenedor.setPadding(new Insets(18));
        contenedor.setStyle("-fx-background-color: #ffffff;");

        Scene escena = new Scene(contenedor, 840, 540);
        ventanaNueva.setScene(escena);
        ventanaNueva.show();
    }
}