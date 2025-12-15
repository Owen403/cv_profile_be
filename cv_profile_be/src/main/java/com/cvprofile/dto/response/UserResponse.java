package com.cvprofile.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private String fullName;
    private String phone;
    private String address;
    private LocalDate birthDate;
    private String summary;
    private String avatar;
    private String linkedin;
    private String github;
    private String website;
    private String organization;
    private String hobbies;
    private String videoUrl;
}
