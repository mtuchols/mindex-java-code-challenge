package com.mindex.challenge.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;

@RestController
public class ReportingStructureController {
    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureController.class);

    @Autowired
    private ReportingStructureService ReportingStructureService;

    @GetMapping("/reportingstructure/{id}")
    public ReportingStructure read(@PathVariable String id) {
        LOG.debug("Received reportingStructure get request for id [{}]", id);
        return ReportingStructureService.read(id);
    }
}
