package com.univali.transex.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.univali.transex.R;
import com.univali.transex.classes.Sessao;
import com.univali.transex.database.TranspEXDAO;
import com.univali.transex.database.tables.Entrega;
import com.univali.transex.viewholder.ListaEntregaViewHolder;

public class ListaEntregaAdapter extends RecyclerView.Adapter {
    private TranspEXDAO db = TranspEXDAO.getInstance();

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ConstraintLayout container = (ConstraintLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista_entrega, parent, false);
        ListaEntregaViewHolder viewHolder = new ListaEntregaViewHolder(container);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Entrega entrega;

        if(Sessao.getInstance().getUsuario().isEntregaEncomendas()) {
            entrega = db.readMinhasEntregas(Sessao.getInstance().getUsuario().getId()).get(position);
        }else{
            entrega = db.readMinhasSolicitacoes(Sessao.getInstance().getUsuario().getId()).get(position);
        }

        ListaEntregaViewHolder viewHolder = (ListaEntregaViewHolder) holder;
        viewHolder.tvDescricao.setText(entrega.getDescricao());
        viewHolder.tvCidUf.setText(entrega.getCidadeDestino() + "/" + entrega.getUfDestino());
        viewHolder.tvVolPes.setText(String.valueOf(entrega.getVolumePeso()));

        viewHolder.setIdEntrega(entrega.getId());
    }

    @Override
    public int getItemCount() {
        int count = 0;

        if(Sessao.getInstance().getUsuario().isEntregaEncomendas()) {
            count = db.readMinhasEntregas(Sessao.getInstance().getUsuario().getId()).size();
        }else{
            count = db.readMinhasSolicitacoes(Sessao.getInstance().getUsuario().getId()).size();
        }

        return count;
    }
}
