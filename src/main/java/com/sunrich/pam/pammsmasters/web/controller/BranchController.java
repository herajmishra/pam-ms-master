package com.sunrich.pam.pammsmasters.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunrich.pam.pammsmasters.domain.Branch;
import com.sunrich.pam.pammsmasters.service.BranchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * Exposes crud operation endpoints for branch
 */
@RestController
@Slf4j
@RequestMapping(path = "/branch")
public class BranchController {

    @Autowired
    BranchService branchService;

    @PostMapping("/save")
    public ResponseEntity save(@RequestBody String strBranch) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Branch branch = objectMapper.readValue(strBranch, Branch.class);
        return ResponseEntity.ok().body(branchService.saveOrUpdate(branch));
    }

    @GetMapping("/findAll")
    public ResponseEntity findAll() {
        List<Branch> branches = branchService.findAll();
        return ResponseEntity.ok().body(branches);
    }

    @GetMapping("/findById")
    public ResponseEntity findById(@RequestParam Long id) {
        Branch branch = branchService.findById(id);
        return ResponseEntity.ok().body(branch);
    }

    @GetMapping("/findByName")
    public ResponseEntity findByName(@RequestParam String name) {
        Branch branch = branchService.findByName(name);
        return ResponseEntity.ok().body(branch);
    }

    @GetMapping("/delete")
    public ResponseEntity delete(@RequestParam Long id) {
        branchService.delete(id);
        return ResponseEntity.ok().body("Branch removed");
    }
}
