package org.trainingTracker.database.valueObject;

public class UsuarioVO {
	private String nickname;
	private String nombre;
	private String passwd;
	private String fecha;
	
	
	public UsuarioVO (int _id, String nickname, String nombre, String passwd, String fecha){
		this.nickname = nickname;
		this.nombre = nombre;
		this.passwd = passwd;
		this.fecha = fecha;
	}

	public String getNickname() {
		return nickname;
	}
	public String getNombre() {
		return nombre;
	}
	public String getPasswd() {
		return passwd;
	}
	public String getFecha() {
		return fecha;
	}
	
	@Override
	public String toString() {
		return String.format("Nickname: %s Nombre: %s Pass: %s Fecha: %s;", nickname, nombre, passwd, fecha);
		
	}
	
	public String serialize(){	
		return String.format("{\"nombreUsuario\": \"%s\",  \"nombreReal\": \"%s\", \"fecha\": \"%s\" }",
				nickname, nombre, fecha);
	}
		
}
