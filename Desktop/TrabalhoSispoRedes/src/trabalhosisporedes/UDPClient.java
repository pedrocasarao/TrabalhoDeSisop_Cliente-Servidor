/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabalhosisporedes;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 *
 * @author admin
 */
public class UDPClient {
     public static void main(String args[]) throws Exception
   {
      // cria o stream do teclado
      BufferedReader inFromUser =
         new BufferedReader(new InputStreamReader
                     (System.in));

      // declara socket cliente
      DatagramSocket clientSocket = new DatagramSocket();
      
      // obtem endereço IP do servidor com o DNS
      InetAddress IPAddress = 
                      InetAddress.getByName("localhost");
      
      byte[] sendData = new byte[1024];
      byte[] receiveData = new byte[1024];

      // lê uma linha do teclado
      String sentence = inFromUser.readLine();
      sendData = sentence.getBytes();
      
      // cria pacote com o dado, o endere�o do server e porta do servidor
      DatagramPacket sendPacket =
         new DatagramPacket(sendData, sendData.length, IPAddress, 9876);

      //envia o pacote
      clientSocket.send(sendPacket);
      
      // fecha o cliente
      clientSocket.close();
   }

}
