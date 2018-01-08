package gov.hud.sams.naid.servlets;

import gov.hud.sams.entities.A8spt191;
import gov.hud.sams.entities.A8spt248;
import gov.hud.sams.entities.A8spt276;
import gov.hud.sams.naid.database.Entities.validators.HudOneValidator;
import gov.hud.sams.bean.HudOneForm;
import gov.hud.sams.service.cases.CaseManager;
import gov.hud.sams.service.cases.HudOneManager;
import gov.hud.sams.util.NaidConstants;
import org.springframework.validation.Errors;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

@Controller
@RequestMapping("/hud1")
@SessionAttributes("hudOne")
public class HudOneController {
    @Autowired
    private HudOneManager hudOneManager;
    @Autowired
    private CaseManager caseManager;
    
    private static final String JSON_VAR_SEPARATOR = "\",\"";
    private static final String USER_HUD_OFFICE_REQUEST_ATTRIBUTE = "userHudOffice";
    private static final String ERROR_REQUEST_ATTRIBUTE = "error";

    @RequestMapping("/find")
    public String searchPage(Model model) {
        List<A8spt276> caseHocAreas = caseManager.getAvailableHocAreas();
        model.addAttribute("mm3HocAreas", caseHocAreas.stream().filter(p -> "M3".equals(p.getAreaMMStructure())).collect(Collectors.toList()));
        model.addAttribute("mm2HocAreas", caseHocAreas.stream().filter(p -> "M2".equals(p.getAreaMMStructure())).collect(Collectors.toList()));
        model.addAttribute("mm1HocAreas", caseHocAreas.stream().filter(p -> "M1".equals(p.getAreaMMStructure())).collect(Collectors.toList()));
        model.addAttribute("otherHocAreas", caseHocAreas.stream().filter(p -> !Arrays.asList("M3", "M2", "M1").contains(p.getAreaMMStructure())).collect(Collectors.toList()));
        return "/hud1/search";
    }

    @RequestMapping("/view")
    public String view(@RequestParam(value = "caseNumber") String caseNumber, @RequestParam(value = "message", required = false) String message, 
            @RequestParam(value = "error", required = false) String error,
            Model model) {
        A8spt191 viewCase = hudOneManager.getCase(caseNumber);
        model.addAttribute("message", message);
        model.addAttribute("error", error);
        model.addAttribute("viewCase", viewCase);
        model.addAttribute("hudProperty", viewCase.getHudProperty());
        model.addAttribute("viewCase190", hudOneManager.getSalesOffer(caseNumber, viewCase.getHudProperty().getBidReceiptNumber()));
        model.addAttribute("settlementErrorHistory", viewCase.getSalesPackageHistory()
                                                             .stream()
                                                             .sorted(Comparator.comparing(A8spt248::getHistoryDate))
                                                             .collect(Collectors.toList()));
        BigDecimal fedwireAmount = hudOneManager.getFedwireAmount(caseNumber);
        model.addAttribute("fedwireAmount", fedwireAmount == null ?  "" : fedwireAmount.toString());
                                                             
        return "/hud1/view";
        
    }

    @RequestMapping("/modify")
    public String modify(Model model, @RequestParam(value = "caseNumber") String caseNumber, @RequestParam(value = "message", required = false) String message, @RequestParam(value = "error", required = false) String error) {
        A8spt191 viewCase = hudOneManager.getCaseToModify(caseNumber);
        model.addAttribute("message", message);
        
        HudOneForm modifyHudOne = new HudOneForm();
        modifyHudOne.setA8spt191(viewCase);
        modifyHudOne.setA8spt190(hudOneManager.getSalesOffer(caseNumber, viewCase.getHudProperty().getBidReceiptNumber()));
        modifyHudOne.setA8spt028(hudOneManager.getFedwireRecord(caseNumber));
        model.addAttribute("hudOne", modifyHudOne);
        return "/hud1/modify";
    }
    
    @RequestMapping("/reopen")
    public void reopen(@RequestParam(value = "caseNumber") String caseNumber, HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/text");
        try {
            hudOneManager.reopenCase(caseNumber, (String) request.getSession().getAttribute(NaidConstants.SESSION_LOGGED_IN_USER));
        } catch (Exception e) {
            
        }
        
    }
    
