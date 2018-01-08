<!DOCTYPE html>


<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html lang="en">
<head>
    <title>SAMS - HUD1 - Closing Disclosure</title>
    <script type="text/javascript">
        $(function() {
            

            loadFieldErrors();
            $('.moneyField').autoNumeric('init', {
                    vMax : '999999.99',
                    vMin : '000000.00'
                });
            $(".datePicker").datepicker({
                defaultDate: <c:out value="${hudOne.a8spt191.dateReceived}"/>
            });

            $('#myForm').submit(function() {
                
                if($('a8spt191\\.reconcileWithinT').val() == 'Y' && ($('#difference').val() > 10.00 || $('#difference').val() < -100.00)) {
                    alert("The Difference(603C) must be greater than -100 and less than 10 when reconcile within tolerance is 'Y'");
                    event.preventDefault();
                }
                if($('#a8spt191\\.financeType').val() == 'PM' && $('#a8spt191\\.noteRecvdSeller').val() == 0.00) {
                    highlightErrorField('a8spt191\\.noteRecvdSeller');
                    highlightErrorField('a8spt191\\.financeType');
                    alert("Line 503 cannot be 0.00 if Finance Type is 'PM'.");
                    $('#a8spt191\\.financeType').focus();
                    event.preventDefault();
                } else if($('#a8spt191\\.financeType').val() != 'PM' && $('#a8spt191\\.noteRecvdSeller').val() != 0.00) {
                    highlightErrorField('a8spt191\\.noteRecvdSeller');
                    highlightErrorField('a8spt191\\.financeType');
                    alert("Line 503 must be 0.00 if Finance Type is not 'PM'.");
                    $('#a8spt191\\.financeType').focus();
                    event.preventDefault();
                }
                
                if(moment().isBefore(moment($('#a8spt191\\.dateReceived').val(), 'MM/DD/YYYY'))) {
                    highlightErrorField('a8spt191\\.dateReceived');
                    alert("Date Received cannot be after today.");
                    $('#a8spt191\\.dateReceived').focus();
                    event.preventDefault();
                } 
                if(moment().isBefore(moment($('#a8spt191\\.dateClosed').val(), 'MM/DD/YYYY'))) {
                    highlightErrorField('a8spt191\\.dateClosed');
                    alert("Closing Date Cannot Be After The Current Date.");
                    $('#a8spt191\\.dateClosed').focus();
                    event.preventDefault();
                }
                if(moment($('#a8spt191\\.dateReceived').val(), 'MM/DD/YYYY').isBefore(moment($('#a8spt191\\.dateClosed').val(), 'MM/DD/YYYY'))) {
                    highlightErrorField('a8spt191\\.dateReceived');
                    highlightErrorField('a8spt191\\.dateClosed');
                    alert("Date Received cannot be earlier than Date Closed.");
                    $('#a8spt191\\.dateReceived').focus();
                    event.preventDefault();
                }
                if(moment($('#a8spt191\\.dateClosed').val(), 'MM/DD/YYYY').isBefore(moment('${hudOne.a8spt190.scheduledClosing}', 'YYYY-MM-DD'))) {
                    highlightErrorField('a8spt191\\.dateClosed');
                    alert("Date Closed cannot be later than Scheduled Close Date of " + formatDate('${hudOne.a8spt190.scheduledClosing}'));
                    $('#a8spt191\\.dateClosed').focus();
                    event.preventDefault();
                }
                if(moment($('#a8spt191\\.dateClosed').val(), 'MM/DD/YYYY').isAfter(moment('${hudOne.a8spt191.salesExtension[0].currSchedClosing}', 'YYYY-MM-DD'))) {
                    highlightErrorField('a8spt191\\.dateClosed');
                    alert("Date Closed cannot be after Extension Request Date of " + formatDate('${hudOne.a8spt191.salesExtension[0].currSchedClosing}'));
                    $('#a8spt191\\.dateClosed').focus();
                    event.preventDefault();
                }
                if(moment($('#a8spt191\\.dateClosed').val(), 'MM/DD/YYYY').isBefore(moment('${hudOne.a8spt191.hudProperty.dateAcquired}', 'YYYY-MM-DD'))) {
                    highlightErrorField('a8spt191\\.dateClosed');
                    alert("Date Closed cannot be before Property Acquired Date of " + formatDate('${hudOne.a8spt191.hudProperty.dateAcquired}'));
                    $('#a8spt191\\.dateClosed').focus();
                    event.preventDefault();
                }
                if(moment($('#a8spt191\\.dateClosed').val(), 'MM/DD/YYYY').isBefore(moment('${hudOne.a8spt190.offerAcceptedDat}', 'YYYY-MM-DD'))) {
                    highlightErrorField('a8spt191\\.dateClosed');
                    alert("Date Closed cannot be before Offer Accepted Date of " + formatDate('${hudOne.a8spt190.offerAcceptedDat}'));
                    $('#a8spt191\\.dateClosed').focus();
                    event.preventDefault();
                } 
                $('.moneyField').each(function() {
                        $(this).val($(this).val().replace(",", ""));
                });
            });                         
        });

        function formatDate(date) {
            if(date != "") {
                var temp = date.split("-");
                return temp[1] + "/" + temp[2] + "/" + temp[0];
            }
            return date;
        }
    </script>
</head>

<body>

