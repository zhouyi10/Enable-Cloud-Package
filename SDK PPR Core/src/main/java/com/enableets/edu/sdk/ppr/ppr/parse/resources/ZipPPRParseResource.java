package com.enableets.edu.sdk.ppr.ppr.parse.resources;

import cn.hutool.core.io.FileUtil;

import com.enableets.edu.sdk.ppr.exceptions.PPRVersionMismatchException;
import com.enableets.edu.sdk.ppr.ppr.core.question.Question;
import com.enableets.edu.sdk.ppr.ppr.parse.xml.PaperCardXmlIgnoreVersionParser;
import com.enableets.edu.sdk.ppr.ppr.parse.xml.PaperCardXmlParser;
import com.enableets.edu.sdk.ppr.ppr.parse.xml.PaperXmlIgnoreVersionParser;
import com.enableets.edu.sdk.ppr.ppr.parse.xml.PaperXmlParser;
import com.enableets.edu.sdk.ppr.ppr.parse.xml.QuestionXmlIgnoreVersionParser;
import com.enableets.edu.sdk.ppr.ppr.parse.xml.QuestionXmlParser;
import com.enableets.edu.sdk.ppr.ppr.core.Constants;
import com.enableets.edu.sdk.ppr.ppr.core.PPRInfoXML;
import com.enableets.edu.sdk.ppr.ppr.core.paper.PaperXML;
import com.enableets.edu.sdk.ppr.ppr.core.paperCard.PaperCardXML;
import com.enableets.edu.sdk.ppr.ppr.parse.exceptions.PPRParserResourceException;
import com.enableets.edu.sdk.ppr.zip.ZipUtils;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @Date 2020/07/01$ 17:05$
 * @Author caleb_liu@enable-ets.com
 **/

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ZipPPRParseResource extends AbstractPPRParseResource {

    protected String pprZipPath;

    @Override
    public PPRInfoXML parsePPR() throws PPRVersionMismatchException {
        setPprZipPath();
        unPPRZip();
        return new PPRInfoXML(parsePaper(), parsePaperCard());
    }

    private PaperXML parsePaper() throws PPRVersionMismatchException {
        String paperXmlFilePath = this.pprRootPath + Constants.PPR_PAPER_XML_PATH;
        PaperXML parse = null;
        if (ignoreVersion){
            parse = new PaperXmlIgnoreVersionParser(new File(paperXmlFilePath)).parse();
        }else {
            parse = new PaperXmlParser(new File(paperXmlFilePath)).parse();
        }
        parse.setQuestionMap(parsePaperQuestion());
        return parse;
    }

    private PaperCardXML parsePaperCard() throws PPRVersionMismatchException {
        String paperCardXmlFilePath = pprRootPath + Constants.PPR_PAPER_CARD_XML_PATH;
        if (ignoreVersion){
            return new PaperCardXmlIgnoreVersionParser(new File(paperCardXmlFilePath)).parse();
        }
        return new PaperCardXmlParser(new File(paperCardXmlFilePath)).parse();
    }

    private Map<String, Question> parsePaperQuestion() throws PPRVersionMismatchException  {
        Map<String, Question> questionMap = new HashMap<>();
        String questionXmlFilePath = pprRootPath + File.separator + "files";
        File file1 = new File(questionXmlFilePath);
        String[] files = file1.list();
        for (String fileName : files) {
            if (!fileName.endsWith(".xml")) continue;
            Question parse = null;
            if (ignoreVersion){
                parse = new QuestionXmlIgnoreVersionParser(new File(questionXmlFilePath + File.separator + fileName)).parse();
            }else{
                parse = new QuestionXmlParser(new File(questionXmlFilePath + File.separator + fileName)).parse();
            }
            questionMap.put(parse.getQuestionId(), parse);
        }
        return questionMap;
    }


    public void clean() {
        if (FileUtil.isDirectory(pprRootPath)) {
            FileUtil.del(pprRootPath);
        }
    }

    private void unPPRZip() {
        if (FileUtil.isFile(this.pprZipPath)) {
            ZipUtils.unZip(this.pprZipPath);
            File pprZip = new File(this.pprZipPath);
            pprRootPath =  pprZip.getParent() + File.separator + FileUtil.mainName(pprZip);
        } else {
            throw new PPRParserResourceException(String.format("pprZipPath is not a file! pprZipPath: [%s]", pprZipPath));
        }
    }

    protected void setPprZipPath(){

    }
}
