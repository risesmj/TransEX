package com.univali.transex.classes;

import com.univali.transex.database.tables.Usuario;

public class Sessao {
    private Usuario usuario;
    private static Sessao Instance;
    public String latDestino;
    public String lonDestino;
    public String placeIdDestino;
    public double latOrigem;
    public double lngOrigem;

    private Sessao(){
        this.placeIdDestino = "";
        this.latDestino = "";
        this.lonDestino = "";
        this.latOrigem = 0;
        this.lngOrigem = 0;
    }

    public static synchronized Sessao getInstance() {
        if (Instance == null)
            Instance = new Sessao();
        return Instance;
    }

    public Usuario getUsuario(){
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
