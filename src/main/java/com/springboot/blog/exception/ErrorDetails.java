package com.springboot.blog.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@AllArgsConstructor
@Getter
@Setter
public class ErrorDetails {
    private Date timestamp;
    private String message;
    private String details;


}
