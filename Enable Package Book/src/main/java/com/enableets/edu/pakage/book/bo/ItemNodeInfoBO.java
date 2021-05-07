package com.enableets.edu.pakage.book.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemNodeInfoBO {


    /** sequence id  */
    private String sequence;

    /** affix url */
    //private String affixPath;

    /** affix downloadurl*/
    private String affixUrl;

    /** question parent id*/
    private String parentId;

    /** question id */
    private String questionId;

    /** content info */
    private ContentBO contentBO;

    /** content region*/
    private ContentRegionBO contentRegionBO;

    private List<RegionBO> regionBOS;
}
