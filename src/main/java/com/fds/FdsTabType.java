package com.fds;

import com.fds.components.reports.*;
import com.vaadin.ui.Component;

/**
 * Danh sach cac component duoc them vao tabsheet
 * Khi them moi cap nhat vao table fds_sys_txn de load vao menu, fds_sys_txn.DESCRIPTION can co noi dung nhu caption cua class
 * @see com.fds.views.MainView
 * */

public enum FdsTabType {

	//Reports
	REPORTCASEACTIONBYUSER(ReportCaseActionByUser.class,ReportCaseActionByUser.CAPTION),
	REPORTCASE(ReportCaseTotal.class,ReportCaseTotal.CAPTION),
	REPORTCASEBYTXN(ReportCaseByTxn.class,ReportCaseByTxn.CAPTION),
	REPORTCASEBYSTATUS(ReportCaseStatus.class,ReportCaseStatus.CAPTION),
	REPORTCASERULEID(ReportCaseRuleId.class,ReportCaseRuleId.CAPTION),
	REPORTCASETXNCRDDET(ReportCaseTxnCrdDet.class,ReportCaseTxnCrdDet.CAPTION),
	REPORTTXNSAMEMERCHANT(ReportTxnSameMerc.class,ReportTxnSameMerc.CAPTION),
	REPORTEXPTIONCASE(ReportExptionCase.class,ReportExptionCase.CAPTION),
	REPORTTXNMONITOR(ReportTransactionMonitor.class,ReportTransactionMonitor.CAPTION),
	REPORTTMPLOCKCARD(ReportTempLockCard.class,ReportTempLockCard.CAPTION);

	private final String caption;
	private final Class<? extends Component> classComponent;

	private FdsTabType(Class<? extends Component> classComponent,String caption) {
		this.caption = caption;
		this.classComponent = classComponent;
	}

	public String getCaption() {
		return caption;
	}

	public Class<? extends Component> getClassComponent() {
		return classComponent;
	}	
	
	public static FdsTabType getTabType(final String caption){
		FdsTabType result=null;
		for (FdsTabType tabType:values()){
			if(tabType.getCaption().equals(caption)){
				result=tabType;
				break;
			}
		}
		return result;
	}
	

}
