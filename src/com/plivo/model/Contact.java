package com.plivo.model;

public class Contact {

    private static String STRING_WITH_QUOTES = "'%s'";
    private static String COMMA_SEPARATOR  = ",";
	private static String TAB_SEPARATOR = "\t";
	private static String NAME_PREFIX = "NAME=";
	private static String EMAIL_PREFIX = "EMAIL=";
	private static String INFO_PREFIX = "INFO=";

    private String name;

    private String email;

    private String info;

    public Contact(String email, String name, String info) {
        this.email = email;
        this.name = name;
        this.info = info;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

	public String toString() {
		return String.join(TAB_SEPARATOR, NAME_PREFIX.concat(name), EMAIL_PREFIX.concat(email), INFO_PREFIX.concat(info));
	}
	
    public String toQuery() {

        return String.join(COMMA_SEPARATOR, String.format(STRING_WITH_QUOTES, email),
											String.format(STRING_WITH_QUOTES, name) ,
											String.format(STRING_WITH_QUOTES, info));
    }
}
