package ch.hftm.wtm.business;

import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import ch.hftm.wtm.model.ReportData;
import ch.hftm.wtm.model.ReportMasterData;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.VerticalTextAlignment;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class ReportBuilder {

    // Objekte
    private List<ReportData> reportDataList;
    private ReportMasterData reportMasterData;
    private String pdfPath;

    // Styles die für das Design des Reports verwendet werden
    public static final StyleBuilder bigTitleStyle = DynamicReports.stl.style().setFontSize(20).setBold(true)
            .setVerticalTextAlignment(VerticalTextAlignment.MIDDLE)
            .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);
    public static final StyleBuilder boldStyle = DynamicReports.stl.style().setBold(true);
    public static final StyleBuilder horizontalCenteredStyle = DynamicReports.stl.style()
            .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);
    public static final StyleBuilder verticalBottomStyle = DynamicReports.stl.style()
            .setVerticalTextAlignment(VerticalTextAlignment.BOTTOM);
    public static final StyleBuilder verticalCenteredStyle = DynamicReports.stl.style()
            .setVerticalTextAlignment(VerticalTextAlignment.MIDDLE);
    public static final StyleBuilder verticalHorizontalCenteredStyle = DynamicReports.stl.style()
            .setVerticalTextAlignment(VerticalTextAlignment.MIDDLE)
            .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);
    public static final StyleBuilder leftPaddingStyle = DynamicReports.stl.style().setLeftPadding(0);
    public static final StyleBuilder rightPaddingStyle = DynamicReports.stl.style().setRightPadding(50);
    public static final StyleBuilder rightBottomPaddingStyle = DynamicReports.stl.style().setRightPadding(0)
            .setVerticalTextAlignment(VerticalTextAlignment.BOTTOM);
    public static final StyleBuilder rootStyle = DynamicReports.stl.style().setPadding(2);
    public static final StyleBuilder boldCenteredStyle = DynamicReports.stl.style(boldStyle);
    public static final StyleBuilder headerStyle = DynamicReports.stl.style()
            .setVerticalTextAlignment(VerticalTextAlignment.BOTTOM).setBottomBorder(DynamicReports.stl.pen1Point());
    public static final StyleBuilder columnTitelStyle = DynamicReports.stl.style().setBold(true)
            .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER).setFontSize(15)
            .setBorder(DynamicReports.stl.pen1Point());
    public static final StyleBuilder rowUnderlineStyle = DynamicReports.stl.style()
            .setBottomBorder(DynamicReports.stl.pen1Point());

    // Footer
    public static final ComponentBuilder<?, ?> footerComponent = DynamicReports.cmp.pageXofY()
            .setStyle(DynamicReports.stl.style(boldCenteredStyle).setTopBorder(DynamicReports.stl.pen1Point()));

    // Konstruktor
    /**
     * @param reportData die abgefüllt werden soll
     * @param reportMasterData die abgefüllt werden soll
     * @param pdfPath Path an dem das pdf abgelget werden soll
     */
    public ReportBuilder(List<ReportData> reportData, ReportMasterData reportMasterData, String pdfPath) {
        this.reportDataList = reportData;
        this.reportMasterData = reportMasterData;
        this.pdfPath = pdfPath;
    }

    /**
     * @return String HTML-Preview
     */
    public String getReport() {
        return build();
    }

    /**
     * @return String HTML-Preview
     */
    private String build() {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            report()
                    // Titel für den Report setzen
                    .title(getHeader()).pageFooter(footerComponent)
                    .addColumn(
                            /*jkl
                             * Columns definieren Für den Arbeitstag
                             * vorübergehend ein StringType
                             */
                            col.column("Arbeitstag", "workingday", type.stringType()).setStyle(rowUnderlineStyle),
                            col.column("Abwesenheit", "absenceType", type.stringType()).setStyle(rowUnderlineStyle),
                            col.column("Stempelung", "stamping", type.listType()).setStyle(rowUnderlineStyle),
                            col.column("Arbeitszeit", "worktime", type.stringType()).setStyle(rowUnderlineStyle))
                    .setColumnTitleStyle(columnTitelStyle)
                    .addSummary(DynamicReports.cmp.verticalList(
                            // Platzhalter damit eine Zeile Abstand dazwischen
                            // ist
                            DynamicReports.cmp.text(""),
                            DynamicReports.cmp.text("Sollzeit: " + reportMasterData.getToTime()),
                            DynamicReports.cmp.text("Istzeit: " + reportMasterData.getActualTime()),
                            DynamicReports.cmp.text("Überzeit: "+ reportMasterData.getOvertime() ), DynamicReports.cmp.text("Feriensaldo alt: 24")))
                    .setDataSource(new JRBeanCollectionDataSource(reportDataList))
                    .toPdf(new FileOutputStream(pdfPath))
                    .toHtml(bos);

        } catch (Exception e) {
            e.printStackTrace();
        }
        byte[] byteArray = bos.toByteArray();
        return new String(byteArray);
    }

    /**
     * @return Header Component
     */
    private ComponentBuilder<?, ?> getHeader() {
        // Formatiert den Sting in folgendes Pattern yyyy-mm
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy");
        Date fromDate = reportMasterData.getFromDate();
        Date tillDate = reportMasterData.getTillDate();
        String fromDatefor = dateFormatter.format(fromDate);
        String tillDatefor = dateFormatter.format(tillDate);

        return DynamicReports.cmp.horizontalList()
                // Bild für die PDF-Version setzen
                .add(DynamicReports.cmp.image(Paths.get("")
                        .toAbsolutePath() + "/src/main/webapp/resources/style/css/images/wtm_icon_small.png"),
                        DynamicReports.cmp.text(fromDatefor + " - " + tillDatefor).setStyle(bigTitleStyle),
                        DynamicReports.cmp.verticalList().add(
                                // Platzhalter damit das Feld weiter unten steht
                                DynamicReports.cmp.text(""), 
                                DynamicReports.cmp.text(""), 
                                DynamicReports.cmp.text(reportMasterData.getPerson().getFirstName() + " "
                                        + reportMasterData.getPerson().getLastName()),
                                DynamicReports.cmp.text(reportMasterData.getPerson().getStreet()),
                                DynamicReports.cmp.text(reportMasterData.getPerson().getPostalCode() + " " + reportMasterData.getPerson().getCity()).setStyle(boldStyle),
                                DynamicReports.cmp.text("Nr: " + reportMasterData.getPerson().getId())
                                        .setStyle(boldStyle),
                                DynamicReports.cmp
                                        .text("Arbeitspensum: " + reportMasterData.getLevelOfEmployment() + "%")
                                        .setStyle(boldStyle)))
                .setStyle(headerStyle);
    }
}
