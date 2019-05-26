package com.sunrich.pam.pammsmasters.web.controller;

import com.sunrich.pam.pammsmasters.dto.PortDTO;
import com.sunrich.pam.pammsmasters.exception.PortNotFoundException;
import com.sunrich.pam.pammsmasters.service.PortService;
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

@RestController
@Slf4j
@RequestMapping("/port")
public class PortController {

    @Autowired
    PortService portService;

    @PostMapping("/save")
    public ResponseEntity save(@Valid @RequestBody PortDTO port) throws PortNotFoundException {
        return ResponseEntity.ok().body(portService.saveOrUpdate(port));
    }

    @GetMapping("/findAll")
    public ResponseEntity findAll() {
        List<PortDTO> ports = portService.findAll();
        return ResponseEntity.ok().body(ports);
    }

    @GetMapping("/findById")
    public ResponseEntity findById(@RequestParam Long id) {
        PortDTO pm = portService.findById(id);
        return ResponseEntity.ok().body(pm);
    }

    @GetMapping("/delete")
    public ResponseEntity delete(@RequestParam Long id) {
        portService.delete(id);
        return ResponseEntity.ok().body("Port removed");
    }
}
