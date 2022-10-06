package com.hoangit3.consistenthash.cache.manager;

import com.hoangit3.consistenthash.cache.Node;
import com.hoangit3.consistenthash.cache.hash.HashGenerateStrategy;
import com.hoangit3.consistenthash.cache.hash.impl.JdkCrc32;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class ConsistentHashDistributedNodeManager implements DistributedNodeManager {
    // nums of virtual servers
    @Setter
    private int virtualNums = 100;
    // represent a list of servers
    private final List<Node> nodes = new ArrayList<>();
    // a ring which servers fit into
    private final SortedMap<Long, Node> hashRing = new TreeMap<>();

    // hash algorithm. default is JdkCrc32
    @Setter
    HashGenerateStrategy hashGenerateStrategy = new JdkCrc32();

    // add new node to list of servers
    @Override
    public void addNode(Node node) {
        this.nodes.add(node);
        // add servers into ring
        for (int i = 0; i < virtualNums; i++) {
            String key = String.format("%s_%d", node.getIp(), i);
            long hash = hashGenerateStrategy.generate(key);
            hashRing.put(hash, node);
        }
    }

    // find the node that holds data based on key
    @Override
    public Node lookupNode(String key) {
        long hashCode = hashGenerateStrategy.generate(key);

        // find servers on the ring which has key equal or greater than hashcode of key
        // we wanna fetch data.
        SortedMap<Long, Node> tailMap = hashRing.tailMap(hashCode);

        // return nearest node that satisfy condition
        long nodeHash = tailMap.isEmpty() ? hashRing.firstKey() : tailMap.firstKey();
        return hashRing.get(nodeHash);
    }

    @Override
    public List<Node> getAllNode() {
        return this.nodes;
    }
}
