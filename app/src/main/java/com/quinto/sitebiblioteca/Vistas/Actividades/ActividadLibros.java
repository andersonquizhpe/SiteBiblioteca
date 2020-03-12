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
import android.widget.Adapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

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

public class ActividadLibros extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {

    RecyclerView recycler;
    List<Libro> listaLibros;
    LibroAdapter adapter;

    ProgressDialog progress;
    JsonObjectRequest jsonObjectRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_libros);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        listaLibros = new ArrayList<>();
        cargarComponentes();
        listarLibros();
    }

    private void cargarComponentes(){
        recycler = findViewById(R.id.recyclerLibros);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setHasFixedSize(true);
    }

    public void listarLibros(){
        progress = new ProgressDialog(ActividadLibros.this);
        progress.setTitle("Obteniendo Datos");
        progress.setMessage("Listando....");
        progress.show();

        String ip = getString(R.string.host);
        String url = ip+"/libros";

        final Map<String, String> mHeaders = new ArrayMap<>();
        mHeaders.put("Content-Type", "application/json");
        mHeaders.put("Accept", "application/json");

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,null, this, this){
            public Map<String, String> getHeaders(){
                return mHeaders;
            }
        };

        VolleySingleton.getIntanciaVolley(this).addToRequestQueue(jsonObjectRequest);
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
        Libro libros = null;
        String data= response.optString("information");

        JSONArray json = response.optJSONArray("data");
        try{
            if(data.contains("successfully")){
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
            }else{
                Toast.makeText(this, "No hay libros", Toast.LENGTH_SHORT).show();
                recycler.setAdapter(null);
                progress.hide();
            }
        }catch (JSONException e){
            e.printStackTrace();
            Toast.makeText(this, "No se ha podido establecer conexión con el servidor" +
                    " "+response, Toast.LENGTH_LONG).show();
            progress.hide();
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}
