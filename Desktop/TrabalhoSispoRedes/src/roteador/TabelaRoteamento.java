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

    public TabelaRoteamento(Semaphore mutex) {
        tabela = new ArrayList<Item>();
        this.mutex = mutex;
    }

    public void update_tabela(String tabela_s, InetAddress IPAddress) {
        /* Atualize a tabela de rotamento a partir da string recebida. */
        if (tabela_s.trim().equals("!")) {
            System.out.println("String vazia");
            boolean jaExiste = false;
            for (Item itemTabela : tabela) {
                if (itemTabela.getIpDestino().equals(IPAddress.toString().substring(1))) {
                    jaExiste = true;
                    System.out.println(itemTabela.getLastUpdate());
                    System.out.println("De um ip já conhecido");
                }
            }
            if (!jaExiste) {
                tabela.add(new Item(IPAddress.toString().substring(1), 1, IPAddress.toString().substring(1), System.currentTimeMillis()));
                System.out.println("Add");
                mutex.release();
            }
        } else {
            System.out.println("String populada");
            tabela_s = tabela_s.substring(1);
            String[] itensTabela_s = tabela_s.split("\\*");
            String ip;
            String metrica;
            boolean jaExiste = false;
            for (String itemTabela_s : itensTabela_s) {
                String[] bits = itemTabela_s.split(";");
                ip = bits[0];
                System.out.println(ip);
                metrica = bits[1];
                for (Item itemTabela : tabela) {
                    if (itemTabela.getIpDestino().equals(ip)) {
                        jaExiste = true;
                        itemTabela.setLastUpdate();
                    }
                }
                if (!jaExiste) {
                    tabela.add(new Item(ip, (Integer.parseInt(metrica.trim()) + 1), IPAddress.toString().substring(1), System.currentTimeMillis()));
                    mutex.release();
                }
            }
            boolean jaExiste2 = false;
            for (Item itemTabela : tabela) {
                if (itemTabela.getIpDestino().equals(IPAddress.toString().substring(1))) {
                    jaExiste2 = true;
                    //itemTabela.setLastUpdate();
                }
            }
            if (!jaExiste2) {
                tabela.add(new Item(IPAddress.toString().substring(1), 1, IPAddress.toString().substring(1), System.currentTimeMillis()));
                mutex.release();
            }
        }
    }

    public String get_tabela_string(String ip) {
        String tabela_string = "!";
        /* Tabela de roteamento vazia conforme especificado no protocolo */
        StringBuilder auxString = new StringBuilder();
        if (!tabela.isEmpty()) {
            for (Item item : tabela) {
                if (!ip.equals(item.getIpSaida())) {
                    auxString.append("*" + item.getIpDestino() + ";" + item.getMetrica());
                }else{
                    System.out.println("ele jja tem isso seu besta");
                }
            }
            if(auxString.length()>0){
               tabela_string = auxString.toString(); 
            }
        }
        /* Converta a tabela de rotamento para string, conforme formato definido no protocolo . */
        return tabela_string;
    }
    public List<Item> getTabela(){
        return tabela;
    }
}
