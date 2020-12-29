package socket.chat;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import socket.chat.messages.Message;
import socket.chat.server.Server;
import socket.chat.utils.ThreadUtils;

public class VentanaServidor extends JFrame implements Runnable {
	private static final long serialVersionUID = -6589278662503118640L;

	private boolean finish = false;
	private Server server;

	private JTextArea areatexto;

	public VentanaServidor() throws IOException {
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
				server.stop();
				finish = true;
				dispose();
			}
		});
		setVisible(true);

		// creamos el server socket indicando el puerto que usara
		server = new Server(9999);
		server.start();
		
		new Thread(this).start();
	}

	@Override
	public void run() {
		while (!finish) {
			if (!server.getQueue().isEmpty()) {
				Message message = server.getQueue().poll();
				areatexto.append("\n" + message.getNick() + ": " + message.getMessage() + " para " + message.getIp());
			}
			ThreadUtils.sleep(500);
		}
	}


}
