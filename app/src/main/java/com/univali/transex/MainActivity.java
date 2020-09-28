package com.univali.transex;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.accessibility.AccessibilityViewCommand;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.univali.transex.activities.CadastroUsuarioActivity;
import com.univali.transex.activities.ListaEntregaActivity;
import com.univali.transex.classes.Sessao;
import com.univali.transex.database.TranspEXDAO;
import com.univali.transex.database.tables.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
    }

    public void logar(View v){
        EditText etEmail = findViewById(R.id.etEmailMain);
        EditText etSenha = findViewById(R.id.etSenhaMain);
        TranspEXDAO db = TranspEXDAO.getInstance();
        Usuario usuario = db.getUsuario(etEmail.getText().toString(),etSenha.getText().toString());

        if(usuario != null) {
            Sessao sessao = Sessao.getInstance();
            sessao.setUsuario(usuario);
            startActivity(new Intent(this, ListaEntregaActivity.class));
            finish();
        }else{
            Toast.makeText(this,"Usuário ou Senha inválido.",Toast.LENGTH_SHORT).show();
        }
    }

    public void botaoCriarNovaConta(View v){
        startActivity(new Intent(this, CadastroUsuarioActivity.class));
    }
}
