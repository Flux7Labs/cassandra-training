package com.flux7;

import me.prettyprint.cassandra.model.CqlQuery;
import me.prettyprint.cassandra.model.CqlRows;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.Row;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.QueryResult;

public class HectorCQL extends CassandraConfig {

	public static void main(String[] args) throws Exception {
		
		Cluster cluster = HFactory.getOrCreateCluster("test", CASSANDRA_ADDRESS);
		 
		StringSerializer se = new StringSerializer();
		
		Keyspace keyspace = HFactory.createKeyspace("test", cluster);
	    
		CqlQuery<String, String, String> cqlQuery = new CqlQuery<String,String,String>(keyspace, se, se, se);
		
	    cqlQuery.setQuery("select * from users");
	    QueryResult<CqlRows<String,String,String>> result = cqlQuery.execute();
	    CqlRows<String, String, String> rows = result.get();
	    
	    for (Row<String, String, String> row : rows){
	    	for(HColumn column :row.getColumnSlice().getColumns()){
	    		System.out.println(column.getName()+" :: "+ column.getValue());
	    		
	    	}
	    	System.out.println("------------");
	    }
	}

}
