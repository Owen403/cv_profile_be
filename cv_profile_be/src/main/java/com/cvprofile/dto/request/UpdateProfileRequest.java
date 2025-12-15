package com.cvprofile.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateProfileRequest {
    @Size(max = 100, message = "Full name must be less than 100 characters")
    private String fullName;
    
    @Size(max = 20, message = "Phone must be less than 20 characters")
    private String phone;
    
    @Size(max = 2000, message = "Address must be less than 2000 characters")
    private String address;
    
    @Size(max = 5000, message = "Summary must be less than 5000 characters")
    private String summary;
    @Size(max = 200, message = "Organization must be less than 200 characters")
    private String organization;
    @Size(max = 1000, message = "Hobbies must be less than 1000 characters")
    private String hobbies;
    private String avatar;
    @Size(max = 500, message = "LinkedIn URL must be less than 500 characters")
    private String linkedin;
    
    @Size(max = 500, message = "GitHub URL must be less than 500 characters")
    private String github;
    
    @Size(max = 500, message = "Website URL must be less than 500 characters")
    private String website;
}
