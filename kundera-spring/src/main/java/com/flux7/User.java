package com.flux7;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users", schema = "KunderaExamples@cassandra_pu")
public class User 
{
    @Id
    private String name;
    
    @Column(name="email")
    private String email;
    
    @Column(name="state")
    private String state;
    
    @Column(name="age")
    private int age;    

    public User(){
    	
    }
    public User(String name,String email,String state, int age)
    {
    	this.name = name;
    	this.age = age;
    	this.email = email;
    	this.state = state;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}


}
