package com.example.tablacsv.Modelo;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Contiene los 6 metodos de ordenamiento:
 * QuickSort, Shell Sort, Seleccion Directa, MergeSort, Arrays.sort()
 * y Arrays.parallelSort(), midiendo su duracion con System.nanoTime().
 */
public class Ordenamientos {

    /**
     * Copia los elementos de un ArrayList hacia un arreglo nuevo para
     * ordenar sin modificar la lista original.
     *
     * @param lista ArrayList original de canciones.
     * @return Arreglo nuevo de objetos Cancion.
     */
    private Cancion[] clonarArreglo(ArrayList<Cancion> lista) {
        Cancion[] arr = new Cancion[lista.size()];
        for (int i = 0; i < lista.size(); i++) {
            arr[i] = lista.get(i);
        }
        return arr;
    }

    /**
     * Pasa los elementos de un arreglo ordenado de regreso a un ArrayList.
     *
     * @param arr Arreglo de canciones ya ordenado.
     * @return ArrayList con los mismos elementos en orden.
     */
    private ArrayList<Cancion> convertirALista(Cancion[] arr) {
        ArrayList<Cancion> lista = new ArrayList<>(arr.length);
        for (Cancion c : arr) {
            lista.add(c);
        }
        return lista;
    }

    /**
     * Ejecuta el metodo QuickSort sobre una copia de los datos y mide su tiempo.
     *
     * @param datos Lista de canciones a ordenar.
     * @param col   Indice de la columna elegida para ordenar.
     * @return Objeto ResultadoOrdenamiento con el tiempo y los datos ordenados.
     */
    public ResultadoOrdenamiento ejecutarQuickSort(ArrayList<Cancion> datos, int col) {
        Cancion[] arr = clonarArreglo(datos);
        long inicio = System.nanoTime();
        quickSort(arr, 0, arr.length - 1, col);
        long fin = System.nanoTime();
        return new ResultadoOrdenamiento("QuickSort", fin - inicio, convertirALista(arr));
    }

    /**
     * Ordena recursivamente el arreglo dividiendolo alrededor de un pivote central.
     *
     * @param arr Arreglo de canciones a ordenar.
     * @param izq Indice inicial del subarreglo.
     * @param der Indice final del subarreglo.
     * @param col Indice de la columna de comparacion.
     */
    private void quickSort(Cancion[] arr, int izq, int der, int col) {
        int i = izq;
        int j = der;
        Cancion pivote = arr[(izq + der) / 2];

        while (i <= j) {
            while (arr[i].compararPorColumna(pivote, col) < 0) i++;
            while (arr[j].compararPorColumna(pivote, col) > 0) j--;
            if (i <= j) {
                Cancion aux = arr[i];
                arr[i] = arr[j];
                arr[j] = aux;
                i++;
                j--;
            }
        }
        if (izq < j) quickSort(arr, izq, j, col);
        if (i < der) quickSort(arr, i, der, col);
    }

    /**
     * Ejecuta el metodo Shell Sort reduciendo saltos a la mitad y mide su tiempo.
     *
     * @param datos Lista de canciones a ordenar.
     * @param col   Indice de la columna elegida para ordenar.
     * @return Objeto ResultadoOrdenamiento con el tiempo y los datos ordenados.
     */
    public ResultadoOrdenamiento ejecutarShellSort(ArrayList<Cancion> datos, int col) {
        Cancion[] arr = clonarArreglo(datos);
        long inicio = System.nanoTime();

        int n = arr.length;
        int salto = n / 2;
        while (salto > 0) {
            for (int i = salto; i < n; i++) {
                Cancion temp = arr[i];
                int j = i;
                while (j >= salto && arr[j - salto].compararPorColumna(temp, col) > 0) {
                    arr[j] = arr[j - salto];
                    j -= salto;
                }
                arr[j] = temp;
            }
            salto /= 2;
        }

        long fin = System.nanoTime();
        return new ResultadoOrdenamiento("Shell Sort", fin - inicio, convertirALista(arr));
    }

    /**
     * Ejecuta el metodo de Seleccion Directa buscando el elemento menor en cada
     * pasada y mide su tiempo.
     *
     * @param datos Lista de canciones a ordenar.
     * @param col   Indice de la columna elegida para ordenar.
     * @return Objeto ResultadoOrdenamiento con el tiempo y los datos ordenados.
     */
    public ResultadoOrdenamiento ejecutarSeleccionDirecta(ArrayList<Cancion> datos, int col) {
        Cancion[] arr = clonarArreglo(datos);
        long inicio = System.nanoTime();

        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            int menor = i;
            for (int j = i + 1; j < n; j++) {
                if (arr[j].compararPorColumna(arr[menor], col) < 0) {
                    menor = j;
                }
            }
            if (menor != i) {
                Cancion aux = arr[i];
                arr[i] = arr[menor];
                arr[menor] = aux;
            }
        }

