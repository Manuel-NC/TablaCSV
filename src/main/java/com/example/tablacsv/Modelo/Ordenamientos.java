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
     * Ejecuta el metodo QuickSort de clase sobre una copia de los datos y mide su tiempo.
     *
     * @param datos Lista de canciones a ordenar.
     * @param col   Indice de la columna elegida para ordenar.
     * @return Objeto ResultadoOrdenamiento con el tiempo y los datos ordenados.
     */
    public ResultadoOrdenamiento ejecutarQuickSort(ArrayList<Cancion> datos, int col) {
        Cancion[] arr = clonarArreglo(datos);
        long inicio = System.nanoTime();
        quickSort(arr, col);
        long fin = System.nanoTime();
        return new ResultadoOrdenamiento("QuickSort", fin - inicio, convertirALista(arr));
    }

    /**
     * Inicia el ordenamiento rapido llamando al metodo recursivo con los limites del arreglo.
     *
     * @param a   Arreglo de canciones a ordenar.
     * @param col Indice de la columna de comparacion.
     */
    public void quickSort(Cancion[] a, int col) {
        if (a.length > 1) {
            recursivo(0, a.length - 1, a, col);
        }
    }

    /**
     * Metodo recursivo visto en clase que acomoda el pivote en su posicion final
     * moviendo los indices izquierdo y derecho mediante una bandera.
     *
     * @param inicio Indice inicial del subarreglo.
     * @param fin    Indice final del subarreglo.
     * @param a      Arreglo de canciones a ordenar.
     * @param col    Indice de la columna de comparacion.
     */
    private void recursivo(int inicio, int fin, Cancion[] a, int col) {
        int izq = inicio;
        int der = fin;
        int pos = inicio;
        boolean band = true;

        while (band) {
            band = false;

            while (a[pos].compararPorColumna(a[der], col) <= 0 && pos != der) {
                der = der - 1;
            }

            if (pos != der) {
                Cancion aux = a[pos];
                a[pos] = a[der];
                a[der] = aux;
                pos = der;

                while (a[pos].compararPorColumna(a[izq], col) >= 0 && pos != izq) {
                    izq = izq + 1;
                }

                if (pos != izq) {
                    band = true;
                    aux = a[pos];
                    a[pos] = a[izq];
                    a[izq] = aux;
                    pos = izq;
                }
            }
        }
        if ((pos - 1) > inicio) {
            recursivo(inicio, pos - 1, a, col);
        }
        if (fin > (pos + 1)) {
            recursivo(pos + 1, fin, a, col);
        }
    }

    /**
     * Ejecuta el metodo Shell Sort de clase sobre una copia de los datos y mide su tiempo.
     *
     * @param datos Lista de canciones a ordenar.
     * @param col   Indice de la columna elegida para ordenar.
     * @return Objeto ResultadoOrdenamiento con el tiempo y los datos ordenados.
     */
    public ResultadoOrdenamiento ejecutarShellSort(ArrayList<Cancion> datos, int col) {
        Cancion[] arr = clonarArreglo(datos);
        long inicio = System.nanoTime();
        shell(arr, col);
        long fin = System.nanoTime();
        return new ResultadoOrdenamiento("Shell Sort", fin - inicio, convertirALista(arr));
    }

    /**
     * Ordena el arreglo usando el metodo Shell visto en clase, dividiendo
     * el intervalo a la mitad y realizando pasadas con bandera hasta que no haya cambios.
     *
     * @param a   Arreglo de canciones a ordenar.
     * @param col Indice de la columna de comparacion.
     */
    public void shell(Cancion[] a, int col) {
        int inter = a.length + 1;

        while (inter > 1) {
            inter = inter / 2;
            boolean band = true;

            while (band) {
                band = false;
                int i = 0;

                while ((i + inter) < a.length) {
                    if (a[i].compararPorColumna(a[i + inter], col) > 0) {
                        Cancion aux = a[i];
                        a[i] = a[i + inter];
                        a[i + inter] = aux;
                        band = true;
                    }
                    i++;
                }
            }
        }
    }

    /**
     * Ejecuta el metodo de Seleccion Directa de clase sobre una copia de los datos
     * y mide su tiempo.
     *
     * @param datos Lista de canciones a ordenar.
     * @param col   Indice de la columna elegida para ordenar.
     * @return Objeto ResultadoOrdenamiento con el tiempo y los datos ordenados.
     */
    public ResultadoOrdenamiento ejecutarSeleccionDirecta(ArrayList<Cancion> datos, int col) {
        Cancion[] arr = clonarArreglo(datos);
        long inicio = System.nanoTime();
        seleccion(arr, col);
        long fin = System.nanoTime();
        return new ResultadoOrdenamiento("Seleccion Directa", fin - inicio, convertirALista(arr));
    }

    /**
     * Ordena el arreglo buscando el elemento menor en cada recorrido e
     * intercambiandolo con la posicion actual como se vio en clase.
     *
     * @param a   Arreglo de canciones a ordenar.
     * @param col Indice de la columna de comparacion.
     */
    public void seleccion(Cancion[] a, int col) {
        for (int i = 0; i < a.length - 1; i++) {
            Cancion menor = a[i];
            int k = i;

            for (int j = i + 1; j < a.length; j++) {
                if (a[j].compararPorColumna(menor, col) < 0) {
                    menor = a[j];
                    k = j;
                }
            }

            a[k] = a[i];
            a[i] = menor;
        }
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