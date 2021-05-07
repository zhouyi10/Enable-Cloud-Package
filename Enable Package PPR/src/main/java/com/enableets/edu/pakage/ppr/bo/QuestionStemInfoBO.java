package com.enableets.edu.pakage.ppr.bo;

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
public class QuestionStemInfoBO {

    /** question stem information with html tags */
    public String richText;

    /** No HTML tag title stem information  */
    public String plaintext;
}
