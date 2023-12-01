package com.innogent.rishii.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.catalina.User;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponsePayload {

    private long id;
    private String name;
    private UserDTO createdBy;
    private Long CompanyId;
}
