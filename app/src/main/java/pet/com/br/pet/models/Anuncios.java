package pet.com.br.pet.models;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by rafae on 20/08/2016.
 */
public class Anuncios {

    private String raca;
    private String dono;
    private String idade;
    private String tipoVenda;
    private String hora;
    private Bitmap imgid;
    private String codigo;
    private String descricao;
    private String categoria;
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Bitmap getImgid() {
        return imgid;
    }

    public void setImgid(Bitmap imgid) {
        this.imgid = imgid;
    }

    public String getRaca() {
        return raca;
    }

    public void setRaca(String raca) {
        this.raca = raca;
    }

    public String getDono() {
        return dono;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setDono(String dono) {
        this.dono = dono;
    }

    public String getIdade() {
        return idade;
    }

    public void setIdade(String idade) {
        this.idade = idade;
    }

    public String getTipoVenda() {
        return tipoVenda;
    }

    public void setTipoVenda(String tipoVenda) {
        this.tipoVenda = tipoVenda;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }


    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }



}
