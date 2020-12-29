package socket.chat.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import socket.chat.messages.Message;

public class Client {

	private String server;
	private int port;
	private Socket socket;
	private Queue<Message> inputQueue = new ConcurrentLinkedQueue<>();
	private Queue<Message> outputQueue = new ConcurrentLinkedQueue<>();
	
	public Client(String server, int port) {
		super();
		this.server = server;
		this.port = port;
	}

	public Client() { }
	
	public void stop() {
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void start() throws UnknownHostException, IOException {
		System.out.println("Client connecting to " + server + ":" + port + " ...");
		start(new Socket(server, port));		
	}
	
	public void start(Socket socket) throws UnknownHostException, IOException {
		this.socket = socket;
		new Thread(() -> processOutgoingMessages()).start();
		new Thread(() -> processIncomingMessages()).start();
		System.out.println("Client connected to socket " + socket + "!");
	}

	public void send(Message message) {
		outputQueue.add(message);
	}
	
	public Queue<Message> getInputQueue() {
		return inputQueue;
	}

	private void processOutgoingMessages() { 
		
		try {

			System.out.println("Processing outgoing messages from socket " + socket);
			ObjectOutputStream ous = new ObjectOutputStream(socket.getOutputStream());
			
			while (true) {
				
				while (!outputQueue.isEmpty()) {
					
					Message messageSent = outputQueue.poll();
					ous.writeObject(messageSent);
					System.out.println("Message send: " + messageSent);
					
				}
				
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void processIncomingMessages() { 
		
		try {

			System.out.println("Processing incoming messages from socket " + socket);
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			
			while (true) {
				
				Message messageReceived = (Message) ois.readObject();
				inputQueue.add(messageReceived);
				System.out.println("Message received: " + messageReceived);
				
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		Client client = new Client("localhost", 9999);
		client.send(new Message("perico", "1.2.3.4", "mensaje de prueba 1"));
		client.send(new Message("palotes", "4.3.2.1", "mensaje de prueba 2"));
		client.start();
	}

}