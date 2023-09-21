package org.example;

import java.io.*;
import java.net.*;

public class ClienteSocket {

    public static void main(String[] args) {
        // Utiliza try-with-resources para garantir o fechamento dos recursos
        try (
            Socket socket = new Socket("localhost", 12345);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            System.out.println("Conexão estabelecida com o servidor.");

            // Construir uma mensagem ISO 8583
            String mensagemISO8583 = construirMensagemISO8583();

            // Enviar a mensagem para o servidor
            out.println(mensagemISO8583);
            System.out.println("Mensagem enviada para o servidor: " + mensagemISO8583);

            // Receber a resposta do servidor
            String respostaISO8583 = in.readLine();
            System.out.println("Resposta recebida do servidor: " + respostaISO8583);

            System.out.println("Conexão encerrada.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String construirMensagemISO8583() {
        Transacao transacao = new Transacao(20.10, "0512", "104446", "040104", "4012310218451", "", "");
        return transacao.toISO8583();
    }
    
}
