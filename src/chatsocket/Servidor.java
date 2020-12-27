package chatsocket;

import javax.swing.SwingUtilities;

public class Servidor {

	public static void main(String[] args) {

		SwingUtilities.invokeLater(() -> new MarcoServidor());

	}
	
}
