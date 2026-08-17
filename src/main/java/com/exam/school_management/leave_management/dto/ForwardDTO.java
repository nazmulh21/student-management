package com.exam.school_management.leave_management.dto;

import lombok.Data;

@Data
public class ForwardDTO {
    private Long requestId;
    private Long forwarderId;
    private String forwarderName;
    private String forwarderDesignation;
    private Long receiverId;
    private String receiverName;
    private String comments;
}
