package org.trainingTracker.database.valueObject;

public class UserVO {
	private String nick;
	private String pass;
    private String mail;
	private String date;

    /**
     * Constructor that initialises the class with the given fields
     * @param nick
     * @param pass
     * @param mail
     * @param date
     */
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

    /**
     * Represents the class fields in a human readable format
     * @return
     */
	@Override
	public String toString() {
		return String.format("Nickname: %s Mail: %s Pass: %s Date: %s;", nick, mail, pass, date);
		
	}

    public String serialize(){
        return String.format("{\"Nick\": \"%s\",  \"Mail\": \"%s\",  \"Pass\": \"%s\", \"Date\": \"%s\" }",
            nick, mail, pass, date);
    }

}
