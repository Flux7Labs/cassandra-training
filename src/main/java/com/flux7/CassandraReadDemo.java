package com.flux7;

import java.util.HashMap;
import java.util.Map;

import me.prettyprint.cassandra.model.ConfigurableConsistencyLevel;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.cassandra.service.template.ColumnFamilyResult;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.cassandra.service.template.ThriftColumnFamilyTemplate;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.HConsistencyLevel;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.ddl.KeyspaceDefinition;
import me.prettyprint.hector.api.factory.HFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CassandraReadDemo {
	private static final Logger LOGGER = LoggerFactory.getLogger(CassandraReadDemo.class);
	
	private static final String COLUMN_FAMILY = "users";
	private static final String KEY_SPACE_NAME = "test";
	
	private ColumnFamilyTemplate<String,String> template;
	
	private Cluster cluster;
	
	@SuppressWarnings("rawtypes")
	
	public CassandraReadDemo() throws Exception {
		
		
		String cassandraNodes = "192.168.50.3:9160";

		CassandraHostConfigurator hosts = new CassandraHostConfigurator(cassandraNodes);
		
		hosts.setAutoDiscoverHosts(true);	
		hosts.setMaxActive(50);
		hosts.setRetryDownedHosts(true);
		hosts.setAutoDiscoveryDelayInSeconds(3);
		
		cluster = HFactory.getOrCreateCluster("test",
					hosts);

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
			
	}

	public void save(String rowKey,String state , String email , Integer age){
		ColumnFamilyUpdater<String, String> updater = template.createUpdater(rowKey);
		updater.setString("state", state);
		updater.setInteger("age", age);
		updater.setString("email", email);
		template.update(updater);
	}
	

	public void get(String rowKey){
		
		ColumnFamilyResult<String, String> res = template.queryColumns(rowKey);
		
		LOGGER.info("Email is {}", res.getString("email"));
		LOGGER.info("Age is {}", res.getInteger("age"));
		LOGGER.info("State is {}", res.getString("state"));
	    
	}
	
	public static void main(String [] args){
		try {
			
			
			CassandraReadDemo demo = new CassandraReadDemo();
			
			demo.get("Alex");
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
