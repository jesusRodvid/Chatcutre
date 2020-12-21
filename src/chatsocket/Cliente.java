/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatsocket;

/**
 *
 * @author informatica
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.ServerSocket;
import javax.swing.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Cliente {
	
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		MarcoCliente mimarco=new MarcoCliente();
		
		mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

}


class MarcoCliente extends JFrame{
	
	public MarcoCliente(){
		
		setBounds(600,300,280,350);
				
		LaminaMarcoCliente milamina=new LaminaMarcoCliente();
		
		add(milamina);
		
		setVisible(true);
		}	
	
}

class LaminaMarcoCliente extends JPanel implements Runnable{ //para que el programa este a la escucha de mensajer recibidos, se tiene que implementar un hilo
	
	private JTextField campo1,nick,ip;
	private JTextArea campochat;
	private JButton miboton;
	public LaminaMarcoCliente(){
                
                nick=new JTextField(5);
                add(nick);
		
                JLabel texto=new JLabel("Chat wapo");
		add(texto);
                
                ip=new JTextField(5);
                add(ip);
                
                campochat=new JTextArea(12,30);
		campo1=new JTextField(20);
                add(campochat);
		add(campo1);		
	
		miboton=new JButton("Enviar");
		EnviaTexto mievento=new EnviaTexto();
                miboton.addActionListener(mievento);
		add(miboton);
                
                Thread mihilo=new Thread(this); //creamos el hilo
                
                mihilo.start();
		
	}

    @Override
    public void run() { //iniciamos el hilo 
        try{
            ServerSocket servidor_cliente=new ServerSocket(9090); //creamos el serverSocket que estara a la escucha con el puerto que lo enviara
            
            Socket cliente; //canal por el que recibira el paquete de datos
            
            PaqueteEnvio paqueteRecibido;
           
            
            while(true){ // !!!!! bucle infinito para que este permanentemente a la escucha
                cliente=servidor_cliente.accept(); //aceptando todas las conexciones que le vengan del exterior
                ObjectInputStream flujo_entrada=new ObjectInputStream(cliente.getInputStream());//flujo de datos de entrada capaz de transportar objetos
                paqueteRecibido=(PaqueteEnvio) flujo_entrada.readObject(); //dentro de esta variable esta oda la informacion que ha recibido
                
                campochat.append("\n"+ paqueteRecibido.getNick()+": "+ paqueteRecibido.getMensaje());
               
            }
        }catch (Exception e){
        
        
        }
    }
	
	private class EnviaTexto implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            //construimos el Socket
            try {
                Socket misocket=new Socket ("25.37.231.70",9999); // ¿¿  10.0.2.15/24 o 127.0.0.1/8 oo 10.0.2.255
                
                PaqueteEnvio datos=new PaqueteEnvio();
                
                datos.setNick(nick.getText());
                datos.setIp(ip.getText());
                datos.setMensaje(campo1.getText());
                
                ObjectOutputStream paquete_datos=new ObjectOutputStream(misocket.getOutputStream()); //creamos un stream de salida que envia el objeto paqueteenvio
                paquete_datos.writeObject(datos); //escribimos en el stream de salida los datos (objeto) que le pasaremos
                misocket.close(); //cerramos el socket
               /* DataOutputStream flujo_salida=new DataOutputStream(misocket.getOutputStream());
                //necesita OutputStream out, mirar api java con el socket que instanciamos
                flujo_salida.writeUTF(campo1.getText());
                
                flujo_salida.close(); //cerrar siempre el DataOutputStream*/
            
            } catch (UnknownHostException ex) {
                ex.printStackTrace();
            }catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
        
        }
	
			
	
}

class PaqueteEnvio implements Serializable{

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
    
    private String nick, ip, mensaje;
    
 
}