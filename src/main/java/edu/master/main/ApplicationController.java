package edu.master.main;

import org.apache.maven.model.Model;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/")
public class ApplicationController {


    @RequestMapping(method = RequestMethod.GET)
    public String printWelcome(ModelMap model) {
        model.addAttribute("message", "Hello world!");
        return "index";
    }

    @RequestMapping(value="/getValues", method = RequestMethod.POST)
    public @ResponseBody String getValues(Model model, @RequestParam(value = "output", required = true) String output, @RequestParam(value = "x0", required = true) String x0, @RequestParam(value = "y0", required = true) String y0, @RequestParam(value = "height", required = true) String height, @RequestParam(value = "sigX", required = true) String sigX, @RequestParam(value = "sigY", required = true) String sigY, @RequestParam(value = "sigZ", required = true) String sigZ, @RequestParam(value = "atmosphere", required = true) String atmosphere, @RequestParam(value = "temperature", required = true) String temperature) {
        
        return "success";
    }
}