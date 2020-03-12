package com.quinto.sitebiblioteca.Vistas.Actividades.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.quinto.sitebiblioteca.Models.Autor;
import com.quinto.sitebiblioteca.R;

import java.util.List;

public class AutorAdapter extends  RecyclerView.Adapter<AutorAdapter.AutorHolder>{

    List<Autor> lista;

    public AutorAdapter(List<Autor> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public AutorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_autor, null);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        vista.setLayoutParams(layoutParams);
        return new AutorHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull AutorHolder holder, int position) {
        holder.apellido.setText(lista.get(position).getApellido()+"");
        holder.nombre.setText(lista.get(position).getNombre()+"");
        holder.email.setText(lista.get(position).getEmail()+"");

    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class AutorHolder extends RecyclerView.ViewHolder{
        TextView nombre, apellido, email;

        public AutorHolder(View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.txtNombre);
            apellido = itemView.findViewById(R.id.txtApellido);
            email = itemView.findViewById(R.id.txtEmail);
        }
    }

}
