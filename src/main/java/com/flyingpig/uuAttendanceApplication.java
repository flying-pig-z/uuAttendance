package com.flyingpig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class uuAttendanceApplication {
    public static void main(String[] args) {
        SpringApplication.run(uuAttendanceApplication.class, args);
    }

}
