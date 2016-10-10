package pet.com.br.pet.models;

/**
 * Created by iaco_ on 29/08/2016.
 */
public class ChatView {

    private String id;
    private String codigo;
    private String username;
    private String mensagem;
    private String dia;
    private String mes;
    private String ano;
    private static long adaptercontador;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {this.mes = mes;}

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {this.ano = ano;}

    public static long getAdaptercontador() {
        return adaptercontador;
    }

    public static void setAdaptercontador(long adaptercontador) {
        ChatView.adaptercontador = adaptercontador;}

}
