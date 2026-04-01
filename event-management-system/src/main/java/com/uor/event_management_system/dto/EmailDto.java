package com.uor.event_management_system.dto;

import com.uor.event_management_system.enums.EmailTemplateType;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter @Setter
public class EmailDto {

    private String to;
    private String subject;
    private EmailTemplateType emailTemplateType;
    private Map<String, Object> variables;
}
