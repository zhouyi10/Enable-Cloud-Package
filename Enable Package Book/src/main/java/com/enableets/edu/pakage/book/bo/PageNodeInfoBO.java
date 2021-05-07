package com.enableets.edu.pakage.book.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageNodeInfoBO {

    /** sequence id  */
    private String sequence;

    /**img file path */
    private String image;

    /** img file width */
    private String width;

    /** img file height */
    private String height;

     /** affix file url */
    //private String affixPath;

    /** affix file downloadurl */
    private String affixUrl;

    private FileInfoBO pageFile;


    List<ItemNodeInfoBO>  itemNodeInfoBOS;

}
