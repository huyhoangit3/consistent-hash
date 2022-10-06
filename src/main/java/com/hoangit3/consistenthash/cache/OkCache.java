package com.hoangit3.consistenthash.cache;

import com.hoangit3.consistenthash.cache.hash.impl.GuavaSha256;
import com.hoangit3.consistenthash.cache.manager.ConsistentHashDistributedNodeManager;
import com.hoangit3.consistenthash.cache.manager.DistributedNodeManager;

import java.util.ArrayList;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.stream.IntStream;

public class OkCache implements Cachable {
    private final DistributedNodeManager manager;

    public OkCache(DistributedNodeManager manager) {
        this.manager = manager;
    }

    @Override
    public String get(String key) {
        Node node = manager.lookupNode(key);
        return node.get(key);
    }

    @Override
    public void put(String key, String value) {
        Node node = manager.lookupNode(key);
        node.put(key, value);
    }

    public void info() {
        List<Node> nodes = manager.getAllNode();

        // numbers of KV data stored on each server
        List<Integer> nodeCount = new ArrayList<>();
        nodes.forEach(node -> {
            int num = node.getMap().size();
            nodeCount.add(num);
            System.out.printf("Server (%s): stored %d data\n", node.getIp(), num);
        });

        // statistics
        final IntSummaryStatistics statistics = nodeCount.stream().mapToInt(Integer::intValue).summaryStatistics();
        long sum = statistics.getSum();
        double average = statistics.getAverage();
        int max = statistics.getMax();
        int min = statistics.getMin();
        int range = max - min;
        double standardDeviation = nodeCount.stream().mapToDouble(n -> Math.abs(n - average)).summaryStatistics().getAverage();
        System.out.printf("Total server: %d, Total KV data: %d\n", nodes.size(), sum);
        System.out.printf("Average：%.2f\n", average);
        System.out.printf("Max：%d,（%.2f%%）\n", max, 100.0 * max / average);
        System.out.printf("Min：%d,（%.2f%%）\n", min, 100.0 * min / average);
        System.out.printf("极差：%d,（%.2f%%）%n", range, 100.0 * range / average);
        System.out.printf("StandardDeviation：%.2f,（%.2f%%）%n", standardDeviation, 100.0 * standardDeviation / average);
    }

    public static void main(String[] args) {
        // amount of data
        final int dataNum = 1000;
        // number of servers
        final int serverNum = 10;
        // number of virtual server
        final int vnodeNum = 100;

        ConsistentHashDistributedNodeManager nodeManager = new ConsistentHashDistributedNodeManager();
        nodeManager.setVirtualNums(vnodeNum);
        nodeManager.setHashGenerateStrategy(new GuavaSha256());

        IntStream.range(0, serverNum).forEach(i -> nodeManager.addNode(new Node("node" + i, "192.168.0." + i)));

        OkCache okCache = new OkCache(nodeManager);

        for (int i = 0; i < dataNum; i++) {
            okCache.put("Name" + i, String.valueOf(Math.random()));
        }
        String key = "Name939";
        System.out.printf("%s:%s%n", key, okCache.get(key));
//        nodeManager.addNode(new Node("node10", "192.168.0.10"));

//        String key1 = "Name939";
//        System.out.println(String.format("%s:%s", key1, okCache.get(key1)));
        okCache.info();
    }
}
