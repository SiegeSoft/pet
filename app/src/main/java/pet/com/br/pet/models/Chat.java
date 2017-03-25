package pet.com.br.pet.models;

/**
 * Created by iaco_ on 27/08/2016.
 */
public class Chat {
    private String username;
    private String usernameDestino;
    private String codigo;
    private String descricao;
    private String profileimg;
    private String destinovisualisado;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsernameDestino() {
        return usernameDestino;
    }

    public void setUsernameDestino(String usernameDestino) {
        this.usernameDestino = usernameDestino;
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

    public String getProfileImg() {
        return profileimg;
    }

    public void setProfileImg(String profileimg) {
        this.profileimg = profileimg;
    }

    public String getDestinoVisualisado() {
        return destinovisualisado;
    }

    public void setDestinoVisualisado(String destinovisualisado) {
        this.destinovisualisado = destinovisualisado;
    }

}