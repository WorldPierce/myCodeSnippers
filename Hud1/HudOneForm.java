package gov.hud.sams.bean;

import gov.hud.sams.entities.A8spt028;
import gov.hud.sams.entities.A8spt190;
import gov.hud.sams.entities.A8spt191;
import gov.hud.sams.util.SamsConstants;

import java.io.Serializable;

public class HudOneForm implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4074480617347822276L;
	private A8spt191 a8spt191;
//	private A8spt157 a8spt157;
	private A8spt190 a8spt190;
	private A8spt028 a8spt028;
	
	public HudOneForm() {
		//default
	}
	public A8spt191 getA8spt191() {
		return a8spt191;
	}
	public void setA8spt191(A8spt191 a8spt191) {
		this.a8spt191 = a8spt191;
	}
//	public A8spt157 getA8spt157() {
//		return a8spt157;
//	}
//	public void setA8spt157(A8spt157 a8spt157) {
//		this.a8spt157 = a8spt157;
//	}
	public A8spt190 getA8spt190() {
		return a8spt190;
	}
	public void setA8spt190(A8spt190 a8spt190) {
		this.a8spt190 = a8spt190;
	}
	public A8spt028 getA8spt028() {
		return a8spt028;
	}
	public void setA8spt028(A8spt028 a8spt028) {
		this.a8spt028 = a8spt028;
	}
	public void formatDates() {
		this.getA8spt191().setDateClosed(SamsConstants.convertDateFromJspToDb(this.getA8spt191().getDateClosed()));
		this.getA8spt191().setDateReceived(SamsConstants.convertDateFromJspToDb(this.getA8spt191().getDateReceived()));	
	}
	
	
	
}
