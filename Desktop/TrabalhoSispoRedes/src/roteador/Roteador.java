package roteador;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Roteador {

    public static void main(String[] args) throws IOException {
        /* Lista de endereço IPs dos vizinhos */
        ArrayList<String> ip_list = new ArrayList<>();
        Semaphore mutex = new Semaphore(0);
        Semaphore mutexSyncReceiver = new Semaphore(0);
        Semaphore mutexSyncSender = new Semaphore(1);
         

        /* Le arquivo de entrada com lista de IPs dos roteadores vizinhos. */
        try ( BufferedReader inputFile = new BufferedReader(new FileReader("IPVizinhos.txt"))) {
            String ip;
            
            while( (ip = inputFile.readLine()) != null){
                ip_list.add(ip);
            }
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Roteador.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        
        /* Cria instâncias da tabela de roteamento e das threads de envio e recebimento de mensagens. */
        TabelaRoteamento tabela = new TabelaRoteamento(mutex);
        Thread receiver = new Thread(new MessageReceiver(tabela,mutexSyncReceiver,mutexSyncSender));
        Thread sender = new Thread(new MessageSender(tabela, ip_list,mutex, mutexSyncReceiver,mutexSyncSender));
        Thread timeChecker = new Thread(new TimeChecker(tabela, mutex));
                
        receiver.start();
        sender.start();
        timeChecker.start();
        
    }    
}
