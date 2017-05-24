package roteador;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class TabelaRoteamento {
    /*Implemente uma estrutura de dados para manter a tabela de roteamento. 
     * A tabela deve possuir: IP Destino, Métrica e IP de Saída.
    */
    
    List<Item> tabela;
    
    public TabelaRoteamento(){
        tabela = new ArrayList<Item>();
    }
    
    
    public void update_tabela(String tabela_s,  InetAddress IPAddress){
        /* Atualize a tabela de rotamento a partir da string recebida. */
        String[] itensTabela_s = tabela_s.split("*");
        String ip;
        String metrica;
        boolean jaExiste = false;
        for(String itemTabela_s : itensTabela_s){
            ip = itemTabela_s.split(";")[0];
            metrica = itemTabela_s.split(";")[1];
            for(Item itemTabela : tabela){
                if(itemTabela.getIpDestino().equals(ip)){
                    jaExiste = true;
                }
            }
            if(!jaExiste){
                tabela.add(new Item(ip,(Integer.parseInt(metrica) + 1),IPAddress.toString()));
            }
        }
        
        System.out.println( IPAddress.getHostAddress() + ": " + tabela_s);
    
    }
    
    public String get_tabela_string(){
        String tabela_string = "!"; /* Tabela de roteamento vazia conforme especificado no protocolo */
        StringBuilder auxString = null;
        if(!tabela.isEmpty()){
            for(Item item :tabela){
                auxString.append("*" + item.getIpDestino()+ ";" + item.getMetrica());
            }            
            tabela_string = auxString.toString();
        }
        /* Converta a tabela de rotamento para string, conforme formato definido no protocolo . */
        
        return tabela_string;
    }
    

    
}