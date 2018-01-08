package gov.hud.sams.service.cases;

import gov.hud.sams.bean.HudOneForm;
import gov.hud.sams.dao.HudOneDAO;
import gov.hud.sams.entities.A8spt028;
import gov.hud.sams.entities.A8spt190;
import gov.hud.sams.entities.A8spt191;
import gov.hud.sams.entities.A8spt248;
import gov.hud.sams.util.ErrorCodeEnum;
import gov.hud.sams.util.SamsConstants;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional("samsTransactionManager")
public class HudOneManagerImpl implements HudOneManager {
    @Autowired
    private HudOneDAO dao;

    @Override
    public List<Object[]> searchForCases(String caseNumber, String earnestMoney, String dateReconciledFrom, String dateReconciledTo, String dateClosedFrom, String dateClosedTo, String contractArea, String financeType, String titleId) {
        List<Object[]> searchCases = dao.searchForCases(caseNumber, earnestMoney, dateReconciledFrom, dateReconciledTo, dateClosedFrom, dateClosedTo, contractArea, financeType, titleId);
        /*for(A8spt191 myCase : searchCases) {
            Hibernate.initialize(myCase.getSalesPackageHistory());
        }   */
        return searchCases;
    }

    @Override
    public A8spt191 getCase(String caseNumber) {
        A8spt191 hudOneCase = dao.getCase(caseNumber);
        Hibernate.initialize(hudOneCase.getHudProperty());
        Hibernate.initialize(hudOneCase.getSalesPackageHistory());
        Hibernate.initialize(hudOneCase.getSalesExtension());    
        return hudOneCase;
        
    }
    
    @Override
    public A8spt191 getCaseToModify(String caseNumber) {
        A8spt191 hudOneCase = dao.getCase(caseNumber);
        Hibernate.initialize(hudOneCase.getHudProperty());
        Hibernate.initialize(hudOneCase.getSalesPackageHistory());
        Hibernate.initialize(hudOneCase.getSalesExtension());
        return hudOneCase;
        
    }

    @Override
    public A8spt190 getSalesOffer(String caseNumber, Integer bidReceiptNumber) {
        return dao.getSalesOffer(caseNumber, bidReceiptNumber);
    }

    @Override
    public void reopenCase(String caseNumber, String userName) {
        dao.reopenCase(caseNumber, userName);
        
    }

    @Override
    public BigDecimal getFedwireAmount(String caseNumber) {
        return dao.getFedwireAmount(caseNumber);
    }

    @Override
    public A8spt028 getFedwireRecord(String caseNumber) {
        return dao.getFedwireRecord(caseNumber);
    }

    @Override
    public void updateHudOne(HudOneForm modifyHudOne) {
        
        //TODO check if difference is in or out of range (-100, 10)

        dao.updateHudOne(modifyHudOne);
        
    }



}
package gov.hud.sams.service.cases;

import gov.hud.sams.bean.HudOneForm;
import gov.hud.sams.dao.HudOneDAO;
import gov.hud.sams.entities.A8spt028;
import gov.hud.sams.entities.A8spt190;
import gov.hud.sams.entities.A8spt191;
import gov.hud.sams.entities.A8spt248;
import gov.hud.sams.util.ErrorCodeEnum;
import gov.hud.sams.util.SamsConstants;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional("samsTransactionManager")
public class HudOneManagerImpl implements HudOneManager {
    @Autowired
    private HudOneDAO dao;

    @Override
    public List<Object[]> searchForCases(String caseNumber, String earnestMoney, String dateReconciledFrom, String dateReconciledTo, String dateClosedFrom, String dateClosedTo, String contractArea, String financeType, String titleId) {
        List<Object[]> searchCases = dao.searchForCases(caseNumber, earnestMoney, dateReconciledFrom, dateReconciledTo, dateClosedFrom, dateClosedTo, contractArea, financeType, titleId);
        /*for(A8spt191 myCase : searchCases) {
            Hibernate.initialize(myCase.getSalesPackageHistory());
        }   */
        return searchCases;
    }

    @Override
    public A8spt191 getCase(String caseNumber) {
        A8spt191 hudOneCase = dao.getCase(caseNumber);
        Hibernate.initialize(hudOneCase.getHudProperty());
        Hibernate.initialize(hudOneCase.getSalesPackageHistory());
        Hibernate.initialize(hudOneCase.getSalesExtension());    
        return hudOneCase;
        
    }
    
    @Override
    public A8spt191 getCaseToModify(String caseNumber) {
        A8spt191 hudOneCase = dao.getCase(caseNumber);
        Hibernate.initialize(hudOneCase.getHudProperty());
        Hibernate.initialize(hudOneCase.getSalesPackageHistory());
        Hibernate.initialize(hudOneCase.getSalesExtension());
        return hudOneCase;
        
    }

    @Override
    public A8spt190 getSalesOffer(String caseNumber, Integer bidReceiptNumber) {
        return dao.getSalesOffer(caseNumber, bidReceiptNumber);
    }

    @Override
    public void reopenCase(String caseNumber, String userName) {
        dao.reopenCase(caseNumber, userName);
        
    }

    @Override
    public BigDecimal getFedwireAmount(String caseNumber) {
        return dao.getFedwireAmount(caseNumber);
    }

    @Override
    public A8spt028 getFedwireRecord(String caseNumber) {
        return dao.getFedwireRecord(caseNumber);
    }

    @Override
    public void updateHudOne(HudOneForm modifyHudOne) {
        
        //TODO check if difference is in or out of range (-100, 10)

        dao.updateHudOne(modifyHudOne);
        
    }



}
