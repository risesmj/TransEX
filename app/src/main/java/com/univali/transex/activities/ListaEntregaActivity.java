package com.univali.transex.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.univali.transex.R;
import com.univali.transex.adapter.ListaEntregaAdapter;
import com.univali.transex.classes.Sessao;
import com.univali.transex.database.TranspEXDAO;

public class ListaEntregaActivity extends AppCompatActivity {
    private ListaEntregaAdapter adapter;
    private RecyclerView rvLista;
    private TranspEXDAO db = TranspEXDAO.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_entrega);

        if(Sessao.getInstance().getUsuario().isEntregaEncomendas()) {
            getSupportActionBar().setTitle("Minhas Entregas");
            FloatingActionButton fbAdd = findViewById(R.id.fbAddLEA);
            fbAdd.hide();
        }else{
            getSupportActionBar().setTitle("Minhas Solicitações");
        }

        adapter = new ListaEntregaAdapter();
        rvLista = findViewById(R.id.rvListaLEA);
        rvLista.setLayoutManager( new LinearLayoutManager(this));
        rvLista.setAdapter(adapter);
    }

    public void visualizarEntrega(int id){
        Intent intent = new Intent(this, AcompanhamentoEntregaActivity.class);
        intent.putExtra("idEntrega",id);
        startActivity(intent);
    }

    public void botaoAdicionarEntrega(View v){
        startActivityForResult(new Intent(this,CadastroEntregaActivity.class),1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1){
            if(resultCode == 200){
                if(Sessao.getInstance().getUsuario().isEntregaEncomendas()) {
                    adapter.notifyItemInserted(db.readMinhasEntregas(Sessao.getInstance().getUsuario().getId()).size()-1);
                }else{
                    adapter.notifyItemInserted(db.readMinhasSolicitacoes(Sessao.getInstance().getUsuario().getId()).size()-1);
                }
            }
        }
    }
}
