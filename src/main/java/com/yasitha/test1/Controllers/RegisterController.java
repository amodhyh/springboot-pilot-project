package com.yasitha.test1.Controllers;

import com.yasitha.test1.DTO.PersonRegReq;
import com.yasitha.test1.Service.PersonRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
public class RegisterController {

    private final PersonRegistrationService personRegistration;

    @Autowired
    public RegisterController(PersonRegistrationService personRegistration) {
        this.personRegistration = personRegistration;
    }


    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }
//controllers 
    @RequestMapping( value = "/reg", method = RequestMethod.POST)
    public ResponseEntity<Map<String,String>> registerReq(@RequestBody PersonRegReq regReq) {
        String message= personRegistration.registerUser(regReq);
        Map<String,String> map = new HashMap<>();
        map.put("message",message);
        return ResponseEntity.ok(map);

        // Return a 200 OK response with
        // the message (server successfully processed the request)
    }
}