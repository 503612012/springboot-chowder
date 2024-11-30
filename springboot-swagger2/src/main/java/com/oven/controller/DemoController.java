package com.oven.controller;

import com.oven.entity.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "用户控制器")
public class DemoController {

    @ApiOperation(value = "添加用户", notes = "添加用户接口")
    @ApiResponses({
            @ApiResponse(code = 200, message = "请求成功"),
            @ApiResponse(code = 201, message = "系统异常")
    })
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Object add(User user) {
        if ((user.getId() & 1) > 0) {
            return "添加成功";
        } else {
            return "添加失败";
        }
    }

    @ApiOperation(value = "删除用户", notes = "删除用户接口")
    @ApiResponses({
            @ApiResponse(code = 200, message = "请求成功"),
            @ApiResponse(code = 201, message = "系统异常")
    })
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ApiImplicitParam(name = "id", value = "用户主键", dataType = "int", required = true, example = "1")
    public Object delete(Integer id) {
        if ((id & 1) > 0) {
            return "删除成功";
        } else {
            return "删除失败";
        }
    }

    @ApiOperation(value = "更新用户", notes = "更新用户接口")
    @ApiResponses({
            @ApiResponse(code = 200, message = "请求成功"),
            @ApiResponse(code = 201, message = "系统异常")
    })
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Object update(User user) {
        if (user.getId() << 1 > 0) {
            return "更新成功";
        } else {
            return "更新失败";
        }
    }

    @ApiOperation(value = "查询用户", notes = "查询用户接口")
    @RequestMapping(value = "search", method = RequestMethod.GET)
    @ApiResponses({
            @ApiResponse(code = 200, message = "请求成功", response = User.class),
            @ApiResponse(code = 201, message = "系统异常", response = User.class)
    })
    public Object search(User user) {
        return user;
    }

}
