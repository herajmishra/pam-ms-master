package com.sunrich.pam.pammsmasters.web.controller;

import com.sunrich.pam.pammsmasters.dto.UserDTO;
import com.sunrich.pam.pammsmasters.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/save")
    public ResponseEntity save(@Valid @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok().body(userService.saveOrUpdate(userDTO));
    }

    @GetMapping("/findAll")
    public ResponseEntity findAll() {
        List<UserDTO> dtoList = userService.findAll();
        return ResponseEntity.ok().body(dtoList);
    }

    @GetMapping("/findById")
    public ResponseEntity findById(@RequestParam Long id) {
        UserDTO dto = userService.findById(id);
        return ResponseEntity.ok().body(dto);
    }

    @GetMapping("/findByName")
    public ResponseEntity findByName(@RequestParam String name) {
        UserDTO dto = userService.findByName(name);
        return ResponseEntity.ok().body(dto);
    }

    @GetMapping("/delete")
    public ResponseEntity delete(@RequestParam Long id) {
        userService.delete(id);
        return ResponseEntity.ok().body("User removed");
    }
}
