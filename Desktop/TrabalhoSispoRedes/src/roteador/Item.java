/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roteador;

/**
 *
 * @author JORGE
 */
public class Item {
    private String ipDestino;
    private int metrica;
    private String ipSaida;
    private long lastUpdate;

    public Item(String ipDestino, int metrica, String ipSaida, long lastUpdate) {
        this.ipDestino = ipDestino;
        this.metrica = metrica;
        this.ipSaida = ipSaida;
        this.lastUpdate = lastUpdate;
    } 

    public String getIpDestino() {
        return ipDestino;
    }

    public int getMetrica() {
        return metrica;
    }
    
    public void setMetrica(int m) {
        metrica = m;
    }

    public String getIpSaida() {
        return ipSaida;
    }
    
    public void setIpSaida(String ip){
        ipSaida = ip;
    }
    
    public long getLastUpdate() {
        return lastUpdate;
    }
    
    public void setLastUpdate(){
        lastUpdate = System.currentTimeMillis();
    }
}

