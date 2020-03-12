package com.quinto.sitebiblioteca.Vistas.Actividades.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.quinto.sitebiblioteca.Models.Libro;
import com.quinto.sitebiblioteca.R;

import java.util.List;

public class LibroAdapter extends RecyclerView.Adapter<LibroAdapter.LibroHolder> implements View.OnClickListener{
    List<Libro> lista;

    private View.OnClickListener click;

    public LibroAdapter(List<Libro> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public LibroHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ibro, null);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        vista.setLayoutParams(layoutParams);
        vista.setOnClickListener(this);
        return new LibroHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull LibroHolder holder, int position) {
        holder.titulo.setText(lista.get(position).getTitulo()+"");
        holder.fecha.setText(lista.get(position).getFecha()+"");
        holder.isbn.setText(lista.get(position).getIsbn());
        holder.editorial.setText(lista.get(position).getEditorial());
        holder.paginas.setText(lista.get(position).getPagina()+"");
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public void setOnclickListener(View.OnClickListener onclickListener){
        this.click = onclickListener;
    }

    @Override
    public void onClick(View v) {
        if(click!= null){
            click.onClick(v);
        }
    }

    public class LibroHolder extends RecyclerView.ViewHolder{
        TextView titulo, fecha, isbn, editorial, paginas, resumen;
        public LibroHolder(@NonNull View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.txtTitulo);
            fecha = itemView.findViewById(R.id.txtFechaP);
            isbn = itemView.findViewById(R.id.txtIsbn);
            editorial = itemView.findViewById(R.id.txtEditorial);
            paginas = itemView.findViewById(R.id.txtPaginas);
        }
    }
}
