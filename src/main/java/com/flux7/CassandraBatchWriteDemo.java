package com.flux7;

import java.util.HashMap;
import java.util.Map;

import me.prettyprint.cassandra.model.ConfigurableConsistencyLevel;
import me.prettyprint.cassandra.serializers.IntegerSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.cassandra.service.template.ThriftColumnFamilyTemplate;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.HConsistencyLevel;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.ddl.KeyspaceDefinition;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CassandraBatchWriteDemo {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CassandraBatchWriteDemo.class);
	
	private static final String COLUMN_FAMILY = "users";
	private static final String KEY_SPACE_NAME = "test";
	
	private int batchSize = 3;
	
	private ColumnFamilyTemplate<String,String> template;
	
	private Cluster cluster;

	private Mutator<String> mutator;
	
	
	public CassandraBatchWriteDemo() throws Exception {
		
		//List of comma separated ips of nodes
		String cassandraNodes = "192.168.50.3:9160";

		CassandraHostConfigurator hosts = new CassandraHostConfigurator(cassandraNodes);
		
		hosts.setAutoDiscoverHosts(true);	
		hosts.setMaxActive(50);
		hosts.setRetryDownedHosts(true);
		hosts.setAutoDiscoveryDelayInSeconds(3);
		
		/* Create connection to cluster */
		cluster = HFactory.getOrCreateCluster("test",
					hosts);
		
		/* Defining Consistency Level */
        ConfigurableConsistencyLevel configurableConsistencyLevel = new ConfigurableConsistencyLevel();
        Map<String, HConsistencyLevel> clmap = new HashMap<String, HConsistencyLevel>();

        // Define CL.ONE for ColumnFamily "MyColumnFamily"
        clmap.put("MyColumnFamily", HConsistencyLevel.ONE);

        // In this we use CL.ONE for read and writes. But you can use different CLs if needed.
        configurableConsistencyLevel.setReadCfConsistencyLevels(clmap);
        configurableConsistencyLevel.setWriteCfConsistencyLevels(clmap);
		
		KeyspaceDefinition keyspaceDef = cluster
				.describeKeyspace(KEY_SPACE_NAME);
		
		if(keyspaceDef == null){
			throw new Exception("Either Keyspace  does not exist or No Cassandra Host Available");
		}
		
		Keyspace keyspace = HFactory.createKeyspace(KEY_SPACE_NAME, cluster, configurableConsistencyLevel);
		
		template = new ThriftColumnFamilyTemplate<String, String>(
				keyspace, COLUMN_FAMILY, StringSerializer.get(),
					StringSerializer.get());
		
		// Create mutator 
		mutator = HFactory.createMutator(keyspace, StringSerializer.get());

			
	}

	public void save(String rowKey,String state , String email , Integer age){
		ColumnFamilyUpdater<String, String> updater = template.createUpdater(rowKey);
		updater.setString("state", state);
		updater.setInteger("age", age);
		updater.setString("email", email);
		template.update(updater);
	}
	
	public synchronized void batchSave(String rowKey,String state , String email , Integer age){
   	
		
		HColumn<String,String> stateColumn = HFactory.createColumn("state", state, StringSerializer.get(), StringSerializer.get());
		HColumn<String,String> emailColumn = HFactory.createColumn("email", email, StringSerializer.get(), StringSerializer.get());
		HColumn<String,Integer> ageColumn = HFactory.createColumn("age", age, StringSerializer.get(), IntegerSerializer.get());
		mutator.addInsertion(rowKey, COLUMN_FAMILY, stateColumn);
		mutator.addInsertion(rowKey, COLUMN_FAMILY, emailColumn);
		mutator.addInsertion(rowKey, COLUMN_FAMILY, ageColumn);
		if(mutator.getPendingMutationCount() == batchSize){
			mutator.execute();
		}
	}

	public static void main(String [] args){
		try {
			
			
			CassandraBatchWriteDemo demo = new CassandraBatchWriteDemo();
			
			demo.batchSave("Alex", "CA", "alex@example.com" , 27);
			demo.batchSave("BOB", "TX", "bob@example.com" , 27);
			demo.batchSave("Brenda", "FL", "brenda@example.com" , 27);
			
			LOGGER.info("Record created successfully");
			
			Thread.sleep(5000);
			LOGGER.info("{}",demo.cluster.getKnownPoolHosts(false));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}

