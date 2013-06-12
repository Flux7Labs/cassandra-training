maintainer       "Lukasz Jastrzebski"
maintainer_email "lukasz.jastrzebski@nsn.com"
license          "All rights reserved"
description      "Installs/Configures cassandra"
long_description IO.read(File.join(File.dirname(__FILE__), 'README.md'))
version          "0.0.1"



recipe "cassandra", "installs cassandra"


%w{ ubuntu debian redhat centos fedora }.each do |os|
  supports os
end
