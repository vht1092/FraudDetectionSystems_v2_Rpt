package com.fds.components.reports;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.util.StringUtils;

import com.fds.TimeConverter;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

@SpringComponent
@Scope("prototype")
public class ReportCaseTxnCrdDet extends ReportForm {

	private static final long serialVersionUID = 1L;
	public static final String CAPTION = "BC CHI TIẾT THEO GIAO DỊCH THẺ";

	public ReportCaseTxnCrdDet() {

		tfMerchantName.setVisible(true);
		tfMerchantName.setValidationVisible(false);
		tfMerchantName.setImmediate(true);

		tfTerminalId.setVisible(true);
		tfTerminalId.setValidationVisible(false);
		tfTerminalId.setImmediate(true);

		tfCardNo.setVisible(true);
		tfCardNo.setValidationVisible(false);
		tfCardNo.setImmediate(true);

		tfMcc.setVisible(true);
		tfMcc.setValidationVisible(false);
		tfMcc.setImmediate(true);
		
		tfMid.setVisible(true);
		tfMid.setValidationVisible(false);
		tfMid.setImmediate(true);

		super.filename = "ReportTxnDetail.jasper";
	}

	@Override
	public boolean checkValidator() {
		try {
			dffromDate.validate();
			dfToDate.validate();
			if (!StringUtils.hasText(tfMerchantName.getValue()) && !StringUtils.hasText(tfTerminalId.getValue())
					&& !StringUtils.hasText(tfCardNo.getValue()) && !StringUtils.hasText(tfMcc.getValue()) && !StringUtils.hasText(tfMid.getValue())) {
				Notification.show("Vui điền ít nhất một trong các tiêu chí", Type.WARNING_MESSAGE);
			}
			//Han che khoan thoi gian toi da la 30 ngay
			long diff = dfToDate.getValue().getTime() - dffromDate.getValue().getTime();
			System.out.println("So ngay tao bao cao:" + diff);
			return true;

		} catch (InvalidValueException ex) {
			dffromDate.setValidationVisible(true);
			dfToDate.setValidationVisible(true);			
		}
		return false;
	}

	@Override
	public Map<String, Object> getParameter() {
		final TimeConverter timeConverter = new TimeConverter();
		final Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("p_fromdate", timeConverter.convertDatetime(dffromDate.getValue(), false));
		parameters.put("p_todate", timeConverter.convertDatetime(dfToDate.getValue(), true));
		if (StringUtils.hasText(tfMerchantName.getValue())) {
			parameters.put("p_merchant", tfMerchantName.getValue().toString());
		} else {
			parameters.put("p_merchant", null);
		}
		if (StringUtils.hasText(tfTerminalId.getValue())) {
			parameters.put("p_tid", tfTerminalId.getValue().toString());
		} else {
			parameters.put("p_tid", null);
		}
		if (StringUtils.hasText(tfCardNo.getValue())) {
			parameters.put("p_crdno", tfCardNo.getValue().toString().replaceAll("[\\s|\\u00A0]+", ""));
		} else {
			parameters.put("p_crdno", null);
		}
		if (StringUtils.hasText(tfMcc.getValue())) {
			parameters.put("p_mcc", tfMcc.getValue().toString());
		} else {
			parameters.put("p_mcc", null);
		}
		if (StringUtils.hasText(tfMid.getValue())) {
			parameters.put("p_mid", tfMid.getValue().toString());
		} else {
			parameters.put("p_mid", null);
		}
		return parameters;
	}

}
