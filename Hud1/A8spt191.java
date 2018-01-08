package gov.hud.sams.entities;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Table - Sales Package
 */
@Entity
@Table(name = "A8SPT191")
@IdClass(A8spt191PK.class)
public class A8spt191 implements Serializable {
    private static final long serialVersionUID = 7997439114280775028L;
    
    @Column(name = "TERMINAL_ID", nullable = false, length = 4, columnDefinition = "CHAR(4)")
    @Type(type = "gov.hud.sams.dao.orm.hibernate.TrimmedString")
    private String terminalId;
    
    @Column(name = "TXN_SEQ_NBR", nullable = false)
    private Short txnSeqNbr;
    
    @Column(name = "DATE_UPDATED", nullable = false, length = 8, columnDefinition = "CHAR(8)")
    @Type(type = "gov.hud.sams.dao.orm.hibernate.TrimmedString")
    private String dateUpdated;
    

	@Column(name = "TIME_UPDATED", nullable = false, length = 6, columnDefinition = "CHAR(6)")
    @Type(type = "gov.hud.sams.dao.orm.hibernate.TrimmedString")
    private String timeUpdated;
    
    @Column(name = "UPDATED_BY", nullable = false, length = 8, columnDefinition = "CHAR(8)")
    @Type(type = "gov.hud.sams.dao.orm.hibernate.TrimmedString")
    private String updatedBy;
    
    
    @Column(name = "CASE_NUM", length = 6, nullable = false, columnDefinition = "CHAR(6)", insertable = false, updatable = false)
    @Type(type = "gov.hud.sams.dao.orm.hibernate.TrimmedString")
    private String caseNum;

    
    @Column(name = "CASE_HUD_OFFICE__P", length = 3, nullable = false, columnDefinition = "CHAR(3)", insertable = false, updatable = false)
    @Type(type = "gov.hud.sams.dao.orm.hibernate.TrimmedString")
    private String caseHudOfficeP;

    
    public String getCaseNum() {
		return caseNum;
	}

	public void setCaseNum(String caseNum) {
		this.caseNum = caseNum;
	}

	public String getCaseHudOfficeP() {
		return caseHudOfficeP;
	}

	public void setCaseHudOfficeP(String caseHudOfficeP) {
		this.caseHudOfficeP = caseHudOfficeP;
	}

