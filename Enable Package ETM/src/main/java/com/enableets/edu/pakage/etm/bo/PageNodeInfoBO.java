package com.enableets.edu.pakage.etm.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageNodeInfoBO {

    /**
     * page Id
     */
    private String id;

    /** sequence id  */
    private String sequence;

    /**img file url */
    private String file;

    /** img file downloadurl */
    private String imgSrc;

    /** img file width */
    private String width;

    /** img file height */
    private String height;

     /** media file url */
    private String media;

    /** media file downloadurl */
    private String mediaSrc;


    List<ItemNodeInfoBO>   itemNodeInfoBOS;

}
