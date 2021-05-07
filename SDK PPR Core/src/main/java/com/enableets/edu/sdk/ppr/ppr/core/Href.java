package com.enableets.edu.sdk.ppr.ppr.core;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.extended.ToAttributedValueConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/06/17
 **/

@Data
@NoArgsConstructor
@AllArgsConstructor
@XStreamAlias("href")
@XStreamConverter(value = ToAttributedValueConverter.class, strings = {"href"})
public class Href {

    private String href;

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
            return false;
        if (obj == this) return true;
        if (this.getClass() != obj.getClass())
            return false;
        Href obj1 = (Href) obj;
        if (href.equals(obj1.getHref())) return true;
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(href);
    }


}
