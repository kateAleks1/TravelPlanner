package org.example.Controller;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.example.Dal.Repository.TripRepository;
import org.example.Service.PdfReportService;
import org.example.entity.Trip;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.*;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final PdfReportService pdfReportService;
    private final TripRepository tripRepository;

    public ReportController(PdfReportService pdfReportService, TripRepository tripRepository) {
        this.pdfReportService = pdfReportService;
        this.tripRepository = tripRepository;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<byte[]> generateReport(@PathVariable int userId) throws Exception {
        List<Trip> trips = tripRepository.getTripByUsersId(userId).get();

        String userName = "User " + userId;
        String formattedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        byte[] pdfReport = pdfReportService.generateUserTripReport(trips, userName + " (" + formattedDate + ")");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.builder("attachment")
                .filename("UserTripsReport_" + formattedDate + ".pdf")
                .build());

        return ResponseEntity.ok().headers(headers).body(pdfReport);
    }


}
