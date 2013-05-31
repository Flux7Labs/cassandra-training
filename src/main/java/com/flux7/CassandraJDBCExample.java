package com.flux7;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CassandraJDBCExample extends CassandraConfig {
	private static final Logger LOGGER = LoggerFactory.getLogger(CassandraJDBCExample.class);
	
	private final static String KEYSPACE = "test";
	
	public static void main(String [] args){
		try {
			
			Class.forName("org.apache.cassandra.cql.jdbc.CassandraDriver");
		    Connection con = DriverManager.getConnection("jdbc:cassandra://"+ CASSANDRA_ADDRESS +"/"+KEYSPACE+"?version=2.0.0");

		    String query = "select * from  users";
		    PreparedStatement statement = con.prepareStatement(query);

		    ResultSet rs = statement.executeQuery();
		    
		    while(rs.next()){
		    	
		    	LOGGER.debug("name : {}",rs.getString(1));
		    	LOGGER.debug("age : {}",rs.getString(2));
		    	LOGGER.debug("email : {}",rs.getString(3));
		    	LOGGER.debug("state : {}",rs.getString(4));
		    	LOGGER.debug("------------");
		    }

		    statement.close();
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
