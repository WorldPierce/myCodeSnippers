package gov.hud.sams.entities;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.Type;


/**
 * The persistent class for the A8SPT248 database table.
 * 
 */
@Entity
@Table(name="A8SPT248")
@IdClass(value = A8spt248PK.class)
public class A8spt248 implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="CASE_HUD_OFFICE__P", nullable=false, length=3, columnDefinition = "CHAR(4)")
	@Type(type = "gov.hud.sams.dao.orm.hibernate.TrimmedString")
	private String caseHudOfficeP;

	@Id
	@Column(name="CASE_NUM", nullable=false, length=6, columnDefinition = "CHAR(6)")
	@Type(type = "gov.hud.sams.dao.orm.hibernate.TrimmedString")
	private String caseNum;

	@Id
	@Column(name="PACKAGE_ERROR_CODE", nullable=false, length=2, columnDefinition = "CHAR(2)")
	@Type(type = "gov.hud.sams.dao.orm.hibernate.TrimmedString")
	private String packageErrorCode;

	@Id
	@Column(name="HISTORY_DATE", nullable=false, length=10, columnDefinition = "CHAR(10)")
	@Type(type = "gov.hud.sams.dao.orm.hibernate.TrimmedString")
	private String historyDate;

	@Column(name="DATE_UPDATED", nullable=false, length=8, columnDefinition = "CHAR(8)")
	@Type(type = "gov.hud.sams.dao.orm.hibernate.TrimmedString")
	private String dateUpdated;

	@Column(name="TERMINAL_ID", nullable=false, length=4)
	@Type(type = "gov.hud.sams.dao.orm.hibernate.TrimmedString")
	private String terminalId;

	@Column(name="TIME_UPDATED", nullable=false, length=6, columnDefinition = "CHAR(6)")
	@Type(type = "gov.hud.sams.dao.orm.hibernate.TrimmedString")
	private String timeUpdated;

	@Column(name="TXN_SEQ_NBR", nullable=false)
	private short txnSeqNbr;

	@Column(name="UPDATED_BY", nullable=false, length=8, columnDefinition = "CHAR(8)")
	@Type(type = "gov.hud.sams.dao.orm.hibernate.TrimmedString")
	private String updatedBy;

	public A8spt248() {
		//default constructor
	}

	

	public String getCaseHudOfficeP() {
		return caseHudOfficeP;
	}



	public void setCaseHudOfficeP(String caseHudOfficeP) {
		this.caseHudOfficeP = caseHudOfficeP;
	}



	public String getCaseNum() {
		return caseNum;
	}



	public void setCaseNum(String caseNum) {
		this.caseNum = caseNum;
	}



	public String getPackageErrorCode() {
		return packageErrorCode;
	}



	public void setPackageErrorCode(String packageErrorCode) {
		this.packageErrorCode = packageErrorCode;
	}



	public String getHistoryDate() {
		return historyDate;
	}



	public void setHistoryDate(String historyDate) {
		this.historyDate = historyDate;
	}



	public String getDateUpdated() {
		return this.dateUpdated;
	}

	public void setDateUpdated(String dateUpdated) {
		this.dateUpdated = dateUpdated;
	}

	public String getTerminalId() {
		return this.terminalId;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	public String getTimeUpdated() {
		return this.timeUpdated;
	}

	public void setTimeUpdated(String timeUpdated) {
		this.timeUpdated = timeUpdated;
	}

	public short getTxnSeqNbr() {
		return this.txnSeqNbr;
	}

	public void setTxnSeqNbr(short txnSeqNbr) {
		this.txnSeqNbr = txnSeqNbr;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

}


public class A8spt248PK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	protected String caseHudOfficeP;

	protected String caseNum;

	protected String packageErrorCode;

	protected String historyDate;

	public A8spt248PK() {
		//default constructor
	}

	public A8spt248PK(String caseHudOfficeP, String caseNum, String packageErrorCode, String historyDate) {
		super();
		this.caseHudOfficeP = caseHudOfficeP;
		this.caseNum = caseNum;
		this.packageErrorCode = packageErrorCode;
		this.historyDate = historyDate;
	}
	
	/**
     * Equals Implementation
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    /**
     * HashCode Implementation
     *
     * @return
     */
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}