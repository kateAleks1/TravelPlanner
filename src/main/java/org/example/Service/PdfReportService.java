package org.example.Service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.example.entity.Trip;
import org.springframework.stereotype.Service;


import java.util.List;
import java.io.ByteArrayOutputStream;

@Service
public class PdfReportService {

    public byte[] generateUserTripReport(List<Trip> trips, String userName) throws Exception {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter.getInstance(document, out);
        document.open();

        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20);
        Paragraph title = new Paragraph("Отчет о путешествиях для пользователя: " + userName, font);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        document.add(new Paragraph("\n"));


        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setWidths(new int[]{3, 3, 4, 3});

        addTableHeader(table, "Поездка", "Старт", "Финиш", "Статус");

        for (Trip trip : trips) {
            table.addCell(trip.getCity().getCityName());
            table.addCell(trip.getStartDate().toString());
            table.addCell(trip.getEndDate().toString());
            table.addCell(trip.getStatusTrip().getStatusName());
        }

        document.add(table);

        document.close();
        return out.toByteArray();
    }

    private void addTableHeader(PdfPTable table, String... headers) {
        for (String header : headers) {
            PdfPCell cell = new PdfPCell();
            cell.setPhrase(new Phrase(header));
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);
        }
    }
}