package com.nammaexpo.vexpo;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class SampleController {

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public ResponseEntity<?> hello() {
        return ResponseEntity.ok(new String("hello world"));
    }
}
