  	@Id
    @OneToOne
    @JoinColumns({
            @JoinColumn(name = "CASE_HUD_OFFICE__P", referencedColumnName = "CASE_HUD_OFFICE__P"),
            @JoinColumn(name = "CASE_NUM", referencedColumnName = "CASE_NUM")
    })
    private A8spt157 hudProperty;


	@Override
	public List<A8spt191> searchForCases(String caseNumber, String earnestMoney) {
		//System.out.println("case# = " + caseNumber + "earnest money = " + earnestMoney);
		Criteria criteria = samsSessionFactory.getCurrentSession().createCriteria(A8spt191.class, "A8spt191");
		if(caseNumber != "" && caseNumber != " ") {
			//criteria.add(Restrictions.eq("caseNum", caseNumber));
			criteria.createAlias("A8spt191.hudProperty", "hudProperty");
			criteria.add(Restrictions.eq("hudProperty.caseHudOfficeP", caseNumber.substring(0,3)));
			criteria.add(Restrictions.eq("hudProperty.caseNum", caseNumber.substring(4)));
		}
		if(earnestMoney != "" && caseNumber != " ") {
			criteria.add(Restrictions.eq("earnestMoneyDepo", new BigDecimal(earnestMoney)));
		}
		criteria.setMaxResults(MAX);
		//criteria.addOrder(Order.desc("caseNum"));
		return criteria.list();
	}