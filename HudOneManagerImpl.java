package gov.hud.sams.service.cases;

import gov.hud.sams.bean.AddJournalEntryBean;
import gov.hud.sams.bean.HudOneForm;
import gov.hud.sams.bean.TotalEarnestMoneyBean;
import gov.hud.sams.bean.ReverseJournalEntryBean;
import gov.hud.sams.dao.HudOneDAO;
import gov.hud.sams.dao.orm.hibernate.AddTheNewJournalEntryDAOImpl;
import gov.hud.sams.dao.orm.hibernate.ReverseTheOldJournalEntryDAOImpl;
import gov.hud.sams.entities.*;
import gov.hud.sams.exceptions.*;
import gov.hud.sams.util.BigDecimalUtils;
import gov.hud.sams.util.SamsConstants;

import org.hibernate.Hibernate;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static gov.hud.sams.util.BigDecimalUtils.isBetween;
import static gov.hud.sams.util.BigDecimalUtils.isEqualToZero;
import static gov.hud.sams.util.SamsConstants.*;

@Service
@Transactional("samsTransactionManager")
public class HudOneManagerImpl implements HudOneManager {
    @Autowired
    private HudOneDAO dao;

    @Autowired
	private AddTheNewJournalEntryDAOImpl jeDao;

	@Autowired
	private ReverseTheOldJournalEntryDAOImpl reverseDao;

	private static BigDecimal eleven = BigDecimal.valueOf(11);
	private static BigDecimal oneHundredAndOne = BigDecimal.valueOf(101);

