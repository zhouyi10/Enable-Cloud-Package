package com.enableets.edu.pakage.etm.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemNodeInfoBO {

    /**
     * item id
     */
    private String id;

    /** sequence id  */
    private String sequence;

    /** media url */
    private String media;

    /** media downloadurl*/
    private String mediaSrc;

    /** content info */
    private ContentBO contentBO;

    private List<RegionBO> regionBOS;
}
