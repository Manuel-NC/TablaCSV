package com.example.tablacsv.Modelo;

/**
 * Representa una fila del conjunto de datos de musica con sus 33 columnas.
 * Guarda cada campo como texto para mostrarlo en la tabla y como numero
 * decimal en las columnas numericas para poder ordenarlas rapido.
 */
public class Cancion {

    private String[] columnas;
    private boolean[] esNumerica;
    private String[] valores;
    private double[] valoresNumericos;

    /**
     * Constructor vacio que inicializa los arreglos con los nombres
     * y tipos de las 33 columnas del archivo CSV.
     */
    public Cancion() {
        this.columnas = new String[]{
                "song_id", "performer", "song", "spotify_genre", "spotify_track_id",
                "spotify_track_preview_url", "spotify_track_duration_ms", "spotify_track_explicit",
                "spotify_track_album", "danceability", "energy", "key", "loudness", "mode",
                "speechiness", "acousticness", "instrumentalness", "liveness", "valence",
                "tempo", "time_signature", "spotify_track_popularity", "peak_position",
                "weeks_on_chart", "first_week", "last_week", "chart_success_tier",
                "hit_longevity_profile", "acoustic_mood_cluster", "musical_era",
                "evidence_span", "cot_rationale", "confidence_score"
        };

        this.esNumerica = new boolean[]{
                false, false, false, false, false,
                false, true, false, false, true,
                true, true, true, true, true,
                true, true, true, true, true,
                true, true, true, true, false,
                false, false, false, false, false,
                false, false, true
        };
    }

    /**
     * Construye un objeto Cancion guardando los 33 campos de texto y
     * convirtiendo a numero las columnas que son numericas. Los campos vacios
     * se marcan con infinito positivo para que queden al final al ordenar.
     *
     * @param campos Arreglo de cadenas obtenido al separar una fila del CSV.
     */
    public Cancion(String[] campos) {
        this();
        this.valores = new String[33];
        this.valoresNumericos = new double[33];

        for (int i = 0; i < 33; i++) {
            String val = (i < campos.length && campos[i] != null) ? campos[i].trim() : "";
            this.valores[i] = val;

            if (esNumerica[i]) {
                try {
                    this.valoresNumericos[i] = val.isEmpty() ? Double.POSITIVE_INFINITY : Double.parseDouble(val);
                } catch (NumberFormatException e) {
                    this.valoresNumericos[i] = Double.POSITIVE_INFINITY;
                }
            }
        }
    }

    /**
     * Devuelve el arreglo con los nombres de las 33 columnas.
     *
     * @return Arreglo de cadenas con los nombres de columna.
     */
    public String[] getColumnas() {
        return columnas;
    }

    /**
     * Devuelve el arreglo que indica que columnas son numericas.
     *
     * @return Arreglo de booleanos por indice de columna.
     */
    public boolean[] getEsNumerica() {
        return esNumerica;
    }

    /**
     * Devuelve el valor en texto de una columna especifica.
     *
     * @param col Indice de la columna del 0 al 32.
     * @return Cadena con el dato de esa columna.
     */
    public String getValorTexto(int col) {
        return valores[col];
    }

    /**
     * Devuelve el valor numerico de una columna especifica.
     *
     * @param col Indice de la columna del 0 al 32.
     * @return Numero decimal almacenado en esa columna.
     */
    public double getValorNumerico(int col) {
        return valoresNumericos[col];
    }

    /**
     * Compara esta cancion con otra tomando en cuenta si la columna
     * es numerica o de texto, mandando siempre los valores vacios al final.
     *
     * @param otra Objeto Cancion contra el cual se va a comparar.
     * @param col  Indice de la columna por la cual se esta ordenando.
     * @return Negativo si esta va antes, positivo si va despues, o 0 si son iguales.
     */
    public int compararPorColumna(Cancion otra, int col) {
        if (esNumerica[col]) {
            return Double.compare(this.valoresNumericos[col], otra.valoresNumericos[col]);
        } else {
            String v1 = this.valores[col];
            String v2 = otra.valores[col];
            if (v1.isEmpty() && v2.isEmpty()) return 0;
            if (v1.isEmpty()) return 1;
            if (v2.isEmpty()) return -1;
            return v1.compareToIgnoreCase(v2);
        }
    }
}
