package org.trainingTracker.database.valueObject;

public class UserVO {
	private String nick;
	private String pass;
    private String mail;
	private String date;


	public UserVO(String nick, String pass, String mail, String date){
		this.nick = nick;
		this.pass = pass;
        this.mail = mail;
		this.date = date;
	}

	public String getNick() { return nick; }
	public String getPass() {
		return pass;
	}
    public String getMail() {
        return mail;
    }
	public String getDate() {
		return date;
	}
	
	@Override
	public String toString() {
		return String.format("Nickname: %s Nombre: %s Pass: %s Fecha: %s;", nick, mail, pass, date);
		
	}
	
	public String serialize(){	
		return String.format("{\"nombreUsuario\": \"%s\",  \"nombreReal\": \"%s\", \"date\": \"%s\" }",
            nick, mail, date);
	}
		
}
