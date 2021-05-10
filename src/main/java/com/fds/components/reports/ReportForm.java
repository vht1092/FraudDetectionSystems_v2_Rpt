package com.fds.components.reports;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Period;
import java.util.Map;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.vaadin.simplefiledownloader.SimpleFileDownloader;

import com.fds.SecurityDataSourceConfig;
import com.fds.SecurityUtils;
import com.fds.SpringConfigurationValueHelper;
import com.fds.SpringContextHelper;
import com.vaadin.data.validator.NullValidator;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;


/*
 * add them menu
 *  - Cap nhat class: FdsTabType
 *  - Cap nhat DB: FDS_SYS_TXN, FDS_SYS_ROLETXN
 */
@SpringComponent
@Scope("prototype")
public class ReportForm extends CustomComponent {

	private static final long serialVersionUID = 7496781413585717785L;
	public DateField dffromDate;
	public DateField dfToDate;
	public ComboBox cbboxTypeAction;
	public ComboBox cbboxTypeCard;
	public ComboBox cbboxCaseStatus;
	public ComboBox cbboxUser;
	public ComboBox cbboxRuleId;
	public TextField tfMerchantName;
	public TextField tfTerminalId;
	public TextField tfCardNo;
	public TextField tfMcc;
	public TextField tfMid;
	public TextField tfCif;
	public TextField tfBrnCode;
	public TextField tfAuthStat;
	public TextField tfSysStat;
	public TextField tfRecurringInd;
	private static final String INPUT_FIELD = "Vui lòng chọn giá trị";
	private SpringConfigurationValueHelper configurationHelper;
	private static final Logger LOGGER = LoggerFactory.getLogger(ReportForm.class);
	public String filename;

	protected DataSource localDataSource;
	protected SecurityDataSourceConfig securityDataSourceConfig;

