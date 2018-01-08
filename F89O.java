//AddTheNewJournalEntryDAO jeDao
//if data not needed change get method to doesExist
@Override
public void performF89O(String journalEntryId, HudOneForm form) {
	//DELETE CURRENT SALES RECEIVABLE
	A8spt005 a8spt005 = dao.get005(journalEntryId);
	A8spt182 a8spt182= dao.get182(a8spt005.getAccountsReceivabl);
	dao.get164(a8spt182); //get this to make sure it is not null?
	//call update 182 
	dao.update182(a8spt182);
	A8spt065 a8spt065 = dao.get065(a8spt005.getAccountsReceivabl);
	if(dao.get066(a8spt065.getCaseHuOfficeP, a8spt065.getCaseNum, a8spt065.bidReceiptNumber, a8spt065.extensionNumber) == null) {
		throw new ObjectNotFoundException();
	} //does exist
	dao.update065(a8spt065);
	A8spt040 a8spt040 = dao.get040(a8spt005.getAccountsReceivabl);
	//gets 191 but that is already in form
	//can probably combine update and get for 040
	dao.update040();
	dao.update142(); //nothing special about this class(update where accountsReceivabl = a8spt005.getAccountsReceivabl)
	A8spt107 a8spt107 = dao.get107(a8spt005.getAccountsReceivabl);
	if(dao.get108(a.getCaseHuOfficeP, a.getCaseNum, a8spt107.getLiquidatedDamages) == null) {
		throw new ObjectNotFoundException();
	} //does exist
	dao.update107();
	A8spt056 a8spt056 = dao.get056(a8spt005.getAccountsReceivabl);
	A8spt125 a8spt125 = dao.get125(a8spt005.getNaid);//on 056
	if(dao.get118(a8spt056.getMortageeNumber, a8spt056.getMortageeOffice) == null) {
		throw new ObjectNotFoundException();
	} //does exist
	if(dao.get106(a8spt056.getLockboxBatchNum, a8spt056.getLockboxBatchSeq) == null) {

	} //does exist
	dao.update056(a8spt056);
	//Collection amount on 045
	A8spt040 a8spt040 = dao.get040(a8spt005.getAccountsReceivabl);
	BigDecimal collectionAmount = a8spt040.getCollectionAmount();
	dao.delete045(a8spt040); //calls this with 045.getcollectionBatchN, 045.getCollectnBatchSeq, a8spt005.getAccountsReceivabl
	//Line 6851 call A8SPF05O 005.getPostCode
	//Create custom AddJournalEntryBean() constructor
	if(a8spt005.getPostCode != "EM") {
		//create journal entry
		//pass in 005.getPostcode, 045.collectionAmount
		AddJournalEntryBean jeBean = AddJournalEntryBean(a8spt005, collectionAmount);
		jeDao.insertNewJournalEntry(jeBean);
	}
	if(dao.get079(a8spt005.getNaid, a8spt005.getHudOffice) == null) {
		throw new ObjectNotFoundException();
	} //does exist
	if(dao.get097(journalEntryId) == null) {
		throw new ObjectNotFoundException();
	}
	//reads a 157 in cbl
	if(dao.get143(a8spt005.getPostCode) == null) {
		throw new ObjectNotFoundException();
	}
	
	dao.update005(a8spt005);
}

private A8spt164 get164(A8spt182 a) {
	criteria = samsSessionFactory.getCurrentSession().createCriteria(A8spt164.class);
	criteria.add(Restrictions.eq("caseHudOfficeP", a.getCaseHuOfficeP));
	criteria.add(Restrictions.eq("caseNum", a.getCaseNum));
	criteria.add(Restrictions.eq("unitNumber", a.getUnitNumber));
	criteria.add(Restrictions.eq("leaseNumber", a.getLeaseNumber));
	criteria.add(Restrictions.eq("number", a.getNumber));
	return (A8spt164) criteria.uniqueResult();
}

private A8spt005 get005(String journalEntryId) {

}

private A8spt182 get182(String accountsReceivabl) {

}

private A8spt164 get005(A8spt182 a8spt182) {

}


private void update182() {
	
}

public AddJournalEntryBean(A8spt005 a, BigDecimal collectionAmount) {
	this.jeCasePrefix = a.getCaseHudOfficeP();
	this.jeCaseNumber = a.getCaseNum();
	this.jePostCodes = [a.getPostcode, "UN"];
	this.jeJournalDetailAmounts = [collectionAmount, collectionAmount.negate()];
	this.jeAccountEvent = "RC";
	this.jeGeneralLedgerAmount = collectionAmount;
	this.jePostDate = " ";

}