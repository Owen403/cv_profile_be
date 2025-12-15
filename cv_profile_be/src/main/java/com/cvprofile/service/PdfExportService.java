package com.cvprofile.service;

import com.cvprofile.entity.*;
import com.cvprofile.exception.ApiException;
import com.cvprofile.exception.ErrorCode;
import com.cvprofile.repository.UserRepository;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.io.font.constants.StandardFonts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class PdfExportService {
    
    private final UserRepository userRepository;
    
    public byte[] generateCvPdf(Long userId) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND, "User not found with id: " + userId));
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument);
        
  
        PdfFont boldFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        PdfFont regularFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        
        
        Paragraph name = new Paragraph(user.getFullName())
                .setFont(boldFont)
                .setFontSize(24)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(5);
        document.add(name);
        
       
        StringBuilder contact = new StringBuilder();
        if (user.getEmail() != null) contact.append("Email: ").append(user.getEmail()).append(" | ");
        if (user.getPhone() != null) contact.append("Phone: ").append(user.getPhone()).append(" | ");
        if (user.getAddress() != null) contact.append(user.getAddress());
        
        Paragraph contactInfo = new Paragraph(contact.toString())
                .setFont(regularFont)
                .setFontSize(10)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(5);
        document.add(contactInfo);
        

        if (user.getLinkedin() != null || user.getGithub() != null || user.getWebsite() != null) {
            StringBuilder links = new StringBuilder();
            if (user.getLinkedin() != null) links.append("LinkedIn: ").append(user.getLinkedin()).append(" | ");
            if (user.getGithub() != null) links.append("GitHub: ").append(user.getGithub()).append(" | ");
            if (user.getWebsite() != null) links.append("Website: ").append(user.getWebsite());
            
            Paragraph linksInfo = new Paragraph(links.toString())
                    .setFont(regularFont)
                    .setFontSize(9)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(15);
            document.add(linksInfo);
        }
        
       
        if (user.getSummary() != null && !user.getSummary().isEmpty()) {
            addSectionTitle(document, "SUMMARY", boldFont);
            Paragraph summary = new Paragraph(user.getSummary())
                    .setFont(regularFont)
                    .setFontSize(10)
                    .setMarginBottom(10);
            document.add(summary);
        }
        
      
        if (user.getSkills() != null && !user.getSkills().isEmpty()) {
            addSectionTitle(document, "SKILLS", boldFont);
            for (Skill skill : user.getSkills()) {
                String skillText = String.format("• %s (%s) - Level: %d/5", 
                        skill.getName(), 
                        skill.getCategory() != null ? skill.getCategory() : "General",
                        skill.getLevel());
                Paragraph skillPara = new Paragraph(skillText)
                        .setFont(regularFont)
                        .setFontSize(10)
                        .setMarginLeft(10);
                document.add(skillPara);
            }
            document.add(new Paragraph("\n").setMarginBottom(5));
        }
        
       
        if (user.getProjects() != null && !user.getProjects().isEmpty()) {
            addSectionTitle(document, "PROJECTS", boldFont);
            for (Project project : user.getProjects()) {
                Paragraph projectTitle = new Paragraph(project.getTitle())
                        .setFont(boldFont)
                        .setFontSize(12)
                        .setMarginLeft(10);
                document.add(projectTitle);
                
                if (project.getRole() != null) {
                    Paragraph role = new Paragraph("Role: " + project.getRole())
                            .setFont(regularFont)
                            .setFontSize(9)
                            .setMarginLeft(10)
                            .setItalic();
                    document.add(role);
                }
                
                if (project.getStartDate() != null) {
                    String dateRange = project.getStartDate().format(DateTimeFormatter.ofPattern("MMM yyyy"));
                    if (project.getEndDate() != null) {
                        dateRange += " - " + project.getEndDate().format(DateTimeFormatter.ofPattern("MMM yyyy"));
                    } else {
                        dateRange += " - Present";
                    }
                    Paragraph dates = new Paragraph(dateRange)
                            .setFont(regularFont)
                            .setFontSize(9)
                            .setMarginLeft(10);
                    document.add(dates);
                }
                
                if (project.getDescription() != null) {
                    Paragraph desc = new Paragraph(project.getDescription())
                            .setFont(regularFont)
                            .setFontSize(10)
                            .setMarginLeft(10);
                    document.add(desc);
                }
                
                if (project.getTechnologies() != null) {
                    Paragraph tech = new Paragraph("Technologies: " + project.getTechnologies())
                            .setFont(regularFont)
                            .setFontSize(9)
                            .setMarginLeft(10);
                    document.add(tech);
                }
                
                document.add(new Paragraph("\n").setMarginBottom(5));
            }
        }
        
        // Publications
        if (user.getPublications() != null && !user.getPublications().isEmpty()) {
            addSectionTitle(document, "PUBLICATIONS", boldFont);
            for (Publication pub : user.getPublications()) {
                Paragraph pubTitle = new Paragraph(pub.getTitle())
                        .setFont(boldFont)
                        .setFontSize(11)
                        .setMarginLeft(10);
                document.add(pubTitle);
                
                if (pub.getAuthors() != null) {
                    Paragraph authors = new Paragraph(pub.getAuthors())
                            .setFont(regularFont)
                            .setFontSize(9)
                            .setMarginLeft(10)
                            .setItalic();
                    document.add(authors);
                }
                
                if (pub.getJournal() != null || pub.getConference() != null) {
                    String venue = pub.getJournal() != null ? pub.getJournal() : pub.getConference();
                    Paragraph pubVenue = new Paragraph(venue)
                            .setFont(regularFont)
                            .setFontSize(9)
                            .setMarginLeft(10);
                    document.add(pubVenue);
                }
                
                if (pub.getPublishedDate() != null) {
                    Paragraph pubDate = new Paragraph(pub.getPublishedDate().format(DateTimeFormatter.ofPattern("MMM yyyy")))
                            .setFont(regularFont)
                            .setFontSize(9)
                            .setMarginLeft(10);
                    document.add(pubDate);
                }
                
                document.add(new Paragraph("\n").setMarginBottom(5));
            }
        }
        
        // Events
        if (user.getEvents() != null && !user.getEvents().isEmpty()) {
            addSectionTitle(document, "EVENTS & ACTIVITIES", boldFont);
            for (Event event : user.getEvents()) {
                Paragraph eventTitle = new Paragraph(event.getTitle())
                        .setFont(boldFont)
                        .setFontSize(11)
                        .setMarginLeft(10);
                document.add(eventTitle);
                
                if (event.getRole() != null) {
                    Paragraph role = new Paragraph("Role: " + event.getRole())
                            .setFont(regularFont)
                            .setFontSize(9)
                            .setMarginLeft(10);
                    document.add(role);
                }
                
                if (event.getEventType() != null) {
                    Paragraph type = new Paragraph("Type: " + event.getEventType())
                            .setFont(regularFont)
                            .setFontSize(9)
                            .setMarginLeft(10);
                    document.add(type);
                }
                
                if (event.getStartDate() != null) {
                    String eventDate = event.getStartDate().format(DateTimeFormatter.ofPattern("MMM dd, yyyy"));
                    if (event.getEndDate() != null) {
                        eventDate += " - " + event.getEndDate().format(DateTimeFormatter.ofPattern("MMM dd, yyyy"));
                    }
                    Paragraph dates = new Paragraph(eventDate)
                            .setFont(regularFont)
                            .setFontSize(9)
                            .setMarginLeft(10);
                    document.add(dates);
                }
                
                if (event.getDescription() != null) {
                    Paragraph desc = new Paragraph(event.getDescription())
                            .setFont(regularFont)
                            .setFontSize(10)
                            .setMarginLeft(10);
                    document.add(desc);
                }
                
                document.add(new Paragraph("\n").setMarginBottom(5));
            }
        }
        
        document.close();
        return outputStream.toByteArray();
    }
    
    private void addSectionTitle(Document document, String title, PdfFont font) {
        Paragraph sectionTitle = new Paragraph(title)
                .setFont(font)
                .setFontSize(14)
                .setFontColor(ColorConstants.DARK_GRAY)
                .setMarginTop(10)
                .setMarginBottom(5);
        document.add(sectionTitle);
    }
}
