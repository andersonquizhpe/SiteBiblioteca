package com.quinto.sitebiblioteca;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.ui.AppBarConfiguration;

import com.google.android.material.navigation.NavigationView;
import com.quinto.sitebiblioteca.Vistas.Actividades.ActividadBuscarLibro;
import com.quinto.sitebiblioteca.Vistas.Actividades.ActividadLibros;
import com.quinto.sitebiblioteca.Vistas.Actividades.ActivityLogin;
import com.quinto.sitebiblioteca.Vistas.Fragments.Autores;
import com.quinto.sitebiblioteca.Vistas.Fragments.StartFragment;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

public class Inicio extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, StartFragment.OnFragmentInteractionListener, Autores.OnFragmentInteractionListener {

    private AppBarConfiguration mAppBarConfiguration;
    //TextView usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_end);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent intent = new Intent(Inicio.this, ActivityLogin.class);
                startActivity(intent);
                Inicio.this.finish();
            }
        });*/
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        Fragment miFragment=new StartFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.content, miFragment).commit();


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        /*mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();*/
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity_end, menu);
        return true;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Fragment miFragmet=null;
        boolean fragmetS = false;
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if ( id==R.id.opcionInicio){
            miFragmet = new StartFragment();
            fragmetS = true;
            drawer.closeDrawer(GravityCompat.START);
        }else if(id==R.id.opcionAutores){
            miFragmet = new Autores();
            fragmetS = true;
            drawer.closeDrawer(GravityCompat.START);

        }else if(id==R.id.opcionLibros){
            Intent intent = new Intent(Inicio.this, ActividadLibros.class);
            startActivity(intent);

            drawer.closeDrawer(GravityCompat.START);

        }else if(id==R.id.opcionBuscarLibro){
            Intent intent = new Intent(Inicio.this, ActividadBuscarLibro.class);
            startActivity(intent);

            drawer.closeDrawer(GravityCompat.START);

        }else if(id==R.id.opcionLogout){
            Intent intent = new Intent(Inicio.this, ActivityLogin.class);
            startActivity(intent);
            Inicio.this.finish();

            drawer.closeDrawer(GravityCompat.START);

        }else{
            Toast.makeText(Inicio.this, "Seleccione una opcion", Toast.LENGTH_SHORT).show();
        }

        if (fragmetS == true){
            getSupportFragmentManager().beginTransaction().replace(R.id.content, miFragmet).commit();
        }


        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
