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
    private String horah;
    private String horam;
    private String horas;
    private String msgInternal;
    private static long adaptercontador;
    private static String ultimaHorah;
    private static String ultimaHoram;
    private static String ultimaHoras;



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

    public String getHorah() {
        return horah;
    }

    public void setHorah(String horah) {this.horah = horah;}

    public String getHoram() {
        return horam;
    }

    public void setHoram(String horam) {this.horam = horam;}

    public String getHoras() {
        return horas;
    }

    public void setHoras(String horas) {this.horas = horas;}

    public String getMsgInternal() {return msgInternal; }

    public void setMsgInternal(String msgInternal) {this.msgInternal = msgInternal;}
    //

    public static String getUltimaHorahHorah() {
        return ultimaHorah;
    }

    public static void setUltimaHorahHorah(String ultimaHorah) {
        ChatView.ultimaHorah = ultimaHorah;}

    public static String getUltimaHoram() {
        return ultimaHoram;
    }

    public static void setUltimaHoramHoram(String ultimaHoram) {
        ChatView.ultimaHoram = ultimaHoram;}

    public static String getUltimaHorasHoras() {
        return ultimaHoras;
    }

    public static void setUltimaHorasHoras(String ultimaHoras) {
        ChatView.ultimaHoras = ultimaHoras;}



}
