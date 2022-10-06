package com.hoangit3.consistenthash.cache;

public interface Cachable {
    String get(String key);

    void put(String key, String value);
}
