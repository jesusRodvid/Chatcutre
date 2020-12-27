package chatsocket;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class MarcoServidor extends JFrame implements Runnable {
	private static final long serialVersionUID = -6589278662503118640L;

	private ServerSocket servidor;
	
	private JTextArea areatexto;

	public MarcoServidor() {
		super();

		areatexto = new JTextArea();

		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(areatexto, BorderLayout.CENTER);

		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setContentPane(panel);
		setBounds(1200, 300, 280, 350);
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
			servidor.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {

			// creamos el server socket indicando el puerto que usara
			servidor = new ServerSocket(9999); 

			while (true) {

				// aceptando las conexiones que vngan por el servidor
				Socket misocket = servidor.accept(); 

				ObjectInputStream paquete_datos = new ObjectInputStream(misocket.getInputStream());
				Mensaje recibido = (Mensaje) paquete_datos.readObject();

				areatexto.append("\n" + recibido.getNick() + ": " + recibido.getMensaje() + " para " + recibido.getIp());

				Socket enviaDestinatario = new Socket(recibido.getIp(), 9090);
				ObjectOutputStream paqueteReenvio = new ObjectOutputStream(enviaDestinatario.getOutputStream());
				paqueteReenvio.writeObject(recibido);
				paqueteReenvio.close();
				
				enviaDestinatario.close();
				misocket.close();
			}

		} catch (IOException | ClassNotFoundException ex) {
			ex.printStackTrace();
		}
	}

}
