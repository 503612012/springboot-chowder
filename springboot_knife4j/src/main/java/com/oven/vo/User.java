package com.oven.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "用户实体类", description = "用户实体类信息")
public class User {

    @ApiModelProperty(value = "用户主键", dataType = "int", example = "1")
    private Integer id;
    @ApiModelProperty(value = "用户名", dataType = "string", example = "admin")
    private String uname;
    @ApiModelProperty(value = "用户密码", dataType = "string", example = "123456")
    private String pwd;
    @ApiModelProperty(value = "用户年龄", dataType = "int", example = "18")
    private Integer age;

}
