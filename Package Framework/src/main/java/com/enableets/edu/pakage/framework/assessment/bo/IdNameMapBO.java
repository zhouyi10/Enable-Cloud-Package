package com.enableets.edu.pakage.framework.assessment.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IdNameMapBO implements Serializable {

    private static final long serialVersionUID = 2694599644864008054L;

    private String id;

    private String name;
}
