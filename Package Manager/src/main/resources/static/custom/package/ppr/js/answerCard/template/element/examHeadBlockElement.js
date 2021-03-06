import {BlockElement} from "./blockElement.js";
import {DocumentWriter} from "../documentWriter.js";

let examHeadBlockElement = function (section, examName, answerCard) {
    BlockElement.call(this, section);
    this.elementObject;
    this.config = section.answerCard.config;
    this.examName = examName;
    this.elementMarginBottom = "margin-bottom:10px;";
};

CommUtils.extendObj(examHeadBlockElement, BlockElement);

examHeadBlockElement.prototype.buildHtml = function (container) {
    this.elementObject = $("<div style='flex-wrap: wrap;display: flex;'>").appendTo(container);
    this.element = this.elementObject;
    this.elementObject.html(this.createdHeadHtml());
};
examHeadBlockElement.prototype.createdHeadHtml = function () {
    if (this.config && this.config.cardType == "1") {
        return this.createWebMarkHtml();
    } else {
        return this.createManualMarkHtml();
    }
};
examHeadBlockElement.prototype.createWebMarkHtml = function () {
    let webMarkHtml = "";
    webMarkHtml += this.createExamNameInfoHtml();
    webMarkHtml += ("<div style='width:50%'>" + this.createStudentInfoHtml() + this.createWarningInfo() + "</div>");
    webMarkHtml += ("<div style='width:50%; display: flex;justify-content: space-around;'>" + this.createdTicket() + "</div>");
    return webMarkHtml;
};
examHeadBlockElement.prototype.createManualMarkHtml = function () {
    let manualMarkHtml = "";
    manualMarkHtml += ("<div style='width:50%'>" + this.createExamNameInfoHtml() + this.createStudentInfoHtml() + this.createWarningInfo() + "</div>");
    manualMarkHtml += ("<div style='width:50%; display: flex;justify-content: space-around;'>" + this.createdTicket() + "</div>");
    return manualMarkHtml;
};
examHeadBlockElement.prototype.createWarningInfo = function () {
    let baseInfoHtml = this.createWarningInfoHtml();
    if (this.config && this.config.candidateNumberEdition == "1") {
        baseInfoHtml += this.createdGraffitiWaringHtml();
    }
    return baseInfoHtml;
};
examHeadBlockElement.prototype.createExamNameInfoHtml = function (customStyle) {
    customStyle = (customStyle == undefined ? this.elementMarginBottom : customStyle);
    return CommUtils.formatStr("<div class='examName' contenteditable='true' " +
        "style='height:66px;width:100%;text-align:center;font-size:25px;border:1px solid black;overflow: hidden;{0}'><div>{1}</div></br></div>", customStyle, this.examName);
};
examHeadBlockElement.prototype.createStudentInfoHtml = function (customStyle) {
    customStyle = (customStyle == undefined ? this.elementMarginBottom : customStyle);
    return CommUtils.formatStr("<div class='studentInfo' style='display:flex; flex-wrap:wrap; font-weight:bold;text-align:left;{0}'>" +
        "<div>?????????</div><div style='border-bottom: 1px solid black; width: 100px; margin-right: 5px;'></div>" +
        "<div>?????????</div><div style='border-bottom: 1px solid black; width: 100px'></div>" +
        "<div style='margin-top: 10px;'>??????/????????????</div><div style='border-bottom: 1px solid black; width: 100px'></div></div>",
        customStyle);
};
examHeadBlockElement.prototype.createWarningInfoHtml = function (customStyle) {
    customStyle = (customStyle == undefined ? this.elementMarginBottom : customStyle);
    return "<div class='warning' style='font-size: 4px;text-align:left;border:1px solid black;" + customStyle + "'>" +
        "<p style='font-weight: bold; text-align=center'>????????????</p>" +
        "1?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????<p></p>" +
        "2?????????????????????????????????2B??????????????????????????????????????????????????????????????????<p></p>" +
        "3????????????????????????????????????0.5??????????????????????????????????????????????????????????????????????????????????????????????????????????????????2B?????????????????????<p></p>" +
        "4?????????????????????????????????????????????<p></p>" +
        "5????????????????????????,???????????????????????????????????????????????????<p></p>" +
        "</div>";
};
examHeadBlockElement.prototype.createdTicket = function () {
    let customStyle = "";
    if (this.config && this.config.cardType == 1) {
        customStyle += "margin-top: 20px;";
    }
    switch (this.config.candidateNumberEdition) {
        case "1":case 1:
            return this.createdAdmissionTicketHtml(customStyle);
        case "2":case 2:
            return this.createdBarCodeHtml(customStyle);
        case "3":case 3:
            return this.createdStudentNumberHtml(customStyle);
    }
};
examHeadBlockElement.prototype.createdBarCodeHtml = function (customStyle) {
    customStyle = (customStyle == undefined ? this.elementMarginBottom : customStyle);
    return "<div class='barCode' style='width:240px;" + customStyle + "'>" +
        "    <div style='border: dashed 1px black;text-align: center;font-size: 24px;color:grey;'>" +
        "        ???????????????" +
        "        <p style='position: relative; font-size: 4px;margin-top: 45px;'>" +
        "            <span>?????????????????????????????????????????????</span>" +
        "        </p>" +
        "    </div>" + this.createdGraffitiWaringHtml() +
        "</div>";
};
examHeadBlockElement.prototype.createdGraffitiWaringHtml = function (customStyle) {
    customStyle = (customStyle == undefined ? this.elementMarginBottom : customStyle);
    return "<div class='graffitiWaring' style='margin-top:10px;border:solid 1px black;height:26px;" + customStyle + "'>" +
        "        <table style='width:100%;height:100%;line-height:24px' border='0' cellspacing='0' cellpadding='0'><tbody>" +
        "            <tr>" +
        "                <td width='35%' colspan='2'><span style='font-size: 12px;font-weight: bold;'>????????????</span></td>" +
        "                <td width='15%'><div style='border: solid 1px black;width:12px;height: 6px;background-color: black;'></div></td>" +
        "                <td width='35%' colspan='2'><span style='font-size: 12px;font-weight: bold;'>????????????</span></td>" +
        "                <td width='15%'><div style='border:1px solid #000000;width:12px;height: 6px;'></div></td>" +
        "            </tr>" +
        "        </tbody>" +
        "        </table>" +
        "    </div>";
};
examHeadBlockElement.prototype.createdAdmissionTicketHtml = function (customStyle) {
    customStyle = (customStyle == undefined ? this.elementMarginBottom : customStyle);
    let admissionTicketHtml = "<table style='border:1px solid black; border-collapse: collapse; " + customStyle + "' class='admission_ticket_table'>" +
        "<caption style='border-style:solid; border-width: 1px 1px 0px 1px;'>????????????</caption>" +
        "<tbody class='panding: 1px;'>";

    for (let i = 0; i <= 10; i++) {
        admissionTicketHtml += "<tr>";
        for (let j = 0; j < 8; j++) {
            if (i == 0) {
                admissionTicketHtml += ("<td style='height:15px; width: 30px; border: 1px solid; text-align:center'></td>");
            } else {
                admissionTicketHtml += ("<td style='height:15px; width: 30px; border-style:solid;border-width:0px 1px; text-align:center'>" +
                    "[<span style='font-size:12px; margin: 0px 2px;'>"
                    + (i - 1) + "</span>]</td>");
            }
        }
        admissionTicketHtml += "</tr>";
    }
    admissionTicketHtml += "</tbody></table>";
    return admissionTicketHtml;
};
examHeadBlockElement.prototype.createdStudentNumberHtml = function (customStyle) {
    customStyle = (customStyle == undefined ? this.elementMarginBottom : customStyle);
    return "";
};

export {examHeadBlockElement as ExamHeadBlockElement};
