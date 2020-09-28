package com.univali.transex.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.univali.transex.R;
import com.univali.transex.classes.Sessao;
import com.univali.transex.database.TranspEXDAO;
import com.univali.transex.database.tables.Entrega;
import com.univali.transex.database.tables.Usuario;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import io.realm.Realm;

public class AcompanhamentoEntregaActivity extends AppCompatActivity implements OnMapReadyCallback {
    private Entrega entrega;
    private TranspEXDAO db = TranspEXDAO.getInstance();
    private MapView mapaDoGoogle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acompanhamento_entrega);
        getSupportActionBar().hide();
        mapaDoGoogle = findViewById(R.id.mapView);
        mapaDoGoogle.onCreate(savedInstanceState);
        mapaDoGoogle.getMapAsync(this);

        int idEntrega = getIntent().getIntExtra("idEntrega",0);

        if(idEntrega > 0){
            entrega = db.getIdEntrega(idEntrega);
        }else{
            //setResult();
            finish();
        }

        try {
            prepararCabecalho();
            prepararDadosEntregador();
            prepararLocalizacao();
            prepararRodape();
        }catch (Exception e){
            finish();
        }
    }

    private void prepararCabecalho(){
        String status;
        TextView tvStatus = findViewById(R.id.tvStatusAEA);
        switch(entrega.getStatus()){
            case 1: status = "Em Rota para a Origem"; break;
            case 2: status = "Em Rota para o Destino"; break;
            case 3:
                status = "Entrega finalizada em " +
                        SimpleDateFormat.getInstance().format(entrega.getDataEntrega());
                break;
            default: status = "Não identificado no momento"; break;
        }
        tvStatus.setText(status);
    }

    private void prepararDadosEntregador(){
        Usuario usuario = db.getIdUsuario(entrega.getCodigoEntregador());
        TextView tvNome = findViewById(R.id.tvNomeAEC);
        TextView tvTelefone = findViewById(R.id.tvTelefoneAEC);

        if(usuario != null){
            tvNome.setText(usuario.getNome());
            tvTelefone.setText(usuario.getTelefone());
        }
    }

    private void prepararLocalizacao(){
        TextView tvEndereco = findViewById(R.id.tvEnderecoAEC);

        tvEndereco.setText("Destino: " + entrega.getEnderecoDestino()+","
        +entrega.getBairroDestino()+","
        +entrega.getCidadeDestino()+","
        +entrega.getUfDestino());
    }

    private void prepararRodape(){
        Button btConfirm = findViewById(R.id.btConfirmarAEC);

        //oculta se nao for entregador ou estiver finalizada
        if(!Sessao.getInstance().getUsuario().isEntregaEncomendas() || entrega.getStatus() == 3) {
            btConfirm.setVisibility(View.INVISIBLE);
        }

        if(entrega.getStatus() == 1){
            btConfirm.setText("Confirmar retirada origem");
        }
    }

    public void botaoAtualizarStatus(View v){
        Button btConfirm = findViewById(R.id.btConfirmarAEC);
        Realm db2 = Realm.getDefaultInstance();
        TranspEXDAO db = TranspEXDAO.getInstance();

        db2.beginTransaction();
        String status = "";
        if(entrega.getStatus() == 1) {
            entrega.setStatus(2);
            status = "Em Rota para o Destino";
            btConfirm.setText("Confirmar entrega");

        }else{
            entrega.setStatus(3);
            entrega.setDataEntrega(Calendar.getInstance().getTime());
            status = "Entrega finalizada em " +
                    SimpleDateFormat.getInstance().format(entrega.getDataEntrega());
            btConfirm.setVisibility(View.INVISIBLE);
        }

        //Atualiza em tela
        TextView tvStatus = findViewById(R.id.tvStatusAEA);
        tvStatus.setText(status);

        //Atualiza no banco de dados
        db.update(entrega);
        db2.commitTransaction();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        getCoordsAtual();

        try {

            if (Sessao.getInstance().latOrigem == 0 || Sessao.getInstance().lngOrigem == 0) {
                return;
            }
            LatLngBounds.Builder limiteLatLong = new LatLngBounds.Builder();

            MarkerOptions marcador = new MarkerOptions();
            LatLng posicaoDoMarcador = new LatLng(Sessao.getInstance().latOrigem, Sessao.getInstance().lngOrigem);
            marcador.position(posicaoDoMarcador);
            marcador.title("Entregador");
            googleMap.addMarker(marcador);
            limiteLatLong.include(posicaoDoMarcador);
        }catch (Exception e){

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
                        Sessao.getInstance().latOrigem = location.getLatitude();
                        Sessao.getInstance().lngOrigem = location.getLongitude();
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
}
