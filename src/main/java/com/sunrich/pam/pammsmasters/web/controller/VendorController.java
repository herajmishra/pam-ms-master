package com.sunrich.pam.pammsmasters.web.controller;

import com.sunrich.pam.pammsmasters.dto.VendorDTO;
import com.sunrich.pam.pammsmasters.service.VendorService;
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
@RequestMapping(path = "/vendor")
public class VendorController {

    @Autowired
    VendorService vendorService;

    @PostMapping("/save")
    public ResponseEntity save(@Valid @RequestBody VendorDTO vendorDTO) {
        return ResponseEntity.ok().body(vendorService.saveOrUpdate(vendorDTO));
    }

    @GetMapping("/findAll")
    public ResponseEntity findAll() {
        List<VendorDTO> vendorList = vendorService.findAll();
        return ResponseEntity.ok().body(vendorList);
    }

    @GetMapping("/findById")
    public ResponseEntity findById(@RequestParam Long id) {
        VendorDTO vendor = vendorService.findById(id);
        return ResponseEntity.ok().body(vendor);
    }

    @GetMapping("/findByName")
    public ResponseEntity findByName(@RequestParam String name) {
        VendorDTO vendor = vendorService.findByName(name);
        return ResponseEntity.ok().body(vendor);
    }

    @GetMapping("/delete")
    public ResponseEntity delete(@RequestParam Long id) {
        vendorService.delete(id);
        return ResponseEntity.ok().body("Vendor removed");
    }
}