    @RequestMapping("/update")
    public String update(Model model, @ModelAttribute("hudOne") HudOneForm modifyHudOne, HttpServletRequest request, HttpServletResponse response, BindingResult result, SessionStatus status) {
        modifyHudOne.getA8spt191().setUpdatedBy((String) request.getSession().getAttribute(NaidConstants.SESSION_LOGGED_IN_USER));
        modifyHudOne.formatDates();
        HudOneValidator validator = new HudOneValidator((String)request.getSession().getAttribute(USER_HUD_OFFICE_REQUEST_ATTRIBUTE));
        validator.validate(modifyHudOne, result);
        if(result.hasErrors()) {
            model.addAttribute(ERROR_REQUEST_ATTRIBUTE, "Form contains errors, please review and fix before submitting again.");
            model.addAttribute("hudOne", modifyHudOne);
            return "/hud1/modify";
        }
        
        try {
            hudOneManager.updateHudOne(modifyHudOne);
            return "redirect:/hud1/view.html?caseNumber="+ modifyHudOne.getA8spt191().getCaseHudOfficeP() + "-" + modifyHudOne.getA8spt191().getCaseNum() +"&message=Update Successful.";
        } catch (Exception e) {
            return "redirect:/hud1/view.html?caseNumber="+ modifyHudOne.getA8spt191().getCaseHudOfficeP() + "-" + modifyHudOne.getA8spt191().getCaseNum() +"&error=Update Failed.";
        }
    }
    
    @RequestMapping("/search")
    public void search(@RequestParam(value = "caseNumber", required=false) String caseNumber, 
                       @RequestParam(value = "earnestMoney", required=false) String earnestMoney,
                       @RequestParam(value = "dateReconciledFrom", required = false) String dateReconciledFrom, 
                       @RequestParam(value = "dateReconciledTo", required = false) String dateReconciledTo,
                       @RequestParam(value = "dateClosedFrom", required = false) String dateClosedFrom, 
                       @RequestParam(value = "dateClosedTo", required = false) String dateClosedTo,
                       @RequestParam(value = "contractArea", required = false) String contractArea,
                       @RequestParam(value = "financeType", required = false) String financeType,
                       @RequestParam(value = "titleId", required = false) String titleId,
                       HttpServletRequest request, HttpServletResponse response) throws IOException {
        
        List<Object[]> cases = hudOneManager.searchForCases(caseNumber, earnestMoney, dateReconciledFrom, dateReconciledTo, dateClosedFrom, dateClosedTo, contractArea, financeType, titleId.toUpperCase());
        writeJSON(response, cases);
    }
    
    private void writeJSON(HttpServletResponse response, List<Object[]> caseList) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        StringBuilder builder = new StringBuilder("{");
        builder.append("\"sEcho\": ").append(3).append(",");
        builder.append("\"iTotalRecords\": ").append(caseList.size()).append(",");
        builder.append("\"iTotalDisplayRecords\": ").append(caseList.size()).append(",");
        builder.append("\"aaData\": [");
                                                                                         
        for (int i = 0; i < caseList.size(); i++) {
            Object[] record = caseList.get(i);
            StringBuilder json = new StringBuilder("[\"");
            //Case Number
            json.append(StringEscapeUtils.escapeHtml4(record[0]+"-"+record[1]));
            json.append(JSON_VAR_SEPARATOR);
            //Address
            json.append(StringEscapeUtils.escapeHtml4(record[2] + ", " + record[9] + ", " + record[10] + " " + record[11]));
            json.append(JSON_VAR_SEPARATOR);
            //Area
            json.append(StringEscapeUtils.escapeHtml4((String) record[3]));
            json.append(JSON_VAR_SEPARATOR);
            //Finance Type
            json.append(StringEscapeUtils.escapeHtml4((String) record[4]));
            json.append(JSON_VAR_SEPARATOR);
            //Date Received
            json.append(StringEscapeUtils.escapeHtml4((String) record[5]));
            json.append(JSON_VAR_SEPARATOR);
            //Closing Date
            json.append(StringEscapeUtils.escapeHtml4((String) record[6]));
            json.append(JSON_VAR_SEPARATOR);
            //Date Reconciled
            json.append(StringEscapeUtils.escapeHtml4((String) record[7]));
            json.append(JSON_VAR_SEPARATOR);
            //Cash Due Settlement
            json.append(StringEscapeUtils.escapeHtml4(String.valueOf(record[8])));
            json.append("\"]");
            if (i != 0) {
                builder.append(",");
            }

            builder.append(json.toString());
        }
        builder.append("] ");
        builder.append("}");

        out.println(builder.toString());
        out.close();
    }
}
