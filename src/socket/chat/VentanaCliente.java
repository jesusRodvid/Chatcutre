package socket.chat;

import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import socket.chat.client.Client;
import socket.chat.messages.Message;
import socket.chat.utils.ThreadUtils;

class VentanaCliente extends JFrame implements Runnable {

	private static final long serialVersionUID = 1L;

	private JTextField campo1, nick, ip;
	private JTextArea campochat;
	private JButton miboton;

	private Client client;

	public VentanaCliente() throws UnknownHostException, IOException {
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
				client.stop();
				dispose();
			}
		});
		setVisible(true);

		client = new Client("localhost", 9999);
		client.start();
		
		new Thread(this).start();
	}

	public void onEnviarText(ActionEvent e) {
		Message message = new Message();
		message.setNick(nick.getText());
		message.setIp(ip.getText());
		message.setMessage(campo1.getText());
		client.send(message);
	}

	public void run() { // iniciamos el hilo
		while (true) {
			if (!client.getInputQueue().isEmpty()) {
				Message message = client.getInputQueue().poll();
				campochat.append("\n" + message.getNick() + ": " + message.getMessage());
			}
			ThreadUtils.sleep(500);
		}
	}

}