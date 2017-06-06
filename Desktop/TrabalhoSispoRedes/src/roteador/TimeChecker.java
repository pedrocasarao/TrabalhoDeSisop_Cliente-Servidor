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
            
            if (!tabela.getTabela().isEmpty()) {
                //Item item = null;
                int index = -1;
                for (Item itemTabela : tabela.getTabela()) {
                    System.out.println(System.currentTimeMillis());
                    if ((System.currentTimeMillis() - itemTabela.getLastUpdate()) > 3000) {
                        index = tabela.getTabela().indexOf(itemTabela);
                    }
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
        }
    }
}