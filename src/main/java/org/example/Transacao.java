package org.example;

public class Transacao {
    private double valor;
    private String data;
    private String hora;
    private String redeTransmissora;
    private String formaPagamento;
    private String NSU;
    private String codigoResposta;

    // Construtores, getters e setters

    public Transacao(double valor, String data, String hora, String redeTransmissora, String formaPagamento, String NSU, String codigoResposta) {
        this.valor = valor;
        this.data = data;
        this.hora = hora;
        this.redeTransmissora = redeTransmissora;
        this.formaPagamento = formaPagamento;
        this.NSU = NSU;
        this.codigoResposta = codigoResposta;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getRedeTransmissora() {
        return redeTransmissora;
    }

    public void setRedeTransmissora(String redeTransmissora) {
        this.redeTransmissora = redeTransmissora;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public String getNSU() {
        return NSU;
    }

    public void setNSU(String NSU) {
        this.NSU = NSU;
    }

    public String getCodigoResposta() {
        return codigoResposta;
    }

    public void setCodigoResposta(String codigoResposta) {
        this.codigoResposta = codigoResposta;
    }

    public static Transacao fromISO8583(String isoMessage) {
        double valor = Double.parseDouble(isoMessage.substring(4, 16)) / 100;
        String data = isoMessage.substring(22, 26);
        String hora = isoMessage.substring(16, 22);
        String redeTransmissora = isoMessage.substring(26, 32);
        String formaPagamento = isoMessage.substring(32, 45); 
        String nsu = "";
        String codigoResposta = "";
        return new Transacao(valor, data, hora, redeTransmissora, formaPagamento, nsu, codigoResposta);
    }

    public String toISO8583() {
        StringBuilder isoMessage = new StringBuilder();
        isoMessage.append("0200");
        isoMessage.append(String.format("%012.0f", valor * 100));
        isoMessage.append(hora);
        isoMessage.append(data);
        isoMessage.append(redeTransmissora);
        isoMessage.append(formaPagamento);
        return isoMessage.toString();
    }
}
