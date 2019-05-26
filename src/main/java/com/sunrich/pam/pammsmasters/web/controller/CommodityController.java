package com.sunrich.pam.pammsmasters.web.controller;

import com.sunrich.pam.pammsmasters.dto.CommodityDTO;
import com.sunrich.pam.pammsmasters.service.CommodityService;
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
 * Exposes crud operation endpoints for commodity
 */
@Slf4j
@RestController
@RequestMapping(path = "/commodity")
public class CommodityController {

    @Autowired
    CommodityService commodityService;

    @PostMapping("/save")
    public ResponseEntity save(@Valid @RequestBody CommodityDTO commodityDTO) {
        return ResponseEntity.ok().body(commodityService.saveOrUpdate(commodityDTO));
    }

    @GetMapping("/findAll")
    public ResponseEntity findAll() {
        List<CommodityDTO> commodityList = commodityService.findAll();
        return ResponseEntity.ok().body(commodityList);
    }

    @GetMapping("/findById")
    public ResponseEntity findById(@RequestParam Long id) {
        CommodityDTO commodity = commodityService.findById(id);
        return ResponseEntity.ok().body(commodity);
    }

    @GetMapping("/findByName")
    public ResponseEntity findByName(@RequestParam String name) {
        CommodityDTO commodity = commodityService.findByName(name);
        return ResponseEntity.ok().body(commodity);
    }

    @GetMapping("/delete")
    public ResponseEntity delete(@RequestParam Long id) {
        commodityService.delete(id);
        return ResponseEntity.ok().body("Commodity removed");
    }
}
