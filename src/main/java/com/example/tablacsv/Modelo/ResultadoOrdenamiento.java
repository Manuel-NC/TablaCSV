package com.example.tablacsv.Modelo;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Guarda el resultado de ejecutar un algoritmo de ordenamiento:
 * el nombre del metodo, el tiempo en milisegundos cortado a 2 decimales
 * y la lista ya ordenada.
 */
public class ResultadoOrdenamiento {

    private String nombreMetodo;
    private double tiempoMs;
    private ArrayList<Cancion> datosOrdenados;

    /**
     * Crea un resultado convirtiendo el tiempo de nanosegundos a milisegundos
     * y ajustandolo a exactamente 2 decimales.
     *
     * @param nombreMetodo   Nombre del algoritmo ejecutado.
     * @param nanoSegundos   Diferencia de tiempo medida con System.nanoTime().
     * @param datosOrdenados ArrayList con las canciones ya ordenadas.
     */
    public ResultadoOrdenamiento(String nombreMetodo, long nanoSegundos, ArrayList<Cancion> datosOrdenados) {
        this.nombreMetodo = nombreMetodo;
        double ms = nanoSegundos / 1000000.0;
        this.tiempoMs = Double.parseDouble(String.format(Locale.US, "%.2f", ms));
        this.datosOrdenados = datosOrdenados;
    }

    /**
     * Devuelve el nombre del metodo de ordenamiento.
     *
     * @return Nombre del algoritmo.
     */
    public String getNombreMetodo() {
        return nombreMetodo;
    }

    /**
     * Devuelve el tiempo de ejecucion en milisegundos con 2 decimales.
     *
     * @return Tiempo numerico en milisegundos.
     */
    public double getTiempoMs() {
        return tiempoMs;
    }

    /**
     * Devuelve el tiempo listo para mostrar en texto con 2 decimales y unidad ms.
     *
     * @return Texto formateado como "12.34 ms".
     */
    public String getTiempoFormateado() {
        return String.format(Locale.US, "%.2f ms", tiempoMs);
    }

    /**
     * Devuelve la lista de canciones ya ordenadas por el metodo.
     *
     * @return ArrayList ordenado.
     */
    public ArrayList<Cancion> getDatosOrdenados() {
        return datosOrdenados;
    }
}
