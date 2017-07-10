package org.cosmos.common.text;

/**
 * 参考Netty的InternalThreadLocalMap 与 BigDecimal, 放在threadLocal中重用的StringBuilder, 节约StringBuilder内部的char[]
 * <p>
 * 不过仅在String对象较大时才有明显效果，否则抵不上访问ThreadLocal的消耗.
 * <p>
 * 当StringBuilder在使用过程中，会调用其他可能也使用StringBuilderHolder的子函数时，需要创建独立的Holder, 否则使用公共的Holder
 * <p>
 * 注意：在Netty环境中，使用Netty提供的基于FastThreadLocal的版本。
 */
public class StringBuilderHolder {

    // 公共的Holder
    private static ThreadLocal<StringBuilder> globalStringBuilder = new ThreadLocal<StringBuilder>() {
        @Override
        protected StringBuilder initialValue() {
            return new StringBuilder(512);
        }
    };
    private int initSize;
    // 独立的Holder
    private ThreadLocal<StringBuilder> stringBuilder = new ThreadLocal<StringBuilder>() {
        @Override
        protected StringBuilder initialValue() {
            return new StringBuilder(initSize);
        }
    };

    /**
     * 创建独立的Holder.
     * <p>
     * 用于StringBuilder在使用过程中，会调用其他可能也使用StringBuilderHolder的子函数.
     *
     * @param initSize StringBulder的初始大小, 建议512,如果容量不足将进行扩容，扩容后的数组将一直保留.
     */
    public StringBuilderHolder(int initSize) {
        this.initSize = initSize;
    }

    /**
     * 获取公共Holder的StringBuilder.
     * <p>
     * 当StringBuilder会被连续使用，期间不会调用其他可能也使用StringBuilderHolder的子函数时使用.
     * <p>
     * 重置StringBuilder内部的writerIndex, 而char[]保留不动.
     */
    public static StringBuilder getGlobal() {
        StringBuilder sb = globalStringBuilder.get();
        sb.setLength(0);
        return sb;
    }

    /**
     * 获取独立Holder的StringBuilder.
     * <p>
     * 重置StringBuilder内部的writerIndex, 而char[]保留不动.
     */
    public StringBuilder get() {
        StringBuilder sb = stringBuilder.get();
        sb.setLength(0);
        return sb;
    }
}
