package com.sunrich.pam.pammsmasters.web.controller;

import com.sunrich.pam.pammsmasters.dto.BerthDTO;
import com.sunrich.pam.pammsmasters.service.BerthService;
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
@RequestMapping("/berth")
public class BerthController {

    @Autowired
    BerthService berthService;

    @PostMapping("/save")
    public ResponseEntity save(@Valid @RequestBody BerthDTO berthDTO) {
        BerthDTO dto = berthService.saveOrUpdate(berthDTO);
        return ResponseEntity.ok().body(dto);
    }

    @GetMapping("/findAll")
    public ResponseEntity findAll() {
        List<BerthDTO> berths = berthService.findAll();
        return ResponseEntity.ok().body(berths);
    }

    @GetMapping("/findById")
    public ResponseEntity findById(@RequestParam Long id) {
        BerthDTO berth = berthService.findById(id);
        return ResponseEntity.ok().body(berth);
    }

    @GetMapping("/delete")
    public ResponseEntity delete(@RequestParam Long id) {
        berthService.delete(id);
        return ResponseEntity.ok().body("Berth removed");
    }
}
