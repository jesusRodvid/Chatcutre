package socket.chat;

import java.io.IOException;

import javax.swing.SwingUtilities;

public class Main {

	public static void main(String[] args) {

		SwingUtilities.invokeLater(() -> {
			try {
				new VentanaServidor();			
				new VentanaCliente();
				new VentanaCliente();
				new VentanaCliente();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		
	}

}
