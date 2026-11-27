package com.example.tablacsv.Modelo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * Clase encargada de leer el archivo CSV desde los recursos del proyecto
 * y guardar cada registro valido dentro de un ArrayList de canciones.
 */
public class LectorCSV {

    /**
     * Abre el archivo CSV, omite la linea de encabezados, une los registros
     * que tienen saltos de linea entre comillas y construye la lista completa.
     *
     * @param rutaRecurso Ruta interna del archivo CSV dentro de resources.
     * @return ArrayList con todas las canciones leidas del archivo.
     */
    public ArrayList<Cancion> cargarCSV(String rutaRecurso) {
        ArrayList<Cancion> lista = new ArrayList<>();

        try (InputStream is = getClass().getResourceAsStream(rutaRecurso)) {
            if (is == null) {
                System.err.println("No se encontro el archivo: " + rutaRecurso);
                return lista;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            br.readLine(); // Saltar encabezado

            String linea;
            StringBuilder registroAcumulado = new StringBuilder();
            boolean dentroDeComillas = false;

            while ((linea = br.readLine()) != null) {
                if (registroAcumulado.length() > 0) {
                    registroAcumulado.append("\n");
                }
                registroAcumulado.append(linea);

                for (int i = 0; i < linea.length(); i++) {
                    if (linea.charAt(i) == '"') {
                        dentroDeComillas = !dentroDeComillas;
                    }
                }

                if (!dentroDeComillas) {
                    String[] campos = separarCampos(registroAcumulado.toString());
                    if (campos.length >= 33) {
                        lista.add(new Cancion(campos));
                    }
                    registroAcumulado.setLength(0);
                }
            }
            br.close();
        } catch (Exception e) {
            System.err.println("Error al leer el CSV: " + e.getMessage());
        }

        return lista;
    }

    /**
     * Separa una fila completa en sus columnas usando la coma como divisor,
     * pero respetando las comas que vienen dentro de comillas dobles.
     *
     * @param fila Cadena completa de un registro del CSV.
     * @return Arreglo de cadenas con cada columna separada.
     */
    private String[] separarCampos(String fila) {
        ArrayList<String> resultado = new ArrayList<>();
        StringBuilder actual = new StringBuilder();
        boolean enComillas = false;

        for (int i = 0; i < fila.length(); i++) {
            char c = fila.charAt(i);
            if (c == '"') {
                enComillas = !enComillas;
            } else if (c == ',' && !enComillas) {
                resultado.add(actual.toString());
                actual.setLength(0);
            } else {
                actual.append(c);
            }
        }
        resultado.add(actual.toString());
        return resultado.toArray(new String[0]);
    }
}