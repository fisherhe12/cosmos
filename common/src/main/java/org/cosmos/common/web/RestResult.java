package org.cosmos.common.web;

/**
 * 封装REST处理结果
 *
 * @author fisher
 */


public class RestResult {
    /**
     * 状态码
     */
    private int code;
    /**
     * 消息
     */
    private String message;
    /**
     * 数据
     */
    private Object data;
    /**
     * 时间戳
     */
    private long timestamp;

    RestResult(int code, String message, Object data, long timestamp) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = timestamp;
    }

    public static RestResult.ResultBuilder builder() {
        return new RestResult.ResultBuilder();
    }

    public int getcode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public Object getData() {
        return this.data;
    }

    public long getTimestamp() {
        return this.timestamp;
    }


    public static class ResultBuilder {
        private int code;
        private String message;
        private Object data;

        ResultBuilder() {
        }

        public RestResult.ResultBuilder code(int code) {
            this.code = code;
            return this;
        }

        public RestResult.ResultBuilder message(String message) {
            this.message = message;
            return this;
        }

        public RestResult.ResultBuilder data(Object data) {
            this.data = data;
            return this;
        }


        public RestResult build() {
            return new RestResult(this.code, this.message, this.data, System.currentTimeMillis());
        }
    }

}
