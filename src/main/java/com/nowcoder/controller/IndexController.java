package com.nowcoder.controller;

import com.nowcoder.model.User;
import com.nowcoder.service.WendaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @Author: LiuHao
 * @Descirption:
 * @Date: 2018/8/26_17:48
 */
@Controller
@RequestMapping
public class IndexController {

    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);


    @Autowired
    private WendaService wendaService;//IOC依赖注入类似于享元模式，只要有人定义好了，大家都可以用

    @RequestMapping(path = {"/","/index"},method = RequestMethod.GET)
    @ResponseBody
    public String index(HttpServletRequest request,HttpSession httpSession){
        String path = request.getRequestURI();
        /**
         *  发现一个问题，只要路径前面拼对了，后面一个点随便填都能访问，
         *  例如：
         *  http://localhost:8080/.eqweqw.321312.
         *  http://localhost:8080/index.eqweqw.321312
         *  都能访问成功
         */
        logger.info("调用index方法,path:{}",path);
        return wendaService.getMessage(1) + "Hello NowCoder" + httpSession.getAttribute("msg");
    }

    @RequestMapping(path = {"/profile/{groupId}/{userId}"},method = RequestMethod.GET)
    @ResponseBody
    public String profile(@PathVariable("userId") int userId,
                          @PathVariable("groupId") String groupId,
                          @RequestParam(value = "type",defaultValue = "1") int type,
                          @RequestParam(value = "key",required = false) String key
                          ){
        /**
         * 访问示例：
         * http://localhost:8080/profile/liuhao/1?key=11&type=2
         * type可以不写例如：
         * http://localhost:8080/profile/liuhao/1?key=11
         * 写了defaultValue属性默认带一个required = false，访问示例：
         * http://localhost:8080/profile/liuhao/1
         */
        return String.format("groupId: %s,userId: %d,key: %s,type: %d",groupId,userId,key,type);
    }

    /**
     * Http Method
     * GET 获取数据
     * POST 写入数据
     * PUT  支持幂等性的post
     * DELETE  删除数据（一般用post代替delete，逻辑删除）
     * HEAD  紧急查看http请求头
     * OPTIONS 查看支持方法
     */


    /**
     * velocity模版
     */
    @RequestMapping(path = {"/vm"},method = {RequestMethod.GET})
    public String template(Model model){
        model.addAttribute("value1","value1");
        List<String> colors = Arrays.asList(new String[]{"RED", "GREEN", "BLUE"});
        model.addAttribute("colors", colors);
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < 4; ++i) {
            map.put(String.valueOf(i), String.valueOf(i * i));
        }
        model.addAttribute("map", map);
        model.addAttribute("user", new User("LEE"));
        return "home";
    }

    /**
     * velocity模版
     */
    @RequestMapping(path = {"/request"},method = {RequestMethod.GET})
    @ResponseBody
    public String request(Model model,
                          HttpServletRequest request,
                          HttpServletResponse response,
                          HttpSession httpSession,
                          @CookieValue("JSESSIONID") String sessionId) {

        StringBuilder sb = new StringBuilder();
        sb.append("COOKIEVALUE:" + sessionId + "<br>");

        sb.append("=====header.cookie=====<br>");
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            sb.append(name + ":" + request.getHeader(name) + "<br>");
        }

        sb.append("======request.cookie=======<br>");
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                sb.append("Cookie:" + cookie.getName() + " value:" + cookie.getValue()+"<br>");
            }
        }

        sb.append("======request=======<br>");
        sb.append(request.getMethod() + "<br>");
        sb.append(request.getQueryString() + "<br>");
        sb.append(request.getPathInfo() + "<br>");
        sb.append(request.getRequestURI() + "<br>");

        sb.append("======response=======<br>");
        response.addHeader("nowcoderId", "hello");
        response.addCookie(new Cookie("username", "nowcoder"));

        return sb.toString();
    }


    @RequestMapping(path = {"/redirect/{code}"}, method = {RequestMethod.GET})
    public RedirectView redirect(@PathVariable("code") int code,
                                 HttpSession httpSession) {
        httpSession.setAttribute("msg", "jump from redirect");
        RedirectView red = new RedirectView("/", true);
        if (code == 301) {
            red.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        }
        return  red;
    }


    /**
     * 处理spring未处理的异常
     * @param e
     * @return
     */
    @ExceptionHandler()
    @ResponseBody
    public String error(Exception e) {
        return "error:" + e.getMessage();
    }



}
