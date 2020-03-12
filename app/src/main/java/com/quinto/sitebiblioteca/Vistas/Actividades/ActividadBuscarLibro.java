package com.quinto.sitebiblioteca.Vistas.Actividades;

import androidx.appcompat.app.AppCompatActivity;
import androidx.collection.ArrayMap;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.quinto.sitebiblioteca.Controller.VolleySingleton;
import com.quinto.sitebiblioteca.Models.Libro;
import com.quinto.sitebiblioteca.R;
import com.quinto.sitebiblioteca.Vistas.Actividades.Adapters.LibroAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ActividadBuscarLibro extends AppCompatActivity implements View.OnClickListener, Response.Listener<JSONObject>, Response.ErrorListener {
    EditText busqueda;
    Button boton;

    RecyclerView recycler;
    LibroAdapter adapter;
    List<Libro> listaLibros;

    ProgressDialog progress;
    JsonObjectRequest jsonObjectRequest;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_buscar_libro);
        //recycler = findViewById(R.id.reciclerLibro);
        //recycler.setLayoutManager(new LinearLayoutManager(this));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        cargarComponentes();

    }

    private void cargarComponentes(){
        recycler = findViewById(R.id.reciclerLibro);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        busqueda = findViewById(R.id.txtNombreBusquedaLibro);

        boton = findViewById(R.id.btnConsultarLibro);
        boton.setOnClickListener(this);
    }

    private void buscarLibro(){
        progress = new ProgressDialog(ActividadBuscarLibro.this);
        progress.setTitle("Obteniendo datos");
        progress.setMessage("Buscando...");
        progress.show();

        String ip = getString(R.string.host);
        String url = ip+"/libros/getLibroT/"+busqueda.getText().toString();
        final Map<String, String> mHeaders = new ArrayMap<>();
        mHeaders.put("Content-Type", "application/json");
        mHeaders.put("Accept", "application/json");

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this){
          public Map<String, String> getHeaders(){
              return mHeaders;
          }
        };
        VolleySingleton.getIntanciaVolley(this).addToRequestQueue(jsonObjectRequest);
    }

    @Override
    public void onClick(View v) {
        if(busqueda.length()!=0){
            listaLibros = new ArrayList<>();
            buscarLibro();
        }else{
            Toast.makeText(this, "Escriba el libro que desea buscar", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(this, "No se puede conectar "+error.toString(), Toast.LENGTH_LONG).show();
        System.out.println();
        Log.d("ERROR: ", error.toString());
        progress.hide();
    }

    @Override
    public void onResponse(JSONObject response) {

        Libro libros=null;
        String info = response.optString("information");
        JSONArray json = response.optJSONArray("data");
        try{
            if(info.contains("successfully")){
                ////////////////////////////////////////////////////////////////////////////////////
                for(int i=0; i<json.length();i++){
                    libros = new Libro();
                    JSONObject jsonObject = json.getJSONObject(i);
                    libros.setTitulo(jsonObject.optString("titulo"));
                    libros.setFecha(jsonObject.optString("fechaPublicacion"));
                    libros.setPagina(jsonObject.optInt("numeroPaginas"));
                    libros.setEditorial(jsonObject.optString("nombre_editorial"));
                    libros.setIsbn(jsonObject.optString("isbn"));
                    libros.setResumen(jsonObject.optString("resumen"));

                    listaLibros.add(libros);
                }
                progress.hide();
                adapter = new LibroAdapter(listaLibros);
                adapter.setOnclickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cargarDialogo(v);
                    }
                });
                recycler.setAdapter(adapter);

                ////////////////////////////////////////////////////////////////////////////////////

            }else{
                Toast.makeText(this, "El libro buscado no se encuentra", Toast.LENGTH_SHORT).show();
                //recycler.setAdapter(null);
                progress.hide();
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void cargarDialogo(View v) {
        Dialog dlgDetalles = new Dialog(this);
        dlgDetalles.setContentView(R.layout.dlg_libros);
        TextView titulo = dlgDetalles.findViewById(R.id.lblTitulo);
        TextView fecha = dlgDetalles.findViewById(R.id.lblFecha);
        TextView isbn = dlgDetalles.findViewById(R.id.lblIsbn);
        TextView editorial = dlgDetalles.findViewById(R.id.lblEditorial);
        TextView res = dlgDetalles.findViewById(R.id.lblResumen);


        titulo.setText("Titulo: "+listaLibros.get(recycler.getChildAdapterPosition(v)).getTitulo());
        fecha.setText("Publicado: "+listaLibros.get(recycler.getChildAdapterPosition(v)).getFecha());
        isbn.setText("ISBN: "+listaLibros.get(recycler.getChildAdapterPosition(v)).getIsbn());
        editorial.setText("Editorial: "+listaLibros.get(recycler.getChildAdapterPosition(v)).getEditorial());
        String resumen = listaLibros.get(recycler.getChildAdapterPosition(v)).getResumen();
        if(resumen.isEmpty()){
            res.setText("Resumen:\n"+"No hay un resumen de este libro");
        }else{
            res.setText("Resumen:\n\n"+listaLibros.get(recycler.getChildAdapterPosition(v)).getResumen());
        }

        dlgDetalles.show();

    }

}
