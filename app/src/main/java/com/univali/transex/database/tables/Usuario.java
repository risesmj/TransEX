package com.univali.transex.database.tables;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Usuario extends RealmObject {
    @PrimaryKey
    private int id;
    private String nome;
    private String email;
    private boolean entregaEncomendas;
    private String senha;
    private String telefone;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEntregaEncomendas() {
        return entregaEncomendas;
    }

    public void setEntregaEncomendas(boolean entregaEncomendas) {
        this.entregaEncomendas = entregaEncomendas;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}
