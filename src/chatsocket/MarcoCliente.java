package chatsocket;

import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

class MarcoCliente extends JFrame implements Runnable {

	private static final long serialVersionUID = 1L;

	private JTextField campo1, nick, ip;
	private JTextArea campochat;
	private JButton miboton;

	private ServerSocket servidor_cliente; 

	public MarcoCliente() {
		super();

		nick = new JTextField(5);

		JLabel texto = new JLabel("Chat wapo");

		ip = new JTextField(5);

		campochat = new JTextArea(12, 30);
		
		campo1 = new JTextField(20);

		miboton = new JButton("Enviar");
		miboton.addActionListener(e -> onEnviarText(e));

		JPanel panel = new JPanel();
		panel.add(nick);
		panel.add(texto);
		panel.add(ip);
		panel.add(campochat);
		panel.add(campo1);
		panel.add(miboton);

		setBounds(600, 300, 280, 350);
		setContentPane(panel);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				finish();
			}
		});
		setVisible(true);
		
		new Thread(this).start();
	}
	
	private void finish() {
		try {
			dispose();
			servidor_cliente.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void onEnviarText(ActionEvent e) {
		// construimos el Socket
		try {
			Socket misocket = new Socket("localhost", 9999); // ¿¿ 10.0.2.15/24 o 127.0.0.1/8 oo 10.0.2.255

			Mensaje datos = new Mensaje();
			datos.setNick(nick.getText());
			datos.setIp(ip.getText());
			datos.setMensaje(campo1.getText());

			// creamos un stream de salida que envia el objeto paqueteenvio
			ObjectOutputStream paquete_datos = new ObjectOutputStream(misocket.getOutputStream());
			
			// escribimos en el stream de salida los datos (objeto) que le pasaremos
			paquete_datos.writeObject(datos); 
			
			// cerramos el socket
			misocket.close();
			
			/*
			 * DataOutputStream flujo_salida=new
			 * DataOutputStream(misocket.getOutputStream()); //necesita OutputStream out,
			 * mirar api java con el socket que instanciamos
			 * flujo_salida.writeUTF(campo1.getText());
			 * 
			 * flujo_salida.close(); //cerrar siempre el DataOutputStream
			 */

		} catch (UnknownHostException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			System.err.println(ex.getMessage());
		}
	}

	public void run() { // iniciamos el hilo
		try {

			// creamos el serverSocket que estara a la escucha con el puerto que lo enviara
			servidor_cliente = new ServerSocket(9090); 

			// !!!!! bucle infinito para que esté permanentemente a la escucha
			while (true) { 
				
				// aceptando todas las conexciones que le vengan del exterior
				Socket cliente = servidor_cliente.accept(); 
				
				// flujo de datos de entrada capaz de transportar objetos
				ObjectInputStream flujo_entrada = new ObjectInputStream(cliente.getInputStream());
				
				// dentro de esta variable esta da la informacion que ha recibido
				Mensaje paqueteRecibido = (Mensaje) flujo_entrada.readObject(); 

				campochat.append("\n" + paqueteRecibido.getNick() + ": " + paqueteRecibido.getMensaje());

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}