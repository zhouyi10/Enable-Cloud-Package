package com.enableets.edu.pakage.ppr.bean.body;

import com.enableets.edu.pakage.core.bean.Body;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/11/16
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@XStreamAlias("body")
public class PPRBody extends Body {

    @XStreamImplicit
    private List<TestPart> testParts;
}
