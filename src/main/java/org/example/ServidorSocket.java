package org.example;

import java.io.*;
import java.net.*;
import java.util.concurrent.atomic.AtomicLong;

public class ServidorSocket {

    private static final AtomicLong nsuCounter = new AtomicLong(1);

    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(12345); // Porta do servidor
            System.out.println("Servidor aguardando conexões...");
    
            while (true) {
                Socket clientSocket = serverSocket.accept();
                Thread clientThread = new Thread(new ClientHandler(clientSocket));
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (serverSocket != null && !serverSocket.isClosed()) {
                    serverSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    

    static class ClientHandler implements Runnable {
        private Socket clientSocket;


        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                System.out.println("Conexão estabelecida com " + clientSocket.getInetAddress());

                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                String mensagemISO8583 = in.readLine();
                String respostaISO8583 = processarMensagem(mensagemISO8583);

                out.println(respostaISO8583);

                in.close();
                out.close();
                clientSocket.close();

                System.out.println("Conexão encerrada com " + clientSocket.getInetAddress());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private String processarMensagem(String mensagemISO8583) {
    Transacao transacao = Transacao.fromISO8583(mensagemISO8583);

    // Como exemplo, estou apenas verificando o valor da transação para decidir se é aprovado.
    boolean transacaoAprovada = transacao.getValor() <= 1000.00;

    StringBuilder resposta = new StringBuilder();
    resposta.append("0210"); 
    resposta.append(String.format("%012.0f", transacao.getValor() * 100)); 
    resposta.append(transacao.getHora()); 
    resposta.append(transacao.getData()); 
    resposta.append(transacao.getRedeTransmissora());

    if (transacaoAprovada) {
        resposta.append("0000"); 
        resposta.append(gerarNSU());
    } else {
        resposta.append("0500");
        resposta.append("000000000001");
    }

    return resposta.toString();
}

        private static String gerarNSU() {
            long nsu = nsuCounter.getAndIncrement();
            return String.format("%012d", nsu);
        }
    }

}