	public ReportForm() {
		FormLayout mainLayout = new FormLayout();
		mainLayout.setMargin(true);

		final SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		configurationHelper = (SpringConfigurationValueHelper) helper.getBean("springConfigurationValueHelper");
		localDataSource = (DataSource) helper.getBean("dataSource");

		dffromDate = new DateField("Từ ngày");
		dffromDate.setDateFormat("dd/MM/yyyy");
		dffromDate.addValidator(new NullValidator(INPUT_FIELD, false));
		dffromDate.setValidationVisible(false);

		dfToDate = new DateField("Đến ngày");
		dfToDate.setDateFormat("dd/MM/yyyy");
		dfToDate.addValidator(new NullValidator(INPUT_FIELD, false));
		dfToDate.setValidationVisible(false);

		cbboxCaseStatus = new ComboBox("Tình trạng case");
		cbboxCaseStatus.setVisible(false);
		cbboxCaseStatus.setItemCaptionMode(ItemCaptionMode.EXPLICIT);

		cbboxTypeAction = new ComboBox("Loại thao tác");
		cbboxTypeAction.setVisible(false);
		cbboxTypeAction.setItemCaptionMode(ItemCaptionMode.EXPLICIT);

		cbboxTypeCard = new ComboBox("Loại thẻ");
		cbboxTypeCard.setVisible(false);
		cbboxTypeCard.setItemCaptionMode(ItemCaptionMode.EXPLICIT);

		cbboxUser = new ComboBox("Người dùng");
		cbboxUser.setVisible(false);

		cbboxRuleId = new ComboBox("Rule id");
		cbboxRuleId.setVisible(false);

		tfMerchantName = new TextField("Merchant Name");
		tfMerchantName.setVisible(false);

		tfTerminalId = new TextField("Terminal ID");
		tfTerminalId.setVisible(false);

		tfCardNo = new TextField("Card No");
		tfCardNo.setVisible(false);

		tfMcc = new TextField("MCC");
		tfMcc.setVisible(false);

		tfMid = new TextField("MID");
		tfMid.setVisible(false);
		
		tfCif = new TextField("CIF");
		tfCif.setVisible(false);
		
		tfBrnCode = new TextField("Mã ĐV");
		tfBrnCode.setVisible(false);
		
		tfAuthStat = new TextField("Auth Status");
		tfAuthStat.setVisible(false);
		
		tfSysStat = new TextField("Sys Status");
		tfSysStat.setVisible(false);
		
		tfRecurringInd = new TextField("Recurring Indicator");
		tfRecurringInd.setVisible(false);
		
		final HorizontalLayout exportLayout = new HorizontalLayout();
		exportLayout.setSpacing(true);

		final Button btPDFDowload = new Button("PDF");
		btPDFDowload.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		btPDFDowload.setIcon(FontAwesome.DOWNLOAD);

		final Button btXLSXDowload = new Button("XLSX");
		btXLSXDowload.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		btXLSXDowload.setIcon(FontAwesome.DOWNLOAD);

		SimpleFileDownloader downloader = new SimpleFileDownloader();
		addExtension(downloader);

		// Dowload file pdf
		final StreamResource myResourcePDF = createTransMKResourcePDF();
		btPDFDowload.addClickListener(evt -> {
			long diff = dfToDate.getValue().getTime() - dffromDate.getValue().getTime();
			long diffDays = diff / (24 * 60 * 60 * 1000);
				
			if(diffDays>732) {
				Notification.show("Lỗi","Khung thời gian vượt quá 2 năm.", Type.ERROR_MESSAGE);
				return;
			}
			
			if (checkValidator()) {
				downloader.setFileDownloadResource(myResourcePDF);
				downloader.download();
			}
			
		});
		// Dowload file xlsx
		final StreamResource myResourceXLSX = createTransMKResourceXLS();
		btXLSXDowload.addClickListener(evt -> {
			long diff = dfToDate.getValue().getTime() - dffromDate.getValue().getTime();
			long diffDays = diff / (24 * 60 * 60 * 1000);
				
			if(diffDays>732) {
				Notification.show("Lỗi","Khung thời gian vượt quá 2 năm.", Type.ERROR_MESSAGE);
				return;
			}
			
			if (checkValidator()) {
				downloader.setFileDownloadResource(myResourceXLSX);
				downloader.download();
			}
			
		});
		// final AdvancedFileDownloader fileDownloaderPDF = new AdvancedFileDownloader(myResourcePDF);
		// fileDownloaderPDF.addAdvancedDownloaderListener(new AdvancedDownloaderListener() {
		//
		// @Override
		// public boolean beforeDownload(DownloaderEvent downloadEvent) {
		// try {
		// dffromDate.validate();
		// dfToDate.validate();
		// return true;
		// } catch (InvalidValueException ex) {
		// dffromDate.setValidationVisible(true);
		// dfToDate.setValidationVisible(true);
		// LOGGER.error("Validate");
		// return false;
		// }
		//
		// }
		// });
		// fileDownloaderPDF.extend(btPDFDowload);

		// // Dowload file xlsx
		// final StreamResource myResourceXLSX = createTransMKResourceXLS();
		// final AdvancedFileDownloader fileDownloaderXLSX = new AdvancedFileDownloader(myResourceXLSX);
		// fileDownloaderXLSX.extend(btXLSXDowload);

		mainLayout.addComponent(dffromDate);
		mainLayout.addComponent(dfToDate);
		mainLayout.addComponent(cbboxTypeAction);
		mainLayout.addComponent(cbboxCaseStatus);
		mainLayout.addComponent(cbboxTypeCard);
		mainLayout.addComponent(tfMid);
		mainLayout.addComponent(cbboxUser);
		mainLayout.addComponent(cbboxRuleId);
		mainLayout.addComponent(tfMerchantName);
		mainLayout.addComponent(tfTerminalId);
		mainLayout.addComponent(tfCardNo);
		mainLayout.addComponent(tfMcc);
		mainLayout.addComponent(tfCif);
		mainLayout.addComponent(tfBrnCode);
		mainLayout.addComponent(tfAuthStat);
		mainLayout.addComponent(tfSysStat);
		mainLayout.addComponent(tfRecurringInd);
		
		exportLayout.addComponent(btPDFDowload);
		exportLayout.addComponent(btXLSXDowload);

		mainLayout.addComponent(exportLayout);

		setCompositionRoot(mainLayout);
	}

