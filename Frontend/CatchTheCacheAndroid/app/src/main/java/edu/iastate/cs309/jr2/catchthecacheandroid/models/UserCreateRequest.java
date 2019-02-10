package edu.iastate.cs309.jr2.catchthecacheandroid.models;

public class UserCreateRequest {
    private String username, password, security_question, security_answer;

    public UserCreateRequest(String u, String p, String q, String a){
        this.username = u;
		this.password = p;
		this.security_question = q;
		this.security_answer = a;
    }
}
