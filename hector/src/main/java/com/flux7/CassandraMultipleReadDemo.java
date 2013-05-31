package com.flux7;

import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.Row;
import me.prettyprint.hector.api.beans.Rows;
import me.prettyprint.hector.api.exceptions.HectorException;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.MultigetSliceQuery;
import me.prettyprint.hector.api.query.QueryResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CassandraMultipleReadDemo extends CassandraConfig{
	private static final Logger LOGGER = LoggerFactory.getLogger(CassandraMultipleReadDemo.class);
	
	   private static StringSerializer stringSerializer = StringSerializer.get();

	    public static void main(String[] args) throws Exception {

	        Cluster cluster = HFactory.getOrCreateCluster("test", CASSANDRA_ADDRESS);

	        Keyspace keyspaceOperator = HFactory.createKeyspace("test", cluster);

	        try {

	            
	            MultigetSliceQuery<String, String, String> multigetSliceQuery = 
	                HFactory.createMultigetSliceQuery(keyspaceOperator, stringSerializer, stringSerializer, stringSerializer);
	            multigetSliceQuery.setColumnFamily("users");            
	            multigetSliceQuery.setKeys("Brenda", "thrift","Alex", "BOB", "fake_key_4");

	            // set null range for empty byte[] on the underlying predicate
	            multigetSliceQuery.setRange(null, null, false, 3);
	            System.out.println(multigetSliceQuery);

	            QueryResult<Rows<String, String, String>> result = multigetSliceQuery.execute();
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
