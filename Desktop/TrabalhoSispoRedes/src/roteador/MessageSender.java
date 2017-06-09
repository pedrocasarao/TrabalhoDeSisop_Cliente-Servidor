package roteador;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MessageSender implements Runnable {

    TabelaRoteamento tabela;
    /*Tabela de roteamento */
    ArrayList<String> vizinhos;
    /* Lista de IPs dos roteadores vizinhos */
    Semaphore mutex;
    Long time = new Long(1);
    Semaphore mutexSyncReceiver = new Semaphore(1);
    Semaphore mutexSyncSender = new Semaphore(1);

    public MessageSender(TabelaRoteamento t, ArrayList<String> v, Semaphore mutex, Semaphore mutexSyncReceiver, Semaphore mutexSyncSender) {
        tabela = t;
        vizinhos = v;
        this.mutex = mutex;
        this.mutexSyncReceiver = mutexSyncReceiver;
        this.mutexSyncSender = mutexSyncSender;
    }

    @Override
    public void run() {
        DatagramSocket clientSocket = null;
        byte[] sendData;
        InetAddress IPAddress = null;

        /* Cria socket para envio de mensagem */
        try {
            clientSocket = new DatagramSocket();
        } catch (SocketException ex) {
            Logger.getLogger(MessageSender.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }

        while (true) {
            if (mutex.tryAcquire() || System.currentTimeMillis() >= time) {
                try {
                    mutexSyncSender.acquire();
                } catch (InterruptedException ex) {
                    Logger.getLogger(MessageSender.class.getName()).log(Level.SEVERE, null, ex);
                }
                /* Anuncia a tabela de roteamento para cada um dos vizinhos */
                for (String ip : vizinhos) {
                    /* Converte string com o IP do vizinho para formato InetAddress */
 /* Pega a tabela de roteamento no formato string, conforme especificado pelo protocolo. */
                    String tabela_string = tabela.get_tabela_string(ip);

                    /* Converte string para array de bytes para envio pelo socket. */
                    sendData = tabela_string.getBytes();

                    try {
                        IPAddress = InetAddress.getByName(ip);
                    } catch (UnknownHostException ex) {
                        Logger.getLogger(MessageSender.class.getName()).log(Level.SEVERE, null, ex);
                        continue;
                    }

                    /* Configura pacote para envio da menssagem para o roteador vizinho na porta 5000*/
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 5000);

                    /* Realiza envio da mensagem. */
                    try {
                        clientSocket.send(sendPacket);
                        System.out.println("String: '" + tabela_string + "' sent to Ip: " + ip);
                        
                    } catch (IOException ex) {
                        Logger.getLogger(MessageSender.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                mutexSyncReceiver.release();

                /* Espera 10 segundos antes de realizar o próximo envio. CONTUDO, caso
             * a tabela de roteamento sofra uma alteração, ela deve ser reenvida aos
             * vizinho imediatamente.
                 */
                time = System.currentTimeMillis() + 10000;
            }

        }

    }

}
