package com.hoangit3.consistenthash.cache.hash.impl;

import com.hoangit3.consistenthash.cache.hash.HashGenerateStrategy;

import java.util.zip.CRC32;

public class JdkCrc32 implements HashGenerateStrategy {
    @Override
    public long generate(String key) {
        CRC32 crc32 = new CRC32();
        crc32.update(key.getBytes());
        return crc32.getValue();
    }
}
