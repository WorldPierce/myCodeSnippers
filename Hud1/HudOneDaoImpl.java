package gov.hud.sams.dao.orm.hibernate;

import gov.hud.sams.dao.HudOneDAO;
import gov.hud.sams.bean.HudOneForm;
import gov.hud.sams.entities.A8spt028;
import gov.hud.sams.entities.A8spt148;
import gov.hud.sams.entities.A8spt190;
import gov.hud.sams.entities.A8spt191;
import gov.hud.sams.util.SamsConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class HudOneDAOImpl implements HudOneDAO {
    private Logger log = LogManager.getLogger(this.getClass());
    private static final int MAX = 1000;

    @Autowired
    @Qualifier(value = "samsSessionFactory")
    private SessionFactory samsSessionFactory;

/*  @Override
    public List<A8spt191> searchForCases(String caseNumber, String earnestMoney, String dateReconciledFrom, String dateReconciledTo, String dateClosedFrom, String dateClosedTo, String contractArea) {
    
        Criteria criteria = samsSessionFactory.getCurrentSession().createCriteria(A8spt191.class, "A8spt191");
        criteria.createAlias("A8spt191.hudProperty", "hudProperty");

        if(caseNumber != null && !"".equals(caseNumber.trim())) {
            criteria.add(Restrictions.eq("hudProperty.caseHudOfficeP", parseStringToCaseHudOfficeP(caseNumber)));
            criteria.add(Restrictions.eq("hudProperty.caseNum", parseStringToCaseNum(caseNumber)));
        }
        if(earnestMoney != null && !"".equals(earnestMoney.trim())) {
            criteria.add(Restrictions.eq("cashDueSettlemen", new BigDecimal(earnestMoney.replace(",", ""))));
        }
        if (contractArea != null  && !"null".equals(contractArea) &&  !"".equals(contractArea.trim())) {
            criteria.add(Restrictions.in("hudProperty.currentHudOffice", Arrays.asList(contractArea.split(","))));
        }
        if ((dateReconciledFrom != null  && !"".equals(dateReconciledFrom)) && (dateReconciledTo != null  && !"".equals(dateReconciledTo))) {
            criteria.add(Restrictions.between("dateReconciled", SamsConstants.DB_FORMAT.format(SamsConstants.JSP_FORMAT.parse(dateReconciledFrom)), SamsConstants.DB_FORMAT.format(SamsConstants.JSP_FORMAT.parse(dateReconciledTo))));
        } else if (dateReconciledFrom != null  && !"".equals(dateReconciledFrom)) {
            criteria.add(Restrictions.ge("dateReconciled", SamsConstants.DB_FORMAT.format(SamsConstants.JSP_FORMAT.parse(dateReconciledFrom))));
        } else if (dateReconciledTo != null  && !"".equals(dateReconciledTo)) {
            criteria.add(Restrictions.le("dateReconciled", SamsConstants.DB_FORMAT.format(SamsConstants.JSP_FORMAT.parse(dateReconciledTo))));
        }
        if ((dateClosedFrom != null  && !"undefined".equals(dateClosedFrom) && !"".equals(dateClosedFrom)) && (dateClosedTo != null  && !"undefined".equals(dateClosedTo) && !"".equals(dateClosedTo))) {
            criteria.add(Restrictions.between("dateClosed", SamsConstants.DB_FORMAT.format(SamsConstants.JSP_FORMAT.parse(dateClosedFrom)), SamsConstants.DB_FORMAT.format(SamsConstants.JSP_FORMAT.parse(dateClosedTo))));
        } else if (dateClosedFrom != null  && !"undefined".equals(dateClosedFrom) && !"".equals(dateClosedFrom)) {
            criteria.add(Restrictions.ge("dateClosed", SamsConstants.DB_FORMAT.format(SamsConstants.JSP_FORMAT.parse(dateClosedFrom))));
        } else if (dateClosedTo != null  && !"undefined".equals(dateClosedTo) && !"".equals(dateClosedTo)) {
            criteria.add(Restrictions.le("dateClosed", SamsConstants.DB_FORMAT.format(SamsConstants.JSP_FORMAT.parse(dateClosedTo))));
        }
        criteria.setMaxResults(MAX);
        //criteria.addOrder(Order.desc("caseNum"));
        return criteria.list();
    }*/
    
    @Override
    public List<Object[]> searchForCases(String caseNumber, String earnestMoney, String dateReconciledFrom, String dateReconciledTo, String dateClosedFrom, String dateClosedTo, String contractArea, String financeType, String titleId) {
        StringBuilder hql = new StringBuilder("select a.caseHudOfficeP, a.caseNum, b.address1, b.currentHudOffice, a.financeType, a.dateReceived, a.dateClosed, b.dateReconciled, a.cashDueSettlemen, b.city, b.stateCode, b.displayZipCode from A8spt191 a inner join A8spt157 b on a.caseNum = b.caseNum and a.caseHudOfficeP = b.caseHudOfficeP");
        boolean whereClause = false;
        Map<String,Object> paramMap = new HashMap<String, Object>();
        
        if(titleId != null && !"".equals(titleId.trim())) {
            hql.append(" inner join A8spt190 c on a.caseNum = c.caseNum and a.caseHudOfficeP = c.caseHudOfficeP and b.bidReceiptNumber = c.bidReceiptNumber");
            whereClause = appendClause(hql, whereClause);
            hql.append(" c.titleId = :titleId");
            paramMap.put("titleId",  titleId);
        }
        if(financeType != null && !"".equals(financeType.trim())) {
            whereClause = appendClause(hql, whereClause);
            hql.append(" a.financeType = :financeType");
            paramMap.put("financeType",  financeType);
        }
        if(caseNumber != null && !"".equals(caseNumber.trim())) {
            whereClause = appendClause(hql, whereClause);
            hql.append(" a.caseNum = :caseNum and a.caseHudOfficeP = :caseHudOfficeP");
            paramMap.put("caseNum",parseStringToCaseNum(caseNumber));
            paramMap.put("caseHudOfficeP",parseStringToCaseHudOfficeP(caseNumber));
        }
        if(earnestMoney != null && !"".equals(earnestMoney.trim())) {
            whereClause = appendClause(hql, whereClause);
            hql.append(" a.cashDueSettlemen = :cashDueSettlemen");
            paramMap.put("cashDueSettlemen",  new BigDecimal(earnestMoney.replace(",", "")));
        }
        if (contractArea != null  && !"null".equals(contractArea) &&  !"".equals(contractArea.trim())) {
            whereClause = appendClause(hql, whereClause);
            hql.append(" b.currentHudOffice in (:currentHudOffice)");
            paramMap.put("currentHudOffice", Arrays.asList(contractArea.split(",")));
        }
        if ((dateReconciledFrom != null  && !"".equals(dateReconciledFrom)) && (dateReconciledTo != null  && !"".equals(dateReconciledTo))) {
            whereClause = appendClause(hql, whereClause);
            hql.append(" b.dateReconciled >= :dateReconciledFrom");
            hql.append(" and b.dateReconciled <= :dateReconciledTo");
            paramMap.put("dateReconciledFrom", SamsConstants.DB_FORMAT.format(SamsConstants.JSP_FORMAT.parse(dateReconciledFrom)));
            paramMap.put("dateReconciledTo", SamsConstants.DB_FORMAT.format(SamsConstants.JSP_FORMAT.parse(dateReconciledTo)));
        } else if (dateReconciledFrom != null  && !"".equals(dateReconciledFrom)) {
            whereClause = appendClause(hql, whereClause);
            hql.append(" b.dateReconciled >= :dateReconciledFrom");
            paramMap.put("dateReconciledFrom", SamsConstants.DB_FORMAT.format(SamsConstants.JSP_FORMAT.parse(dateReconciledFrom)));
        } else if (dateReconciledTo != null  && !"".equals(dateReconciledTo)) {
            whereClause = appendClause(hql, whereClause);
            hql.append(" b.dateReconciled <= :dateReconciledTo");
            paramMap.put("dateReconciledTo", SamsConstants.DB_FORMAT.format(SamsConstants.JSP_FORMAT.parse(dateReconciledTo)));
        }
        if ((dateClosedFrom != null  && !"undefined".equals(dateClosedFrom) && !"".equals(dateClosedFrom)) && (dateClosedTo != null  && !"undefined".equals(dateClosedTo) && !"".equals(dateClosedTo))) {
            whereClause = appendClause(hql, whereClause);
            hql.append(" a.dateClosed >= :dateClosedFrom");
            hql.append(" and a.dateClosed <= :dateClosedTo");
            paramMap.put("dateClosedFrom", SamsConstants.DB_FORMAT.format(SamsConstants.JSP_FORMAT.parse(dateClosedFrom)));
            paramMap.put("dateClosedTo", SamsConstants.DB_FORMAT.format(SamsConstants.JSP_FORMAT.parse(dateClosedTo)));
        } else if (dateClosedFrom != null  && !"undefined".equals(dateClosedFrom) && !"".equals(dateClosedFrom)) {
            whereClause = appendClause(hql, whereClause);
            hql.append(" a.dateClosed >= :dateClosedFrom");
            paramMap.put("dateClosedFrom", SamsConstants.DB_FORMAT.format(SamsConstants.JSP_FORMAT.parse(dateClosedFrom)));
        } else if (dateClosedTo != null  && !"undefined".equals(dateClosedTo) && !"".equals(dateClosedTo)) {
            whereClause = appendClause(hql, whereClause);
            hql.append(" a.dateClosed <= :dateClosedTo");
            paramMap.put("dateClosedTo", SamsConstants.DB_FORMAT.format(SamsConstants.JSP_FORMAT.parse(dateClosedTo)));
        }
        Query query = samsSessionFactory.getCurrentSession().createQuery(hql.toString());
        for (String key : paramMap.keySet()){
            if("currentHudOffice".equals(key)) {
                query.setParameterList(key, (List) paramMap.get(key));
            } else {
                query.setParameter(key, paramMap.get(key));
            }
 
        }
        query.setMaxResults(MAX);       
        return query.list();
    }

    private boolean appendClause(StringBuilder hql, boolean whereClause) {
        if(!whereClause) {
            hql.append(" where");
        }
        else {
            hql.append(" and");
        }
        return true;
    }

    @Override
    public A8spt191 getCase(String caseNumber) {
        Criteria criteria = samsSessionFactory.getCurrentSession().createCriteria(A8spt191.class);
        //criteria.createAlias("A8spt191.hudProperty", "hudProperty");
        criteria.add(Restrictions.eq("caseHudOfficeP", parseStringToCaseHudOfficeP(caseNumber)));
        criteria.add(Restrictions.eq("caseNum", parseStringToCaseNum(caseNumber)));
        return (A8spt191) criteria.uniqueResult();
        
    }
    
    @Override
    public A8spt190 getSalesOffer(String caseNumber, Integer bidReceiptNumber) {
        Criteria criteria = samsSessionFactory.getCurrentSession().createCriteria(A8spt190.class);
        criteria.add(Restrictions.eq("caseHudOfficeP", parseStringToCaseHudOfficeP(caseNumber)));
        criteria.add(Restrictions.eq("caseNum", parseStringToCaseNum(caseNumber)));
        criteria.add(Restrictions.eq("bidReceiptNumber", bidReceiptNumber));
        return (A8spt190) criteria.uniqueResult();
        
    }
    
    public String parseStringToCaseHudOfficeP (String caseNumber) {
        return caseNumber.substring(0, 3);
    }
    public String parseStringToCaseNum (String caseNumber) {
        return caseNumber.substring(4);
    }

    @Override
    public void reopenCase(String caseNumber, String userName) {
        /*
          update the step 9 record on table 148
          and update 157
          delete the step 10 148
        */
        String caseHudOfficeP = parseStringToCaseHudOfficeP(caseNumber);
        String caseNum = parseStringToCaseNum(caseNumber);
        LocalDateTime now = LocalDateTime.now();
        
        String beginDate = getBeginDate(caseHudOfficeP, caseNum);
        
        updateStepFor157(userName, caseHudOfficeP, caseNum, now, beginDate);
        
        updateStepFor148(userName, caseHudOfficeP, caseNum, now);
                
        deleteStep10From148(caseHudOfficeP, caseNum);
        
    }

    private String getBeginDate(String caseHudOfficeP, String caseNum) {
        Criteria criteria = samsSessionFactory.getCurrentSession().createCriteria(A8spt148.class);
        criteria.add(Restrictions.eq("caseHudOfficeP", caseHudOfficeP));
        criteria.add(Restrictions.eq("caseNum", caseNum));
        criteria.add(Restrictions.eq("caseStepNumber", Integer.valueOf(9)));
        criteria.setProjection(Projections.property("beginDate"));
        return (String) criteria.list().get(0);
    }

    private void deleteStep10From148(String caseHudOfficeP, String caseNum) {
                
        StringBuilder hql = new StringBuilder("delete from A8spt148 where caseNum = :caseNum and caseHudOfficeP = :caseHudOfficeP and caseStepNumber = :ten");
        Query query = samsSessionFactory.getCurrentSession().createQuery(hql.toString());
        query.setParameter("caseNum" , caseNum);
        query.setParameter("caseHudOfficeP", caseHudOfficeP);
        query.setParameter("ten", Integer.valueOf(10));
        query.executeUpdate();
    }

    private void updateStepFor148(String userName, String caseHudOfficeP, String caseNum, LocalDateTime now) {
        
        StringBuilder hql = new StringBuilder("update A8spt148 set timeUpdated = :timeUpdated, dateUpdated = :dateUpdated, updatedBy = :updatedBy where caseNum = :caseNum and caseHudOfficeP = :caseHudOfficeP and caseStepNumber = :nine");
        Query query = samsSessionFactory.getCurrentSession().createQuery(hql.toString());
        query.setParameter("caseNum" , caseNum);
        query.setParameter("caseHudOfficeP", caseHudOfficeP);
        query.setParameter("updatedBy", userName);
        query.setParameter("timeUpdated", SamsConstants.TIME_UPDATED_FORMAT.format(now));
        query.setParameter("dateUpdated", SamsConstants.DATE_UPDATED_FORMAT.format(now));
        query.setParameter("nine", Integer.valueOf(9));
        query.executeUpdate();
    }

    private void updateStepFor157(String userName, String caseHudOfficeP, String caseNum, LocalDateTime now, String beginDate) {

        StringBuilder hql = new StringBuilder("update A8spt157 set caseStepNumber = :nine, activeInventoryF = 'A', dateReconciled = '', date1 = :date1, timeUpdated = :timeUpdated, dateUpdated = :dateUpdated, updatedBy = :updatedBy where caseNum = :caseNum and caseHudOfficeP = :caseHudOfficeP");
        Query query = samsSessionFactory.getCurrentSession().createQuery(hql.toString());
        query.setParameter("updatedBy", userName);
        query.setParameter("date1", beginDate);
        query.setParameter("nine", Integer.valueOf(9));
        query.setParameter("timeUpdated", SamsConstants.TIME_UPDATED_FORMAT.format(now));
        query.setParameter("dateUpdated", SamsConstants.DATE_UPDATED_FORMAT.format(now));
        query.setParameter("caseNum" , caseNum);
        query.setParameter("caseHudOfficeP", caseHudOfficeP);
        query.executeUpdate();
    }

    @Override
    public BigDecimal getFedwireAmount(String caseNumber) {
        //StringBuilder hql =  new StringBuilder("select t1.sequenceAmount from A8spt028 t1 where (t1.batchNumber, t1.batchSequenceNum) in (select t2.batchNumber, t2.batchSequenceNum from A8spt226 t2 where t2.fedWireData like :caseNumber)");
        StringBuilder hql =  new StringBuilder("select t1.sequenceAmount from A8spt028 t1 inner join A8spt226 t2 on t1.batchNumber = t2.batchNumber and t1.batchSequenceNum = t2.batchSequenceNum where t2.fedWireData like :caseNumber");
        Query query = samsSessionFactory.getCurrentSession().createQuery(hql.toString());
        query.setParameter("caseNumber", "%" + caseNumber + "%");
        return (BigDecimal) query.uniqueResult();
    }

    @Override
    public A8spt028 getFedwireRecord(String caseNumber) {
        StringBuilder hql =  new StringBuilder("select t1 from A8spt028 t1 inner join A8spt226 t2 on t1.batchNumber = t2.batchNumber and t1.batchSequenceNum = t2.batchSequenceNum where t2.fedWireData like :caseNumber");
        Query query = samsSessionFactory.getCurrentSession().createQuery(hql.toString());
        query.setParameter("caseNumber", "%" + caseNumber + "%");
        return (A8spt028) query.uniqueResult();
    }

    @Override
    public void updateHudOne(HudOneForm modifyHudOne) {
        samsSessionFactory.getCurrentSession().update(modifyHudOne.getA8spt191());
        //samsSessionFactory.getCurrentSession().update(modifyHudOne.getA8spt190());
        //samsSessionFactory.getCurrentSession().update(modifyHudOne.getA8spt028());
        
    }



}
