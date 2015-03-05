package com.sara.mislugares;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Lugares {

    private static LugaresBD lugaresBD;

    public static void indicializaBD(Context contexto) {
        lugaresBD = new LugaresBD(contexto);
    }

    public static Cursor listado() {
        SQLiteDatabase bd = lugaresBD.getReadableDatabase();
        return bd.rawQuery("SELECT * FROM lugares", null);
        // return bd.rawQuery("SELECT * FROM lugares WHERE valoracion>1.0 ORDER BY nombre LIMIT 3", null);
        // String[] campos = {"*"};
        // return bd.query("lugares", campos, null, null, null, null, null, null);
        // return bd.query("lugares", campos, "valoracion>1.0", null, null, null, "nombre", Integer.toString(3));
    }

    public static String TAG = "Lugares";
    protected static GeoPunto posicionActual = new GeoPunto(0, 0);
    // public static ArrayList<Lugar> vectorLugares = ejemploLugares();

    /* public Lugares() {
        vectorLugares = ejemploLugares();
    } */

    /*static Lugar elemento(int id) {
        return vectorLugares.get(id);
    }*/
    public static Lugar elemento(int id) {
        Lugar lugar = null;
        SQLiteDatabase bd = lugaresBD.getReadableDatabase();
        Cursor cursor = bd.rawQuery("SELECT * FROM lugares WHERE _id = " + id, null);
        if (cursor.moveToNext()) {
            lugar = new Lugar();
            lugar.setNombre(cursor.getString(1));
            lugar.setDireccion(cursor.getString(2));
            lugar.setPosicion(new GeoPunto(cursor.getDouble(3), cursor.getDouble(4)));
            lugar.setTipo(TipoLugar.values()[cursor.getInt(5)]);
            lugar.setFoto(cursor.getString(6));
            lugar.setTelefono(cursor.getInt(7));
            lugar.setUrl(cursor.getString(8));
            lugar.setComentario(cursor.getString(9));
            lugar.setFecha(cursor.getLong(10));
            lugar.setValoracion(cursor.getFloat(11));
        }
        cursor.close();
        bd.close();
        return lugar;
    }

    public static void actualizaLugar(int id, Lugar lugar) {
        SQLiteDatabase bd = lugaresBD.getWritableDatabase();
        bd.execSQL("UPDATE lugares SET nombre = '"
                        + lugar.getNombre()
                        + "', direccion = '"
                        + lugar.getDireccion()
                        + "', longitud = "
                        + lugar.getPosicion().getLongitud()
                        + " , latitud = "
                        + lugar.getPosicion().getLatitud()
                        + " , tipo = "
                        + lugar.getTipo().ordinal()
                        + " , foto = '"
                        + lugar.getFoto()
                        + "', telefono = "
                        + lugar.getTelefono()
                        + " , url = '"
                        + lugar.getUrl()
                        + "', comentario = '"
                        + lugar.getComentario()
                        + "', fecha = "
                        + lugar.getFecha()
                        + " , valoracion = "
                        + lugar.getValoracion()
                        + " WHERE _id = "
                        + id
        );
        bd.close();
    }

    /* static void anyade(Lugar lugar) {
        vectorLugares.add(lugar);
    } */

    /*static int nuevo() {
        Lugar lugar = new Lugar();
        vectorLugares.add(lugar);
        return vectorLugares.size() - 1;
    }*/

    public static int nuevo() {
        int id = -1;
        Lugar lugar = new Lugar();
        SQLiteDatabase bd = lugaresBD.getWritableDatabase();
        bd.execSQL("INSERT INTO lugares (longitud, latitud, tipo, fecha) VALUES ( "
                        + lugar.getPosicion().getLongitud()
                        + ", "
                        + lugar.getPosicion().getLatitud()
                        + ", "
                        + lugar.getTipo().ordinal()
                        + ", "
                        + lugar.getFecha()
                        + ")"
        );
        Cursor c = bd.rawQuery("SELECT _id FROM lugares WHERE fecha = " + lugar.getFecha(), null);
        if (c.moveToNext()) {
            id = c.getInt(0);
        }
        c.close();
        bd.close();
        return id;
    }

    /*static void borrar(int id) {
        vectorLugares.remove(id);
    }*/

    public static void borrar(int id) {
        SQLiteDatabase bd = lugaresBD.getWritableDatabase();
        bd.execSQL("DELETE FROM lugares WHERE _id = " + id);
        bd.close();
    }

    /* public static int size() {
        return vectorLugares.size();
    } */

    /* public static ArrayList<Lugar> ejemploLugares() {
        ArrayList<Lugar> lugares = new ArrayList<Lugar>();
        lugares.add(new Lugar("Escuela Politécnica Superior de Gandía",
                "C/ Paranimf, 1 46730 Gandia (SPAIN)", -0.166093, 38.995656,
                TipoLugar.EDUCACION, 962849300, "http://www.epsg.upv.es",
                "Uno de los mejores lugares para formarse.", 3));
        lugares.add(new Lugar("Al de siempre",
                "P.Industrial Junto Molí Nou - 46722, Benifla (Valencia)",
                -0.190642, 38.925857, TipoLugar.BAR, 636472405, "",
                "No te pierdas el arroz en calabaza.", 3));
        lugares.add(new Lugar("androidcurso.com", "ciberespacio", 0.0, 0.0,
                TipoLugar.EDUCACION, 962849300, "http://androidcurso.com",
                "Amplia tus conocimientos sobre Android.", 5));
        lugares.add(new Lugar(
                "Barranco del Infierno",
                "Vía Verde del río Serpis. Villalonga (Valencia)",
                -0.295058,
                38.867180,
                TipoLugar.NATURALEZA,
                0,
                "http://sosegaos.blogspot.com.es/2009/02/lorcha-villalonga-via-verde-del-rio.html",
                "Espectacular ruta para bici o andar", 4));
        lugares.add(new Lugar("La Vital",
                "Avda. de La Vital, 0 46701 Gandía (Valencia)", -0.1720092,
                38.9705949, TipoLugar.COMPRAS, 962881070,
                "http://www.lavital.es/", "El típico centro comercial", 2));
        return lugares;
    } */

    public static int primerId() {
        int id = -1;
        SQLiteDatabase bd = lugaresBD.getReadableDatabase();
        Cursor c = bd.rawQuery("SELECT _id FROM lugares LIMIT 1", null);
        if (c.moveToNext()) {
            id = c.getInt(0);
        }
        c.close();
        bd.close();
        return id;
    }

    public static int buscarNombre(String nombre) {
        int id = -1;
        SQLiteDatabase bd = lugaresBD.getReadableDatabase();
        Cursor c = bd.rawQuery("SELECT * FROM lugares WHERE nombre = " + nombre, null);
        if (c.moveToNext()) {
            id = c.getInt(0);
        }
        c.close();
        bd.close();
        return id;
    }

    /* static List<String> listaNombres() {
        ArrayList<String> resultado = new ArrayList<String>();
        for (Lugar lugar : vectorLugares) {
            resultado.add(lugar.getNombre());
        }
        return resultado;
    }*/

}