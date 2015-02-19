package com.sara.mislugares;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {


    private Button bAcercaDe;
    private Button bSalir;
    private Button bPreferencias;
    private Button bMostrar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bAcercaDe = (Button) findViewById(R.id.bAcerca);
        bAcercaDe.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                lanzarAcercaDe(null);
            }
        });
        bPreferencias = (Button) findViewById(R.id.bPreferencias);
        bPreferencias.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                lanzarPreferencias(null);
            }
        });
        bSalir = (Button) findViewById(R.id.bSalir);
        bSalir.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
            }
        });
        bMostrar = (Button) findViewById(R.id.bMostrar);
        bMostrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                lanzarVistaLugar(null);
            }
        });
    }

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
