package com.hoangit3.consistenthash.cache.hash.impl;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.hoangit3.consistenthash.cache.hash.HashGenerateStrategy;

public class GuavaMd5 implements HashGenerateStrategy {
    @Override
    public long generate(String key) {
        HashFunction hashFunction = Hashing.md5();
        return hashFunction.hashUnencodedChars(key).asLong();
    }
}
