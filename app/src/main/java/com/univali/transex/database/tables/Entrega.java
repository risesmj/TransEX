package com.univali.transex.database.tables;

import java.util.Date;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Entrega extends RealmObject {
    @PrimaryKey
    private int id;
    private String descricao;
    private int status;
    private Date dataInclusao;
    private String cepDestino;
    private String cidadeDestino;
    private String ufDestino;
    private String enderecoDestino;
    private String bairroDestino;
    private Date dataEntrega;
    private int codigoEntregador;
    private int codigoSolicitante;
    private double latOrigem;
    private double lngOrigem;
    private double latDestino;
    private double lngDestino;
    private String placeIdDestino;
    private String fotoObjeto;
    private double volumePeso;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setDataInclusao(Date dataInclusao) {
        this.dataInclusao = dataInclusao;
    }

    public void setDataEntrega(Date dataEntrega) {
        this.dataEntrega = dataEntrega;
    }

    public int getCodigoEntregador() {
        return codigoEntregador;
    }

    public void setCodigoEntregador(int codigoEntregador) {
        this.codigoEntregador = codigoEntregador;
    }

    public int getCodigoSolicitante() {
        return codigoSolicitante;
    }

    public void setCodigoSolicitante(int codigoSolicitante) {
        this.codigoSolicitante = codigoSolicitante;
    }

    public String getFotoObjeto() {
        return fotoObjeto;
    }

    public void setFotoObjeto(String fotoObjeto) {
        this.fotoObjeto = fotoObjeto;
    }

    public String getCidadeDestino() {
        return cidadeDestino;
    }

    public void setCidadeDestino(String cidadeDestino) {
        this.cidadeDestino = cidadeDestino;
    }

    public String getUfDestino() {
        return ufDestino;
    }

    public void setUfDestino(String ufDestino) {
        this.ufDestino = ufDestino;
    }

    public double getVolumePeso() {
        return volumePeso;
    }

    public void setVolumePeso(double volumePeso) {
        this.volumePeso = volumePeso;
    }

    public double getLatOrigem() {
        return latOrigem;
    }

    public void setLatOrigem(double latOrigem) {
        this.latOrigem = latOrigem;
    }

    public double getLngOrigem() {
        return lngOrigem;
    }

    public void setLngOrigem(double lngOrigem) {
        this.lngOrigem = lngOrigem;
    }

    public void setLatDestino(double latDestino) {
        this.latDestino = latDestino;
    }

    public void setLngDestino(double lngDestino) {
        this.lngDestino = lngDestino;
    }

    public String getEnderecoDestino() {
        return enderecoDestino;
    }

    public void setEnderecoDestino(String enderecoDestino) {
        this.enderecoDestino = enderecoDestino;
    }

    public void setPlaceIdDestino(String placeIdDestino) {
        this.placeIdDestino = placeIdDestino;
    }

    public void setCepDestino(String cepDestino) {
        this.cepDestino = cepDestino;
    }

    public String getBairroDestino() {
        return bairroDestino;
    }

    public void setBairroDestino(String bairroDestino) {
        this.bairroDestino = bairroDestino;
    }

    public Date getDataEntrega() {
        return dataEntrega;
    }
}
