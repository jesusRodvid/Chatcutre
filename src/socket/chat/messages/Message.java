package socket.chat.messages;

import java.io.Serializable;

public class Message implements Serializable {
	private static final long serialVersionUID = -2647285985177257635L;

	private String nick;
	private String ip;
	private String message;
	
	public Message() {}

	public Message(String nick, String ip, String message) {
		this.nick = nick;
		this.ip = ip;
		this.message = message;
	}

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

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "Message [nick=" + nick + ", ip=" + ip + ", message=" + message + "]";
	}

}
