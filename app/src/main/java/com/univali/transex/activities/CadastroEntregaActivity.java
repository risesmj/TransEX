package com.univali.transex.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.univali.transex.R;
import com.univali.transex.classes.RegraNegocio;
import com.univali.transex.classes.Sessao;
import com.univali.transex.database.tables.Entrega;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class CadastroEntregaActivity extends AppCompatActivity {
    private EditText etDescricao;
    private EditText etCep;
    private EditText etEndereco;
    private EditText etBairro;
    private EditText etCidade;
    private EditText etUf;
    private EditText etVolPes;
    private Entrega entrega = new Entrega();
    private String caminhoDaFoto = null;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_entrega);
        getSupportActionBar().setTitle("Nova Solicitação de Entrega");

        getCoordsAtual();

        etDescricao = findViewById(R.id.etDescricaoCEA);
        etCep = findViewById(R.id.etCepCEA);
        etEndereco = findViewById(R.id.etEnderecoCEA);
        etBairro = findViewById(R.id.etBairroCEA);
        etCidade = findViewById(R.id.etCidadeCEA);
        etUf = findViewById(R.id.etUFCEA);
        etVolPes = findViewById(R.id.etVolPesCEA);

        etCep.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String cep = editable.toString();

                try {
                    //Prepara o URL do GET
                    String url = "https://viacep.com.br/ws/"+cep+"/json/";

                    RequestQueue queue = Volley.newRequestQueue(CadastroEntregaActivity.this);

                    JsonObjectRequest jsObjRequest =
                            new JsonObjectRequest(
                                    Request.Method.GET, // Requisição via HTTP_GET
                                    url,   // url da requisição
                                    null,  // JSONObject a ser enviado via POST
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            try {
                                                CadastroEntregaActivity.this.etCidade.setText(response.getString("localidade"));
                                                CadastroEntregaActivity.this.etUf.setText(response.getString("uf"));
                                                CadastroEntregaActivity.this.etEndereco.setText(response.getString("logradouro"));
                                                CadastroEntregaActivity.this.etBairro.setText(response.getString("bairro"));
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Sessao.getInstance().placeIdDestino = "";
                                            Sessao.getInstance().latDestino = "";
                                            Sessao.getInstance().lonDestino = "";
                                        }
                                    }); // Response.ErrorListener

                    queue.add(jsObjRequest);
                }catch(Exception e){
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void solicitarEntrega(View v){
        entrega.setDescricao(etDescricao.getText().toString());
        entrega.setCepDestino(etCep.getText().toString());
        entrega.setCidadeDestino(etCidade.getText().toString());
        entrega.setUfDestino(etUf.getText().toString());
        entrega.setBairroDestino(etBairro.getText().toString());
        entrega.setEnderecoDestino(etEndereco.getText().toString());
        entrega.setVolumePeso(Double.valueOf(etVolPes.getText().toString()));

        if(entrega.getFotoObjeto() == null){
            entrega.setFotoObjeto("");
        }

        RegraNegocio rg = new RegraNegocio(this);
        if(rg.prepararSolicitacaoEntrega(entrega)){
            Toast.makeText(this,"Nova solicitação de entrega efetuada com sucesso.",Toast.LENGTH_SHORT).show();
            setResult(200);
            finish();
        }
    }

    private File criarArquivoParaSalvarFoto() throws IOException {
        String nomeFoto = UUID.randomUUID().toString();
        //getExternalStoragePublicDirectory()
        //    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
        File diretorio = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File fotografia = File.createTempFile(nomeFoto, ".jpg", diretorio);
        caminhoDaFoto = fotografia.getAbsolutePath();
        return fotografia;
    }


    public void tirarFoto(View v) {
        Intent intecaoAbrirCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File arquivoDaFoto = null;
        try {
            arquivoDaFoto = criarArquivoParaSalvarFoto();
        } catch (IOException ex) {
            Toast.makeText(this, "Não foi possível criar arquivo para foto", Toast.LENGTH_LONG).show();
        }
        if (arquivoDaFoto != null) {
            Uri fotoURI = FileProvider.getUriForFile(this,
                    "com.univali.transex.fileprovider",
                    arquivoDaFoto);
            intecaoAbrirCamera.putExtra(MediaStore.EXTRA_OUTPUT, fotoURI);
            startActivityForResult(intecaoAbrirCamera, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                entrega.setFotoObjeto(caminhoDaFoto);
                atualizaFoto();
            }
        }
    }

    private void atualizaFoto() {
        if (entrega.getFotoObjeto() != null) {
            ImageView ivFotografia = findViewById(R.id.ivFotoCEA);
            ivFotografia.setImageURI(Uri.parse(entrega.getFotoObjeto()));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getCoordsAtual() {
        LocationManager gerenciadorLocalizacao = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED 
                && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        gerenciadorLocalizacao.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                0,
                0,
                new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        Sessao sessao = Sessao.getInstance();
                        sessao.latOrigem = location.getLatitude();
                        sessao.lngOrigem = location.getLongitude();
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override

                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                });

        return;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 1){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "GANHOU permissão", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this, "Não GANHOU permissão", Toast.LENGTH_LONG).show();
            }
        }

    }
}
