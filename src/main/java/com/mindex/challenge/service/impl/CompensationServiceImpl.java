package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.service.CompensationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompensationServiceImpl implements CompensationService {

    private static final Logger LOG = LoggerFactory.getLogger(CompensationServiceImpl.class);
    
    @Autowired
    private CompensationRepository compensationRepository;
    
	@Override
	public Compensation create(Compensation compensation) {
        LOG.debug("Creating compensation [{}]", compensation);
        compensationRepository.insert(compensation);
        return compensation;
	}

	@Override
	public Compensation read(String employeeId) {
        LOG.debug("Finding compensation record for employeeId [{}]", employeeId);
        Compensation compensation = compensationRepository.findByEmployeeEmployeeId(employeeId);
        if (compensation == null ) {
            throw new RuntimeException("Error while attempting to find compensation record for employeeId: " + employeeId);
        }
		return compensation;
	}
}
