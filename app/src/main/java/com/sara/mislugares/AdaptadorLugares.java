package com.sara.mislugares;

/*public class AdaptadorLugares extends BaseAdapter {
    private LayoutInflater inflador; // Crea Layouts a partir del XML
    TextView nombre, direccion;
    ImageView foto;
    RatingBar valoracion;
    TextView distancia;

    public AdaptadorLugares(Context contexto) {
        inflador = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(int posicion, View vistaReciclada, ViewGroup padre) {
        Lugar lugar = Lugares.elemento(posicion);
        if (vistaReciclada == null) {
            vistaReciclada = inflador.inflate(R.layout.elemento_lista, null);
        }
        nombre = (TextView) vistaReciclada.findViewById(R.id.nombre);
        direccion = (TextView) vistaReciclada.findViewById(R.id.direccion);
        foto = (ImageView) vistaReciclada.findViewById(R.id.foto);
        valoracion = (RatingBar) vistaReciclada.findViewById(R.id.valoracion);
        nombre.setText(lugar.getNombre());
        direccion.setText(lugar.getDireccion());
        int id = R.drawable.otros;
        switch (lugar.getTipo()) {
            case RESTAURANTE:
                id = R.drawable.restaurante;
                break;
            case BAR:
                id = R.drawable.bar;
                break;
            case COPAS:
                id = R.drawable.copas;
                break;
            case ESPECTACULO:
                id = R.drawable.espectaculos;
                break;
            case HOTEL:
                id = R.drawable.hotel;
                break;
            case COMPRAS:
                id = R.drawable.compras;
                break;
            case EDUCACION:
                id = R.drawable.educacion;
                break;
            case DEPORTE:
                id = R.drawable.deporte;
                break;
            case NATURALEZA:
                id = R.drawable.naturaleza;
                break;
            case GASOLINERA:
                id = R.drawable.gasolinera;
                break;
        }
        foto.setImageResource(id);
        foto.setScaleType(ImageView.ScaleType.FIT_END);
        valoracion.setRating(lugar.getValoracion());
        distancia = (TextView) vistaReciclada.findViewById(R.id.distancia);
        if (Lugares.posicionActual != null && lugar.getPosicion() != null) {
            int d = (int) Lugares.posicionActual.distancia(lugar.getPosicion());
            if (d < 2000) {
                distancia.setText(d + " m");
            } else {
                distancia.setText(d / 1000 + "Km");
            }
        }
        return vistaReciclada;
    }

    public int getCount() {
        return Lugares.size();
    }

    public Object getItem(int posicion) {
        return Lugares.elemento(posicion);
    }

    public long getItemId(int posicion) {
        return posicion;
    }
} */