        long fin = System.nanoTime();
        return new ResultadoOrdenamiento("Seleccion Directa", fin - inicio, convertirALista(arr));
    }

    /**
     * Ejecuta el metodo MergeSort sobre una copia de los datos y mide su tiempo.
     *
     * @param datos Lista de canciones a ordenar.
     * @param col   Indice de la columna elegida para ordenar.
     * @return Objeto ResultadoOrdenamiento con el tiempo y los datos ordenados.
     */
    public ResultadoOrdenamiento ejecutarMergeSort(ArrayList<Cancion> datos, int col) {
        Cancion[] arr = clonarArreglo(datos);
        long inicio = System.nanoTime();
        mergeSort(arr, 0, arr.length - 1, col);
        long fin = System.nanoTime();
        return new ResultadoOrdenamiento("MergeSort", fin - inicio, convertirALista(arr));
    }

    /**
     * Divide el arreglo en dos mitades de forma recursiva y luego las mezcla en orden.
     *
     * @param arr Arreglo de canciones a ordenar.
     * @param izq Indice inicial del tramo.
     * @param der Indice final del tramo.
     * @param col Indice de la columna de comparacion.
     */
    private void mergeSort(Cancion[] arr, int izq, int der, int col) {
        if (izq < der) {
            int medio = (izq + der) / 2;
            mergeSort(arr, izq, medio, col);
            mergeSort(arr, medio + 1, der, col);
            mezclar(arr, izq, medio, der, col);
        }
    }

    /**
     * Combina dos subarreglos previamente ordenados dentro del arreglo principal.
     *
     * @param arr   Arreglo principal donde se guardan los datos mezclados.
     * @param izq   Inicio de la primera mitad.
     * @param medio Final de la primera mitad.
     * @param der   Final de la segunda mitad.
     * @param col   Indice de la columna de comparacion.
     */
    private void mezclar(Cancion[] arr, int izq, int medio, int der, int col) {
        int n1 = medio - izq + 1;
        int n2 = der - medio;

        Cancion[] L = new Cancion[n1];
        Cancion[] R = new Cancion[n2];

        for (int i = 0; i < n1; i++) L[i] = arr[izq + i];
        for (int j = 0; j < n2; j++) R[j] = arr[medio + 1 + j];

        int i = 0, j = 0, k = izq;
        while (i < n1 && j < n2) {
            if (L[i].compararPorColumna(R[j], col) <= 0) {
                arr[k++] = L[i++];
            } else {
                arr[k++] = R[j++];
            }
        }
        while (i < n1) arr[k++] = L[i++];
        while (j < n2) arr[k++] = R[j++];
    }

    /**
     * Ordena una copia de los datos usando el metodo Arrays.sort() de Java
     * y mide el tiempo que tarda.
     *
     * @param datos Lista de canciones a ordenar.
     * @param col   Indice de la columna elegida para ordenar.
     * @return Objeto ResultadoOrdenamiento con el tiempo y los datos ordenados.
     */
    public ResultadoOrdenamiento ejecutarArraysSort(ArrayList<Cancion> datos, int col) {
        Cancion[] arr = clonarArreglo(datos);
        long inicio = System.nanoTime();
        Arrays.sort(arr, (a, b) -> a.compararPorColumna(b, col));
        long fin = System.nanoTime();
        return new ResultadoOrdenamiento("Arrays.sort()", fin - inicio, convertirALista(arr));
    }

    /**
     * Ordena una copia de los datos usando el metodo paralelo Arrays.parallelSort()
     * de Java y mide el tiempo que tarda.
     *
     * @param datos Lista de canciones a ordenar.
     * @param col   Indice de la columna elegida para ordenar.
     * @return Objeto ResultadoOrdenamiento con el tiempo y los datos ordenados.
     */
    public ResultadoOrdenamiento ejecutarParallelSort(ArrayList<Cancion> datos, int col) {
        Cancion[] arr = clonarArreglo(datos);
        long inicio = System.nanoTime();
        Arrays.parallelSort(arr, (a, b) -> a.compararPorColumna(b, col));
        long fin = System.nanoTime();
        return new ResultadoOrdenamiento("Arrays.parallelSort()", fin - inicio, convertirALista(arr));
    }
}
