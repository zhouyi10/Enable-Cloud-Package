package com.enableets.edu.sdk.ppr.ppr.core.paperCard;

import com.enableets.edu.sdk.ppr.ppr.core.Header;
import com.enableets.edu.sdk.ppr.xml.xstream.EntityToXml;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/06/29
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@XStreamAlias("paperCard")
public class PaperCardXML extends EntityToXml {

    private Header header;

    private Body body;
}
