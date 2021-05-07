package com.enableets.edu.sdk.ppr.ppr.core;

import com.enableets.edu.sdk.ppr.ppr.core.paper.PaperXML;
import com.enableets.edu.sdk.ppr.ppr.core.paperCard.PaperCardXML;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Date 2020/07/01$ 16:46$
 * @Author caleb_liu@enable-ets.com
 **/

@Data
@NoArgsConstructor
@AllArgsConstructor
@XStreamAlias("pprinfo")
public class PPRInfoXML {

    private PaperXML paper;

    private PaperCardXML paperCard;

}
