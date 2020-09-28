package com.univali.transex.viewholder;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.univali.transex.R;
import com.univali.transex.activities.ListaEntregaActivity;

public class ListaEntregaViewHolder extends RecyclerView.ViewHolder {
    private int idEntrega;
    public TextView tvDescricao;
    public TextView tvCidUf;
    public TextView tvVolPes;
    public TextView tvPreco;

    public ListaEntregaViewHolder(@NonNull View itemView) {

        super(itemView);

        tvDescricao = itemView.findViewById(R.id.tvDescricaoILE);
        tvCidUf = itemView.findViewById(R.id.tvCidEstILEE);
        tvVolPes = itemView.findViewById(R.id.tvVolPesILE);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ListaEntregaActivity) view.getContext()).visualizarEntrega(idEntrega);
            }
        });
    }

    public int getIdEntrega() {
        return idEntrega;
    }

    public void setIdEntrega(int idEntrega) {
        this.idEntrega = idEntrega;
    }
}