	@Override
	public List<Object[]> searchForCases(String caseNumber, String earnestMoney, String dateReconciledFrom, String dateReconciledTo, String dateClosedFrom, String dateClosedTo, String contractArea, String financeType, String titleId) {
		List<Object[]> searchCases = dao.searchForCases(caseNumber, earnestMoney, dateReconciledFrom, dateReconciledTo, dateClosedFrom, dateClosedTo, contractArea, financeType, titleId);
		/*for(A8spt191 myCase : searchCases) {
			Hibernate.initialize(myCase.getSalesPackageHistory());
		}	*/
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
	public void update191And194(HudOneForm form, Boolean dateClosed) {
		dao.update191And194(form, dateClosed);
	}

	@Override
	public void reconcileHudOne(HudOneForm form) throws Exception {
		//Reconcile with no regard to differences in amounts
		if (Arrays.asList("Y", "N").contains(form.getA8spt191().getReconcileWithinT())) {
			//A8SPF79O
			attemptToReconcile(form);
		} else {
			//Should not have gotten this far with no flag set, prompt user to select Y or N
			throw new InvalidReconcileValueException(form.getA8spt191().getReconcileWithinT());
		}

		//A8SPF04O A8SPFJQO A8SPC4O
		dao.create148AndUpdate157(form, form.getA8spt191().getUpdatedBy());
	}


	private void attemptToReconcile(HudOneForm form) throws Exception {
		//Call A8SPF1ZO  Select * from 66 order by case prefix asc, case number asc, bid receipt asc, extension number desc
		BigDecimal brokerCommission = form.getA8spt191().getSettlementCharge().subtract(form.getA8spt191().getBrokerEarlyClsng()).subtract(form.getA8spt191().getClosingAgentFee());
		BigDecimal brokerBonusAmount = form.getA8spt191().getSettlementCharge().subtract(form.getA8spt191().getSellingBrokerCom()).subtract(form.getA8spt191().getClosingAgentFee());
		BigDecimal closingAgentAmount = form.getA8spt191().getSettlementCharge().subtract(form.getA8spt191().getBrokerEarlyClsng()).subtract(form.getA8spt191().getSellingBrokerCom());

		//Make sure our money
		if (form.getA8spt191().getSellingBrokerCom().compareTo(brokerCommission) > 0) {
			throw new BrokerCommissionExceededException();
		}

		if (form.getA8spt191().getBrokerEarlyClsng().compareTo(brokerBonusAmount) > 0) {
			throw new BrokerCommissionExceededException();
		}

		if (form.getA8spt191().getClosingAgentFee().compareTo(closingAgentAmount) > 0) {
			throw new ClosingAgentFeeExceededException();
		}

		//F79 if Active Seals offer not found, don't allow reconcile


		A8spt097 journalEntry = dao.getJournalEntry(form.getA8spt191().getCaseHudOfficeP(), form.getA8spt191().getCaseNum());

		// Compute Sum of Postings
		BigDecimal eeBalance = journalEntry.getA8spt096List().stream().filter(j -> POST_CODE_EE.equals(j.getPostCode())).map(A8spt096::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
		BigDecimal epBalance = journalEntry.getA8spt096List().stream().filter(j -> POST_CODE_EP.equals(j.getPostCode())).map(A8spt096::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
		BigDecimal emBalance = journalEntry.getA8spt096List().stream().filter(j -> POST_CODE_EM.equals(j.getPostCode())).map(A8spt096::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
		BigDecimal accountingPostingSum = journalEntry.getA8spt096List().stream().filter(j -> POST_CODES_ACCOUNTING_EVENTS.contains(j.getAccountingEvent())).map(A8spt096::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

		if (isEqualToZero(eeBalance)) {
			reconcileEvenly(form, epBalance, emBalance);
		} else {
			reconcileUnevenly(form, eeBalance, epBalance, emBalance);
		}
	}

	private void reconcileUnevenly(HudOneForm form, BigDecimal eeBalance, BigDecimal epBalance, BigDecimal emBalance) throws ReconcileOutOfRangeException, ReconcileFlagNotConfirmedException, InsertJournalEntryException {
		boolean userApprovedToReconcile = "Y".equals(form.getA8spt191().getReconcileWithinT()) ? true : false;
		BigDecimal actualEmee;
		BigDecimal negativeEe;
		BigDecimal absoluteEe;
		BigDecimal absoluteEmee;
		BigDecimal negativeEmee;
		actualEmee = eeBalance.add(emBalance).add(epBalance);
		negativeEe = eeBalance.abs().negate();
		absoluteEe = negativeEe.negate();
		absoluteEmee = actualEmee.abs();
		negativeEmee = absoluteEmee.negate();
		BigDecimal cashAtSettlement = form.getA8spt191().getCashDueSettlemen();
		BigDecimal displayCash = emBalance.negate();

		//Create Journal Entry if equal to 0
		if (isEqualToZero(actualEmee)) {
            //Call A8SPF05O - A8SPF79O - LINE 3387
            createJournalEntry(form, absoluteEe, new BigDecimal[] {eeBalance, negativeEe}, new String[] {POST_CODE_EP, POST_CODE_EE}, ACCOUNTING_EVENT_H1);
            //Create Journal Entry if between (0 and -101) or between (0 and 11)
        } else if (isBetween(actualEmee, BigDecimal.ZERO, oneHundredAndOne.negate()) || isBetween(actualEmee, eleven, BigDecimal.ZERO)) {
            if (userApprovedToReconcile) {
                //Call A8SPF05O - A8SPF79O - LINE - 3430
                createJournalEntry(form, absoluteEe, new BigDecimal[] {eeBalance, negativeEe}, new String[] {POST_CODE_EP, POST_CODE_EE}, ACCOUNTING_EVENT_H1);
            } else {
                throw new ReconcileFlagNotConfirmedException();
            }
        } else {
			throw new ReconcileOutOfRangeException(cashAtSettlement.abs(), displayCash.abs(), cashAtSettlement.abs().subtract(displayCash.abs()).abs());
        }

        //Create Journal Entry if Between (0 and -1)
		if (isBetween(actualEmee, BigDecimal.ZERO, BigDecimal.ONE.negate())) {
            //CALL A8SPF05O - A8SPF79O - LINE 3566
            createJournalEntry(form, absoluteEmee, new BigDecimal[] {actualEmee, absoluteEmee}, new String[] {POST_CODE_EW, POST_CODE_EP}, ACCOUNTING_EVENT_H1);
        }

        //Create Journal Entry if Between (0 and 11)
		if (isBetween(actualEmee, eleven, BigDecimal.ZERO)) {
            //CALL A8SPF05O - A8SPF79O - LINE 3604
            createJournalEntry(form, absoluteEmee, new BigDecimal[] {absoluteEmee, negativeEmee}, new String[] {POST_CODE_DW, POST_CODE_EP}, ACCOUNTING_EVENT_H1);
        }
	}

	private void reconcileEvenly(HudOneForm form, BigDecimal epBalance, BigDecimal emBalance) throws ReconcileOutOfRangeException, ReconcileFlagNotConfirmedException, InsertJournalEntryException {
		boolean userApprovedToReconcile = "Y".equals(form.getA8spt191().getReconcileWithinT()) ? true : false;
		BigDecimal actualEmep;
		BigDecimal absoluteEmep;
		BigDecimal negativeEmep;
		actualEmep = epBalance.add(emBalance);
		absoluteEmep = actualEmep.abs();
		negativeEmep = absoluteEmep.negate();
		BigDecimal cashAtSettlement = form.getA8spt191().getCashDueSettlemen();
		BigDecimal displayCash = emBalance.negate();

		//In the event that we are reconciling evenly, we don't need to create journal entries.
		if (!isEqualToZero(actualEmep)) {
			//Create journal entry if between (0 and -101)
			if (isBetween(actualEmep, BigDecimal.ZERO, oneHundredAndOne.negate())) {
				if (userApprovedToReconcile) {
					createJournalEntry(form, absoluteEmep, new BigDecimal[]{absoluteEmep, actualEmep}, new String[]{POST_CODE_EP, POST_CODE_EW}, ACCOUNTING_EVENT_H1);
				} else {
                    throw new ReconcileFlagNotConfirmedException();
				}
			//Only attempt to create the journal entry if in range (emep > 0 and emep <= 10)
			} else if (isBetween(actualEmep, eleven, BigDecimal.ZERO)) {
				if (userApprovedToReconcile) {
					createJournalEntry(form, absoluteEmep, new BigDecimal[]{actualEmep, negativeEmep}, new String[]{POST_CODE_DW, POST_CODE_EP}, ACCOUNTING_EVENT_H1);
				} else {
                    throw new ReconcileFlagNotConfirmedException();
				}
			//Throw error and alert user they are outside of the reconcile limit
			} else {
				throw new ReconcileOutOfRangeException(cashAtSettlement.abs(), displayCash.abs(), cashAtSettlement.abs().subtract(displayCash.abs()).abs());
			}
		}
	}

	/**
	 * Constructs a journal entry bean and inserts into database
	 * @param form
	 * @param generalLedgerAmount
	 * @param journalDetailAmounts
	 * @param postCodes
	 * @param accountEvent
	 */
	private void createJournalEntry(HudOneForm form, BigDecimal generalLedgerAmount, BigDecimal[] journalDetailAmounts, String[] postCodes, String accountEvent) throws InsertJournalEntryException {
		AddJournalEntryBean jeBean = new AddJournalEntryBean();
		jeBean.setJePostDate("");
		jeBean.setJePostCodes(postCodes);
		jeBean.setJeJournalDetailAmounts(journalDetailAmounts);
		jeBean.setJeAccountEvent(accountEvent);
		jeBean.setJeCasePrefix(form.getA8spt191().getCaseHudOfficeP());
		jeBean.setJeCaseNumber(form.getA8spt191().getCaseNum());
		jeBean.setJeGeneralLedgerAmount(generalLedgerAmount);
		jeBean.setJeHudEmployeeLogon(form.getUser());
	    AddJournalEntryBean returnBean = jeDao.insertNewJournalEntry(jeBean);
	    if (returnBean.getJeRollBackIndicator().equals(1)) {
            throw new InsertJournalEntryException(returnBean.getJeMessageText());
        }
	}

	@Override
	public boolean validateLine1304(HudOneForm form) {
		return dao.validateLine1304(form);
	}

	@Override
	public void deleteSalesReceivable(int journalEntryId, String updatedBy) throws InsertJournalEntryException {
		//DELETE CURRENT SALES RECEIVABLE - F89O
		LocalDateTime now = LocalDateTime.now();
		A8spt005 a8spt005 = dao.get005(journalEntryId);
		A8spt182 a8spt182= dao.get182(a8spt005.getAccountsReceivabl());
		if(dao.check164Exists(a8spt182)) {
			throw new ObjectNotFoundException(A8spt164.class, "Property Rent");
		}
		//call update 182
		dao.update182(a8spt182, now, updatedBy);
		A8spt065 a8spt065 = dao.get065(a8spt005.getAccountsReceivabl());
		if(dao.check066Exists(a8spt065.getCaseHudOfficeP(), a8spt065.getCaseNum(), a8spt065.getBidReceiptNumber(), a8spt065.getExtensionNumber())) {
			throw new ObjectNotFoundException(A8spt066.class, "Extension Request");
		} //does exist
		dao.update065(a8spt065, now, updatedBy);
		//A8spt040 a8spt040 = dao.get040(a8spt005.getAccountsReceivabl());
		//gets 191 but that is already in form
		//can probably combine update and get for 040
		dao.update040(a8spt005.getAccountsReceivabl(), now, updatedBy);
		dao.update142(a8spt005.getAccountsReceivabl(), now, updatedBy); //nothing special about this class(update where accountsReceivabl = a8spt005.getAccountsReceivabl)
		A8spt107 a8spt107 = dao.get107(a8spt005.getAccountsReceivabl());
		if(dao.check108Exists(a8spt005.getCaseHudOfficeP(), a8spt005.getCaseNum(), a8spt107.getLiquidatedDamages())) {
			throw new ObjectNotFoundException(A8spt108.class, "Table 108");
		} //does exist
		dao.update107(a8spt107, now, updatedBy);
		A8spt056 a8spt056 = dao.get056(a8spt005.getAccountsReceivabl());
		if(dao.check125Exists(a8spt056.getNaid())) {
			throw new ObjectNotFoundException(A8spt125.class, "NAID Table");
		}
		if(dao.check118Exists(a8spt056.getMortageeNumber(), a8spt056.getMortgeeOffice())) {
			throw new ObjectNotFoundException(A8spt118.class, "IMF Mortgages");
		} //does exist
		if(dao.check106Exists(a8spt056.getLockbxBatchNumbe(), a8spt056.getLockboxBatchSequ())) {
			throw new ObjectNotFoundException(A8spt106.class, "Lockbox");
		}
		dao.update056(a8spt056, now, updatedBy);
		//Collection amount on 045
		A8spt045 a8spt045 = dao.get045(a8spt005.getAccountsReceivabl());
		BigDecimal collectionAmount = a8spt045.getCollectionAmount();
		dao.delete045(a8spt045); //calls this with 045.getcollectionBatchN, 045.getCollectnBatchSeq, a8spt005.getAccountsReceivabl
		//Line 6851 call A8SPF05O 005.getPostCode
		//Create custom AddJournalEntryBean() constructor
		if(a8spt005.getPostCode() != "EM") {
			//create journal entry
			//pass in 005.getPostcode, 045.collectionAmount
			AddJournalEntryBean jeBean = new AddJournalEntryBean(a8spt005, collectionAmount, updatedBy);
			jeDao.insertNewJournalEntry(jeBean);
		}
		if(dao.check079Exists(a8spt005.getNaid(), a8spt005.getHudOffice())) {
			throw new ObjectNotFoundException(A8spt079.class, "Hud Offices Name Address");
		} //does exist
		if(dao.check097Exists(journalEntryId)) {
			throw new ObjectNotFoundException(A8spt097.class, "Journal Entry Header Table");
		}
		//reads a 157 in cbl
		if(dao.check143Exists(a8spt005.getPostCode())) {
			throw new ObjectNotFoundException(A8spt143.class, "Post Code");
		}

		dao.update005(a8spt005, now, updatedBy);
	}
	//Function that processes the H3 journal entries when a case is reconciled.
	@Override
	public void FH3O(HudOneForm form) throws InsertJournalEntryException {
		String accountEvent = "H3";
		BigDecimal generalLedger = computeGeneralLedgerAmount(form.getA8spt191());
		BigDecimal[] journalDetailAmounts = new BigDecimal[]{generalLedger, generalLedger.negate(), generalLedger, generalLedger.negate()};
		String[] postCodes = new String[]{"W7", "W8", "W8", "W9"};
		//String offset = "Y"; This get set in addJournalEntry method is null
		generalLedger = generalLedger.add(generalLedger);
		//call FH1O (substitute FH1 with code below)
		createJournalEntry(form, generalLedger, (BigDecimal[]) journalDetailAmounts, postCodes, accountEvent);
	
		//if(postCodeFlag == 'Y') -> call FETO (Add accounts receivable and sales receivable)
		//set postCode = "EE"
		//set journalEntrySeqNumber = insertedJeId
		//FETO(form, jeBean.getid, updatedBy);
	}
	
	//postingDate = postDate, originalAmount = stillOwed
	@Override
	public void FETO(String postCode, String caseNum, String caseHudOfficeP, int journalEntryId, String hudOfficeCode, String naid, String postingDate, BigDecimal originalAmount, String updatedBy) throws EntityAlreadyExistsException {
		//generate src key select src where seedKey = 333 (seedKey 333)
		//seedValue += 1
		//update a8sptsrc / if record does not exist insert a8sptsrc with seedValue = 1 seedKey = 333
		A8sptsrc a8sptsrc = getA8sptsrc(updatedBy);

		int accountsReceivable = a8sptsrc.getSeedValue();

		if(dao.check005Exists(accountsReceivable)) {
			throw new EntityAlreadyExistsException("Accounts Receivables Sub-Ledger");
		}
		if(dao.check040Exists(accountsReceivable)) {
			 
			throw new EntityAlreadyExistsException("Sales Receivable");
		}
		//2750 read 191 (already on form) TODO possibly delete
		if(dao.check191Exists(caseNum, caseHudOfficeP)) {
			throw new ObjectNotFoundException(A8spt191.class, "Sales Package");
		}
		//2844 read 182 checkDoesNotExist
		if(dao.check182Exists(accountsReceivable)) {
			throw new EntityAlreadyExistsException("Rent Receivable");
		}

		if(dao.check107Exists(accountsReceivable)) {
			throw new EntityAlreadyExistsException("Table 107");
		}
		if(dao.check056Exists(accountsReceivable)) {
			throw new EntityAlreadyExistsException("Dishonoured Check Penalty");
		}
		//3083 read 142
		if(dao.check142Exists(accountsReceivable)) {
			throw new EntityAlreadyExistsException("Penalty and Interest Due");
		}
		//3163 read 060
		if(dao.check060Exists(accountsReceivable)) {
			throw new EntityAlreadyExistsException("Earnest Money Receivable");
		}
		//3243 read 123
		if(dao.check123Exists(accountsReceivable)) {
			throw new EntityAlreadyExistsException("Misc Receivables");
		}
		//3324 read 065
		if(dao.check065Exists(accountsReceivable)) {
			throw new EntityAlreadyExistsException("Extension Receivable");
		}
		//3495 insert 040
		dao.insert040(accountsReceivable, caseHudOfficeP, caseNum, updatedBy);
		
		//3605 read 079
		if(dao.check079Exists(naid, hudOfficeCode)) {
			throw new ObjectNotFoundException(A8spt079.class, "Hud Offices Name Address");
		}
		//3695 read 097
		if(dao.check097Exists(journalEntryId)) {
			throw new ObjectNotFoundException(A8spt079.class, "Journal Entry Header Table");
		}
		if(dao.check157Exists(caseNum, caseHudOfficeP)) {
			throw new ObjectNotFoundException(A8spt191.class, "Sales Package");
		}
		//3876 read 143
		if(dao.check143Exists(postCode)) {
			throw new ObjectNotFoundException(A8spt143.class, "Post Code");
		}
		//insert new 005
		dao.insert005(accountsReceivable, journalEntryId, caseNum, caseHudOfficeP, hudOfficeCode, naid, postingDate, originalAmount, postCode, updatedBy);
	}
	
	@Override
	public A8sptsrc getA8sptsrc(String updatedBy) {
		//TODO Possibly pass localdateTime now in to all methods??
		//TODO pass Key value in?
		//Key = 333
        A8sptsrc a8sptsrc = dao.getA8sptsrc(SamsConstants.SRC_KEY_HUD_ONE);
        LocalDateTime now = LocalDateTime.now();
        //update a8sptsrc or insert if null
        if(a8sptsrc == null) {
        	//make insert new record
        	a8sptsrc = new A8sptsrc();
        	a8sptsrc.setSeedKey(new Integer(SamsConstants.SRC_KEY_HUD_ONE));
        	a8sptsrc.setSeedValue(Integer.valueOf(1));
        	a8sptsrc.setTerminalId(SamsConstants.SAMS);
            a8sptsrc.setDateUpdated(SamsConstants.DATE_UPDATED_FORMAT.format(now));
            a8sptsrc.setTimeUpdated(SamsConstants.TIME_UPDATED_FORMAT.format(now));
            a8sptsrc.setUpdatedBy(updatedBy);
        	dao.insertA8sptsrc(a8sptsrc);
        	
        } else {
        	Integer increment = Integer.valueOf(1);
            a8sptsrc.setSeedValue(a8sptsrc.getSeedValue() + increment);
            a8sptsrc.setTerminalId(SamsConstants.SAMS);
            a8sptsrc.setDateUpdated(SamsConstants.DATE_UPDATED_FORMAT.format(now));
            a8sptsrc.setTimeUpdated(SamsConstants.TIME_UPDATED_FORMAT.format(now));
            a8sptsrc.setUpdatedBy(updatedBy);
            dao.updateA8sptsrc(a8sptsrc);
        }
        
        
        return a8sptsrc;
	}

	private BigDecimal computeGeneralLedgerAmount(A8spt191 a) {
		BigDecimal part1 = a.getPrepaidCityTown().add(a.getPrepaidCountyTax()).add(a.getPrepaidAssessment()).add(a.getAllOtherTaxesDu());
		part1 = part1.negate();
		BigDecimal part2 = a.getUnpaidCityTownT().add(a.getUnpaidCountyTaxe()).add(a.getUnpaidAssessment()).add(a.getAllOtherUnpaidT());
		BigDecimal part3 = a.getSellingBrokerCom().add(a.getBrokerEarlyClsng()).add(a.getClosingAgentFee());
		part3 = a.getSettlementCharge().subtract(part3);
		return part1.add(part2).add(part3).add(a.getSettlementCharge());
	}
	
	@Override
	public void FESO(HudOneForm form) throws EntityAlreadyExistsException, InsertJournalEntryException{
		//first call FE0O to retreive post code amounts
		//compute totalEarnestMoney from FE0O 096.amount sum
		String postCodeFlag = "N";
		TotalEarnestMoneyBean totalEarnestMoneyBean = computeTotalEarnestMoney(form.getA8spt191().getCaseNum(), form.getA8spt191().getCaseHudOfficeP());
		BigDecimal totalEarnestMoney = totalEarnestMoneyBean.getSumEmPosting().negate();

		String accountEvent = "H1";
		BigDecimal generalLedger = computeFetGeneralLedgerAmount(form.getA8spt191());

		BigDecimal[] journalDetailAmounts = new BigDecimal[26];
		setJournalDetailAmountValues(form, journalDetailAmounts);
		
		String postCode18 = getPostCode18(form.getA8spt191().getFinanceType());
		//use journalDetailEntr to compute postCode20 (probably set in FTEO) -> sums up 096.getAmount()
		String postCode20 = "";
		String postCode26 = "";
		BigDecimal je26 = BigDecimal.ZERO;
		BigDecimal journalDetailAmount = totalEarnestMoneyBean.getRentSum();
		if(journalDetailAmount.compareTo(BigDecimal.ZERO) <= 0) {
			postCode20 = "RI";
			journalDetailAmounts[19] = (form.getA8spt191().getRent().negate());
		} else if(journalDetailAmount.compareTo(form.getA8spt191().getRent()) >= 0){
			postCode20 = "RT";
			journalDetailAmounts[19] = (form.getA8spt191().getRent().negate());
		} else {
			postCode20 = "RT";
			if(journalDetailAmount.compareTo(BigDecimal.ZERO) == 1) {
				journalDetailAmounts[19] = (journalDetailAmount.negate());
			} else {
				journalDetailAmounts[19] = (journalDetailAmount);
			}
			postCode26 = "RI";
			je26 = (form.getA8spt191().getRent().subtract(journalDetailAmount)).abs();
		}
		
		//totalOwed = 191.cashDueSettleMent + 191.earnestMoneyDepo
		BigDecimal totalOwed =  form.getA8spt191().getCashDueSettlemen().add(form.getA8spt191().getEarnestMoneyDepo());
		
		//line 1884 -> computation to determine postrCode24 and je24
		String postCode24 = "";
		String postCode25 = "";
		BigDecimal je24 = BigDecimal.ZERO;
		BigDecimal je25 = BigDecimal.ZERO;
		if(form.getA8spt191().getCashDueSettlemen().compareTo(BigDecimal.ZERO) >= 0 ) {
			if(totalEarnestMoney.compareTo(totalOwed) >= 0) {
				postCode24 = "EP";
				je24 = form.getA8spt191().getCashDueSettlemen();
			} else {
				postCode24 = "EE";
				postCode25 = "EP";
				//je24 = stillOwed
				je24 = totalOwed.subtract(totalEarnestMoney).subtract(form.getA8spt191().getEarnestMoneyDepo());
				je25 = totalEarnestMoney;
				postCodeFlag = "Y";
			}
			
		} else {
			postCode24 = "EP";
			je24 = form.getA8spt191().getCashDueSettlemen();
		}
		journalDetailAmounts[23] = (je24);
		journalDetailAmounts[24] = (je25);
		journalDetailAmounts[25] = (je26);
		if(form.getA8spt191().getCashDueSettlemen().compareTo(BigDecimal.ZERO) == -1) {
			generalLedger = generalLedger.add(form.getA8spt191().getCashDueSettlemen().negate());
		} else if(totalEarnestMoney.compareTo(BigDecimal.ZERO) == -1) {
			generalLedger = generalLedger.subtract(totalEarnestMoney);
		}
		//TODO uncomment and delete replacement once we know how to fix WF
		String[] postCodes = new String[] {
		        "SL", "KB", "LZ", "CL", "WF", 
		        "EP", "MC", "CM", "EX", "SI",
				"UT", "EO", "TX", "TP", "TV",
				"CF", "EO", postCode18, "EX",
				postCode20, "TX", "CF", "MI",
				postCode24, postCode25, postCode26};
		
		//sort and remove journalEntries and postCodes
		filterDetailAmountsAndPostCodes(journalDetailAmounts, postCodes);
		
		//line 1974 call F05 to write JE's
		createJournalEntry(form, generalLedger, journalDetailAmounts, postCodes, accountEvent);
		if("Y".equals(postCodeFlag)) {
			//postCode = "EE", je24 = stillOwed
			A8spt097 journalEntry = dao.getJournalEntry(form.getA8spt191().getCaseHudOfficeP(), form.getA8spt191().getCaseNum());
			try {
				FETO("EE", form.getA8spt191().getCaseNum(), form.getA8spt191().getCaseHudOfficeP(), journalEntry.getJournalEntryId(),
					form.getA8spt191().getHudProperty().getCurrentHudOffice(), journalEntry.getNaid(), journalEntry.getPostDate(), je24, journalEntry.getUpdatedBy());
			} catch (EntityAlreadyExistsException e) {
				throw new EntityAlreadyExistsException(e.getMessage());
			}
		}
		
	}

	private void setJournalDetailAmountValues(HudOneForm form, BigDecimal[] journalDetailAmounts) {
		journalDetailAmounts[0] = form.getA8spt191().getSellingBrokerCom();
		journalDetailAmounts[1] = form.getA8spt191().getBrokerEarlyClsng();
		journalDetailAmounts[2] = (BigDecimal.ZERO);
		journalDetailAmounts[3] = (form.getA8spt191().getClosingAgentFee());
		journalDetailAmounts[4] = (BigDecimal.ZERO);
		journalDetailAmounts[5] = (form.getA8spt191().getEarnestMoneyDepo());
		journalDetailAmounts[6] = (calculateJorunalEntrytDetail7(form.getA8spt191()));
		journalDetailAmounts[7] = (form.getA8spt191().getNoteRecvdSeller());
		journalDetailAmounts[8] = (form.getA8spt191().getExtensionFeeRefu());
		journalDetailAmounts[9] = (form.getA8spt191().getPurchaserAllowanc());
		journalDetailAmounts[10] = (form.getA8spt191().getUtilityEscrowAmo());
		journalDetailAmounts[11] = (form.getA8spt191().getRepairEscrowAmou());
		journalDetailAmounts[12] = (calculateJorunalEntrytDetail13(form.getA8spt191()));
		journalDetailAmounts[13] = (form.getA8spt191().getHudTaxInterestA());
		journalDetailAmounts[14] = (form.getA8spt191().getMortgageeInterest());
		journalDetailAmounts[15] = (form.getA8spt191().getCondoHoaFee2());
		journalDetailAmounts[16] = (calculateJorunalEntrytDetail17(form.getA8spt191()));
		journalDetailAmounts[17] = (form.getA8spt191().getContractSalesPri().negate());
		journalDetailAmounts[18] = (form.getA8spt191().getExtensionFee().negate());
		//19 set in calling method
		journalDetailAmounts[20] = (calculateJorunalEntrytDetail21(form.getA8spt191()));
		journalDetailAmounts[21] = (form.getA8spt191().getCondoHoaFee().negate());
		journalDetailAmounts[22] = (calculateJorunalEntrytDetail23(form.getA8spt191()));
		//23, 24, 25 set in calling method
	}
	
	private void filterDetailAmountsAndPostCodes(BigDecimal[] journalDetailAmounts, String[] postCodes) {
		for(int i = 0; i < journalDetailAmounts.length; i++) {
			if(BigDecimalUtils.isEqualToZero(journalDetailAmounts[i])) {
				for(int j = i + 1; j < journalDetailAmounts.length; j++) {
					if(BigDecimalUtils.isEqualToZero(journalDetailAmounts[i]) && !BigDecimalUtils.isEqualToZero(journalDetailAmounts[j])) {
						postCodes[i] = postCodes[j];
						journalDetailAmounts[i] = journalDetailAmounts[j];
						postCodes[j] = " ";
						journalDetailAmounts[j] = BigDecimal.ZERO;
					}
				}
			}
			if(BigDecimalUtils.isEqualToZero(journalDetailAmounts[i])) {
				postCodes[i] = " ";
			}
		}
		
	}

	private BigDecimal calculateJorunalEntrytDetail23(A8spt191 a) {
		return (a.getFirstOtherAmtDu().add(a.getSecondOtherAmtD())).negate();
	}

	private BigDecimal calculateJorunalEntrytDetail21(A8spt191 a) {
		return (a.getPrepaidCityTown().add(a.getPrepaidCountyTax()).add(a.getPrepaidAssessment()).add(a.getAllOtherTaxesDu())).negate();
	}

	private BigDecimal calculateJorunalEntrytDetail17(A8spt191 a) {
		return a.getOtherUnpaidItem().add(a.getSecondOtherUnpai()).add(a.getThirdOtherUnpaid()).add(a.getFourthOtherUnpai());
	}

	private BigDecimal calculateJorunalEntrytDetail13(A8spt191 a) {
		return a.getUnpaidCityTownT().add(a.getUnpaidCountyTaxe()).add(a.getUnpaidAssessment()).add(a.getAllOtherUnpaidT());
	}

	private BigDecimal calculateJorunalEntrytDetail7(A8spt191 a) {
		return a.getSettlementCharge().subtract(a.getSellingBrokerCom()).subtract(a.getBrokerEarlyClsng()).subtract(a.getClosingAgentFee());
	}

	//FE0
	private TotalEarnestMoneyBean computeTotalEarnestMoney(String caseNum, String casePre) {
		//loop through journal entries for case, if post code is equal add to sum
		//get097 loop through 096
		List<String> accountingEvents = Arrays.asList("H1", "RC", "CF", "CT", "MM", "H2");
		A8spt097 a8spt097 = dao.get097(caseNum, casePre);
		BigDecimal sumEmPosting = BigDecimal.ZERO;
		BigDecimal sumJeDetailAmount = BigDecimal.ZERO;
		BigDecimal sumEePosting = BigDecimal.ZERO;
		BigDecimal sumEpPosting = BigDecimal.ZERO;
		BigDecimal rentSum = BigDecimal.ZERO;
		for(A8spt096 a : a8spt097.getA8spt096List()) {
			String postCode = a.getPostCode();
			
			if("EM".equals(postCode)) {
				sumEmPosting = sumEmPosting.add(a.getAmount());
				if(accountingEvents.contains(a.getAccountingEvent())) {
					sumJeDetailAmount = sumJeDetailAmount.add(a.getAmount());
				}
			} else if("EE".equals(postCode) || "S9".equals(postCode)) {
				sumEePosting = sumEePosting.add(a.getAmount());
			} else if("EP".equals(postCode)) {
				sumEpPosting = sumEpPosting.add(a.getAmount());
			}
			if("RT".equals(postCode)) {
				rentSum = rentSum.add(a.getAmount());
			}
		}
		return new TotalEarnestMoneyBean(sumEmPosting, sumJeDetailAmount, sumEePosting, sumEpPosting, rentSum);

	}

	private String getPostCode18(String financeType) {
		if("UH".equals(financeType)) {
			return "SA";
		} else if ("PM".equals(financeType)) {
			return "SB";
		} else {
			return "SC";
		}
	}

	private BigDecimal computeFetGeneralLedgerAmount(A8spt191 a) {
		return a.getContractSalesPri().add(a.getExtensionFee())
				.add(a.getRent()).add(a.getPrepaidCityTown())
				.add(a.getPrepaidCountyTax()).add(a.getPrepaidAssessment())
				.add(a.getCondoHoaFee()).add(a.getFirstOtherAmtDu())
				.add(a.getSecondOtherAmtD()).add(a.getAllOtherTaxesDu());
	}

	@Override
	public boolean doesEarnestMoneyJournalEntriesExist(String casePrefix, String caseNumber) {
		return dao.doesEarnestMoneyJournalEntriesExist(casePrefix, caseNumber);
	}

	/**
	 * Reverses each journal entry for the case passed in
	 * @param casePrefix
	 * @param caseNumber
	 */
	@Override
	public void reverseJournalEntries(String casePrefix, String caseNumber) {
		for (A8spt097 a97 : dao.getH1AndH3JournalEntriesToReverse(casePrefix, caseNumber)) {
			reverseDao.reverseTheOldJournalEntry(new ReverseJournalEntryBean(a97));
		}
	}
}
