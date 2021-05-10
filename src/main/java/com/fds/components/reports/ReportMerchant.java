package com.fds.components.reports;

import com.fds.SpringContextHelper;
import com.fds.services.CaseDetailService;
import com.vaadin.server.VaadinServlet;

public class ReportMerchant extends ReportForm {

	private static final long serialVersionUID = 1L;
	public static final String CAPTION = "BC THEO GIAO DỊCH THẺ CỦA KH";
	private final transient CaseDetailService caseDetailService;

	public ReportMerchant() {
		final SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		caseDetailService = (CaseDetailService) helper.getBean("caseDetailService");

		tfMerchantName.setVisible(true);
		tfTerminalId.setVisible(true);
		tfCardNo.setVisible(true);
		tfMcc.setVisible(true);

		super.filename = "BaoCaoMerchant.xlsx";

	}

//	@Override
//	public List<Object[]> getResult() {
//		if (dffromDate.getValue() != null && dfToDate.getValue() != null) {
//			final TimeConverter timeConverter = new TimeConverter();
//			final List<Object[]> listResult = caseDetailService.reportCaseByTxn(timeConverter.convertDatetime(dffromDate.getValue(), false),
//					timeConverter.convertDatetime(dfToDate.getValue(), true),
//					cbboxTypeCard.getValue() != null ? cbboxTypeCard.getValue().toString() : null);
//			return listResult;
//		}
//		return null;
//	}

}
