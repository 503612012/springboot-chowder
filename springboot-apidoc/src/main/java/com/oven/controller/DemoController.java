package com.oven.controller;

import com.oven.vo.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    /**
     * @api {POST} /add 添加用户
     * @apiGroup users
     * @apiVersion v1.0.0
     * @apiDescription 添加用户接口
     * @apiParam {String} uname 用户名
     * @apiParam {String} pwd 用户密码
     * @apiParam {Integer} age 年龄
     * @apiParamExample {json} 请求样例：
     * ?uname=admin&pwd=123&age=18
     * @apiSuccess (200) {String} data 响应信息
     * @apiSuccess (200) {int} code 请求状态码
     * @apiSuccess (201) {String} data 错误信息
     * @apiSuccess (201) {int} code 请求状态码
     * @apiSuccessExample {json} 返回样例:
     * {code: 200, data: '添加成功'}
     * @apiSuccessExample {json} 返回样例:
     * {code: 201, data: '添加失败'}
     */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Object add(User user) {
        System.out.println(user);
        if ((user.getId() & 1) > 0) {
            return "添加成功";
        } else {
            return "添加失败";
        }
    }

    /**
     * @api {get} /search/ 查询用户息
     * @apiGroup users
     * @apiVersion v1.0.0
     * @apiDescription 查询用户息接口
     * @apiParam {String} uname 用户名
     * @apiParam {String} pwd 密码
     * @apiParam {Integer} age 年龄
     * @apiParamExample {json} 请求样例：
     * ?uname=admin&pwd=123&age=18
     * @apiSuccessExample Success-Response:
     * HTTP/1.1 200 请求成功
     * {
     *      "code": "200",
     *      "data": [{
     *                   "id": "1",
     *                   "uname": "admin"
     *                   "pwd": "123"
     *                   "age": 18
     *              }]
     * }
     * @apiErrorExample Error-Response:
     * HTTP/1.1 201 接口异常
     * {
     *      "code": "201",
     *      "data": "系统异常"
     * }
     */
    @RequestMapping(value = "search", method = RequestMethod.GET)
    public Object search(User user) {
        return user;
    }

}