package com.yasitha.test1.Controllers;

import com.yasitha.test1.DTO.PersonRegReq;
import com.yasitha.test1.Service.PersonRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
public class RegisterController {

    private final PersonRegistrationService personRegistration;

    @Autowired
    public RegisterController(PersonRegistrationService personRegistration) {
        this.personRegistration = personRegistration;
    }

    @RequestMapping(value = "/reg", method = RequestMethod.GET)
    public String loginPage() {
        return "register";
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    @RequestMapping(name ="reg_req", value = "/reg", method = RequestMethod.POST)
    public String registerReq(@RequestBody PersonRegReq regReq) {
        return personRegistration.registerUser(regReq);
    }
}