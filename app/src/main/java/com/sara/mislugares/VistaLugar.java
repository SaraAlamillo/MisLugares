package com.sara.mislugares;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.DateFormat;
import java.util.Date;


public class VistaLugar extends ActionBarActivity {
    private long id;
    private Lugar lugar;
    private ImageView imageView;
    private Uri uriFoto;
    final static int RESULTADO_EDITAR = 1;
    final static int RESULTADO_GALERIA = 2;
    final static int RESULTADO_FOTO = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_lugar);
        LinearLayout pUrl = (LinearLayout) findViewById(R.id.capa_url);
        pUrl.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                pgWeb(null);
            }
        });

        LinearLayout direccion = (LinearLayout) findViewById(R.id.capa_direccion);
        direccion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                verMapa(null);
            }
        });

        LinearLayout telefono = (LinearLayout) findViewById(R.id.capa_telefono);
        telefono.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                llamadaTelefono(null);
            }
        });

        ImageView camara = (ImageView) findViewById(R.id.camara);
        camara.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                tomarFoto(null);
            }
        });

        ImageView bGaleria = (ImageView) findViewById(R.id.galeria);
        bGaleria.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                galeria(null);
            }
        });

        ImageView eliminar = (ImageView) findViewById(R.id.eliminar);
        eliminar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                eliminarFoto(null);
            }
        });
        Bundle extras = getIntent().getExtras();
        id = extras.getLong("id", -1);
        imageView = (ImageView) findViewById(R.id.foto);
        actualizarVistas();
    }

    public void eliminarFoto(View view) {
        lugar.setFoto(null);
        ponerFoto(imageView, null);
    }

    public void galeria(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, RESULTADO_GALERIA);
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
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, lugar.getNombre() + " - " + lugar.getUrl());
                startActivity(intent);
                return true;
            case R.id.accion_llegar:
                verMapa(null);
                return true;
            case R.id.accion_editar:
                edicionLugar(null);
                return true;
            case R.id.accion_borrar:
                borrarLugar(null, (int) id);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void llamadaTelefono(View view) {
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + lugar.getTelefono())));
    }

    public void pgWeb(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(lugar.getUrl())));
    }

    public void verMapa(View view) {
        Uri uri;
        double lat = lugar.getPosicion().getLatitud();
        double lon = lugar.getPosicion().getLongitud();
        if (lat != 0 || lon != 0) {
            uri = Uri.parse("geo:" + lat + "," + lon);
        } else {
            uri = Uri.parse("geo:0,0?q=" + lugar.getDireccion());
        }

        Toast.makeText(this, uri.toString(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
    }

    public void borrarLugar(View view, final int id) {
        new AlertDialog.Builder(this)
                .setTitle("¿Está seguro que quiere eliminar el lugar?")
                .setMessage("Tras su eliminación, la recuperación será imposible.")
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Lugares.borrar(id);
                        VistaLugar.this.recreate();
                    }
                })
                .setNegativeButton("No", null).show();
    }

    public void edicionLugar(View view) {
        Intent i = new Intent(VistaLugar.this, EdicionLugar.class);
        i.putExtra("id", id);
        startActivityForResult(i, RESULTADO_EDITAR);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULTADO_EDITAR) {
            actualizarVistas();
            findViewById(R.id.scrollView1).invalidate();
        } else if (requestCode == RESULTADO_GALERIA && resultCode == Activity.RESULT_OK) {
            lugar.setFoto(data.getDataString());
            Lugares.actualizaLugar((int) id, lugar);
            ponerFoto(imageView, lugar.getFoto());
        } else if (requestCode == RESULTADO_FOTO && resultCode == Activity.RESULT_OK && lugar != null && uriFoto != null) {
            lugar.setFoto(uriFoto.toString());
            Lugares.actualizaLugar((int) id, lugar);
            ponerFoto(imageView, lugar.getFoto());
        }
    }

    public void tomarFoto(View view) {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        uriFoto = Uri.fromFile(new File(
                Environment.getExternalStorageDirectory() +
                        File.separator +
                        "img_" +
                        (System.currentTimeMillis() / 1000) + ".jpg"));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uriFoto);
        startActivityForResult(intent, RESULTADO_FOTO);
    }

    protected void ponerFoto(ImageView imageView, String uri) {
        if (uri != null) {
            try {
                imageView.setImageURI(Uri.parse(uri));
            } catch (Throwable e) {
                Toast.makeText(this, "La imagen es demasiado grande. Intente reducir el tamaño.", Toast.LENGTH_SHORT).show();
            }
            imageView.setImageURI(Uri.parse(uri));
        } else {
            imageView.setImageBitmap(null);
        }
    }


    public void actualizarVistas() {
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
                Lugares.actualizaLugar((int) id, lugar);
            }
        });
        ponerFoto(imageView, lugar.getFoto());
    }
}

/*
public class VistaLugar extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_lugar);
    }
}
 */