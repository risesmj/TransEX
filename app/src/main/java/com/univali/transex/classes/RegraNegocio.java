package com.univali.transex.classes;

import android.content.Context;
import android.os.Build;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.univali.transex.database.TranspEXDAO;
import com.univali.transex.database.tables.Entrega;
import com.univali.transex.database.tables.Usuario;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Calendar;

public class RegraNegocio  extends AppCompatActivity {
    private Context contexto;
    private Entrega entrega;

    public RegraNegocio(Context context){
        this.contexto = context;
    }

    //Prepara o cadastro de entrega e inclui no banco
    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean prepararSolicitacaoEntrega(Entrega entregaDB) {
        boolean sucess = true;

        entrega = entregaDB;

        int codigoEntregador = 0;

        if (entrega.getDescricao().trim().isEmpty()) {
            Toast.makeText(contexto, "O campo Descrição não foi preenchido.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (entrega.getCidadeDestino().trim().isEmpty()) {
            Toast.makeText(contexto, "O campo Cidade não foi preenchido.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (entrega.getUfDestino().trim().isEmpty()) {
            Toast.makeText(contexto, "O campo UF não foi preenchido.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (entrega.getVolumePeso() <= 0) {
            Toast.makeText(contexto, "O campo Volume/Peso não foi preenchido.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (entrega.getFotoObjeto().trim().isEmpty()) {
            Toast.makeText(contexto, "A Foto do objeto a ser entregue é obrigatório.", Toast.LENGTH_SHORT).show();
            return false;
        }

        codigoEntregador = TranspEXDAO.getInstance().getIdEntregadorDisponivel();
        if (codigoEntregador == 0) {
            Toast.makeText(contexto, "Não há entregadores disponíveis no momento.", Toast.LENGTH_SHORT).show();
            return false;
        }


        //Dados de localização origem e destino

        try {
            //Prepara o URL do GET
            String url = "https://maps.google.com/maps/api/geocode/json?address="
                    + entrega.getEnderecoDestino().trim() + ","
                    + entrega.getBairroDestino().trim() + ","
                    + entrega.getCidadeDestino().trim() + ","
                    + entrega.getUfDestino().trim() +
                    "&components=country:BR&key=AIzaSyBj5n_oduK7SkFzb4EuXAmNIKYF94pBv90";

            RequestQueue queue = Volley.newRequestQueue(this.contexto);

            JsonObjectRequest jsObjRequest =
                    new JsonObjectRequest(
                            Request.Method.GET, // Requisição via HTTP_GET
                            url,   // url da requisição
                            null,  // JSONObject a ser enviado via POST
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {

                                    try {
                                        JSONArray resultados = response.getJSONArray("results");
                                        Sessao sessao = Sessao.getInstance();

                                        resultados.getJSONObject(0).getString("place_id");

                                        RegraNegocio.this.entrega.setLatDestino(Double.valueOf(resultados.getJSONObject(0).
                                                getJSONObject("geometry").
                                                getJSONObject("location").
                                                getString("lat")));

                                        RegraNegocio.this.entrega.setLngDestino(Double.valueOf(resultados.getJSONObject(0).
                                                getJSONObject("geometry").
                                                getJSONObject("location").
                                                getString("lng")));

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                }
                            }); // Response.ErrorListener

            queue.add(jsObjRequest);
        } catch (Exception e) {
            Toast.makeText(this.contexto, "Não foi possível obter os dados de geolocalização do destino.", Toast.LENGTH_LONG).show();
        }


        //Seta os dados restantes
        entrega.setCodigoSolicitante(Sessao.getInstance().getUsuario().getId());
        entrega.setStatus(1);
        entrega.setDataInclusao(Calendar.getInstance().getTime());
        entrega.setCodigoEntregador(codigoEntregador);
        entrega.setLatOrigem(Sessao.getInstance().latOrigem);
        entrega.setLngOrigem(Sessao.getInstance().lngOrigem);
        entrega.setPlaceIdDestino(Sessao.getInstance().placeIdDestino);
        entrega.setId(TranspEXDAO.getInstance().identify(Entrega.class));

        TranspEXDAO.getInstance().insert(entrega);

        return sucess;
    }


    //Prepara o cadastro de usuário e inclui no banco
    public boolean prepararCadastroUsuario(Usuario usuario){
        boolean sucess = true;

        if(usuario.getNome().trim().isEmpty()){
            Toast.makeText(contexto,"O campo Nome não foi preenchido.",Toast.LENGTH_SHORT).show();
            return false;
        }

        if(usuario.getEmail().trim().isEmpty()){
            Toast.makeText(contexto,"O campo E-mail não foi preenchido.",Toast.LENGTH_SHORT).show();
            return false;
        }

        if(usuario.getSenha().trim().isEmpty()){
            Toast.makeText(contexto,"O campo Senha não foi preenchido.",Toast.LENGTH_SHORT).show();
            return false;
        }

        if(usuario.getTelefone().trim().isEmpty()){
            Toast.makeText(contexto,"O campo Telefone não foi preenchido.",Toast.LENGTH_SHORT).show();
            return false;
        }

        TranspEXDAO db = TranspEXDAO.getInstance();
        usuario.setId(db.identify(Usuario.class));
        db.insert(usuario);

        return sucess;
    }


}
