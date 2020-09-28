package com.univali.transex.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.univali.transex.R;
import com.univali.transex.classes.RegraNegocio;
import com.univali.transex.database.tables.Usuario;

public class CadastroUsuarioActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);
        getSupportActionBar().setTitle("Nova conta");
    }

    public void confirmarCadastroUsuario(View v){
        TextView etNome = findViewById(R.id.etNomeCUA);
        TextView etEmail = findViewById(R.id.etEmailCUA);
        TextView etTelefone = findViewById(R.id.etTelefoneCUA);
        TextView etSenha = findViewById(R.id.etSenhaCUA);
        CheckBox cbENtrga = findViewById(R.id.cbEntregCUA);

        Usuario usuario = new Usuario();
        usuario.setNome(etNome.getText().toString());
        usuario.setEmail(etEmail.getText().toString());
        usuario.setSenha(etSenha.getText().toString());
        usuario.setTelefone(etTelefone.getText().toString());
        usuario.setEntregaEncomendas(cbENtrga.isChecked());

        RegraNegocio rg = new RegraNegocio(this);

        if(rg.prepararCadastroUsuario(usuario)){
            Toast.makeText(this,"Usuário cadastrado com sucesso.",Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
