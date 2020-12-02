package com.askme.askmespring;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.logging.Logger;

@Controller
public class LoginController {
    private static final Logger log = Logger.getLogger(LoginController.class.getName());

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public ModelAndView login(@RequestBody JSONObject userAuth) {

        log.info("fsdfasdfasd");
        String token = userAuth.get("access_token").toString();

        ModelMap map=new ModelMap();
        map.put("token", token);
        log.info("sending to message" + token);

        return new ModelAndView("login",map);
    }

    @RequestMapping("/")
    public String index(ModelMap map) {
        // 加入一个属性，用来在模板中读取
        map.addAttribute("host", "Spring test");
        // return模板文件的名称，对应src/main/resources/templates/index.html
        return "index";
    }


}
