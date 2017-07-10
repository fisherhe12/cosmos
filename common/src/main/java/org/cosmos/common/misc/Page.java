package org.cosmos.common.misc;

import org.apache.commons.lang3.StringUtils;

import java.util.List;



/**
 * 与具体ORM实现无关的分页参数及查询结果封装.
 * <p>
 * 所有序号从1开始.
 *
 * @param <T> Page中记录的类型.
 * @author fisher
 */
public class Page<T> {
    // -- 排序公共变量 --//
    public static final String ASC = "asc";
    public static final String DESC = "desc";
    //--默认每页显示数量--//
    private static final int DEFAULT_PAGE_SIZE = 10;
    //--默认总记录数--//
    private static final int DEFAULT_TOTAL_COUNT = Integer.MAX_VALUE;
    // -- 返回结果 --//
    private List<T> result;
    // -- 分页参数 --//
    private int pageNo;
    private int pageSize;
    private String orderField = null;
    private String orderType = null;
    private int totalCount;

    // -- 构造函数 --//
    public Page() {
        this(0);
    }

    public Page(int pageSize) {
        this(pageSize, DEFAULT_TOTAL_COUNT);
    }

    public Page(int pageSize, int totalCount) {
        this.pageSize = pageSize > 0 ? pageSize : DEFAULT_PAGE_SIZE;
        this.totalCount = totalCount > 0 ? totalCount : DEFAULT_TOTAL_COUNT;
    }


    /**
     * 获得当前页的页号,序号从1开始,默认为1.
     */
    public int getPageNo() {
        return pageNo;
    }

    /**
     * 设置当前页的页号,序号从1开始,低于1时自动调整为1.
     */
    public void setPageNo(final int pageNo) {
        this.pageNo = pageNo;

        if (pageNo < 1) {
            this.pageNo = 1;
        }
    }

    /**
     * 返回Page对象自身的setPageNo函数,可用于连续设置。
     */
    public Page<T> pageNo(final int thePageNo) {
        setPageNo(thePageNo);
        return this;
    }

    /**
     * 获得每页的记录数量, 默认为-1.
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * 设置每页的记录数量.
     */
    public void setPageSize(final int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * 返回Page对象自身的setPageSize函数,可用于连续设置。
     */
    public Page<T> pageSize(final int thePageSize) {
        setPageSize(thePageSize);
        return this;
    }

    /**
     * 获得排序字段,无默认值. 多个排序字段时用','分隔.
     */
    public String getOrderField() {
        return orderField;
    }

    /**
     * 设置排序字段,多个排序字段时用','分隔.
     */
    public void setOrderField(final String orderField) {
        this.orderField = orderField;
    }

    /**
     * 返回Page对象自身的setOrderBy函数,可用于连续设置。
     */
    public Page<T> orderBy(final String orderField) {
        setOrderField(orderField);
        return this;
    }

    /**
     * 获得排序方向, 无默认值.
     */
    public String getOrderType() {
        return orderType;
    }

    /**
     * 设置排序方式向.
     *
     * @param orderType 可选值为desc或asc,多个排序字段时用','分隔.
     */
    public void setOrderType(final String orderType) {
        String lowcaseOrderType = StringUtils.lowerCase(orderType);

        // 检查order字符串的合法值
        String[] orders = StringUtils.split(lowcaseOrderType, ',');
        for (String orderStr : orders) {
            if (!StringUtils.equals(DESC, orderStr) && !StringUtils.equals(ASC, orderStr)) {
                throw new IllegalArgumentException("排序方向" + orderStr + "不是合法值");
            }
        }

        this.orderType = lowcaseOrderType;
    }

    /**
     * 返回Page对象自身的setOrder函数,可用于连续设置。
     */
    public Page<T> order(final String orderType) {
        setOrderType(orderType);
        return this;
    }

    /**
     * 是否已设置排序字段,无默认值.
     */
    public boolean isOrderBySetted() {
        return (StringUtils.isNotBlank(orderField) && StringUtils.isNotBlank(orderType));
    }

    /**
     * 获得页内的记录列表.
     */
    public List<T> getResult() {
        return result;
    }

    /**
     * 设置页内的记录列表.
     */
    public void setResult(final List<T> result) {
        this.result = result;
    }

    /**
     * 获得总记录数, 默认值为-1.
     */
    public long getTotalCount() {
        return totalCount;
    }

    /**
     * 设置总记录数.
     */
    public void setTotalCount(final int totalCount) {
        this.totalCount = totalCount;
    }

    /**
     * 根据pageSize与totalCount计算总页数, 默认值为-1.
     */
    public long getTotalPages() {
        if (totalCount < 0) {
            return -1;
        }

        long count = totalCount / pageSize;
        if (totalCount % pageSize > 0) {
            count++;
        }
        return count;
    }

    /**
     * 是否还有下一页.
     */
    public boolean isHasNext() {
        return (pageNo + 1 <= getTotalPages());
    }

    /**
     * 取得下页的页号, 序号从1开始. 当前页为尾页时仍返回尾页序号.
     */
    public int getNextPage() {
        if (isHasNext()) {
            return pageNo + 1;
        } else {
            return pageNo;
        }
    }

    /**
     * 是否还有上一页.
     */
    public boolean isHasPre() {
        return (pageNo - 1 >= 1);
    }

    /**
     * 取得上页的页号, 序号从1开始. 当前页为首页时返回首页序号.
     */
    public int getPrePage() {
        if (isHasPre()) {
            return pageNo - 1;
        } else {
            return pageNo;
        }
    }

    /**
     * 取得当前页第一项在全部项中的偏移量 (0-based)。
     *
     * @return 偏移量
     */
    public int getOffset() {
        return pageNo > 0 ? pageSize * (pageNo - 1) : 0;
    }

    /**
     * 取得当前页的长度，即当前页的实际项数。相当于 <code>endIndex() - beginIndex() + 1</code>
     *
     * @return 当前页的长度
     */
    public int getLength() {
        if (pageNo > 0) {
            return Math.min(pageSize * pageNo, totalCount) - pageSize * (pageNo - 1);
        } else {
            return 0;
        }
    }

    /**
     * 取得当前页显示的项的起始序号 (1-based)。
     *
     * @return 起始序号
     */
    public int getBeginIndex() {
        if (pageNo > 0) {
            return pageSize * (pageNo - 1) + 1;
        } else {
            return 0;
        }
    }

    /**
     * 取得当前页显示的末项序号 (1-based)。
     *
     * @return 末项序号
     */
    public int getEndIndex() {
        if (pageNo > 0) {
            return Math.min(pageSize * pageNo, totalCount);
        } else {
            return 0;
        }
    }
}
