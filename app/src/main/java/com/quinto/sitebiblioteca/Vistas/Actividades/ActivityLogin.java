package com.quinto.sitebiblioteca.Vistas.Actividades;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.quinto.sitebiblioteca.Inicio;
import com.quinto.sitebiblioteca.R;
import com.quinto.sitebiblioteca.Vistas.Fragments.StartFragment;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class ActivityLogin extends AppCompatActivity implements View.OnClickListener {

    /////////////////////////////////////////////


    String host = "http://10.20.4.149:8089";
    String auntenticar = "/usuarios/login";
    SWHilo sw;


    EditText usuario, clave;
    Button ingresar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        cargarComponentes();
    }

    private void cargarComponentes() {
        usuario = findViewById(R.id.txtUser);
        clave = findViewById(R.id.txtClave);

        ingresar = findViewById(R.id.btnIngresar);
        ingresar.setOnClickListener(this);
    }

    public class SWHilo extends AsyncTask<String, Void,String> {

        @Override
        protected String doInBackground(String... parametros) {
            String consulta ="";
            URL url = null;
            String ruta = parametros[0];

            if(parametros[1].equals("1")){
                try {
                    url = new URL(ruta);
                    HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
                    conexion.setDoInput(true);
                    conexion.setDoOutput(true);
                    conexion.setUseCaches(false);
                    conexion.setRequestProperty("Content-Type", "application/json");
                    conexion.setRequestProperty("Accept", "application/json");
                    conexion.connect();

                    //se crea el json
                    JSONObject json = new JSONObject();
                    json.put("username", parametros[2]);
                    json.put("password", parametros[3]);

                    Log.e("mes", json+"");
                    // Envio los parámetros post.
                    OutputStream os = conexion.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    writer.write(json.toString());
                    writer.flush();
                    writer.close();

                    int codigoRespuesta = conexion.getResponseCode();
                    if (codigoRespuesta == HttpURLConnection.HTTP_OK){
                        InputStream in = new BufferedInputStream(conexion.getInputStream());
                        BufferedReader lector = new BufferedReader(new InputStreamReader(in));
                        consulta += lector.readLine();
                        lector.close();
                    }else {

                        Log.e("mensaje","no hay coneccion");
                    }
                    conexion.disconnect();
                }catch (Exception ex){
                    Log.e("mensaje","Error Ingresar");
                }
            }
            return consulta;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.e("Usuario", s+"");
        }
    }

    @Override
    public void onClick(View v) {
        sw = new SWHilo();
        String user = usuario.getText().toString();
        String pass = clave.getText().toString();

        if(user.length() == 0 || pass.length() == 0){
            Toast.makeText(this,"Por favor llene todos los campos",Toast.LENGTH_SHORT).show();
        }else {
            try {
                String c = sw.execute(host.concat(auntenticar),"1",user,pass).get();

                if(c.contains("Welcome to site")){
                    Toast.makeText(this," Bienvenido Usuario: "+usuario.getText().toString(),Toast.LENGTH_SHORT).show();
                    String nombre = usuario.getText().toString();

                    Intent intent = new Intent(ActivityLogin.this, Inicio.class);
                    //////////////////////////////////
                    Bundle datos = new Bundle();
                    datos.putString("nombre", nombre );
                    intent.putExtras(datos);
                    //////////////////////////////////////
                    startActivity(intent);
                    ActivityLogin.this.finish();
                }else if(c.contains("invalidate username or password")){
                    final Dialog dlg = new Dialog(ActivityLogin.this);
                    dlg.setContentView(R.layout.dlg_usernotfound);
                    TextView texto = dlg.findViewById(R.id.lblUserNotFound);
                    texto.setText("Usuario y/o contraseña invalida");
                    Button cancelar = dlg.findViewById(R.id.btnUserNot);
                    cancelar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dlg.hide();
                        }
                    });
                    dlg.show();
                    //Toast.makeText(this," Usuario y/o clave incorrectos ",Toast.LENGTH_SHORT).show();
                    clave.setText("");
                }else{
                    final Dialog dlg = new Dialog(ActivityLogin.this);
                    dlg.setContentView(R.layout.dlg_usernotfound);
                    TextView texto = dlg.findViewById(R.id.lblUserNotFound);
                    Button cancelar = dlg.findViewById(R.id.btnUserNot);
                    cancelar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dlg.hide();
                        }
                    });
                    dlg.show();
                    //Toast.makeText(this," Usuario no encontrado",Toast.LENGTH_SHORT).show();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    ///////////////////////////////////777777777///////
}
