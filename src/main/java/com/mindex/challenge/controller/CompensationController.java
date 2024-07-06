package com.mindex.challenge.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.service.CompensationService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class CompensationController {
    private static final Logger LOG = LoggerFactory.getLogger(CompensationController.class);

    @Autowired
    private CompensationService compensationService;
    
    @PostMapping("/compensation")
    public Compensation create(@RequestBody Compensation compensation) {
        LOG.debug("Received compensation post request for [{}]", compensation);
        return compensationService.create(compensation);
    }

    @GetMapping("/compensation/{employeeId}")
    public Compensation read(@PathVariable String employeeId) {
        LOG.debug("Received compensation get request for id [{}]", employeeId);
        return compensationService.read(employeeId);
    }

}
