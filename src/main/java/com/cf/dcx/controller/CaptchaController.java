package com.cf.dcx.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CaptchaController {

    @RequestMapping("captcha")
    public String captcha(Model model){

    model.addAttribute("recaptcha","구글리캡챠V3");

        return "captcha";
    }
}
