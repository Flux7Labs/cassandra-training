package com.flux7;

import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.beans.Row;
import me.prettyprint.hector.api.beans.Rows;
import me.prettyprint.hector.api.exceptions.HectorException;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CassandraRangeReadDemo extends CassandraConfig{
	private static final Logger LOGGER = LoggerFactory.getLogger(CassandraRangeReadDemo.class);
	
	   private static StringSerializer stringSerializer = StringSerializer.get();

	    public static void main(String[] args) throws Exception {

	        Cluster cluster = HFactory.getOrCreateCluster("test", CASSANDRA_ADDRESS);

	        Keyspace keyspaceOperator = HFactory.createKeyspace("test", cluster);

	        try {

	            
	        	RangeSlicesQuery<String, String, String> rangeSliceQuery = 
	                HFactory.createRangeSlicesQuery(keyspaceOperator, stringSerializer, stringSerializer, stringSerializer);
	            rangeSliceQuery.setColumnFamily("users");            
	            rangeSliceQuery.setKeys("Brenda","Alex");

	            // set null range for empty byte[] on the underlying predicate
	            rangeSliceQuery.setRange("", "", false, 3);
	            System.out.println(rangeSliceQuery);

	            QueryResult<OrderedRows<String, String, String>> result = rangeSliceQuery.execute();
	            Rows<String, String, String> orderedRows = result.get();
	           
	            
	            LOGGER.info("Contents of rows: \n");
	            LOGGER.info("---------------------------------------------------");
	            for (Row<String, String, String> r : orderedRows) {
	                LOGGER.info("   " + r);
	            }
	            LOGGER.info("---------------------------------------------------");
	            
	        } catch (HectorException he) {
	            he.printStackTrace();
	        }
	        cluster.getConnectionManager().shutdown();        
	    }
}
