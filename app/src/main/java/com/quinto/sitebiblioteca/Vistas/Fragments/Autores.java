package com.quinto.sitebiblioteca.Vistas.Fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.collection.ArrayMap;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.quinto.sitebiblioteca.Controller.VolleySingleton;
import com.quinto.sitebiblioteca.Models.Autor;
import com.quinto.sitebiblioteca.R;
import com.quinto.sitebiblioteca.Vistas.Actividades.ActivityLogin;
import com.quinto.sitebiblioteca.Vistas.Actividades.Adapters.AutorAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Autores.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Autores#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Autores extends Fragment implements Response.Listener<JSONObject>,Response.ErrorListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    RecyclerView recycler;
    List<Autor> listaAutores;

    ProgressDialog progress;

    EditText cajaBusqueda;
    Button buscar;

    // RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    public Autores() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Autores.
     */
    // TODO: Rename and change types and number of parameters
    public static Autores newInstance(String param1, String param2) {
        Autores fragment = new Autores();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View vista= inflater.inflate(R.layout.fragment_autores, container, false);
        listaAutores = new ArrayList<>();
        cajaBusqueda = vista.findViewById(R.id.txtNombreBusqueda);
        buscar = vista.findViewById(R.id.btnConsultarAutor);
        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cajaBusqueda.length()!=0){
                    InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

                    inputMethodManager.hideSoftInputFromWindow(cajaBusqueda.getWindowToken(), 0);
                    listaAutores = new ArrayList<>();
                    buscarAutor();


                }else {
                    Toast.makeText(getContext(), "Ingrese el nombre del autor de su busqueda",Toast.LENGTH_SHORT).show();
                }

            }
        });

        recycler = (RecyclerView) vista.findViewById(R.id.reciclerAutor);
        recycler.setLayoutManager(new LinearLayoutManager(this.getContext()));
        listarAutores();
        return vista;
    }

    private void buscarAutor(){
        progress=new ProgressDialog(getContext());
        progress.setTitle("Obteniendo Datos");
        progress.setMessage("Buscando...");
        progress.show();
        String ip=getString(R.string.host);

        String url=ip+"/autores/getautor/"+cajaBusqueda.getText().toString();

        final Map<String, String> mHeaders = new ArrayMap<>();
        mHeaders.put("Content-Type", "application/json");
        mHeaders.put("Accept", "application/json");


        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,url,null,this,this){
            public Map<String, String> getHeaders(){
                return mHeaders;
            }
        };
        // request.add(jsonObjectRequest);
        VolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(jsonObjectRequest);


    }

    private void listarAutores(){
        progress=new ProgressDialog(getContext());
        progress.setTitle("Obteniendo Datos");
        progress.setMessage("Consultando...");
        progress.show();
        String ip=getString(R.string.host);

        String url=ip+"/autores";

        final Map<String, String> mHeaders = new ArrayMap<>();
        mHeaders.put("Content-Type", "application/json");
        mHeaders.put("Accept", "application/json");


        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,url,null,this,this){
            public Map<String, String> getHeaders(){
                return mHeaders;
            }
        };
        // request.add(jsonObjectRequest);
        VolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(jsonObjectRequest);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getContext(), "No se puede conectar "+error.toString(), Toast.LENGTH_LONG).show();
        System.out.println();
        Log.d("ERROR: ", error.toString());
        progress.hide();

    }

    @Override
    public void onResponse(JSONObject response) {
        Autor autores = null;
        String data = response.optString("information");
        JSONArray json = response.optJSONArray("data");

        try {
            if (data.contains("successfully")){
                for(int i=0; i<json.length(); i++){
                    autores = new Autor();
                    JSONObject jsonObject = json.getJSONObject(i);
                    autores.setNombre(jsonObject.optString("nombre"));
                    autores.setApellido(jsonObject.optString("apellido"));
                    String mail = jsonObject.optString("email");
                    if (mail.isEmpty()){
                        autores.setEmail("Sin Correo");
                    }else{
                        autores.setEmail(jsonObject.optString("email"));
                    }
                    listaAutores.add(autores);
                }
                progress.hide();
                AutorAdapter adapter = new AutorAdapter(listaAutores);
                recycler.setAdapter(adapter);
            }else if(data.contains("Data could not be found")){
                final Dialog dlg = new Dialog(getContext());
                dlg.setContentView(R.layout.dlg_notfound);
                TextView texto = dlg.findViewById(R.id.lblRegNotFound);
                texto.setText("No se han encontrado registros");
                Button cancelar = dlg.findViewById(R.id.btnFoundNot);
                cancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dlg.hide();
                    }
                });
                dlg.show();

                //Toast.makeText(getContext(), "No existe", Toast.LENGTH_SHORT).show();
                recycler.setAdapter(null);
                progress.hide();
            }
        }catch (Exception e){
            e.printStackTrace();
            final Dialog dlg = new Dialog(getContext());
            dlg.setContentView(R.layout.dlg_error);
            TextView texto = dlg.findViewById(R.id.lblError);
            Button cancelar = dlg.findViewById(R.id.btnError);
            cancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dlg.hide();
                }
            });
            dlg.show();
            Toast.makeText(getContext(), "No se ha podido establecer conexión con el servidor" +
                    " "+response, Toast.LENGTH_LONG).show();
            progress.hide();
        }
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
