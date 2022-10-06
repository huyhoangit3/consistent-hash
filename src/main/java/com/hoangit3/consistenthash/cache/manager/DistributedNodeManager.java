package com.hoangit3.consistenthash.cache.manager;

import com.hoangit3.consistenthash.cache.Node;

import java.util.List;

public interface DistributedNodeManager {
    void addNode(Node node);

    Node lookupNode(String key);

    List<Node> getAllNode();
}
