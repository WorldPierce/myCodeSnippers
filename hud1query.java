@Override
	public List<Object[]> searchForCases(String caseNumber, String earnestMoney, String dateReconciledFrom, String dateReconciledTo, String dateClosedFrom, String dateClosedTo, String contractArea) {
		StringBuilder hql = new StringBuilder("select a.caseHudOfficeP, a.caseNum, b.address1, a.financeType, a.dateReceived, a.dateClosed, b.dateReconciled, a.cashDueSettlemen from A8spt191 a inner join A8spt157 b on a.caseNum = b.caseNum and a.caseHudOfficeP = b.caseHudOfficeP");
		boolean whereClause = false;
		Map<String,Object> paramMap = new HashMap<String, Object>();
		
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