package com.tontron.security.auth.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Result {
    private int code;
    private String msg;
}
