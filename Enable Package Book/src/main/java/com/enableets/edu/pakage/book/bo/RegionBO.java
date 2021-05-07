package com.enableets.edu.pakage.book.bo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegionBO {

    private String sequence;

    private String x;

    private String y;

    private String width;

    private String height;

    //private String affixPath;

    private String affixUrl;

    private ContentBO contentBO;

}
