case node['platform_family']
when "rhel", "fedora", "suse"

  template "/etc/yum.repos.d/datastax.repo" do
    source "datastax.repo.erb"
    owner "root"
    group "root"
    mode "0755"
  end  
when "debian"
  apt_repository "datastax-cassandra" do
    uri "http://debian.datastax.com/community"
    components ["stable", "main"]
    key "http://debian.datastax.com/debian/repo_key"
    action :add
  end
end
seeds = ["192.168.50.3"]

package "dsc12" do
   version "1.2.1-1"
end

## Lets first fix init.d script restart option by putting little sleep between start/stop for restart #
template "/etc/init.d/cassandra" do
  source "cassandra_init.erb"
  mode 755
end

remote_ip = node[:network][:interfaces][:eth1][:addresses].detect{|k,v| v[:family] == "inet" }.first

puts remote_ip

template "/etc/cassandra/conf/cassandra.yaml" do
  source "cassandra.yaml.erb"
  owner "cassandra"
  group "cassandra"
  mode "0755"
  # notifies :restart, "service[cassandra]", :immediately
  variables({
    :seeds => seeds,
    :node_ip => remote_ip
  })
end

# service "cassandra" do
#   action [ :enable, :start ]
# end
execute "start_cassandra" do
  command "/etc/init.d/cassandra restart"
end