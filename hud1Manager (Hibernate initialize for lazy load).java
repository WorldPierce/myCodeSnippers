package gov.hud.sams.service.cases;

import gov.hud.sams.dao.HudOneDAO;
import gov.hud.sams.entities.A8spt190;
import gov.hud.sams.entities.A8spt191;

import java.util.List;

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
	public List<A8spt191> searchForCases(String caseNumber, String earnestMoney, String dateReconciledFrom, String dateReconciledTo, String dateClosedFrom, String dateClosedTo, String contractArea) {
		return dao.searchForCases(caseNumber, earnestMoney, dateReconciledFrom, dateReconciledTo, dateClosedFrom, dateClosedTo, contractArea);
	}

	@Override
	public A8spt191 getCase(String caseNumber) {
		A8spt191 hudOneCase = dao.getCase(caseNumber);
		Hibernate.initialize(hudOneCase.getSalesPackageHistory());
		return hudOneCase;
		
	}

	@Override
	public A8spt190 getSalesOffer(String caseNumber) {
		return dao.getSalesOffer(caseNumber);
	}
}



@OneToMany(fetch = FetchType.LAZY)
    @JoinColumns(value = {
    		@JoinColumn(name = "CASE_HUD_OFFICE__P", referencedColumnName = "CASE_HUD_OFFICE__P"),
            @JoinColumn(name = "CASE_NUM", referencedColumnName = "CASE_NUM")
        })
    private List<A8spt248> salesPackageHistory;