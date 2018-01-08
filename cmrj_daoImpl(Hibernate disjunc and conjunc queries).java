    @Override
	public List<A8spt295> loadCmrj(String office) {

		Criteria criteria = samsSessionFactory.getCurrentSession().createCriteria(A8spt295.class);
		

//		add restrictions for HUD Office if not HQ
		if(!office.equals("HQ")) {
			Disjunction or = Restrictions.disjunction();
			A8spt276 a = naidServiceManager.getAreasHocId(office);
			List<LookupItem> list = naidServiceManager.getHocAreas(a.getHocId());
			for(LookupItem item : list) {
				SimpleExpression restriction = Restrictions.like("currentHudOffice", item.getCode());
				or.add(restriction);
			}
			criteria.add(or);
		}
		return criteria.list();
	}

	@Override
	public void updateCmrj(CMRJObject cmrjFormObj) {
		String hql;		
		Query query;
		List<A8spt295> my295List = cmrjFormObj.getListOf295();
		List<A8spt295> my295DeleteList = new ArrayList<>();
		List<String> myRevitFlagList = cmrjFormObj.getRevitFlag();
		LocalDateTime now = LocalDateTime.now();
		for(int i = 0; i < myRevitFlagList.size(); i++) {
			String flag = myRevitFlagList.get(i);
			if(flag.equals("Y") || flag.equals("N")) {
				hql =   "Update A8spt157 set "
						+"revitalizationAre = :revitArea, dateUpdated = :dateUpdated, "
						+"timeUpdated = :timeUpdated, updatedBy = :updatedBy, "
						+"txnSeqNbr = :txnSeqNbr, terminalId = :terminalId "
						+"where caseHudOfficeP = :caseHudOfficeP and caseNum = :caseNum";							
				query = samsSessionFactory.getCurrentSession().createQuery(hql);
				query.setParameter("revitArea", flag);
		        query.setParameter("dateUpdated", SamsConstants.DATE_UPDATED_FORMAT.format(now));
		        query.setParameter("timeUpdated", SamsConstants.TIME_UPDATED_FORMAT.format(now));
		        query.setParameter("updatedBy", cmrjFormObj.getUpdatedBy());
		        query.setParameter("txnSeqNbr", Short.valueOf("0"));
		        query.setParameter("terminalId", "SAMS");
		        query.setParameter("caseHudOfficeP", my295List.get(i).getCaseHudOfficeP());
		        query.setParameter("caseNum", my295List.get(i).getCaseNum());
		        try {
		        	query.executeUpdate();
		        	my295DeleteList.add(my295List.get(i));
		        } catch (Exception e) {
					 throw(e);
				}
		        
			}
				
		}
		if(my295DeleteList.size() > 0) {
			deleteCmrj(my295DeleteList);
		}
	}

	@Override
	public void deleteCmrj(List<A8spt295> my295DeleteList) {
        String hql;
		Query query;
		for(A8spt295 caseToDelete : my295DeleteList) {
			hql = "Delete from A8spt295 where caseHudOfficeP = :caseHudOfficeP and caseNum = :caseNum";
			query = samsSessionFactory.getCurrentSession().createQuery(hql);
	        query.setParameter("caseHudOfficeP", caseToDelete.getCaseHudOfficeP());
	        query.setParameter("caseNum", caseToDelete.getCaseNum());
	        try {
	        	query.executeUpdate();
	        } catch (Exception e) {
				 throw(e);
			}
		}
	}

	@Override
	public List<A8spt157> loadCmrj157(List<A8spt295> list) {
		Criteria criteria = samsSessionFactory.getCurrentSession().createCriteria(A8spt157.class);
		
		Disjunction or = Restrictions.disjunction();
//		add restrictions for HUD Office if not HQ
		for(A8spt295 case295 : list) {
			
			Conjunction and = Restrictions.conjunction();

			SimpleExpression restriction = Restrictions.like("caseHudOfficeP", case295.getCaseHudOfficeP());
			SimpleExpression restriction2 = Restrictions.like("caseNum", case295.getCaseNum());
			//SimpleExpression restriction3 = Restrictions.like("currentHudOffice", case295.getCurrentHudOffice());
			and.add(restriction);
			and.add(restriction2);
			//and.add(restriction3);
			or.add(and);
						
		}
		criteria.add(or);
		return criteria.list();
	}