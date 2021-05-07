package com.enableets.edu.pakage.etm.bean.body;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.extended.ToAttributedValueConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XStreamAlias("content")
@XStreamConverter(value = ToAttributedValueConverter.class, strings = {"value"})
public class Content {


    private String value;

}
