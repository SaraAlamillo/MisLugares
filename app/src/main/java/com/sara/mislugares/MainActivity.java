package com.sara.mislugares;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


//public class MainActivity extends ListActivity implements LocationListener {
public class MainActivity extends FragmentActivity implements LocationListener {
    // public BaseAdapter adaptador;
    private Button bAcercaDe;
    private Button bSalir;
    private Button bPreferencias;
    private Button bMostrar;
    // MediaPlayer mp;
    private LocationManager manejador;
    private Location mejorLocaliz;
    private static final long DOS_MINUTOS = 2 * 60 * 1000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.fragment_selector);
        setContentView(R.layout.activity_main);
        //mp = MediaPlayer.create(this, R.raw.audio);
        Toast.makeText(this, "onCreate", Toast.LENGTH_SHORT).show();
        manejador = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (manejador.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            actualizaMejorLocaliz(manejador.getLastKnownLocation(LocationManager.GPS_PROVIDER));
        }
        if (manejador.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            actualizaMejorLocaliz(manejador.getLastKnownLocation(LocationManager.NETWORK_PROVIDER));
        }
        // adaptador = new AdaptadorLugares(this);
        Lugares.indicializaBD(this);
        /*adaptador = new SimpleCursorAdapter(this,
                R.layout.elemento_lista,
                Lugares.listado(),
                new String[]{"nombre", "direccion"},
                new int[]{R.id.nombre, R.id.direccion},
                0
        );*/
        // adaptador = new AdaptadorCursorLugares(this, Lugares.listado());
        // setListAdapter(adaptador);
    }

    private void actualizaMejorLocaliz(Location localiz) {
        if (localiz != null && (mejorLocaliz == null
                || localiz.getAccuracy() < 2 * mejorLocaliz.getAccuracy()
                || localiz.getTime() - mejorLocaliz.getTime() > DOS_MINUTOS)) {
            Log.d(Lugares.TAG, "Nueva mejor localización");
            mejorLocaliz = localiz;
            Lugares.posicionActual.setLatitud(localiz.getLatitude());
            Lugares.posicionActual.setLongitud(localiz.getLongitude());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(Lugares.TAG, "Nueva localización: " + location);
        actualizaMejorLocaliz(location);
    }

    @Override
    public void onProviderDisabled(String proveedor) {
        Log.d(Lugares.TAG, "Se deshabilita: " + proveedor);
        activarProveedores();
    }

    @Override
    public void onProviderEnabled(String proveedor) {
        Log.d(Lugares.TAG, "Se habilita: " + proveedor);
        activarProveedores();
    }

    @Override
    public void onStatusChanged(String proveedor, int estado, Bundle extras) {
        Log.d(Lugares.TAG, "Cambia estado: " + proveedor);
        activarProveedores();
    }

    @Override
    protected void onResume() {
        super.onResume();
        activarProveedores();
        //mp.start();
        Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();
    }

    private void activarProveedores() {
        if (manejador.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            manejador.requestLocationUpdates(LocationManager.GPS_PROVIDER, 20 * 1000, 5, this);
        }
        if (manejador.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            manejador.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10 * 1000, 10, this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        manejador.removeUpdates(this);
        Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onSaveInstanceState(Bundle estadoGuardado) {
        super.onSaveInstanceState(estadoGuardado);
        /* if (mp != null) {
            int pos = mp.getCurrentPosition();
            estadoGuardado.putInt("posicion", pos);
        } */
    }

    @Override
    protected void onRestoreInstanceState(Bundle estadoGuardado) {
        super.onRestoreInstanceState(estadoGuardado);
        /* if (estadoGuardado != null && mp != null) {
            int pos = estadoGuardado.getInt("posicion");
            mp.seekTo(pos);
        } */
    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(this, "onStart", Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onStop() {
        super.onStop();
        // mp.pause();
        Toast.makeText(this, "onStop", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Toast.makeText(this, "onRestart", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "onDestroy", Toast.LENGTH_SHORT).show();
    }

    /*
    @Override
    protected void onListItemClick(ListView listView, View vista, int posicion, long id) {
        super.onListItemClick(listView, vista, posicion, id);
        Intent intent = new Intent(this, VistaLugar.class);
        intent.putExtra("id", id);
        startActivity(intent);
    } */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        switch (id) {
            case R.id.menu_mapa:
                Intent i = new Intent(this, Mapa.class);
                startActivity(i);
                break;
            case R.id.acercaDe:
                lanzarAcercaDe(null);
                break;
            case R.id.menu_preferencias:
                lanzarPreferencias(null);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void lanzarAcercaDe(View view) {
        Intent i = new Intent(this, AcercaDe.class);
        startActivity(i);
    }

    public void lanzarPreferencias(View view) {
        Intent i = new Intent(this, Preferencias.class);
        startActivity(i);
    }


    public void mostrarPreferencias(View view) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String s = "notificaciones: " + pref.getBoolean("notificaciones", true) + ", distancia mínima: " + pref.getString("distancia", "?");
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    public void lanzarVistaLugar(View view) {
        final EditText entrada = new EditText(this);
        entrada.setText("0");
        new AlertDialog.Builder(this).setTitle("Selección de lugar").setMessage("indica su id:").setView(entrada).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                long id = Long.parseLong(entrada.getText().toString());
                Intent i = new Intent(MainActivity.this, VistaLugar.class);
                i.putExtra("id", id);
                startActivity(i);
            }
        })
                .setNegativeButton("Cancelar", null).show();
    }

}
