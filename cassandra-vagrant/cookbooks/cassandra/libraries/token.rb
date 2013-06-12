def generate_token(servers, my_index)
  Chef::Log.info("cluster size #{node.cassandra.clustersize}");
  token = ((2**64 /node.cassandra.clustersize ) * my_index)- 2**63
  Chef::Log.info("Token generated #{token} for servers #{servers} with index #{my_index}") 
  token
end 