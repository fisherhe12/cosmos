package org.cosmos.common.misc;

/**
 * 采用Twitter的Snowflake算法,在分布式环境中生成唯一ID
 * <p>
 * 结构如:0 - 0000000000 0000000000 0000000000 0000000000 0 - 00000 - 00000 - 000000000000
 * <p>
 * 第一位为未使用，接下来的41位为毫秒级时间(41位的长度可以使用69年）<br>
 * 然后是5位datacenterId和5位workerId(10位的长度最多支持部署1024个节点）<br>
 * 最后12位是毫秒内的计数（12位的计数顺序号支持每个节点每毫秒产生4096个ID序号）<br>
 *
 * @author fisher
 */
public class SnowflakeGenerator {

    private final long twepoch = 1288834974657L;
    private final long workerIdBits = 5L;
    private final long datacenterIdBits = 5L;
    private final long maxWorkerId = -1L ^ (-1L << workerIdBits);
    private final long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);
    private final long sequenceBits = 12L;
    private final long workerIdShift = sequenceBits;
    private final long datacenterIdShift = sequenceBits + workerIdBits;
    private final long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;
    private final long sequenceMask = -1L ^ (-1L << sequenceBits);

    private long workerId=1L;
    private long dataCenterId=1L;
    private long sequence = 0L;
    private long lastTimestamp = -1L;

    /**
     * 构造
     *
     * @param workerId     终端ID
     * @param dataCenterId 数据中心ID
     */
    public SnowflakeGenerator(long workerId, long dataCenterId) {
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0",
                maxWorkerId));
        }
        if (dataCenterId > maxDatacenterId || dataCenterId < 0) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0",
                maxDatacenterId));
        }
        this.workerId = workerId;
        this.dataCenterId = dataCenterId;
    }

    /**
     * 下一个ID
     *
     * @return ID
     */
    public synchronized long nextId() {
        long timestamp = System.currentTimeMillis();
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d "
                    + "milliseconds",
                lastTimestamp - timestamp));
        }
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }

        lastTimestamp = timestamp;

        return ((timestamp - twepoch) << timestampLeftShift) | (dataCenterId << datacenterIdShift) | (workerId
            << workerIdShift) | sequence;
    }

    private long tilNextMillis(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }
}
