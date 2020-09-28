package com.univali.transex.database;

import com.univali.transex.classes.Sessao;
import com.univali.transex.database.tables.Entrega;
import com.univali.transex.database.tables.Usuario;
import java.util.ArrayList;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class TranspEXDAO{
    private Realm db;
    private static TranspEXDAO Instance;

    private TranspEXDAO(){
        db = Realm.getDefaultInstance();
    }

    public static synchronized TranspEXDAO getInstance() {
        if (Instance == null)
            Instance = new TranspEXDAO();

        return Instance;
    }

    //-----------------------------------------------------------------
    //------------------------------GENÉRICOS--------------------------
    //-----------------------------------------------------------------
    //Inseri registros
    public void insert(RealmObject obj){
        db.beginTransaction();
        db.insert(obj);
        db.commitTransaction();
    }

    //Efetua a leitura de todos os itens
    public ArrayList<RealmObject> read(Class obj){
        ArrayList<RealmObject> lista = new ArrayList<>();
        RealmResults results = db.where(obj).findAll();
        lista.addAll(results);
        return lista;
    }

    //Altera registros
    public void update(RealmObject obj){
       // db.beginTransaction();
        db.insertOrUpdate(obj);
        //db.commitTransaction();
    }

    //Resgata a próxima sequencia de id de uma tabela
    public int identify(Class classe) {
        if (db.where(classe).max("id") == null){
            return 1;
        }

        int id = db.where(classe).max("id").intValue()+1;

        //enquanto exisitir a sequencia, incrementa o id
        while (db.where(classe).equalTo("id",id).findFirst() != null){
            id++;
        }

        return id;
    }

    //-----------------------------------------------------------------
    //------------------------------USUÁRIO----------------------------
    //-----------------------------------------------------------------
    public Usuario getIdUsuario(int id){
        return db.where(Usuario.class).equalTo("id",id).findFirst();
    }

    public Usuario getUsuario(String email, String senha){
        return db.where(Usuario.class).equalTo("email",email).and().equalTo("senha",senha).findFirst();
    }

    //Retorna o código do usuario disponivel para entrega
    public int getIdEntregadorDisponivel(){
        ArrayList<RealmObject> usuarios =  this.read(Usuario.class);
        int id = 0;
        //Percorre todos os usuários verificando na tabela de entrega
        //se ele existe
        for (RealmObject u: usuarios) {
            Usuario user = (Usuario) u;

            Entrega e = db.where(Entrega.class).equalTo("codigoEntregador",user.getId()).findFirst();

            //Se não encontrar entrega, significa que ele não está vinculado
            //a nenhuma entrega
            if(e == null){
                if (user.getId() != Sessao.getInstance().getUsuario().getId() &&
                user.isEntregaEncomendas()) {
                    return user.getId();
                }
            }
        }

        return id;
    }

    //-----------------------------------------------------------------
    //------------------------------ENTREGA----------------------------
    //-----------------------------------------------------------------
    public Entrega getIdEntrega(int id){
        return db.where(Entrega.class).equalTo("id",id).findFirst();
    }

    //Efetua a leitura de todos os itens
    public ArrayList<Entrega> readMinhasEntregas(int codUsuario){
        ArrayList<Entrega> lista = new ArrayList<>();
        RealmResults results = db.where(Entrega.class).equalTo("codigoEntregador",codUsuario).findAll();
        lista.addAll(results);
        return lista;
    }

    //Efetua a leitura de todos os itens
    public ArrayList<Entrega> readMinhasSolicitacoes(int codUsuario){
        ArrayList<Entrega> lista = new ArrayList<>();
        RealmResults results = db.where(Entrega.class).equalTo("codigoSolicitante",codUsuario).findAll();
        lista.addAll(results);
        return lista;
    }
}
