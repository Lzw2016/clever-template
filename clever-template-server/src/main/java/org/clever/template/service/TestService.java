package org.clever.template.service;

import org.clever.template.mapper.PermissionMapper;
import org.clever.template.model.NextId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 作者：lizw <br/>
 * 创建时间：2019/12/20 15:25 <br/>
 */
@Transactional(readOnly = true)
@Service
public class TestService {
    @Autowired
    private PermissionMapper permissionMapper;

    @Transactional(propagation = Propagation.NEVER)
    public Long nextId(String companyFlag, String businessType, String prefix, Long step) {
        NextId nextId = new NextId();
        nextId.setCompanyFlag(companyFlag);
        nextId.setBusinessType(businessType);
        nextId.setPrefix(prefix);
        nextId.setStep(step);
        permissionMapper.nextId(nextId);
        return nextId.getCurrentValue();
    }

    @Transactional
    public void updateCurrentValue(String companyFlag, String businessType, String prefix, Long step) {
        permissionMapper.updateCurrentValue(companyFlag, businessType, prefix, step);
    }
}
