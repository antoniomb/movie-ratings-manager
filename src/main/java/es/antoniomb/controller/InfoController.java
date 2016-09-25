package es.antoniomb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by amiranda on 25/9/16.
 */
@Controller
public class InfoController {

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    @ResponseBody
    String info(){
        return "OK";
    }
}
