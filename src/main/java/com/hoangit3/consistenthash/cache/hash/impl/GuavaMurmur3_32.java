package com.hoangit3.consistenthash.cache.hash.impl;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.hoangit3.consistenthash.cache.hash.HashGenerateStrategy;

public class GuavaMurmur3_32 implements HashGenerateStrategy {
    @Override
    public long generate(String key) {
        HashFunction hashFunction = Hashing.murmur3_32();
        return hashFunction.hashUnencodedChars(key).asLong();
    }
}
