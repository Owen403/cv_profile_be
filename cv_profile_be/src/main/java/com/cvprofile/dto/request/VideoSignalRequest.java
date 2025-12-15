package com.cvprofile.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoSignalRequest {
    private String type; // "offer", "answer", "ice-candidate"
    private String fromUserId;
    private String toUserId;
    private Object data; // SDP or ICE candidate data
}
