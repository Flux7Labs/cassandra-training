#
#./launch.py -n 1 -a ami-f3d1bb9a -k cassandra2 -v
#ssh ec2-184-72-189-148.compute-1.amazonaws.com -l ubuntu -i cassandra2.pem

#
sudo add-apt-repository ppa:webupd8team/java
sudo apt-get update
sudo apt-get install -y oracle-java6-installer
sudo update-alternatives --config java
wget http://apache.mirrors.pair.com/cassandra/1.2.5/apache-cassandra-1.2.5-src.tar.gz
tar -xvf apache-cassandra-1.2.5-src.tar.gz 
cd apache-cassandra-1.2.5-src/
sudo apt-get install ant 
ant 
ant //second ant due to some issue
 
#wget http://apache.mirrors.pair.com/cassandra/1.2.5/apache-cassandra-1.2.5-bin.tar.gz
#tar -xvf apache-cassandra-1.2.5-bin.tar.gz 
#cd apache-cassandra-1.2.5/


cd .. 
sudo apt-get install -y python-yaml git
git clone https://github.com/pcmanus/ccm.git
cd ccm/
sudo python setup.py install
cd ..
cd apache-cassandra-1.2.5-src/
ccm create test -n 3 -s 
