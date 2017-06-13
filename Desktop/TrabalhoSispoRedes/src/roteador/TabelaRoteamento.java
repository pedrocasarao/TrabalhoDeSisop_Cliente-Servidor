package roteador;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TabelaRoteamento {

    /*Implemente uma estrutura de dados para manter a tabela de roteamento. 
     * A tabela deve possuir: IP Destino, Métrica e IP de Saída.
     */
    List<Item> tabela;
    Semaphore mutex;
    Semaphore mutexSync;
    
    public TabelaRoteamento(Semaphore mutex,Semaphore mutexSync) {
        tabela = new ArrayList<Item>();
        this.mutex = mutex;
        this.mutexSync = mutexSync;
    }

    public void update_tabela(String tabela_s, InetAddress IPAddress) {
        try {
            /* Atualize a tabela de rotamento a partir da string recebida. */
            mutexSync.acquire();
        } catch (InterruptedException ex) {
            Logger.getLogger(TabelaRoteamento.class.getName()).log(Level.SEVERE, null, ex);
        }
        boolean alterouTabela = false;
        System.out.println(tabela_s.trim());
        if (tabela_s.trim().equals("!")) {
            boolean jaExiste = false;
            System.out.println("Entrei !");
            for (Item itemTabela : tabela) {
                if (itemTabela.getIpDestino().equals(IPAddress.toString().substring(1))) {
                    jaExiste = true;
                    if (itemTabela.getMetrica() > 1) {
                        itemTabela.setMetrica(1);
                        itemTabela.setIpSaida(IPAddress.toString().substring(1));
                        alterouTabela =true;
                    }
                    
                    if (itemTabela.getIpSaida().equals(IPAddress.toString().substring(1))) {
                        itemTabela.setLastUpdate();
                        System.out.println("Dei Update no IP "+itemTabela.getIpSaida());
                    }
                    //System.out.println("UPDATED" + System.lineSeparator()
                    //        + "IP:" + itemTabela.getIpDestino());
                }
            }
            if (!jaExiste) {
                //Adiciona o ip da maquina que não enviou tabela com metrica 1
                tabela.add(new Item(IPAddress.toString().substring(1), 1, IPAddress.toString().substring(1), System.currentTimeMillis()));
                System.out.println("ADDED" + System.lineSeparator()
                        + "IP:" + IPAddress.toString().substring(1) + System.lineSeparator()
                        + "Metrica:" + 1 + System.lineSeparator()
                        + "IpSaida:" + IPAddress.toString().substring(1));
                alterouTabela = true;
            }
        } else {
            System.out.println("Entrei no else / " + tabela_s);
            tabela_s = tabela_s.substring(1).trim();
            String[] itensTabela_s = tabela_s.split("\\*");
            String ip;
            int metrica;
            boolean jaExiste = false;
            for (String itemTabela_s : itensTabela_s) {
                String[] bits = itemTabela_s.split(";");
                ip = bits[0];
                metrica = Integer.parseInt(bits[1]);
                for (Item itemTabela : tabela) {
                    if (itemTabela.getIpDestino().equals(ip)) {
                        jaExiste = true;
                        if (itemTabela.getMetrica() > metrica + 1) {
                            itemTabela.setMetrica(metrica + 1);
                            itemTabela.setIpSaida(IPAddress.toString().substring(1));
                            alterouTabela = true;
                        }
                        if (itemTabela.getIpSaida().equals(IPAddress.toString().substring(1))) {
                            itemTabela.setLastUpdate();
                        }
                        //System.out.println("UPDATED" + System.lineSeparator()
                        //        + "IP:" + itemTabela.getIpDestino());
                    }
                }
                if (!jaExiste) {
                    //Adiciona um ip enviado que ainda não está na tabela com metrica enviada +1 e saida do ip que enviou
                    tabela.add(new Item(ip, (metrica + 1), IPAddress.toString().substring(1), System.currentTimeMillis()));
                    System.out.println("ADDED" + System.lineSeparator()
                            + "IP:" + ip + System.lineSeparator()
                            + "Metrica:" + metrica + System.lineSeparator()
                            + "IpSaida:" + IPAddress.toString().substring(1));
                    alterouTabela = true;
                }
            }
            boolean jaExiste2 = false;
            for (Item itemTabela : tabela) {
                if (itemTabela.getIpDestino().equals(IPAddress.toString().substring(1))) {
                    jaExiste2 = true;
                    if (itemTabela.getMetrica() > 1) {
                        itemTabela.setMetrica(1);
                        itemTabela.setIpSaida(IPAddress.toString().substring(1));
                        alterouTabela = true;
                    }
                    if (itemTabela.getIpSaida().equals(IPAddress.toString().substring(1))) {
                        itemTabela.setLastUpdate();
                    }
                }
            }
            if (!jaExiste2) {
                //Adiciona um ip que enviou tabela diretamennte porem ele próprio não estava cadastrado.
                tabela.add(new Item(IPAddress.toString().substring(1), 1, IPAddress.toString().substring(1), System.currentTimeMillis()));
                System.out.println("ADDED" + System.lineSeparator()
                        + "IP:" + IPAddress.toString().substring(1) + System.lineSeparator()
                        + "Metrica:" + 1 + System.lineSeparator()
                        + "IpSaida:" + IPAddress.toString().substring(1));
                alterouTabela = true;
            }
        }
        if(alterouTabela){
            mutex.release();
        }
        mutexSync.release();
    }

    public String get_tabela_string(String ip) {
        try {
            mutexSync.acquire();
        } catch (InterruptedException ex) {
            Logger.getLogger(TabelaRoteamento.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        String tabela_string = "!";
        /* Tabela de roteamento vazia conforme especificado no protocolo */
        StringBuilder auxString = new StringBuilder();
        if (tabela.isEmpty()) {            
            mutexSync.release();
            return "!";
        }else{
            for (Item item : tabela) {
                auxString = new StringBuilder();
                if (!ip.equals(item.getIpSaida())) {
                    String aux = "*" + item.getIpDestino() + ";" + item.getMetrica();
                    if (!ip.equals(item.getIpDestino())) {
                        auxString.append(aux);
                    }
                }
            }
            if (auxString.length() > 0) {
                tabela_string = auxString.toString();
                System.out.println(tabela_string);
            }
            mutexSync.release();
            /* Converta a tabela de rotamento para string, conforme formato definido no protocolo . */
            return tabela_string;
        }                
    }

    public List<Item> getTabela() {
        
        return tabela;
    }
}
