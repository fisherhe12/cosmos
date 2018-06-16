package org.cosmos.admin.domain.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 系统角色类
 *
 * @author fisher
 * @date 2018-01-05
 */
@TableName("sys_role")
@Getter
@Setter
@ToString
public class Role extends BaseEntity {
    private String roleName;
    private String roleDesc;
}
