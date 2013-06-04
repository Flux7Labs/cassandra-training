#!/usr/bin/env python

import mmh3
import argparse 

# mmh3 hash https://github.com/hajimes/mmh3

class murmur3_partitioner:
    bits = 64
    def get(self, key):
        return mmh3.hash64(key)[0]
    def max(self):
        return 2 ** self.bits
    def min(self):
        return self.max()/-2

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description='Mimic Cassandra hashing',
                                     epilog="Example: key_to_hash.py alex Alex Brenda --nodes 5 --replication_factor 3")
    parser.add_argument('keys', metavar='N', type=str, nargs='+',
                        help='Keys to convert')
    parser.add_argument('--nodes', dest='nodes', action='store', type=int, help='Num nodes')
    parser.add_argument('--replication_factor', dest='replication_factor', action='store', type=int, default=1, help='Replication factor')
    args = parser.parse_args()
    p = murmur3_partitioner()
    tokens = [(i*(p.max())/args.nodes) + p.min() for i in range(0, args.nodes)]

    print "="*80
    print "Tokens"
    print "="*80
    print tokens

    print "="*80
    print "Keys"
    print "="*80

    for key in args.keys:
        raw =  p.get(key)
        replicas = []
        node = 0
        for i in range(1, args.nodes):
            if raw > tokens[i-1] and raw < tokens[i]:
                node = i
                break
        replicas = [(node+x)%args.nodes for x in range(0, args.replication_factor)]
        replica_names = [ "node%d" % (i+1)  for i in replicas]
        representation =  "".join(["%x" % ord(c) for c in key])
        print "%s (%s) : %s" % (key, representation, ", ".join(map(str, replica_names)))
            
        
    
