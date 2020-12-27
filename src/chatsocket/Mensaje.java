package chatsocket;

import java.io.Serializable;

public class Mensaje implements Serializable {
	private static final long serialVersionUID = -2647285985177257635L;
	
	private String nick;
	private String ip;
	private String mensaje;

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

}
