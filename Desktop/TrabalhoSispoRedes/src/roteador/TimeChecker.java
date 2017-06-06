/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roteador;

import java.util.concurrent.Semaphore;

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
            if(!tabela.getTabela().isEmpty()){            
                if (tabela.getTabela().removeIf(i -> i.getLastUpdate() < (System.currentTimeMillis()-15000))) {
                    mutexAlteracao.release();
                    System.out.println("APAGOOOO!!!!");
                }
            }
        }
    }
}