<main>

    <div class="container">
        <div>
            <fieldset>
                <legend class="heading">HUD1 - Closing Disclosure
                    <input type="button" class="btn btn-primary btn-sm floatR" value="VIEW" onclick="window.location.href = '<c:url value="/hud1/view.html"/>'"/>
                </legend>
            </fieldset>
        </div>

        <c:set var="uploadUrl"><c:url value="/hud1/update.html"/></c:set>
        <form:form method="POST" id="myForm" modelAttribute="hudOne" enctype="multipart/form-data" action="${uploadUrl}">
        
            
            
            <div class="panel panel-default">
                <div class="panel-heading">
                    <label class="panel-title"><fmt:message key="lbl.header.caseInformation"/></label>
                </div>
                <div class="panel-body">
                 <c:if test="${error != null && error != ''}">
                            <div class="form-group col-md-12">
                                <ul>
                                    <li style="color: #d9534f"><b><form:errors path="a8spt191.noteRecvdSeller" /></b></li>
                                    <li style="color: #d9534f"><b><form:errors path="a8spt191.dateClosed" /></b></li>
                                    <li style="color: #d9534f"><b><form:errors path="a8spt191.dateReceived" /></b></li>
                                    <li style="color: #d9534f"><b><form:errors path="a8spt191.financeType" /></b></li>
                                    <li style="color: #d9534f"><b><form:errors path="a8spt191.hudProperty.currentHudOffice" /></b></li>                                    
                                </ul>
                            </div>
                        </c:if>
                    <div class="form-group col-md-2">
                        <label for="caseNumber" style="white-space: nowrap;font-size: small"><fmt:message key="lbl.caseNumber"/></label>
                        <input type="text" class="form-control input-sm  readonly" id="caseNumber" placeholder="e.g. 001-000001" value='<c:out value="${hudOne.a8spt191.hudProperty.caseHudOfficeP}-${hudOne.a8spt191.hudProperty.caseNum}"/>'  readonly="readonly"/>
                    </div>
                    <div class="form-group col-md-2">
                        <label for="contractNumber" style="white-space: nowrap;font-size: small"><fmt:message key="lbl.contractNumber"/></label>
                        <input type="text" class="form-control input-sm  readonly" id="contractNumber" placeholder="" value='<c:out value="${hudOne.a8spt190.contractNumber}"/>'  readonly="readonly"/>
                    </div>
                    <div class="form-group col-md-2">
                        <label for="earnestMoney" style="white-space: nowrap;font-size: small"><fmt:message key="lbl.earnestMoney"/></label>
                        <input type="text" name="a8spt191.cashDueSettlemen" class="form-control input-sm moneyField" id="a8spt191.cashDueSettlemen" placeholder="" value='<fmt:formatNumber value = "${hudOne.a8spt191.cashDueSettlemen}" type = "currency" currencySymbol=""/>'  />
                    </div>
                    <div class="form-group col-md-6">
                        <label for="address" style="white-space: nowrap;font-size: small"><fmt:message key="lbl.address"/></label>
                        <input type="text" class="form-control input-sm  readonly" id="address" placeholder="" value='<c:out value="${hudOne.a8spt191.hudProperty.address1}, ${hudOne.a8spt191.hudProperty.city}, ${hudOne.a8spt191.hudProperty.stateCode} ${hudOne.a8spt191.hudProperty.displayZipCode}"/>'  readonly="readonly"/>
                    </div>
                    <c:catch var="dateException">
                        <fmt:parseDate var="dateReceived" value="${hudOne.a8spt191.dateReceived}" pattern="yyyy-MM-dd"/>
                        <c:set var="dateReceived"><fmt:formatDate pattern="MM/dd/yyyy"  value="${dateReceived}"/></c:set>
                    </c:catch>
                    <c:if test="${not empty dateException}">
                        <c:set var="dateReceived" value="${hudOne.a8spt191.dateReceived}"/>
                    </c:if>
                    <div class="form-group col-md-2">
                        <label for="dateReceived" style="white-space: nowrap;font-size: small"><fmt:message key="lbl.dateReceived"/></label>
                        <input type="text" class="form-control input-sm datePicker " name="a8spt191.dateReceived" id="a8spt191.dateReceived" placeholder="" value="${dateReceived}"  />
                    </div>
                     <c:catch var="dateException">
                        <fmt:parseDate var="dateClosed" value="${hudOne.a8spt191.dateClosed}" pattern="yyyy-MM-dd"/>
                        <c:set var="dateClosed"><fmt:formatDate pattern="MM/dd/yyyy"  value="${dateClosed}"/></c:set>
                    </c:catch>
                    <c:if test="${not empty dateException}">
                        <c:set var="dateClosed" value="${hudOne.a8spt191.dateClosed}"/>
                    </c:if>
                    <div class="form-group col-md-2">
                        <label for="a8spt191.dateClosed" class="control-label" style="white-space: nowrap;font-size: small"><fmt:message key="lbl.closingDate"/></label>
                        <%-- <form:errors path="a8spt191.dateClosed" cssClass="alert-danger panel-heading" element="div"/> --%>
                        <input type="text" class="form-control input-sm datePicker " name="a8spt191.dateClosed" id="a8spt191.dateClosed" placeholder="" value="${dateClosed}"  />
                        
                    </div>
                    <c:catch var="dateException">
                        <fmt:parseDate var="dateReconciled" value="${hudOne.a8spt191.hudProperty.dateReconciled}" pattern="yyyy-MM-dd"/>
                        <c:set var="dateReconciled"><fmt:formatDate pattern="MM/dd/yyyy"  value="${dateReconciled}"/></c:set>
                    </c:catch>
                    <c:if test="${not empty dateException}">
                        <c:set var="dateClosed" value="${hudOne.a8spt191.hudProperty.dateReconciled}"/>
                    </c:if>
                    <div class="form-group col-md-2">
                        <label for="reconciliationDate" style="white-space: nowrap;font-size: small"><fmt:message key="lbl.reconciliationDate"/></label>
                        <input type="text" class="form-control input-sm  readonly" id="reconciliationDate" placeholder="" value="${dateReconciled}"  readonly="readonly"/>
                    </div>
                    <div class="form-group col-md-1">
                        <label for="a8spt191.financeType" class="control-label" style="white-space: nowrap;font-size: small"><fmt:message key="lbl.financeType"/></label>
                        <%-- <input type="text" class="form-control input-sm  " id="financeType" placeholder="" value='<c:out value="${hudOne.a8spt191.financeType}"/>'/> --%>
                       <%--  <form:errors path="a8spt191.noteRecvdSeller" cssClass="alert-danger panel-heading" element="div"/> --%>
                        <select class="form-control input-sm" name="a8spt191.financeType" id="a8spt191.financeType">
                            <option value="<c:out value="${hudOne.a8spt191.financeType}"/>"><c:out value="${hudOne.a8spt191.financeType}"/></option>
                            <option value="IN">IN</option>
                            <option value="UI">UI</option>
                            <option value="AK">AK</option>
                            <option value="PM">PM</option>
                        </select>
                    </div>
                    <div class="form-group col-md-2">
                        <label for="optionalField" style="white-space: nowrap;font-size: small"><fmt:message key="lbl.optionalField"/></label>
                        <input type="text" class="form-control input-sm  " name="a8spt191.optionalField" id="a8spt191.optionalField" placeholder="" value='<c:out value="${hudOne.a8spt191.optionalField}"/>'/>
                    </div>
                    <div class="form-group col-md-1">
                        <label for="area" style="white-space: nowrap;font-size: small"><fmt:message key="lbl.area"/></label>
                        <%-- <input type="text" class="form-control input-sm  " name="a8spt191.hudProperty.hudOfficeCode" id="a8spt191.hudProperty.hudOfficeCode" placeholder="" value='<c:out value="${hudOne.a8spt191.hudProperty.hudOfficeCode}"/>'  /> --%>
                        <select class="form-control input-sm" name="a8spt191.hudProperty.currentHudOffice" id="a8spt191.hudProperty.currentHudOffice">
                            <option value="<c:out value="${hudOne.a8spt191.hudProperty.hudOfficeCode}"/>"><c:out value="${hudOne.a8spt191.hudProperty.currentHudOffice}"/></option>
                            <option value="HA">HA</option>
                            
                            <option value="1A">1A</option>
                            
                            <option value="2A">2A</option>
                            
                            <option value="3A">3A</option>
                            
                            <option value="4A">4A</option>
                            
                            <option value="5A">5A</option>
                            
                            <option value="6A">6A</option>
                            
                            <option value="6B">6B</option>
                            
                            <option value="7A">7A</option>
                            
                            <option value="8A">8A</option>
                            
                            <option value="HC">HC</option>
                            
                            <option value="1S">1S</option>
                            
                            <option value="2S">2S</option>
                            
                            <option value="3S">3S</option>
                            
                            <option value="4S">4S</option>
                            
                            <option value="5S">5S</option>
                            
                            <option value="6S">6S</option>
                            
                            <option value="HD">HD</option>
                            
                            <option value="1D">1D</option>
                            
                            <option value="2D">2D</option>
                            
                            <option value="3D">3D</option>
                            
                            <option value="4D">4D</option>
                            
                            <option value="5D">5D</option>
                            
                            <option value="HP">HP</option>
                            
                            <option value="1P">1P</option>
                            
                            <option value="2P">2P</option>
                            
                            <option value="3P">3P</option>
                            
                            <option value="4P">4P</option>
                            
                            <option value="5P">5P</option>
                            
                            <option value="HQ">HQ</option>
                            
                            <option value="AA">AA</option>
                            
                            <option value="AB">AB</option>
                            
                            <option value="AC">AC</option>
                            
                            <option value="AD">AD</option>
                            
                            <option value="AE">AE</option>
                            
                            <option value="AF">AF</option>
                            
                            <option value="AG">AG</option>
                            
                            <option value="AH">AH</option>
                            
                            <option value="CA">CA</option>
                            
                            <option value="CB">CB</option>
                            
                            <option value="CC">CC</option>
                            
                            <option value="CD">CD</option>
                            
                            <option value="CE">CE</option>
                            
                            <option value="DA">DA</option>
                            
                            <option value="DB">DB</option>
                            
                            <option value="DC">DC</option>
                            
                            <option value="DD">DD</option>
                            
                            <option value="DE">DE</option>
                            
                            <option value="DF">DF</option>
                            
                            <option value="PA">PA</option>
                            
                            <option value="PB">PB</option>
                            
                            <option value="PC">PC</option>
                            
                            <option value="PD">PD</option>
                            
                            <option value="PE">PE</option>
                            
                            <option value="PF">PF</option>
                            
                            <option value="PG">PG</option>
                            
                            <option value="PH">PH</option>
                            
                            <option value="A1">A1</option>
                            
                            <option value="A2">A2</option>
                            
                            <option value="A3">A3</option>
                            
                            <option value="A4">A4</option>
                            
                            <option value="C1">C1</option>
                            
                            <option value="C2">C2</option>
                            
                            <option value="C3">C3</option>
                            
                            <option value="C4">C4</option>
                            
                            <option value="C5">C5</option>
                            
                            <option value="C6">C6</option>
                            
                            <option value="C7">C7</option>
                            
                            <option value="C8">C8</option>
                            
                            <option value="C9">C9</option>
                            
                            <option value="D1">D1</option>
                            
                            <option value="D2">D2</option>
                            
                            <option value="D3">D3</option>
                            
                            <option value="D4">D4</option>
                            
                            <option value="P1">P1</option>
                            
                            <option value="P2">P2</option>
                            
                            <option value="P3">P3</option>
                            
                            <option value="P4">P4</option>
                            
                            <option value="P5">P5</option>
                            
                            <option value="P6">P6</option>
                            
                            <option value="P7">P7</option>
                            
                            <option value="P8">P8</option>
                            
                            <option value="1T">1T</option>
                            
                            <option value="2T">2T</option>
                            
                            <option value="3T">3T</option>
                            
                            <option value="4C">4C</option>
                            
                            <option value="4E">4E</option>
                            
                            <option value="4F">4F</option>
                            
                            <option value="4G">4G</option>
                            
                            <option value="4H">4H</option>
                            
                            <option value="4I">4I</option>
                            
                            <option value="4J">4J</option>
                            
                            <option value="4K">4K</option>
                            
                            <option value="4L">4L</option>
                            
                            <option value="4M">4M</option>
                            
                            <option value="4N">4N</option>
                            
                            <option value="4O">4O</option>
                            
                            <option value="4T">4T</option>
                            
                            <option value="5H">5H</option>
                            
                            <option value="5T">5T</option>
                            
                            <option value="6T">6T</option>
                            
                            <option value="7T">7T</option>
                            
                            <option value="8T">8T</option>
                            
                            <option value="9T">9T</option>
                            
                            <option value="0A">0A</option>
                            
                            <option value="0C">0C</option>
                            
                            <option value="0D">0D</option>
                            
                            <option value="0E">0E</option>
                            
                            <option value="0G">0G</option>
                            
                            <option value="9A">9A</option>
                            
                            <option value="9B">9B</option>
                            
                            <option value="9C">9C</option>
                            
                            <option value="9D">9D</option>
                            
                            <option value="9E">9E</option>
                            
                            <option value="9F">9F</option>
                            
                            <option value="9G">9G</option>
                            
                            <option value="9H">9H</option>
                            
                            <option value="9J">9J</option>
                            
                            <option value="9K">9K</option>
                            
                            <option value="9L">9L</option>
                            
                            <option value="2R">2R</option>
                            
                            <option value="4R">4R</option>
                            
                            <option value="5I">5I</option>
                            
                            <option value="5K">5K</option>
                            
                            <option value="5R">5R</option>
                            
                            <option value="6E">6E</option>
                            
                            <option value="6F">6F</option>
                            
                            <option value="6G">6G</option>
                            
                            <option value="6H">6H</option>
                            
                            <option value="6I">6I</option>
                            
                            <option value="6J">6J</option>
                            
                            <option value="6K">6K</option>
                            
                            <option value="6L">6L</option>
                            
                            <option value="7B">7B</option>
                            
                            <option value="7D">7D</option>
                            
                            <option value="7E">7E</option>
                            
                            <option value="8D">8D</option>
                            
                            <option value="8E">8E</option>
                            
                            <option value="8F">8F</option>
                            
                            <option value="1E">1E</option>
                            
                            <option value="1F">1F</option>
                            
                            <option value="1G">1G</option>
                            
                            <option value="2B">2B</option>
                            
                            <option value="2C">2C</option>
                            
                            <option value="2F">2F</option>
                            
                            <option value="3B">3B</option>
                            
                            <option value="3C">3C</option>
                            
                            <option value="3E">3E</option>
                            
                            <option value="3F">3F</option>
                            
                            <option value="3G">3G</option>
                            
                            <option value="5C">5C</option>
                            
                            <option value="5E">5E</option>
                            
                            <option value="5F">5F</option>
                            
                            <option value="5G">5G</option>
                            
                            <option value="5J">5J</option>
                        </select>
                    </div>
                    <div class="form-group col-md-2">
                        <label for="updatedBy" style="white-space: nowrap;font-size: small"><fmt:message key="lbl.updatedBy"/></label>
                        <input type="text" class="form-control readonly" id="updatedBy" placeholder="" value='<c:out value="${hudOne.a8spt191.updatedBy}"/>' readonly />
                    </div>
                </div>
            </div>

            <ul class="nav nav-tabs" role="tablist">
                <li role="presentation" class="active"><a href="#page1" aria-controls="page1" role="tab" data-toggle="tab">Page 1</a></li>
            </ul>

            <div class="tab-content">
                <div role="tabpanel" class="tab-pane active" id="page1">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <label class="panel-title"><fmt:message key="lbl.header.sellerInformation"/></label>
                        </div>
                        <div class="panel-body">
                            <div class="table-responsive col-md-6">
                                <table class="table">
                                    <thead>
                                        <tr>
                                            <th><strong><fmt:message key="lbl.table.dueToSeller"/></strong></th>
                                            <th><strong>&nbsp;</strong></th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                    <tr>
                                        <td class="col-md-9"><fmt:message key="lbl.line.401"/></td>
                                        <td class="col-md-3"><input type="text" class="form-control input-xs moneyField readonly" value='<fmt:formatNumber  type = "currency" currencySymbol="" value="${hudOne.a8spt191.contractSalesPri}"/>' readonly="readonly"/></td>
                                    </tr>
                                    <tr>
                                        <td class="col-md-9"><fmt:message key="lbl.line.404"/></td>
                                        <td class="col-md-3"><input type="text" name="a8spt191.extensionFee" id="a8spt191.extensionFee" class="form-control input-xs moneyField " value='<fmt:formatNumber  type = "currency" currencySymbol="" value="${hudOne.a8spt191.extensionFee}"/>'/></td>
                                    </tr>
                                    <tr>
                                        <td class="col-md-9"><fmt:message key="lbl.line.405"/></td>
                                        <td class="col-md-3"><input type="text" name="a8spt191.rent" id="a8spt191.rent" class="form-control input-xs moneyField " value='<fmt:formatNumber  type = "currency" currencySymbol="" value="${hudOne.a8spt191.rent}"/>'/></td>
                                    </tr>
                                    </tbody>
                                </table>

                                <table class="table">
                                    <thead>
                                        <tr>
                                            <th><strong><fmt:message key="lbl.table.adjustmentsPaidSeller"/></strong></th>
                                            <th><strong>&nbsp;</strong></th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                    <tr>
                                        <td class="col-md-9"><fmt:message key="lbl.line.406"/></td>
                                        <td class="col-md-3"><input type="text" name="a8spt191.prepaidCityTown" id="a8spt191.prepaidCityTown" class="form-control input-xs moneyField " value='<fmt:formatNumber  type = "currency" currencySymbol="" value="${hudOne.a8spt191.prepaidCityTown}"/>'/></td>
                                    </tr>
                                    <tr>
                                        <td class="col-md-9"><fmt:message key="lbl.line.407"/></td>
                                        <td class="col-md-3"><input type="text" name="a8spt191.prepaidCountyTax" id="a8spt191.prepaidCountyTax" class="form-control input-xs moneyField " value="<fmt:formatNumber  type = "currency" currencySymbol="" value="${hudOne.a8spt191.prepaidCountyTax}"/>"/></td>
                                    </tr>
                                    <tr>
                                        <td class="col-md-9"><fmt:message key="lbl.line.408"/></td>
                                        <td class="col-md-3"><input type="text" name="a8spt191.prepaidAssessment" id="a8spt191.prepaidAssessment" class="form-control input-xs moneyField " value="<fmt:formatNumber  type = "currency" currencySymbol="" value="${hudOne.a8spt191.prepaidAssessment}"/>"/></td>
                                    </tr>
                                    <tr>
                                        <td class="col-md-9"><fmt:message key="lbl.line.409"/></td>
                                        <td class="col-md-3"><input type="text" name="a8spt191.allOtherTaxesDu" id="a8spt191.allOtherTaxesDu" class="form-control input-xs moneyField " value="<fmt:formatNumber  type = "currency" currencySymbol="" value="${hudOne.a8spt191.allOtherTaxesDu}"/>"/></td>
                                    </tr>
                                    <tr>
                                        <td class="col-md-9"><fmt:message key="lbl.line.410"/></td>
                                        <td class="col-md-3"><input type="text" name="a8spt191.firstOtherAmtDu" id="a8spt191.firstOtherAmtDu" class="form-control input-xs moneyField " value="<fmt:formatNumber  type = "currency" currencySymbol="" value="${hudOne.a8spt191.firstOtherAmtDu}"/>"/></td>
                                    </tr>
                                    <tr>
                                        <td class="col-md-9"><fmt:message key="lbl.line.411"/></td>
                                        <td class="col-md-3"><input type="text" name="a8spt191.secondOtherAmtD" id="a8spt191.secondOtherAmtD" class="form-control input-xs moneyField " value="<fmt:formatNumber  type = "currency" currencySymbol="" value="${hudOne.a8spt191.secondOtherAmtD}"/>"/></td>
                                    </tr>
                                    <tr>
                                        <td class="col-md-9"><fmt:message key="lbl.line.412"/></td>
                                        <td class="col-md-3"><input type="text" name="a8spt191.condoHoaFee" id="a8spt191.condoHoaFee" class="form-control input-xs moneyField " value="<fmt:formatNumber  type = "currency" currencySymbol="" value="${hudOne.a8spt191.condoHoaFee}"/>"/></td>
                                    </tr>
                                    <tr>
                                        <td class="col-md-9"><fmt:message key="lbl.line.420_601"/></td>
                                        <td class="col-md-3"><input type="text" name="a8spt191.grossAmountDueS" id="a8spt191.grossAmountDueS" class="form-control input-xs moneyField " value="<fmt:formatNumber  type = "currency" currencySymbol="" value="${hudOne.a8spt191.grossAmountDueS}"/>"/></td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                            <div class="table-responsive  col-md-6">
                                <table class="table">
                                    <thead>
                                        <tr>
                                            <th><strong><fmt:message key="lbl.table.dueFromSeller"/></strong></th>
                                            <th><strong>&nbsp;</strong></th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                    <tr>
                                        <td class="col-md-9"><fmt:message key="lbl.line.501"/></td>
                                        <td class="col-md-3"><input type="text" name="a8spt191.earnestMoneyDepo" id="a8spt191.earnestMoneyDepo" class="form-control input-xs moneyField " value="<fmt:formatNumber  type = "currency" currencySymbol="" value="${hudOne.a8spt191.earnestMoneyDepo}"/>"/></td>
                                    </tr>
                                    <tr>
                                        <td class="col-md-9"><fmt:message key="lbl.line.502"/></td>
                                        <td class="col-md-3"><input type="text" name="a8spt191.settlementCharge" id="a8spt191.settlementCharge" class="form-control input-xs moneyField " value="<fmt:formatNumber  type = "currency" currencySymbol="" value="${hudOne.a8spt191.settlementCharge}"/>"/></td>
                                    </tr>
                                    <tr>
                                        <td class="col-md-9" class="control-label"><fmt:message key="lbl.line.503"/></td>
                                        <td class="col-md-3"><input type="text" name="a8spt191.noteRecvdSeller" id="a8spt191.noteRecvdSeller" class="form-control input-xs moneyField " value="<fmt:formatNumber  type = "currency" currencySymbol="" value="${hudOne.a8spt191.noteRecvdSeller}"/>"/></td>
                                    </tr>
                                    <tr>
                                        <td class="col-md-9"><fmt:message key="lbl.line.506"/></td>
                                        <td class="col-md-3"><input type="text" name="a8spt191.extensionFeeRefu" id="a8spt191.extensionFeeRefu" class="form-control input-xs moneyField " value="<fmt:formatNumber  type = "currency" currencySymbol="" value="${hudOne.a8spt191.extensionFeeRefu}"/>"/></td>
                                    </tr>
                                    <tr>
                                        <td class="col-md-9"><fmt:message key="lbl.line.507"/></td>
                                        <td class="col-md-3"><input type="text" class="form-control input-xs moneyField readonly" value="<fmt:formatNumber  type = "currency" currencySymbol="" value="${hudOne.a8spt191.purchaserAllowanc}"/>" readonly="readonly"/></td>
                                    </tr>
                                    <tr>
                                        <td class="col-md-9"><fmt:message key="lbl.line.508"/></td>
                                        <td class="col-md-3"><input type="text" name="a8spt191.utilityEscrowAmo" id="a8spt191.utilityEscrowAmo" class="form-control input-xs moneyField " value="<fmt:formatNumber  type = "currency" currencySymbol="" value="${hudOne.a8spt191.utilityEscrowAmo}"/>"/></td>
                                    </tr>
                                    <tr>
                                        <td class="col-md-9"><fmt:message key="lbl.line.509"/></td>
                                        <td class="col-md-3"><input type="text" name="a8spt191.repairEscrowAmou" id="a8spt191.repairEscrowAmou" class="form-control input-xs moneyField " value="<fmt:formatNumber  type = "currency" currencySymbol="" value="${hudOne.a8spt191.repairEscrowAmou}"/>"/></td>
                                    </tr>
                                    </tbody>
                                </table>
                                <table class="table">
                                    <thead>
                                        <tr>
                                            <th><strong><fmt:message key="lbl.table.adjustmentsUnpaidSeller"/></strong></th>
                                            <th><strong>&nbsp;</strong></th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                    <tr>
                                        <td class="col-md-9"><fmt:message key="lbl.line.510"/></td>
                                        <td class="col-md-3"><input type="text" name="a8spt191.unpaidCityTownT" id="a8spt191.unpaidCityTownT" class="form-control input-xs moneyField " value="<fmt:formatNumber  type = "currency" currencySymbol="" value="${hudOne.a8spt191.unpaidCityTownT}"/>"/></td>
                                    </tr>
                                    <tr>
                                        <td class="col-md-9"><fmt:message key="lbl.line.511"/></td>
                                        <td class="col-md-3"><input type="text" name="a8spt191.unpaidCountyTaxe" id="a8spt191.unpaidCountyTaxe" class="form-control input-xs moneyField " value="<fmt:formatNumber  type = "currency" currencySymbol="" value="${hudOne.a8spt191.unpaidCountyTaxe}"/>"/></td>
                                    </tr>
                                    <tr>
                                        <td class="col-md-9"><fmt:message key="lbl.line.512"/></td>
                                        <td class="col-md-3"><input type="text" name="a8spt191.unpaidAssessment" id="a8spt191.unpaidAssessment" class="form-control input-xs moneyField " value="<fmt:formatNumber  type = "currency" currencySymbol="" value="${hudOne.a8spt191.unpaidAssessment}"/>"/></td>
                                    </tr>
                                    <tr>
                                        <td class="col-md-9"><fmt:message key="lbl.line.513_1400"/></td>
                                        <td class="col-md-3"><input type="text" name="a8spt191.allOtherUnpaidT" id="a8spt191.allOtherUnpaidT" class="form-control input-xs moneyField " value="<fmt:formatNumber  type = "currency" currencySymbol="" value="${hudOne.a8spt191.allOtherUnpaidT}"/>"/></td>
                                    </tr>
                                    <tr>
                                        <td class="col-md-9"><fmt:message key="lbl.line.514"/></td>
                                        <td class="col-md-3"><input type="text" name="a8spt191.hudTaxInterestA" id="a8spt191.hudTaxInterestA" class="form-control input-xs moneyField " value="<fmt:formatNumber  type = "currency" currencySymbol="" value="${hudOne.a8spt191.hudTaxInterestA}"/>"/></td>
                                    </tr>
                                    <tr>
                                        <td class="col-md-9"><fmt:message key="lbl.line.514_2"/></td>
                                        <td class="col-md-3"><input type="text" name="a8spt191.mortgageeInterest" id="a8spt191.mortgageeInterest" class="form-control input-xs moneyField " value="<fmt:formatNumber  type = "currency" currencySymbol="" value="${hudOne.a8spt191.mortgageeInterest}"/>"/></td>
                                    </tr>
                                    <tr>
                                        <td class="col-md-9"><fmt:message key="lbl.line.519"/></td>
                                        <td class="col-md-3"><input type="text" name="a8spt191.condoHoaFee2" id="a8spt191.condoHoaFee2" class="form-control input-xs moneyField " value="<fmt:formatNumber  type = "currency" currencySymbol="" value="${hudOne.a8spt191.condoHoaFee2}"/>"/></td>
                                    </tr>
                                    <tr>
                                        <td class="col-md-9"><fmt:message key="lbl.line.515"/></td>
                                        <td class="col-md-3"><input type="text" name="a8spt191.otherUnpaidItem" id="a8spt191.otherUnpaidItem" class="form-control input-xs moneyField " value="<fmt:formatNumber  type = "currency" currencySymbol="" value="${hudOne.a8spt191.otherUnpaidItem}"/>"/></td>
                                    </tr>
                                    <tr>
                                        <td class="col-md-9"><fmt:message key="lbl.line.516"/></td>
                                        <td class="col-md-3"><input type="text" name="a8spt191.secondOtherUnpai" id="a8spt191.secondOtherUnpai" class="form-control input-xs moneyField " value="<fmt:formatNumber  type = "currency" currencySymbol="" value="${hudOne.a8spt191.secondOtherUnpai}"/>"/></td>
                                    </tr>
                                    <tr>
                                        <td class="col-md-9"><fmt:message key="lbl.line.517"/></td>
                                        <td class="col-md-3"><input type="text" name="a8spt191.thirdOtherUnpaid" id="a8spt191.thirdOtherUnpaid" class="form-control input-xs moneyField " value="<fmt:formatNumber  type = "currency" currencySymbol="" value="${hudOne.a8spt191.thirdOtherUnpaid}"/>"/></td>
                                    </tr>
                                    <tr>
                                        <td class="col-md-9"><fmt:message key="lbl.line.518"/></td>
                                        <td class="col-md-3"><input type="text" name="a8spt191.fourthOtherUnpai" id="a8spt191.fourthOtherUnpai" class="form-control input-xs moneyField " value="<fmt:formatNumber  type = "currency" currencySymbol="" value="${hudOne.a8spt191.fourthOtherUnpai}"/>"/></td>
                                    </tr>
                                    <tr>
                                        <td class="col-md-9"><fmt:message key="lbl.line.520_602"/></td>
                                        <td class="col-md-3"><input type="text" name="a8spt191.lessReductionsDu" id="a8spt191.lessReductionsDu" class="form-control input-xs moneyField " value="<fmt:formatNumber  type = "currency" currencySymbol="" value="${hudOne.a8spt191.lessReductionsDu}"/>"/></td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>

                    <div class="panel panel-default">
                        <div class="panel-heading col-md-6">
                            <label class="panel-title"><fmt:message key="lbl.header.closingInformation"/></label>
                        </div>
                        <div class="panel-body">
                            <div class="table-responsive col-md-6">
                                <table class="table">
                                    <thead>
                                        <tr>
                                            <th><strong><fmt:message key="lbl.table.calculations"/></strong></th>
                                            <th></th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                    <tr>
                                        <td class="col-md-9"><fmt:message key="lbl.line.totalDueTo"/></td>
                                        <td class="col-md-3"><input type="text" class="form-control input-xs moneyField readonly" value="<fmt:formatNumber  type = "currency" currencySymbol="" value="${hudOne.a8spt191.grossAmountDueS}"/>" readonly="readonly"/></td>
                                    </tr>
                                    <tr>
                                        <td class="col-md-9"><fmt:message key="lbl.line.totalDueFrom"/></td>
                                        <td class="col-md-3"><input type="text" class="form-control input-xs moneyField readonly" value="<fmt:formatNumber  type = "currency" currencySymbol="" value="${hudOne.a8spt191.lessReductionsDu}"/>" readonly="readonly"/></td>
                                    </tr>
                                    <tr>
                                        <td class="col-md-9"><fmt:message key="lbl.line.603"/></td>
                                        <td class="col-md-3"><input type="text" class="form-control input-xs moneyField readonly" value="<fmt:formatNumber  type = "currency" currencySymbol="" value="${hudOne.a8spt191.cashDueSettlemen}"/>" readonly="readonly"/></td>
                                    </tr>
                                    <tr>
                                        <td class="col-md-9"><fmt:message key="lbl.line.603B"/></td>
                                        <td class="col-md-3"><input type="text" name="a8spt028.sequenceAmount" id="a8spt028.sequenceAmount" class="form-control input-xs moneyField " value="<fmt:formatNumber  type = "currency" currencySymbol="" value="${hudOne.a8spt028.sequenceAmount}"/>"/></td>
                                    </tr>
                                    <tr>
                                        <c:choose>
                                            <c:when test="${hudOne.a8spt028.sequenceAmount == ''}">
                                                <c:set  var="difference" value="${hudOne.a8spt191.cashDueSettlemen}"/>
                                            </c:when>
                                            <c:otherwise>
                                                <c:set  var="difference" value="${hudOne.a8spt191.cashDueSettlemen - hudOne.a8spt028.sequenceAmount}"/>
                                            </c:otherwise>
                                        </c:choose>
                                        <td class="col-md-9"><fmt:message key="lbl.line.603C"/></td>
                                        <td class="col-md-3"><input type="text" id="difference" class="form-control input-xs moneyField " value="<fmt:formatNumber  type = "currency" currencySymbol="" value="${difference}"/>"/></td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                            <div class="table-responsive col-md-6">
                                <table class="table">
                                    <thead>
                                        <tr>
                                            <th><strong><fmt:message key="lbl.table.otherCosts"/></strong></th>
                                            <th></th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                    <tr>
                                        <td class="col-md-9"><fmt:message key="lbl.line.703"/></td>
                                        <td class="col-md-3"><input type="text" class="form-control input-xs moneyField readonly" value="<fmt:formatNumber  type = "currency" currencySymbol="" value="${hudOne.a8spt191.sellingBrokerCom}"/>" readonly="readonly"/></td>
                                    </tr>
                                    <tr>
                                        <td class="col-md-9"><fmt:message key="lbl.line.sellingBroker"/></td>
                                        <td class="col-md-3"><input type="text" class="form-control input-xs readonly" value="<c:out value="${hudOne.a8spt190.sellingBrokerNai}"/>" readonly="readonly"/></td>
                                    </tr>
                                    <tr>
                                        <td class="col-md-9"><fmt:message key="lbl.line.sellingBrokerFee"/></td>
                                        <td class="col-md-3"><input type="text" class="form-control input-xs moneyField readonly" value="<fmt:formatNumber  type = "currency" currencySymbol="" value="${hudOne.a8spt190.sellBrokerFee}"/>" readonly="readonly"/></td>
                                    </tr>
                                    <tr>
                                        <td class="col-md-9"><fmt:message key="lbl.line.listingBroker"/></td>
                                        <td class="col-md-3"><input type="text" class="form-control input-xs readonly" value="<c:out value="${hudOne.a8spt190.listingBrokerNai}"/>" readonly="readonly"/></td>
                                    </tr>
                                    <tr>
                                        <td class="col-md-9"><fmt:message key="lbl.line.listingBrokerFee"/></td>
                                        <td class="col-md-3"><input type="text" class="form-control input-xs moneyField readonly" value="<fmt:formatNumber  type = "currency" currencySymbol="" value="${hudOne.a8spt190.listBrokerFee}"/>" readonly="readonly"/></td>
                                    </tr>
                                    <tr>
                                        <td class="col-md-9"><fmt:message key="lbl.line.703"/>)</td>
                                        <td class="col-md-3"><input type="text" name="a8spt191.sellingBrokerCom" id="a8spt191.sellingBrokerCom" class="form-control input-xs moneyField " value="<fmt:formatNumber  type = "currency" currencySymbol="" value="${hudOne.a8spt191.sellingBrokerCom}"/>"/></td>
                                    </tr>
                                    <tr>
                                        <td class="col-md-9"><fmt:message key="lbl.line.704"/></td>
                                        <td class="col-md-3"><input type="text" name="a8spt191.brokerEarlyClsng" id="a8spt191.brokerEarlyClsng" class="form-control input-xs moneyField " value="<fmt:formatNumber  type = "currency" currencySymbol="" value="${hudOne.a8spt191.brokerEarlyClsng}"/>"/></td>
                                    </tr>
                                    <tr>
                                        <td class="col-md-9"><fmt:message key="lbl.line.1102"/></td>
                                        <td class="col-md-3"><input type="text" name="a8spt190.titleId" id="a8spt190.titleId" class="form-control input-xs " value="<c:out value="${hudOne.a8spt190.titleId}"/>"/></td>
                                    </tr>
                                    <tr>
                                        <td class="col-md-9"><fmt:message key="lbl.line.closingAgent"/></td>
                                        <td class="col-md-3"><input type="text" class="form-control input-xs readonly" value="<c:out value="${hudOne.a8spt190.closingAgent}"/>" readonly="readonly"/></td>
                                    </tr>
                                    <tr>
                                        <td class="col-md-9"><fmt:message key="lbl.line.name"/></td>
                                        <td class="col-md-3"><input type="text" class="form-control input-xs readonly" value="<c:out value="${hudOne.a8spt190.name}"/>" readonly="readonly"/></td>
                                    </tr>
                                    <tr>
                                        <td class="col-md-9"><fmt:message key="lbl.line.1304"/></td>
                                        <td class="col-md-3"><input type="text" name="a8spt191.purchaser1304" id="a8spt191.purchaser1304" class="form-control input-xs moneyField " value="<fmt:formatNumber  type = "currency" currencySymbol="" value="${hudOne.a8spt191.purchaser1304}"/>"/></td>
                                    </tr>
                                    <tr>
                                        <td class="col-md-9"><fmt:message key="lbl.line.closingFee"/></td>
                                        <%-- <td class="col-md-3"><input type="text" name="a8spt191.thirdPartyClosin" id="a8spt191.thirdPartyClosin" class="form-control input-xs " value="<c:out value="${hudOne.a8spt191.thirdPartyClosin}"/>"/></td> --%>
                                        <td class="col-md-3">
                                            <select class="form-control input-sm" name="a8spt191.thirdPartyClosin" id="a8spt191.thirdPartyClosin">
                                                <option value="<c:out value="${hudOne.a8spt191.thirdPartyClosin}"/>"><c:out value="${hudOne.a8spt191.thirdPartyClosin}"/></option>
                                                <option value="Y">Y</option>
                                                <option value="N">N</option>
                                            </select>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="col-md-9"><fmt:message key="lbl.line.tolerance"/></td>                                       
                                        <td class="col-md-3">
                                            <select class="form-control input-sm" name="a8spt191.reconcileWithinT" id="a8spt191.reconcileWithinT">
                                                <option value="<c:out value="${hudOne.a8spt191.reconcileWithinT}"/>"><c:out value="${hudOne.a8spt191.reconcileWithinT}"/></option>
                                                <option value="Y">Y</option>
                                                <option value="N">N</option>
                                            </select>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
           
            </div>

            <div >
                <input type="button" value="Cancel" class="btn btn-warning floatR" data-toggle="modal" data-target="#cancelModal"/>
                <input type="submit" value="Update" class="btn btn-success floatR" style="margin-right: .5em;"/>
            </div>
            <script>
                function loadFieldErrors() {
                    <spring:hasBindErrors name="hudOne">
                    <form:errors path="a8spt191.noteRecvdSeller">highlightErrorField('a8spt191\\.noteRecvdSeller');highlightErrorField('a8spt191\\.financeType');</form:errors>
                    <form:errors path="a8spt191.dateClosed">highlightErrorField('a8spt191\\.dateClosed');</form:errors>
                    <form:errors path="a8spt191.dateReceived">highlightErrorField('a8spt191\\.dateReceived');</form:errors>
                    <form:errors path="a8spt191.financeType">highlightErrorField('a8spt191\\.financeType');</form:errors>
                    <form:errors path="a8spt191.hudProperty.currentHudOffice">highlightErrorField('a8spt191\\.hudProperty\\.currentHudOffice');</form:errors>
                    
        
                    $('.alert-danger').first().find('.form-control').focus();
                    </spring:hasBindErrors>
                }
        
                function highlightErrorField(field) {
                    $('#' + field).addClass('alert-danger');
                    $('#' + field).prev().addClass('text-danger');
                }  
            </script>
        </form:form>
    </div>
</main>
</body>
</html>
