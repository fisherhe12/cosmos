package org.cosmos.admin.domain.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 系统用户类
 *
 * @author fisher
 * @date 2017-08-11
 */
@TableName(value = "sys_user",resultMap = "BaseResultMap")
@Getter
@Setter
@ToString
public class User extends BaseEntity {
    private String account;
    private String avatar;
    private String name;
    private String password;
    private String salt;
    private String gender;
    private String email;
    @TableField(value = "is_active")
    @TableLogic
    private Boolean active;
}
