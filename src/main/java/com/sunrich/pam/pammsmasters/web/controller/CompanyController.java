package com.sunrich.pam.pammsmasters.web.controller;

import com.sunrich.pam.pammsmasters.dto.CompanyDTO;
import com.sunrich.pam.pammsmasters.service.CompanyService;
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

/**
 * Exposes crud operation endpoints for organization
 */
@RestController
@Slf4j
@RequestMapping(path = "/company")
public class CompanyController {

    @Autowired
    CompanyService companyService;

    @PostMapping("/save")
    public ResponseEntity save(@RequestBody @Valid CompanyDTO payload) throws Exception {
        CompanyDTO companyDTO = companyService.saveOrUpdate(payload);
        return ResponseEntity.ok().body(companyDTO);
    }

    @GetMapping("/findAll")
    public ResponseEntity findAll() throws Exception {
        List<CompanyDTO> companies = companyService.findAll();
        return ResponseEntity.ok().body(companies);
    }

    @GetMapping("/findById")
    public ResponseEntity findById(@RequestParam Long id) throws Exception {
        CompanyDTO companyDTO = companyService.findById(id);
        return ResponseEntity.ok().body(companyDTO);
    }

    @GetMapping("/findByName")
    public ResponseEntity findByName(@RequestParam String name) throws Exception {
        CompanyDTO companyDTO = companyService.findByName(name);
        return ResponseEntity.ok().body(companyDTO);
    }

    @GetMapping("/delete")
    public ResponseEntity delete(@RequestParam Long id) {
        companyService.delete(id);
        return ResponseEntity.ok().body("Company removed");
    }
}
