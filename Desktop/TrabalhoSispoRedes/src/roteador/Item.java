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

    public Item(String ipDestino, int metrica, String ipSaida) {
        this.ipDestino = ipDestino;
        this.metrica = metrica;
        this.ipSaida = ipSaida;
    } 

    public String getIpDestino() {
        return ipDestino;
    }

    public int getMetrica() {
        return metrica;
    }

    public String getIpSaida() {
        return ipSaida;
    }
    
}

