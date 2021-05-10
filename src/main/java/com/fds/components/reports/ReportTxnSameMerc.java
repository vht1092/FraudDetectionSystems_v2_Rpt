package com.fds.components.reports;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;

import com.fds.SpringContextHelper;
import com.fds.TimeConverter;
import com.fds.services.DescriptionService;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.server.VaadinServlet;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

/**
 * Man hinh tim kiem
 * 
 */

@SpringComponent
@Scope("prototype")
public class ReportTxnSameMerc extends ReportForm {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(ReportTxnSameMerc.class);
	public static final String CAPTION = "BC MERCHANT O VN CO DOANH SO LON"; //BC MERCHANT O VN CO DOANH SO LON
	
	public ReportTxnSameMerc() {
		final SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		final DescriptionService descService = (DescriptionService) helper.getBean("descriptionService");
		
		dfToDate.setVisible(false);
		
		cbboxTypeCard.setVisible(true);
		descService.findAllByType("CARD").forEach(r -> {
			cbboxTypeCard.addItem(r.getId());
			cbboxTypeCard.setItemCaption(r.getId(), r.getDescription());
		});
		
		tfMcc.setVisible(true);
		tfMcc.setValidationVisible(false);
		tfMcc.setImmediate(true);
		
		tfMid.setVisible(true);
		tfMid.setValidationVisible(false);
		tfMid.setImmediate(true);
		
		tfTerminalId.setVisible(true);
		tfTerminalId.setValidationVisible(false);
		tfTerminalId.setImmediate(true);
		
		tfMerchantName.setVisible(true);
		tfMerchantName.setValidationVisible(false);
		tfMerchantName.setImmediate(true);
		
		tfBrnCode.setVisible(true);
		tfBrnCode.setValidationVisible(false);
		tfBrnCode.setImmediate(true);
		
		super.filename = "ReportBigMerchant.jasper";
	}
	
	@Override
	public boolean checkValidator() {
		try {
			dffromDate.validate();
			if (cbboxTypeCard.getValue() == null) {
				Notification.show("Vui lòng chọn CardType", Type.WARNING_MESSAGE);
			}
			return true;

		} catch (InvalidValueException ex) {
			dffromDate.setValidationVisible(true);
		}
		return false;
	}
	
	
	/*@Override
	public Map<String, Object> getParameter() {
		final TimeConverter timeConverter = new TimeConverter();
		final Map<String, Object> parameters = new HashMap<String, Object>();
		String pfromdate = timeConverter.convertDatetime(dffromDate.getValue(), false);
		parameters.put("p_fromdate", timeConverter.convertDatetime(dffromDate.getValue(), false));
		
		String cboxTypeCard = (String) cbboxTypeCard.getValue();
		if (cbboxTypeCard.getValue() != null) {
			parameters.put("p_crdtype", String.valueOf(cbboxTypeCard.getValue()));
		} else {
			parameters.put("p_crdtype", null);
		}
		
		String tfMcc11 = tfMcc.getValue();
		if (StringUtils.hasText(tfMcc.getValue())) {
			parameters.put("p_mcc", tfMcc.getValue().toString());
		} else {
			parameters.put("p_mcc", null);
		}
		
		String tfMid11 = tfMid.getValue();
		if (StringUtils.hasText(tfMid.getValue())) {
			parameters.put("p_mid", tfMid.getValue().toString());
		} else {
			parameters.put("p_mid", null);
		}
		
		String tfTerminalId11 = tfTerminalId.getValue();
		if (StringUtils.hasText(tfTerminalId.getValue())) {
			parameters.put("p_tid", tfTerminalId.getValue().toString());
		} else {
			parameters.put("p_tid", null);
		}
		
		String tfMerchantName11 = tfMerchantName.getValue();
		if (StringUtils.hasText(tfMerchantName.getValue())) {
			parameters.put("p_merchant", tfMerchantName.getValue().toString());
		} else {
			parameters.put("p_merchant", null);
		}
		
		String tfBrnCode11 = tfBrnCode.getValue();
		if (StringUtils.hasText(tfBrnCode.getValue())) {
			parameters.put("p_brncode", tfBrnCode.getValue().toString());
		} else {
			parameters.put("p_brncode", null);
		}
		
		Notification.show("pfromdate= "+pfromdate + " cboxTypeCard= "+ cboxTypeCard + " tfMcc11= " + tfMcc11 + " tfMid11= " +tfMid11 + " tfTerminalId11= "+ tfTerminalId11 + " tfMerchantName11= " + tfMerchantName11 + " tfBrnCode11= " + tfBrnCode11, Type.ERROR_MESSAGE);
		return parameters;
	}*/
	
	
	@Override
	public Map<String, Object> getParameter() {
		final TimeConverter timeConverter = new TimeConverter();
		final Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("p_fromdate", "201808140000000");
		
		
		
		
		parameters.put("p_crdtype", "VS");
		
		
		
		parameters.put("p_mcc", null);
		
		
		
		parameters.put("p_mid", null);
		
		
		
		parameters.put("p_tid", null);
		
		
		parameters.put("p_merchant", "zalo");
		
		
		
		parameters.put("p_brncode", null);
		
		
		return parameters;
	}
	

}
