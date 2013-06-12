package com.flux7;

import me.prettyprint.cassandra.model.CqlQuery;
import me.prettyprint.cassandra.model.CqlRows;
import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.query.QueryResult;

public class HectorCQL {

	private final static String KEYSPACE = "User";
	private static final StringSerializer se = new StringSerializer();
	private static final LongSerializer le = new LongSerializer();

	public static void main(String[] args) {
		CqlQuery<String,String,Long> cqlQuery = new CqlQuery<String,String,Long>(keyspace, se, se, le);
	    cqlQuery.setQuery("select * from User");
	    QueryResult<CqlRows<String,String,Long>> result = cqlQuery.execute();
	    CqlRows<String, String, Long> rows = result.get();
	}

}