	@Id
	@OneToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "CASE_HUD_OFFICE__P", referencedColumnName = "CASE_HUD_OFFICE__P"),
            @JoinColumn(name = "CASE_NUM", referencedColumnName = "CASE_NUM")
    })
    private A8spt157 hudProperty;
    
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumns(value = {
    		@JoinColumn(name = "CASE_HUD_OFFICE__P", referencedColumnName = "CASE_HUD_OFFICE__P"),
            @JoinColumn(name = "CASE_NUM", referencedColumnName = "CASE_NUM")
        })
    private List<A8spt248> salesPackageHistory;

	
	@OneToMany(fetch = FetchType.LAZY)
    @JoinColumns(value = {
    		@JoinColumn(name = "CASE_HUD_OFFICE__P", referencedColumnName = "CASE_HUD_OFFICE__P"),
            @JoinColumn(name = "CASE_NUM", referencedColumnName = "CASE_NUM")
        })
    private List<A8spt066> salesExtension;

	@Column(name = "FINANCE_TYPE", nullable = false, length = 2, columnDefinition = "CHAR(2)")
    @Type(type = "gov.hud.sams.dao.orm.hibernate.TrimmedString")
    private String financeType;
    
    @Column(name = "MORTAGEE_BOUNS_FLA", nullable = false, length = 1, columnDefinition = "CHAR(1)")
    @Type(type = "gov.hud.sams.dao.orm.hibernate.TrimmedString")
    private String mortageeBounsFla;
    
    @Column(name = "DATE_CLOSED", nullable = false, length = 10, columnDefinition = "CHAR(10)")
    @Type(type = "gov.hud.sams.dao.orm.hibernate.TrimmedString")
    private String dateClosed;
    
    @Column(name = "DATE_RECEIVED", nullable = false, length = 10, columnDefinition = "CHAR(10)")
    @Type(type = "gov.hud.sams.dao.orm.hibernate.TrimmedString")
    private String dateReceived;
    
    @Column(name = "OPTIONAL_FIELD", nullable = false, precision = 5)
    private Integer optionalField;
    
    @Column(name = "BROKER_EARLY_CLSNG", nullable = false, precision = 8 , scale = 2)
    private BigDecimal brokerEarlyClsng;
    
    @Column(name = "EXTENSION_FEE", nullable = false, precision = 8 , scale = 2)
    private BigDecimal extensionFee;

    @Column(name = "RENT", nullable = false, precision = 8 , scale = 2)
    private BigDecimal rent;

    @Column(name = "PREPAID_CITY_TOWN", nullable = false, precision = 8 , scale = 2)
    private BigDecimal prepaidCityTown;

    @Column(name = "PREPAID_COUNTY_TAX", nullable = false, precision = 8 , scale = 2)
    private BigDecimal prepaidCountyTax;

    @Column(name = "PREPAID_ASSESSMENT", nullable = false, precision = 8 , scale = 2)
    private BigDecimal prepaidAssessment;

    @Column(name = "CONDO_HOA_FEE", nullable = false, precision = 8 , scale = 2)
    private BigDecimal condoHoaFee;

    @Column(name = "FIRST_OTHER_AMT_DU", nullable = false, precision = 8 , scale = 2)
    private BigDecimal firstOtherAmtDu;

    @Column(name = "SECOND_OTHER_AMT_D", nullable = false, precision = 8 , scale = 2)
    private BigDecimal secondOtherAmtD;

    @Column(name = "ALL_OTHER_TAXES_DU", nullable = false, precision = 8 , scale = 2)
    private BigDecimal allOtherTaxesDu;
    
    @Column(name = "EARNEST_MONEY_DEPO", nullable = false, precision = 8 , scale = 2)
    private BigDecimal earnestMoneyDepo;
    
    @Column(name = "SETTLEMENT_CHARGE", nullable = false, precision = 8 , scale = 2)
    private BigDecimal settlementCharge;
    
    @Column(name = "NOTE_RECVD_SELLER", nullable = false, precision = 8 , scale = 2)
    private BigDecimal noteRecvdSeller;
    
    @Column(name = "EXTENSION_FEE_REFU", nullable = false, precision = 8 , scale = 2)
    private BigDecimal extensionFeeRefu;
    
    @Column(name = "UTILITY_ESCROW_AMO", nullable = false, precision = 8 , scale = 2)
    private BigDecimal utilityEscrowAmo;
    
    @Column(name = "UNPAID_CITY_TOWN_T", nullable = false, precision = 8 , scale = 2)
    private BigDecimal unpaidCityTownT;
    
    @Column(name = "UNPAID_COUNTY_TAXE", nullable = false, precision = 8 , scale = 2)
    private BigDecimal unpaidCountyTaxe;
    
    @Column(name = "UNPAID_ASSESSMENT", nullable = false, precision = 8 , scale = 2)
    private BigDecimal unpaidAssessment;
    
    @Column(name = "OTHER_UNPAID_ITEM", nullable = false, precision = 8 , scale = 2)
    private BigDecimal otherUnpaidItem;
    
    @Column(name = "HUD_TAX_INTEREST_A", nullable = false, precision = 8 , scale = 2)
    private BigDecimal hudTaxInterestA;
    
    @Column(name = "MORTGAGEE_INTEREST", nullable = false, precision = 8 , scale = 2)
    private BigDecimal mortgageeInterest;
    
    @Column(name = "CONDO_HOA_FEE_2", nullable = false, precision = 8 , scale = 2)
    private BigDecimal condoHoaFee2;
    
    @Column(name = "SECOND_OTHER_UNPAI", nullable = false, precision = 8 , scale = 2)
    private BigDecimal secondOtherUnpai;
    
    @Column(name = "THIRD_OTHER_UNPAID", nullable = false, precision = 8 , scale = 2)
    private BigDecimal thirdOtherUnpaid;
    
    @Column(name = "FOURTH_OTHER_UNPAI", nullable = false, precision = 8 , scale = 2)
    private BigDecimal fourthOtherUnpai;
    
    @Column(name = "ALL_OTHER_UNPAID_T", nullable = false, precision = 8 , scale = 2)
    private BigDecimal allOtherUnpaidT;
    
    @Column(name = "GROSS_AMOUNT_DUE_S", nullable = false, precision = 9 , scale = 2)
    private BigDecimal grossAmountDueS;
    
    @Column(name = "LESS_REDUCTIONS_DU", nullable = false, precision = 8 , scale = 2)
    private BigDecimal lessReductionsDu;
    
    @Column(name = "CASH_DUE_SETTLEMEN", nullable = false, precision = 9 , scale = 2)
    private BigDecimal cashDueSettlemen;
    
    @Column(name = "COMMENTS", nullable = false, length = 70, columnDefinition = "CHAR(70)")
    @Type(type = "gov.hud.sams.dao.orm.hibernate.TrimmedString")
    private String comments;
    
    @Column(name = "SELLING_BROKER_COM", nullable = false, precision = 8 , scale = 2)
    private BigDecimal sellingBrokerCom;
    
    @Column(name = "PURCHASER_ALLOWANC", nullable = false, precision = 8 , scale = 2)
    private BigDecimal purchaserAllowanc;
    
    @Column(name = "REPAIR_ESCROW_AMOU", nullable = false, precision = 8 , scale = 2)
    private BigDecimal repairEscrowAmou;
    
    @Column(name = "THIRD_PARTY_CLOSIN", nullable = false, length = 1, columnDefinition = "CHAR(1)")
    @Type(type = "gov.hud.sams.dao.orm.hibernate.TrimmedString")
    private String thirdPartyClosin;
    
    @Column(name = "RECONCILE_WITHIN_T", nullable = false, length = 1, columnDefinition = "CHAR(1)")
    @Type(type = "gov.hud.sams.dao.orm.hibernate.TrimmedString")
    private String reconcileWithinT;
    
    @Column(name = "CLOSING_AGENT_FEE", nullable = false, precision = 8 , scale = 2)
    private BigDecimal closingAgentFee;
    
    @Column(name = "SALE_PACKAGE__OPTI", nullable = false, length = 5, columnDefinition = "CHAR(5)")
    @Type(type = "gov.hud.sams.dao.orm.hibernate.TrimmedString")
    private String salePackageOpti;
    
    @Column(name = "CONTRACT_SALES_PRI", nullable = false, precision = 9 , scale = 2)
    private BigDecimal contractSalesPri;
    
    @Column(name = "PURCHASER_1304", nullable = false, precision = 8 , scale = 2)
    private BigDecimal purchaser1304;
    
    public List<A8spt248> getSalesPackageHistory() {
		return salesPackageHistory;
	}

	public void setSalesPackageHistory(List<A8spt248> salesPackageHistory) {
		this.salesPackageHistory = salesPackageHistory;
	}
    public List<A8spt066> getSalesExtension() {
		return salesExtension;
	}

	public void setSalesExtension(List<A8spt066> salesExtension) {
		this.salesExtension = salesExtension;
	}

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public Short getTxnSeqNbr() {
        return txnSeqNbr;
    }

    public void setTxnSeqNbr(Short txnSeqNbr) {
        this.txnSeqNbr = txnSeqNbr;
    }

    public String getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(String dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public String getTimeUpdated() {
        return timeUpdated;
    }

    public void setTimeUpdated(String timeUpdated) {
        this.timeUpdated = timeUpdated;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public A8spt157 getHudProperty() {
        return hudProperty;
    }

    public void setHudProperty(A8spt157 hudProperty) {
        this.hudProperty = hudProperty;
    }

    public String getFinanceType() {
        return financeType;
    }

    public void setFinanceType(String financeType) {
        this.financeType = financeType;
    }

    public String getMortageeBounsFla() {
        return mortageeBounsFla;
    }

    public void setMortageeBounsFla(String mortageeBounsFla) {
        this.mortageeBounsFla = mortageeBounsFla;
    }

    public String getDateClosed() {
        return dateClosed;
    }

    public void setDateClosed(String dateClosed) {
        this.dateClosed = dateClosed;
    }

    public String getDateReceived() {
        return dateReceived;
    }

    public void setDateReceived(String dateReceived) {
        this.dateReceived = dateReceived;
    }

    public Integer getOptionalField() {
        return optionalField;
    }

    public void setOptionalField(Integer optionalField) {
        this.optionalField = optionalField;
    }

    public BigDecimal getBrokerEarlyClsng() {
        return brokerEarlyClsng;
    }

    public void setBrokerEarlyClsng(BigDecimal brokerEarlyClsng) {
        this.brokerEarlyClsng = brokerEarlyClsng;
    }

    public BigDecimal getExtensionFee() {
        return extensionFee;
    }

    public void setExtensionFee(BigDecimal extensionFee) {
        this.extensionFee = extensionFee;
    }

    public BigDecimal getRent() {
        return rent;
    }

    public void setRent(BigDecimal rent) {
        this.rent = rent;
    }

    public BigDecimal getPrepaidCityTown() {
        return prepaidCityTown;
    }

    public void setPrepaidCityTown(BigDecimal prepaidCityTown) {
        this.prepaidCityTown = prepaidCityTown;
    }

    public BigDecimal getPrepaidCountyTax() {
        return prepaidCountyTax;
    }

    public void setPrepaidCountyTax(BigDecimal prepaidCountyTax) {
        this.prepaidCountyTax = prepaidCountyTax;
    }

    public BigDecimal getPrepaidAssessment() {
        return prepaidAssessment;
    }

    public void setPrepaidAssessment(BigDecimal prepaidAssessment) {
        this.prepaidAssessment = prepaidAssessment;
    }

    public BigDecimal getCondoHoaFee() {
        return condoHoaFee;
    }

    public void setCondoHoaFee(BigDecimal condoHoaFee) {
        this.condoHoaFee = condoHoaFee;
    }

    public BigDecimal getFirstOtherAmtDu() {
        return firstOtherAmtDu;
    }

    public void setFirstOtherAmtDu(BigDecimal firstOtherAmtDu) {
        this.firstOtherAmtDu = firstOtherAmtDu;
    }

    public BigDecimal getSecondOtherAmtD() {
        return secondOtherAmtD;
    }

    public void setSecondOtherAmtD(BigDecimal secondOtherAmtD) {
        this.secondOtherAmtD = secondOtherAmtD;
    }

    public BigDecimal getAllOtherTaxesDu() {
        return allOtherTaxesDu;
    }

    public void setAllOtherTaxesDu(BigDecimal allOtherTaxesDu) {
        this.allOtherTaxesDu = allOtherTaxesDu;
    }

    public BigDecimal getEarnestMoneyDepo() {
        return earnestMoneyDepo;
    }

    public void setEarnestMoneyDepo(BigDecimal earnestMoneyDepo) {
        this.earnestMoneyDepo = earnestMoneyDepo;
    }

    public BigDecimal getSettlementCharge() {
        return settlementCharge;
    }

    public void setSettlementCharge(BigDecimal settlementCharge) {
        this.settlementCharge = settlementCharge;
    }

    public BigDecimal getNoteRecvdSeller() {
        return noteRecvdSeller;
    }

    public void setNoteRecvdSeller(BigDecimal noteRecvdSeller) {
        this.noteRecvdSeller = noteRecvdSeller;
    }

    public BigDecimal getExtensionFeeRefu() {
        return extensionFeeRefu;
    }

    public void setExtensionFeeRefu(BigDecimal extensionFeeRefu) {
        this.extensionFeeRefu = extensionFeeRefu;
    }

    public BigDecimal getUtilityEscrowAmo() {
        return utilityEscrowAmo;
    }

    public void setUtilityEscrowAmo(BigDecimal utilityEscrowAmo) {
        this.utilityEscrowAmo = utilityEscrowAmo;
    }

    public BigDecimal getUnpaidCityTownT() {
        return unpaidCityTownT;
    }

    public void setUnpaidCityTownT(BigDecimal unpaidCityTownT) {
        this.unpaidCityTownT = unpaidCityTownT;
    }

    public BigDecimal getUnpaidCountyTaxe() {
        return unpaidCountyTaxe;
    }

    public void setUnpaidCountyTaxe(BigDecimal unpaidCountyTaxe) {
        this.unpaidCountyTaxe = unpaidCountyTaxe;
    }

    public BigDecimal getUnpaidAssessment() {
        return unpaidAssessment;
    }

    public void setUnpaidAssessment(BigDecimal unpaidAssessment) {
        this.unpaidAssessment = unpaidAssessment;
    }

    public BigDecimal getOtherUnpaidItem() {
        return otherUnpaidItem;
    }

    public void setOtherUnpaidItem(BigDecimal otherUnpaidItem) {
        this.otherUnpaidItem = otherUnpaidItem;
    }

    public BigDecimal getHudTaxInterestA() {
        return hudTaxInterestA;
    }

    public void setHudTaxInterestA(BigDecimal hudTaxInterestA) {
        this.hudTaxInterestA = hudTaxInterestA;
    }

    public BigDecimal getMortgageeInterest() {
        return mortgageeInterest;
    }

    public void setMortgageeInterest(BigDecimal mortgageeInterest) {
        this.mortgageeInterest = mortgageeInterest;
    }

    public BigDecimal getCondoHoaFee2() {
        return condoHoaFee2;
    }

    public void setCondoHoaFee2(BigDecimal condoHoaFee2) {
        this.condoHoaFee2 = condoHoaFee2;
    }

    public BigDecimal getSecondOtherUnpai() {
        return secondOtherUnpai;
    }

    public void setSecondOtherUnpai(BigDecimal secondOtherUnpai) {
        this.secondOtherUnpai = secondOtherUnpai;
    }

    public BigDecimal getThirdOtherUnpaid() {
        return thirdOtherUnpaid;
    }

    public void setThirdOtherUnpaid(BigDecimal thirdOtherUnpaid) {
        this.thirdOtherUnpaid = thirdOtherUnpaid;
    }

    public BigDecimal getFourthOtherUnpai() {
        return fourthOtherUnpai;
    }

    public void setFourthOtherUnpai(BigDecimal fourthOtherUnpai) {
        this.fourthOtherUnpai = fourthOtherUnpai;
    }

    public BigDecimal getAllOtherUnpaidT() {
        return allOtherUnpaidT;
    }

    public void setAllOtherUnpaidT(BigDecimal allOtherUnpaidT) {
        this.allOtherUnpaidT = allOtherUnpaidT;
    }

    public BigDecimal getGrossAmountDueS() {
        return grossAmountDueS;
    }

    public void setGrossAmountDueS(BigDecimal grossAmountDueS) {
        this.grossAmountDueS = grossAmountDueS;
    }

    public BigDecimal getLessReductionsDu() {
        return lessReductionsDu;
    }

    public void setLessReductionsDu(BigDecimal lessReductionsDu) {
        this.lessReductionsDu = lessReductionsDu;
    }

    public BigDecimal getCashDueSettlemen() {
        return cashDueSettlemen;
    }

    public void setCashDueSettlemen(BigDecimal cashDueSettlemen) {
        this.cashDueSettlemen = cashDueSettlemen;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public BigDecimal getSellingBrokerCom() {
        return sellingBrokerCom;
    }

    public void setSellingBrokerCom(BigDecimal sellingBrokerCom) {
        this.sellingBrokerCom = sellingBrokerCom;
    }

    public BigDecimal getPurchaserAllowanc() {
        return purchaserAllowanc;
    }

    public void setPurchaserAllowanc(BigDecimal purchaserAllowanc) {
        this.purchaserAllowanc = purchaserAllowanc;
    }

    public BigDecimal getRepairEscrowAmou() {
        return repairEscrowAmou;
    }

    public void setRepairEscrowAmou(BigDecimal repairEscrowAmou) {
        this.repairEscrowAmou = repairEscrowAmou;
    }

    public String getThirdPartyClosin() {
        return thirdPartyClosin;
    }

    public void setThirdPartyClosin(String thirdPartyClosin) {
        this.thirdPartyClosin = thirdPartyClosin;
    }

    public String getReconcileWithinT() {
        return reconcileWithinT;
    }

    public void setReconcileWithinT(String reconcileWithinT) {
        this.reconcileWithinT = reconcileWithinT;
    }

    public BigDecimal getClosingAgentFee() {
        return closingAgentFee;
    }

    public void setClosingAgentFee(BigDecimal closingAgentFee) {
        this.closingAgentFee = closingAgentFee;
    }

    public String getSalePackageOpti() {
        return salePackageOpti;
    }

    public void setSalePackageOpti(String salePackageOpti) {
        this.salePackageOpti = salePackageOpti;
    }

    public BigDecimal getContractSalesPri() {
        return contractSalesPri;
    }

    public void setContractSalesPri(BigDecimal contractSalesPri) {
        this.contractSalesPri = contractSalesPri;
    }

    public BigDecimal getPurchaser1304() {
        return purchaser1304;
    }

    public void setPurchaser1304(BigDecimal purchaser1304) {
        this.purchaser1304 = purchaser1304;
    }
}
