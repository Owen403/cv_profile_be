package com.cvprofile.service;

import com.cvprofile.entity.*;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
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
    
    private final ProfileService profileService;
    
    public byte[] generateCvPdf(Long profileId) throws Exception {
        Profile profile = profileService.getProfileById(profileId)
                .orElseThrow(() -> new RuntimeException("Profile not found with id: " + profileId));
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument);
        
  
        PdfFont boldFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        PdfFont regularFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        
        
        Paragraph name = new Paragraph(profile.getFullName())
                .setFont(boldFont)
                .setFontSize(24)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(5);
        document.add(name);
        
       
        StringBuilder contact = new StringBuilder();
        if (profile.getEmail() != null) contact.append("Email: ").append(profile.getEmail()).append(" | ");
        if (profile.getPhone() != null) contact.append("Phone: ").append(profile.getPhone()).append(" | ");
        if (profile.getAddress() != null) contact.append(profile.getAddress());
        
        Paragraph contactInfo = new Paragraph(contact.toString())
                .setFont(regularFont)
                .setFontSize(10)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(5);
        document.add(contactInfo);
        

        if (profile.getLinkedin() != null || profile.getGithub() != null || profile.getWebsite() != null) {
            StringBuilder links = new StringBuilder();
            if (profile.getLinkedin() != null) links.append("LinkedIn: ").append(profile.getLinkedin()).append(" | ");
            if (profile.getGithub() != null) links.append("GitHub: ").append(profile.getGithub()).append(" | ");
            if (profile.getWebsite() != null) links.append("Website: ").append(profile.getWebsite());
            
            Paragraph linksInfo = new Paragraph(links.toString())
                    .setFont(regularFont)
                    .setFontSize(9)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(15);
            document.add(linksInfo);
        }
        
       
        if (profile.getSummary() != null && !profile.getSummary().isEmpty()) {
            addSectionTitle(document, "SUMMARY", boldFont);
            Paragraph summary = new Paragraph(profile.getSummary())
                    .setFont(regularFont)
                    .setFontSize(10)
                    .setMarginBottom(10);
            document.add(summary);
        }
        
      
        if (profile.getSkills() != null && !profile.getSkills().isEmpty()) {
            addSectionTitle(document, "SKILLS", boldFont);
            for (Skill skill : profile.getSkills()) {
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
        
       
        if (profile.getProjects() != null && !profile.getProjects().isEmpty()) {
            addSectionTitle(document, "PROJECTS", boldFont);
            for (Project project : profile.getProjects()) {
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
        if (profile.getPublications() != null && !profile.getPublications().isEmpty()) {
            addSectionTitle(document, "PUBLICATIONS", boldFont);
            for (Publication pub : profile.getPublications()) {
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
        if (profile.getEvents() != null && !profile.getEvents().isEmpty()) {
            addSectionTitle(document, "EVENTS & ACTIVITIES", boldFont);
            for (Event event : profile.getEvents()) {
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
