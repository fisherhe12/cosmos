package org.cosmos.admin.domain.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 实体公共类
 *
 * @author fisher
 * @date 2018-01-05
 */
@Getter
@Setter
@ToString
class BaseEntity {
    /**
     * ID
     */
    private Integer id;
    /**
     * 创建时间
     */
    private Date gmtCreate;
    /**
     * 修改时间
     */
    private Date gmtModified;
}
