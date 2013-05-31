package com.flux7;

import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

public class Main
{
    public static void main(String[] args)
    {
    	XmlBeanFactory beanFactory = new XmlBeanFactory(new ClassPathResource("applicationContext.xml"));
    	SpringDao  dao = (SpringDao) beanFactory.getBean("springDao"); 

    	String userId = "Spring";
    	
    	User user = new User(userId,"spring@example.com","CA",30);
    	dao.addUser(user);

    	User retrievedUser = dao.getUserById("Spring");
    	
    	System.out.println("User name : " + retrievedUser.getName());
    	System.out.println("User email : " + retrievedUser.getEmail());
    	System.out.println("User age : " + retrievedUser.getAge());
    	System.out.println("User state : " + retrievedUser.getState());
    	


    }
}
