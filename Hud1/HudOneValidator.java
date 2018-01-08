package gov.hud.sams.naid.database.Entities.validators;

import gov.hud.sams.bean.HudOneForm;
import gov.hud.sams.util.SamsConstants;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.sun.xml.bind.v2.TODO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/**
 * Form Validator for modifying HUD1
 * Purposely left off @Component to allow us to construct manually
 *
 * TODO externalize messages and properties
 */
public class HudOneValidator implements Validator {
    private LocalDate today = LocalDate.now();
    private LocalDate dateReceived = null;
    private LocalDate dateClosed = null;
    private LocalDate dateAccepted = null;
    private LocalDate dateCloseScheduled = null;
    private LocalDate dateExtensionRequest = null;
    private LocalDate datePropertyAcquired = null;
    
    
    

    private String userHudOffice;

    private HudOneForm form = null;

    private static final List<String> validFinanceTypes = Arrays.asList("IN", "UI", "AK", "PM");

    public HudOneValidator(String userHudOffice) {
        this.userHudOffice = userHudOffice;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return HudOneForm.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        form = (HudOneForm)o;

        validateDateReceivedIsBeforeToday(errors);
        validateDateClosedIsBeforeToday(errors);
        validateDateClosedIsBeforeDateReceived(errors);
        validateDateClosedIsNotBeforeAcceptedDate(errors);
        validateDateClosedIsNotAfterScheduledClosedDate(errors);
        validateDateClosedIsNotAfterExtensionRequestDate(errors);
        validateDateClosedIsNotBeforePropertyAcquiredDate(errors);
        validateFinanceTypeIsValid(errors);
        validateLine503(errors);
        validateOfficeCodeMatches(errors);
        validateLine1304(errors);
    }

    private void validateDateReceivedIsBeforeToday(Errors errors) {
        dateReceived = LocalDate.from(SamsConstants.DB_FORMAT.parse(form.getA8spt191().getDateReceived()));
        if (dateReceived.isAfter(today)) {
            errors.rejectValue("a8spt191.dateReceived","error", "Date Received cannot be after today.");
        }
    }

    private void validateDateClosedIsBeforeToday(Errors errors) {
        dateClosed = LocalDate.from(SamsConstants.DB_FORMAT.parse(form.getA8spt191().getDateClosed()));
        if (dateClosed.isAfter(today)) {
            errors.rejectValue("a8spt191.dateClosed","error", "Date Closed cannot be after today.");
        }
    }

    private void validateDateClosedIsBeforeDateReceived(Errors errors) {
        if (dateReceived != null && dateClosed != null && dateReceived.isBefore(dateClosed)) {
            errors.rejectValue("a8spt191.dateReceived","error", "Date Received cannot be earlier than Date Closed.");
        }
    }

    private void validateFinanceTypeIsValid(Errors errors) {
        if (!validFinanceTypes.contains(form.getA8spt191().getFinanceType())) {
            errors.rejectValue("a8spt191.financeType","error", "Finance Type must be 'IN', 'UI', 'AK', or 'PM'.");
        }
    }

    private void validateLine503(Errors errors) {
        if (form.getA8spt191().getFinanceType() != null && "PM".equals(form.getA8spt191().getFinanceType()) && form.getA8spt191().getNoteRecvdSeller().equals(BigDecimal.valueOf(0.00).setScale(2))) {
            errors.rejectValue("a8spt191.noteRecvdSeller","error", "Line 503 cannot be 0.00 if Finance Type is 'PM'.");
        }

        if (form.getA8spt191().getFinanceType() != null && !"PM".equals(form.getA8spt191().getFinanceType()) && !form.getA8spt191().getNoteRecvdSeller().equals(BigDecimal.valueOf(0.00).setScale(2))) {
            errors.rejectValue("a8spt191.noteRecvdSeller","error", "Line 503 must be 0.00 if Finance Type is not 'PM'.");
        }
    }

    private void validateDateClosedIsNotBeforeAcceptedDate(Errors errors) {
        dateAccepted = LocalDate.from(SamsConstants.DB_FORMAT.parse(form.getA8spt190().getOfferAcceptedDat()));
        if (dateClosed.isBefore(dateAccepted)) {
            errors.rejectValue("a8spt191.dateClosed","error", "Date Closed cannot be before Offer Accepted Date.");
        }
    }

    private void validateDateClosedIsNotAfterScheduledClosedDate(Errors errors) {
        dateCloseScheduled = LocalDate.from(SamsConstants.DB_FORMAT.parse(form.getA8spt190().getScheduledClosing()));
        //TODO change to isBefore
        if (dateClosed.isBefore(dateCloseScheduled)) {
            errors.rejectValue("a8spt191.dateClosed","error", "Date Closed cannot be later than Scheduled Close Date.");
            
        }
    }

    private void validateDateClosedIsNotAfterExtensionRequestDate(Errors errors) {
        if (form.getA8spt191().getSalesExtension() != null && !form.getA8spt191().getSalesExtension().isEmpty()) {
            dateExtensionRequest = LocalDate.from(SamsConstants.DB_FORMAT.parse(form.getA8spt191().getSalesExtension().get(0).getCurrSchedClosing()));
            if (dateClosed.isAfter(dateExtensionRequest)) {
                errors.rejectValue("a8spt191.dateClosed","error", "Date Closed cannot be after Extension Request Date.");
            }
        }
    }

    private void validateDateClosedIsNotBeforePropertyAcquiredDate(Errors errors) {
        datePropertyAcquired = LocalDate.from(SamsConstants.DB_FORMAT.parse(form.getA8spt191().getHudProperty().getDateAcquired()));
        if (dateClosed.isBefore(datePropertyAcquired)) {
            errors.rejectValue("a8spt191.dateClosed","error", "Date Closed cannot be before Property Acquired Date.");
        }
    }

    private void validateOfficeCodeMatches(Errors errors) {
        if (!userHudOffice.equals(form.getA8spt191().getHudProperty().getCurrentHudOffice()) && !userHudOffice.equals("HQ")) {
            errors.rejectValue("a8spt191.hudProperty.currentHudOffice","error", "User's HUD Office Code does not match the Properties.");
        }
    }

    private void validateLine1304(Errors errors) {
        //TODO How do we determine if Purchaser incentive exists
    }
}

