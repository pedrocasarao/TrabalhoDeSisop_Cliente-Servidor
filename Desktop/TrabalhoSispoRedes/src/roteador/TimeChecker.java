/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roteador;

import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author JORGE
 */
public class TimeChecker implements Runnable {

    private TabelaRoteamento tabela;
    private Semaphore mutexAlteracao;

    public TimeChecker(TabelaRoteamento tabela, Semaphore mutexAlteracao) {
        this.tabela = tabela;
        this.mutexAlteracao = mutexAlteracao;
    }

    @Override
    public void run() {
        while (true) {
            
            if (!tabela.getTabela().isEmpty()) {
                //Item item = null;
                int index = -1;
                for (Item itemTabela : tabela.getTabela()) {
                    if ((System.currentTimeMillis() - itemTabela.getLastUpdate()) > 10000) {
                        index = tabela.getTabela().indexOf(itemTabela);
                    }
                    System.out.println(itemTabela.getIpDestino() + "/" + itemTabela.getMetrica() + "/" + itemTabela.getIpSaida() + "/" + itemTabela.getLastUpdate());
                }
                if (index != -1) {
                    if (tabela.getTabela().remove(index) != null) {
                        mutexAlteracao.release();
                        System.out.println("APAGOOOO!!!!");
                    }
                }

                /*i0f (tabela.getTabela().removeIf(i -> i.getLastUpdate() < (System.currentTimeMillis()))) {
                    mutexAlteracao.release();
                    System.out.println("APAGOOOO!!!!");
                }*/
            }
            try {
                Thread.sleep(2500);
            } catch (InterruptedException ex) {
                Logger.getLogger(TimeChecker.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("garbage cleaner passed");
        }
        
    }
}
