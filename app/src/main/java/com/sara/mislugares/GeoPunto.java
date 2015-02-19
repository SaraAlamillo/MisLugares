package com.sara.mislugares;

/**
 * Clase que representa una coordenada geográfica.
 */
public class GeoPunto {
    // declaración de atributos
    private double longitud;
    private double latitud;

    // declaración de constructor
    public GeoPunto(double pLongitud, double pAltitud) {
        this.longitud = (int) (pLongitud * 1E6);
        this.latitud = (int) (pAltitud * 1E6);
    }

    // declaración de métodos

    /**
     * Transcribe la coordenada a String.
     *
     * @return un string con la longitud y la altitud
     */
    public String toString() {
        return "(" + longitud + ", " + latitud + ")";
    }

    /**
     * Aproxima la distancia en metros entre dos coordenadas
     *
     * @param punto la coordenada que queremos aproximar
     */
    public double distancia(GeoPunto punto) {
        final double RADIO_TIERRA = 6371000; // en metros
        double dLat = Math.toRadians(latitud - punto.latitud);
        double dLon = Math.toRadians(longitud - punto.longitud);
        double lat1 = Math.toRadians(punto.latitud);
        double lat2 = Math.toRadians(latitud);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2)
                * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return c * RADIO_TIERRA;

    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }
}
