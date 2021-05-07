package com.enableets.edu.pakage.etm.bo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegionBO {

    /**
     * region id
     */
    private String id;

    private String x;

    private String y;

    private String width;

    private String height;

    /** div semtentce */
    private String semtenceInfoDiv;

    /** div to top distance */
    private String semtenceYtop;

}