	public Map<String, Object> getParameter() {
		return null;
	}

	@SuppressWarnings("serial")
	private StreamResource createTransMKResourcePDF() {
		return new StreamResource(new StreamSource() {
			@Override
			public InputStream getStream() {
				try {
					final ByteArrayOutputStream outpuf = makeFileForDownLoad(filename, "PDF");
					return new ByteArrayInputStream(outpuf.toByteArray());
				} catch (Exception e) {
					LOGGER.error("createTransMKResourcePDF - Message: " + ExceptionUtils.getFullStackTrace(e));
				}
				return null;

			}
		}, "fds_baocao.pdf");
	}

	@SuppressWarnings("serial")
	private StreamResource createTransMKResourceXLS() {
		return new StreamResource(new StreamSource() {
			@Override
			public InputStream getStream() {

				try {
					final ByteArrayOutputStream outpuf = makeFileForDownLoad(filename, "XLSX");
					return new ByteArrayInputStream(outpuf.toByteArray());
				} catch (Exception e) {
					LOGGER.error("createTransMKResourceXLS - Message: " + ExceptionUtils.getFullStackTrace(e));
				}
				return null;

			}
		}, "fds_baocao.xlsx");
	}

	private ByteArrayOutputStream makeFileForDownLoad(String filename, String extension) throws JRException, SQLException {
		Connection con = null;
//		if(filename.equals("ReportTxnDetail.jasper") && configurationHelper.getTurnonDatasource2nd().equals("true")) {
//			securityDataSourceConfig = new SecurityDataSourceConfig();
//			con = securityDataSourceConfig.securityDataSource().getConnection();
//		} else {
			con = localDataSource.getConnection();
//		}
		
		final ByteArrayOutputStream output = new ByteArrayOutputStream();
		if (this.getParameter() != null) {
			// Tham so truyen vao cho bao cao
			final Map<String, Object> parameters = this.getParameter();

			final JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(configurationHelper.getPathTemplateReport() + "/" + filename);
			final JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, con);

			// Xuat file Excel
			if (extension.equals("XLSX")) {
				LOGGER.info(SecurityUtils.getUserId() + ": export XLSX template " + filename);
				for (Map.Entry entry : getParameter().entrySet())
				{
					if(entry.getKey().toString().matches("(p_cardno|p_crdno)") && entry.getValue()!=null){
						String mapValue = entry.getValue().toString().replaceAll("[\\s|\\u00A0]+", "");
						LOGGER.info(entry.getKey() + ": " + (mapValue.length()>=16 ? mapValue.substring(0, 6) + "XXXXXX" + mapValue.substring(12) : ""));
					}
					else
						LOGGER.info(entry.getKey() + ": " + entry.getValue());
					
				}
				final JRXlsxExporter xlsx = new JRXlsxExporter();
				xlsx.setExporterInput(new SimpleExporterInput(jasperPrint));
				xlsx.setExporterOutput(new SimpleOutputStreamExporterOutput(output));
				xlsx.exportReport();
			} else if (extension.equals("PDF")) { // File PDF
				LOGGER.info(SecurityUtils.getUserId() + ": export PDF template " + filename);
				for (Map.Entry entry : getParameter().entrySet())
				{
					if(entry.getKey().toString().matches("(p_cardno|p_crdno)") && entry.getValue()!=null){
						String mapValue = entry.getValue().toString().replaceAll("[\\s|\\u00A0]+", "");
						LOGGER.info(entry.getKey() + ": " + (mapValue.length()>=16 ? mapValue.substring(0, 6) + "XXXXXX" + mapValue.substring(12) : ""));
					}
					else
						LOGGER.info(entry.getKey() + ": " + entry.getValue());
				}
				JasperExportManager.exportReportToPdfStream(jasperPrint, output);
			}
			return output;
		} else {
			return null;
		}

	}

	public boolean checkValidator() {
		return false;
	}
	
}
