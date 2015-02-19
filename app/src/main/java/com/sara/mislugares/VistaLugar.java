package com.sara.mislugares;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;

public class VistaLugar extends Activity {
    private long id;
    private Lugar lugar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_lugar);
        Bundle extras = getIntent().getExtras();
        id = extras.getLong("id", -1);
        lugar = Lugares.elemento((int) id);
        TextView nombre = (TextView) findViewById(R.id.nombre);
        nombre.setText(lugar.getNombre());
        ImageView logo_tipo = (ImageView) findViewById(R.id.logo_tipo);
        logo_tipo.setImageResource(lugar.getTipo().getRecurso());
        TextView tipo = (TextView) findViewById(R.id.tipo);
        tipo.setText(lugar.getTipo().getTexto());
        if (lugar.getDireccion() == "") {
            findViewById(R.id.capa_direccion).setVisibility(View.GONE);
        } else {
            TextView direccion = (TextView) findViewById(R.id.direccion);
            direccion.setText(lugar.getDireccion());
        }
        if (lugar.getTelefono() == 0) {
            findViewById(R.id.capa_telefono).setVisibility(View.GONE);
        } else {
            TextView telefono = (TextView) findViewById(R.id.telefono);
            telefono.setText(Integer.toString(lugar.getTelefono()));
        }
        if (lugar.getUrl() == "") {
            findViewById(R.id.capa_url).setVisibility(View.GONE);
        } else {
            TextView url = (TextView) findViewById(R.id.url);
            url.setText(lugar.getUrl());
        }
        if (lugar.getComentario() == "") {
            findViewById(R.id.capa_comentarios).setVisibility(View.GONE);
        } else {
            TextView comentario = (TextView) findViewById(R.id.comentario);
            comentario.setText(lugar.getComentario());
        }
        TextView fecha = (TextView) findViewById(R.id.fecha);
        fecha.setText(DateFormat.getDateInstance().format(new Date(lugar.getFecha())));
        TextView hora = (TextView) findViewById(R.id.hora);
        hora.setText(DateFormat.getTimeInstance().format(new Date(lugar.getFecha())));
        RatingBar valoracion = (RatingBar) findViewById(R.id.valoracion);
        valoracion.setRating(lugar.getValoracion());
        valoracion.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float valor, boolean fromUser) {
                lugar.setValoracion(valor);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.vista_lugar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.accion_compartir:
                return true;
            case R.id.accion_llegar:
                return true;
            case R.id.accion_editar:
                return true;
            case R.id.accion_borrar:
                Lugares.borrar((int) id);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}