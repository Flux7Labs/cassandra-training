package com.flux7;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.List;

import org.apache.cassandra.thrift.Cassandra;
import org.apache.cassandra.thrift.Column;
import org.apache.cassandra.thrift.ColumnOrSuperColumn;
import org.apache.cassandra.thrift.ColumnParent;
import org.apache.cassandra.thrift.ConsistencyLevel;
import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.cassandra.thrift.NotFoundException;
import org.apache.cassandra.thrift.SlicePredicate;
import org.apache.cassandra.thrift.SliceRange;
import org.apache.cassandra.thrift.TimedOutException;
import org.apache.cassandra.thrift.UnavailableException;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CassandraThriftExample {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(CassandraThriftExample.class);

	public static void main(String[] args) throws TException,
			InvalidRequestException, UnavailableException,
			UnsupportedEncodingException, NotFoundException, TimedOutException {

		TTransport framedTransport = new TFramedTransport(new TSocket(
				"192.168.50.3", 9160));
		TProtocol framedProtocol = new TBinaryProtocol(framedTransport);

		Cassandra.Client client = new Cassandra.Client(framedProtocol);
		framedTransport.open();

		client.set_keyspace("test");
		String columnFamily = "users";

		ColumnParent cp = new ColumnParent(columnFamily);
		byte[] userIDKey = "thrift".getBytes();
		client.insert(
				ByteBuffer.wrap(userIDKey),
				cp,
				new Column(
						ByteBuffer.wrap("email".getBytes("UTF-8")),
						ByteBuffer.wrap("thrift@example.com".getBytes("UTF-8")),
						System.currentTimeMillis()), ConsistencyLevel.ONE);
		client.insert(
				ByteBuffer.wrap(userIDKey),
				cp,
				new Column(ByteBuffer.wrap("state".getBytes("UTF-8")),
						ByteBuffer.wrap("CA".getBytes("UTF-8")), System
								.currentTimeMillis()), ConsistencyLevel.ONE);

		client.insert(
				ByteBuffer.wrap(userIDKey),
				cp,
				new Column(ByteBuffer.wrap("age".getBytes("UTF-8")), ByteBuffer
						.wrap("7".getBytes("UTF-8")), System
						.currentTimeMillis()), ConsistencyLevel.ONE);

		LOGGER.debug("Record inserted");
		
		
		SlicePredicate predicate = new SlicePredicate();
		predicate.setSlice_range(new SliceRange(ByteBuffer.wrap(new byte[0]),
				ByteBuffer.wrap(new byte[0]), false, 3));

		List<ColumnOrSuperColumn> results = client.get_slice(ByteBuffer.wrap("thrift".getBytes()),
				cp, predicate, ConsistencyLevel.ONE); // Reading row-2
		
		for (ColumnOrSuperColumn result : results) {
			Column column = result.column;
			LOGGER.debug(new String(column.getName()) + " "
					+ new String(column.getValue()));
		}
		
		framedTransport.close();

	}
}