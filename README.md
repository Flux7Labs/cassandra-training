import as maven project into eclipse

in the top directory run :
mvn eclipse:eclipse

then you can import them as maven projects into eclipse or intellij idea

for Hector Project : 

change the ip of cassandra server in CassandraConfig.java class

For Kundera/Spring Project :

change the ip of cassandra server in src/main/resources/META-INF/persistence.xml file

---------

All the classes have main methods , so you can run them as java project from eclipse and see the results in console.



