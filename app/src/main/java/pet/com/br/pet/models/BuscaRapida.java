package pet.com.br.pet.models;

import android.graphics.Bitmap;

/**
 * Created by rafae on 24/08/2016.
 */
public class BuscaRapida {


    private String latitudeRandom;
    private String longitudeRandom;
    private String idRandom;
    private String codigo;

    private Bitmap imgid;

    public Bitmap getImgid() {
        return imgid;
    }

    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getCodigo() { return codigo;}

    public void setImgid(Bitmap imgid) {
        this.imgid = imgid;
    }

    public String getLatitudeRandom() {
        return latitudeRandom;
    }

    public void setLatitudeRandom(String latitudeRandom) {
        this.latitudeRandom = latitudeRandom;
    }

    public String getLongitudeRandom() {
        return longitudeRandom;
    }

    public void setLongitudeRandom(String longitudeRandom) {
        this.longitudeRandom = longitudeRandom;
    }

    public String getIdRandom() {
        return idRandom;
    }

    public void setIdRandom(String idRandom) {
        this.idRandom = idRandom;
    }
}
