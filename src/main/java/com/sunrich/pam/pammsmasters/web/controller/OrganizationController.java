package com.sunrich.pam.pammsmasters.web.controller;

import com.sunrich.pam.pammsmasters.dto.OrganizationDTO;
import com.sunrich.pam.pammsmasters.service.OrganizationService;
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
@Slf4j
@RestController
@RequestMapping(path = "/organization")
public class OrganizationController {

    @Autowired
    OrganizationService organizationService;

    @PostMapping("/save")
    public ResponseEntity save(@Valid @RequestBody OrganizationDTO payload) {
        return ResponseEntity.ok().body(organizationService.saveOrUpdate(payload));
    }

    @GetMapping("/findAll")
    public ResponseEntity findAll() {
        List<OrganizationDTO> organizationList = organizationService.findAll();
        return ResponseEntity.ok().body(organizationList);
    }

    @GetMapping("/findById")
    public ResponseEntity findById(@RequestParam Long id) {
        OrganizationDTO organization = organizationService.findById(id);
        return ResponseEntity.ok().body(organization);
    }

    @GetMapping("/findByName")
    public ResponseEntity findByName(@RequestParam String name) {
        OrganizationDTO organization = organizationService.findByName(name);
        return ResponseEntity.ok().body(organization);
    }

    @GetMapping("/delete")
    public ResponseEntity delete(@RequestParam Long id) {
        organizationService.delete(id);
        return ResponseEntity.ok().body("Organization removed");
    }
}
