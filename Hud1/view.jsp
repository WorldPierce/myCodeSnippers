<!DOCTYPE html>


<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="reopenCaseFlag">${sessionScope.userPermissions.get(permissions['REOPEN_CASE'].id()) != null ? 'true' : 'false'}</c:set>

<html lang="en">
<head>
    <title><fmt:message key="lbl.title.hud1"/></title>
    <script>
    function reopenCase() {
        var result = confirm('Are you sure you want to reopen this case?');
        if(result) {
            var reopenUrl = "<c:url value='/hud1/reopen.html'/>?caseNumber=" + $('#caseNumber').val();
            $.ajax({
                url: reopenUrl ,
                success: function(){
                    window.location.href = "<c:url value='/hud1/view.html'/>?caseNumber=" + $('#caseNumber').val() + "&message=Update Successful";
                },
                error: function(XMLHttpRequest, textStatus, errorThrown) { 
                    alert("Error: Could not move case from step 10 to step 9."); 
                }       
            });
        }
    }
    </script>
    
</head>

<body>

<main>
    
    <div class="container">
        <div>
            <fieldset>
                <legend class="heading"><fmt:message key="lbl.header.closingDisclosure"/>
                    <c:if test="${hudProperty.caseStepNumber != 10}">
                        <input type="button" class="btn btn-primary btn-sm floatR" value="<fmt:message key="lbl.btn.edit"/>" onclick="window.location.href = '<c:url value="/hud1/modify.html?caseNumber=${hudProperty.caseHudOfficeP}-${hudProperty.caseNum}"/>'"/>
                    </c:if>
                    <c:if test="${reopenCaseFlag == 'true' && hudProperty.caseStepNumber == 10}">
                        <input type="button" class="btn btn-primary btn-sm floatR" value="<fmt:message key="lbl.btn.reopen"/>" onclick="reopenCase()" style="margin-right: .5em;"/>
                    </c:if>

                </legend>
                
            </fieldset>
        </div>
        <c:if test="${error != null && error != ''}">       
            <div class="alert alert-danger">
                <strong><c:out value="${error}"/></strong>
            </div>
        </c:if>
        <c:if test="${message != null && message != ''}">        
           <div class="alert alert-success">
               <strong><c:out value="${message}"/></strong>
           </div>    
        </c:if>
        <form>
            <div class="panel panel-default">
                <div class="panel-heading">
                    <label class="panel-title"><fmt:message key="lbl.header.caseInformation"/></label>
                </div>
                <div class="panel-body">
                    <div class="form-group col-md-2">
                        <label for="caseNumber" style="white-space: nowrap;font-size: small"><fmt:message key="lbl.caseNumber"/></label>
                        <input type="text" class="form-control readonly" id="caseNumber" placeholder="e.g. 001-000001" value='<c:out value="${hudProperty.caseHudOfficeP}-${hudProperty.caseNum}"/>' readonly />
                    </div>
                    <div class="form-group col-md-2">
                        <label for="contractNumber" style="white-space: nowrap;font-size: small"><fmt:message key="lbl.contractNumber"/></label>
                        <input type="text" class="form-control readonly" id="contractNumber" placeholder="" value='<c:out value="${viewCase190.contractNumber}"/>' readonly />
                    </div>
                    <div class="form-group col-md-2">
                        <label for="earnestMoney" style="white-space: nowrap;font-size: small"><fmt:message key="lbl.earnestMoney"/></label>
                        <input type="text" class="form-control readonly" id="earnestMoney" placeholder="" value='<fmt:formatNumber value = "${viewCase.cashDueSettlemen}" type = "currency" currencySymbol=""/>' readonly />
                    </div>
                    <div class="form-group col-md-6">
                        <label for="address" style="white-space: nowrap;font-size: small"><fmt:message key="lbl.address"/></label>
                        <input type="text" class="form-control readonly" id="address" placeholder="" value='<c:out value="${hudProperty.address1}, ${hudProperty.city}, ${hudProperty.stateCode} ${hudProperty.displayZipCode}"/>' readonly />
                    </div>
                    <c:catch var="dateException">
                        <fmt:parseDate var="dateReceived" value="${viewCase.dateReceived}" pattern="yyyy-MM-dd"/>
                        <c:set var="dateReceived"><fmt:formatDate pattern="MM/dd/yyyy"  value="${dateReceived}"/></c:set>
                    </c:catch>
                    <c:if test="${not empty dateException}">
                        <c:set var="dateReceived" value="${viewCase.dateReceived}"/>
                    </c:if>
                    <div class="form-group col-md-2">
                        <label for="dateReceived" style="white-space: nowrap;font-size: small"><fmt:message key="lbl.dateReceived"/></label>
                        <input type="text" class="form-control readonly" id="dateReceived" placeholder="" value="${dateReceived}" readonly />
                    </div>
                    <c:catch var="dateException">
                        <fmt:parseDate var="dateClosed" value="${viewCase.dateClosed}" pattern="yyyy-MM-dd"/>
                        <c:set var="dateClosed"><fmt:formatDate pattern="MM/dd/yyyy"  value="${dateClosed}"/></c:set>
                    </c:catch>
                    <c:if test="${not empty dateException}">
                        <c:set var="dateClosed" value="${viewCase.dateClosed}"/>
                    </c:if>
                    <div class="form-group col-md-2">
                        <label for="closingDate" style="white-space: nowrap;font-size: small"><fmt:message key="lbl.closingDate"/></label>
                        <input type="text" class="form-control readonly" id="closingDate" placeholder="" value="${dateClosed}" readonly />
                    </div>
                    <c:catch var="dateException">
                        <fmt:parseDate var="dateReconciled" value="${hudProperty.dateReconciled}" pattern="yyyy-MM-dd"/>
                        <c:set var="dateReconciled"><fmt:formatDate pattern="MM/dd/yyyy"  value="${dateReconciled}"/></c:set>
                    </c:catch>
                    <c:if test="${not empty dateException}">
                        <c:set var="dateClosed" value="${hudProperty.dateReconciled}"/>
                    </c:if>
                    <div class="form-group col-md-2">
                        <label for="reconciliationDate" style="white-space: nowrap;font-size: small"><fmt:message key="lbl.reconciliationDate"/></label>
                        <input type="text" class="form-control readonly" id="reconciliationDate" placeholder="" value="${dateReconciled}" readonly />
                    </div>
                    <div class="form-group col-md-1">
                        <label for="financeType" style="white-space: nowrap;font-size: small"><fmt:message key="lbl.financeType"/></label>
                        <input type="text" class="form-control readonly" id="financeType" placeholder="" value='<c:out value="${viewCase.financeType}"/>' readonly />
                    </div>
                    <div class="form-group col-md-2">
                        <label for="optionalField" style="white-space: nowrap;font-size: small"><fmt:message key="lbl.optionalField"/></label>
                        <input type="text" class="form-control readonly" id="optionalField" placeholder="" value='<c:out value="${viewCase.optionalField}"/>' readonly />
                    </div>
                    <div class="form-group col-md-1">
                        <label for="area" style="white-space: nowrap;font-size: small"><fmt:message key="lbl.area"/></label>
                        <input type="text" class="form-control readonly" id="area" placeholder="" value='<c:out value="${hudProperty.hudOfficeCode}"/>' readonly />
                    </div>
                    <div class="form-group col-md-2">
                        <label for="updatedBy" style="white-space: nowrap;font-size: small"><fmt:message key="lbl.updatedBy"/></label>
                        <input type="text" class="form-control readonly" id="updatedBy" placeholder="" value='<c:out value="${viewCase.updatedBy}"/>' readonly />
                    </div>
                </div>
            </div>

            <ul class="nav nav-tabs" role="tablist">
                <li role="presentation" class="active"><a href="#page1" aria-controls="page1" role="tab" data-toggle="tab"><fmt:message key="lbl.tab.pageOne"/></a></li>
                <li role="presentation"><a href="#settlementErrorHistory" aria-controls="settlementErrorHistory" role="tab" data-toggle="tab"><fmt:message key="lbl.header.settlementErrorHistory"/></a></li>
                <li role="presentation"><a href="#salesExtension" aria-controls="salesExtension" role="tab" data-toggle="tab"><fmt:message key="lbl.header.salesExtension"/></a></li>
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
                                            <td class="col-md-3 text-right"><fmt:formatNumber  type = "currency" currencySymbol="" value="${viewCase.contractSalesPri}"/></td>
                                        </tr>
                                        <tr>
                                            <td class="col-md-9"><fmt:message key="lbl.line.404"/></td>
                                            <td class="col-md-3 text-right"><fmt:formatNumber  type = "currency" currencySymbol="" value="${viewCase.extensionFee}"/></td>
                                        </tr>
                                        <tr>
                                            <td class="col-md-9"><fmt:message key="lbl.line.405"/></td>
                                            <td class="col-md-3 text-right"><fmt:formatNumber  type = "currency" currencySymbol="" value="${viewCase.rent}"/></td>
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
                                            <td class="col-md-3 text-right"><fmt:formatNumber  type = "currency" currencySymbol="" value="${viewCase.prepaidCityTown}"/></td>
                                        </tr>
                                        <tr>
                                            <td class="col-md-9"><fmt:message key="lbl.line.407"/></td>
                                            <td class="col-md-3 text-right"><fmt:formatNumber  type = "currency" currencySymbol="" value="${viewCase.prepaidCountyTax}"/></td>
                                        </tr>
                                        <tr>
                                            <td class="col-md-9"><fmt:message key="lbl.line.408"/></td>
                                            <td class="col-md-3 text-right"><fmt:formatNumber  type = "currency" currencySymbol="" value="${viewCase.prepaidAssessment}"/></td>
                                        </tr>
                                        <tr>
                                            <td class="col-md-9"><fmt:message key="lbl.line.409"/></td>
                                            <td class="col-md-3 text-right"><fmt:formatNumber  type = "currency" currencySymbol="" value="${viewCase.allOtherTaxesDu}"/></td>
                                        </tr>
                                        <tr>
                                            <td class="col-md-9"><fmt:message key="lbl.line.410"/></td>
                                            <td class="col-md-3 text-right"><fmt:formatNumber  type = "currency" currencySymbol="" value="${viewCase.firstOtherAmtDu}"/></td>
                                        </tr>
                                        <tr>
                                            <td class="col-md-9"><fmt:message key="lbl.line.411"/></td>
                                            <td class="col-md-3 text-right"><fmt:formatNumber  type = "currency" currencySymbol="" value="${viewCase.secondOtherAmtD}"/></td>
                                        </tr>
                                        <tr>
                                            <td class="col-md-9"><fmt:message key="lbl.line.412"/></td>
                                            <td class="col-md-3 text-right"><fmt:formatNumber  type = "currency" currencySymbol="" value="${viewCase.condoHoaFee}"/></td>
                                        </tr>
                                        <tr>
                                            <td class="col-md-9"><fmt:message key="lbl.line.420_601"/></td>
                                            <td class="col-md-3 text-right"><fmt:formatNumber  type = "currency" currencySymbol="" value="${viewCase.grossAmountDueS}"/></td>
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
                                            <td class="col-md-3 text-right"><fmt:formatNumber  type = "currency" currencySymbol="" value="${viewCase.earnestMoneyDepo}"/></td>
                                        </tr>
                                        <tr>
                                            <td class="col-md-9"><fmt:message key="lbl.line.502"/></td>
                                            <td class="col-md-3 text-right"><fmt:formatNumber  type = "currency" currencySymbol="" value="${viewCase.settlementCharge}"/></td>
                                        </tr>
                                        <tr>
                                            <td class="col-md-9"><fmt:message key="lbl.line.503"/></td>
                                            <td class="col-md-3 text-right"><fmt:formatNumber  type = "currency" currencySymbol="" value="${viewCase.noteRecvdSeller}"/></td>
                                        </tr>
                                        <tr>
                                            <td class="col-md-9"><fmt:message key="lbl.line.506"/></td>
                                            <td class="col-md-3 text-right"><fmt:formatNumber  type = "currency" currencySymbol="" value="${viewCase.extensionFeeRefu}"/></td>
                                        </tr>
                                        <tr>
                                            <td class="col-md-9"><fmt:message key="lbl.line.507"/></td>
                                            <td class="col-md-3 text-right"><fmt:formatNumber  type = "currency" currencySymbol="" value="${viewCase.purchaserAllowanc}"/></td>
                                        </tr>
                                        <tr>
                                            <td class="col-md-9"><fmt:message key="lbl.line.508"/></td>
                                            <td class="col-md-3 text-right"><fmt:formatNumber  type = "currency" currencySymbol="" value="${viewCase.utilityEscrowAmo}"/></td>
                                        </tr>
                                        <tr>
                                            <td class="col-md-9"><fmt:message key="lbl.line.509"/></td>
                                            <td class="col-md-3 text-right"><fmt:formatNumber  type = "currency" currencySymbol="" value="${viewCase.repairEscrowAmou}"/></td>
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
                                            <td class="col-md-3 text-right"><fmt:formatNumber  type = "currency" currencySymbol="" value="${viewCase.unpaidCityTownT}"/></td>
                                        </tr>
                                        <tr>
                                            <td class="col-md-9"><fmt:message key="lbl.line.511"/></td>
                                            <td class="col-md-3 text-right"><fmt:formatNumber  type = "currency" currencySymbol="" value="${viewCase.unpaidCountyTaxe}"/></td>
                                        </tr>
                                        <tr>
                                            <td class="col-md-9"><fmt:message key="lbl.line.512"/></td>
                                            <td class="col-md-3 text-right"><fmt:formatNumber  type = "currency" currencySymbol="" value="${viewCase.unpaidAssessment}"/></td>
                                        </tr>
                                        <tr>
                                            <td class="col-md-9"><fmt:message key="lbl.line.513_1400"/></td>
                                            <td class="col-md-3 text-right"><fmt:formatNumber  type = "currency" currencySymbol="" value="${viewCase.allOtherUnpaidT}"/></td>
                                        </tr>
                                        <tr>
                                            <td class="col-md-9"><fmt:message key="lbl.line.514"/></td>
                                            <td class="col-md-3 text-right"><fmt:formatNumber  type = "currency" currencySymbol="" value="${viewCase.hudTaxInterestA}"/></td>
                                        </tr>
                                        <tr>
                                            <td class="col-md-9"><fmt:message key="lbl.line.514_2"/></td>
                                            <td class="col-md-3 text-right"><fmt:formatNumber  type = "currency" currencySymbol="" value="${viewCase.mortgageeInterest}"/></td>
                                        </tr>
                                        <tr>
                                            <td class="col-md-9"><fmt:message key="lbl.line.519"/></td>
                                            <td class="col-md-3 text-right"><fmt:formatNumber  type = "currency" currencySymbol="" value="${viewCase.condoHoaFee2}"/></td>
                                        </tr>
                                        <tr>
                                            <td class="col-md-9"><fmt:message key="lbl.line.515"/></td>
                                            <td class="col-md-3 text-right"><fmt:formatNumber  type = "currency" currencySymbol="" value="${viewCase.otherUnpaidItem}"/></td>
                                        </tr>
                                        <tr>
                                            <td class="col-md-9"><fmt:message key="lbl.line.516"/></td>
                                            <td class="col-md-3 text-right"><fmt:formatNumber  type = "currency" currencySymbol="" value="${viewCase.secondOtherUnpai}"/></td>
                                        </tr>
                                        <tr>
                                            <td class="col-md-9"><fmt:message key="lbl.line.517"/></td>
                                            <td class="col-md-3 text-right"><fmt:formatNumber  type = "currency" currencySymbol="" value="${viewCase.thirdOtherUnpaid}"/></td>
                                        </tr>
                                        <tr>
                                            <td class="col-md-9"><fmt:message key="lbl.line.518"/></td>
                                            <td class="col-md-3 text-right"><fmt:formatNumber  type = "currency" currencySymbol="" value="${viewCase.fourthOtherUnpai}"/></td>
                                        </tr>
                                        <tr>
                                            <td class="col-md-9"><fmt:message key="lbl.line.520_602"/></td>
                                            <td class="col-md-3 text-right"><fmt:formatNumber  type = "currency" currencySymbol="" value="${viewCase.lessReductionsDu}"/></td>
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
                                            <td class="col-md-3 text-right"><fmt:formatNumber  type = "currency" currencySymbol="" value="${viewCase.grossAmountDueS}"/></td>
                                        </tr>
                                        <tr>
                                            <td class="col-md-9"><fmt:message key="lbl.line.totalDueFrom"/></td>
                                            <td class="col-md-3 text-right"><fmt:formatNumber  type = "currency" currencySymbol="" value="${viewCase.lessReductionsDu}"/></td>
                                        </tr>
                                        <tr>
                                            <td class="col-md-9"><fmt:message key="lbl.line.603"/></td>
                                            <td class="col-md-3 text-right"><fmt:formatNumber  type = "currency" currencySymbol="" value="${viewCase.cashDueSettlemen}"/></td>
                                        </tr>
                                        <tr>
                                            <td class="col-md-9"><fmt:message key="lbl.line.603B"/></td>
                                            <td class="col-md-3 text-right"><fmt:formatNumber  type = "currency" currencySymbol="" value="${fedwireAmount}"/></td>
                                        </tr>
                                        <tr>
                                            <c:choose>
                                                <c:when test="${fedwireAmount == ''}">
                                                    <c:set  var="difference" value="${viewCase.cashDueSettlemen}"/>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:set  var="difference" value="${viewCase.cashDueSettlemen - fedwireAmount}"/>
                                                </c:otherwise>
                                            </c:choose>
                                            <td class="col-md-9"><fmt:message key="lbl.line.603C"/></td>
                                            <td class="col-md-3 text-right"><fmt:formatNumber  type ="number" pattern="#,##0.00;-#,##0.00" value="${difference}"/></td>
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
                                            <td class="col-md-3 text-right"><fmt:formatNumber  type = "currency" currencySymbol="" value="${viewCase.sellingBrokerCom}"/></td>
                                        </tr>
                                        <tr>
                                            <td class="col-md-9"><fmt:message key="lbl.line.sellingBroker"/></td>
                                            <td class="col-md-3 text-right"><c:out value="${viewCase190.sellingBrokerNai}"/></td>
                                        </tr>
                                        <tr>
                                            <td class="col-md-9"><fmt:message key="lbl.line.sellingBrokerFee"/></td>
                                            <td class="col-md-3 text-right"><fmt:formatNumber  type = "currency" currencySymbol="" value="${viewCase190.sellBrokerFee}"/></td>
                                        </tr>
                                        <tr>
                                            <td class="col-md-9"><fmt:message key="lbl.line.listingBroker"/></td>
                                            <td class="col-md-3 text-right"><c:out value="${viewCase190.listingBrokerNai}"/></td>
                                        </tr>
                                        <tr>
                                            <td class="col-md-9"><fmt:message key="lbl.line.listingBrokerFee"/></td>
                                            <td class="col-md-3 text-right"><fmt:formatNumber  type = "currency" currencySymbol="" value="${viewCase190.listBrokerFee}"/></td>
                                        </tr>
                                        <tr>
                                            <td class="col-md-9"><fmt:message key="lbl.line.703"/></td>
                                            <td class="col-md-3 text-right"><fmt:formatNumber  type = "currency" currencySymbol="" value="${viewCase.sellingBrokerCom}"/></td>
                                        </tr>
                                        <tr>
                                            <td class="col-md-9"><fmt:message key="lbl.line.704"/></td>
                                            <td class="col-md-3 text-right"><fmt:formatNumber  type = "currency" currencySymbol="" value="${viewCase.brokerEarlyClsng}"/></td>
                                        </tr>
                                        <tr>
                                            <td class="col-md-9"><fmt:message key="lbl.line.1102"/></td>
                                            <td class="col-md-3 text-right"><c:out value="${viewCase190.titleId}"/></td>
                                        </tr>
                                        <tr>
                                            <td class="col-md-9"><fmt:message key="lbl.line.closingAgent"/></td>
                                            <td class="col-md-3 text-right"><c:out value="${viewCase190.closingAgent}"/></td>
                                        </tr>
                                        <tr>
                                            <td class="col-md-9"><fmt:message key="lbl.line.name"/></td>
                                            <td class="col-md-3 text-right"><c:out value="${viewCase190.name}"/></td>
                                        </tr>
                                        <tr>
                                            <td class="col-md-9"><fmt:message key="lbl.line.1304"/></td>
                                            <td class="col-md-3 text-right"><fmt:formatNumber  type = "currency" currencySymbol="" value="${viewCase.purchaser1304}"/></td>
                                        </tr>
                                        <tr>
                                            <td class="col-md-9"><fmt:message key="lbl.line.closingFee"/></td>
                                            <td class="col-md-3 text-right"><c:out value="${viewCase.thirdPartyClosin}"/></td>
                                        </tr>
                                        <tr>
                                            <td class="col-md-9"><fmt:message key="lbl.line.tolerance"/></td>
                                            <td class="col-md-3 text-right"><c:out value="${viewCase.reconcileWithinT}"/></td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
                
                <div role="tabpanel" class="tab-pane" id="settlementErrorHistory">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <label class="panel-title"><fmt:message key="lbl.header.settlementErrorHistory"/></label>
                        </div>
                        <div class="panel-body">
                        
                            <div class="table-responsive col-md-12">
                            <c:choose>
                                    <c:when test="${empty settlementErrorHistory}">
                                         No Settlement Error History Data Found.
                                     </c:when>
                                     <c:otherwise>
                                        <table class="table">
                                            <thead>
                                                <tr>
                                                    <th><strong><fmt:message key="lbl.table.errorCode"/></strong></th>                                            
                                               
                                                    <th><strong><fmt:message key="lbl.table.errorDescription"/></strong></th>                                           
                                             
                                                    <th><strong><fmt:message key="lbl.table.errorDate"/></strong></th>
                                                </tr>
                                            </thead>
                                            <tbody>                                    
                                                <c:forEach items="${settlementErrorHistory}" var="item">
                                                <tr>
                                                    
                                                    <td class="col-md-1"><c:out value="${item.packageErrorCode}"/></td>
                                                    
                                                    <td class="col-md-2"><c:out value="${item.errorDesc}"/></td>
                                                                                         
                                                    <c:catch var="dateException">
                                                        <fmt:parseDate var="historyDate" value="${item.historyDate}" pattern="yyyy-MM-dd"/>
                                                         <c:set var="historyDate"><fmt:formatDate pattern="MM/dd/yyyy"  value="${historyDate}"/></c:set>
                                                     </c:catch>
                                                    <c:if test="${not empty dateException}">
                                                        <c:set var="historyDate" value="${item.historyDate}"/>
                                                    </c:if>
                                                    <td class="col-md-3">${historyDate}</td>
                                                </tr>
                                                </c:forEach>
                                            </tbody>
                                        </table>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                            
                        </div>
                    </div>
                </div>
                <div role="tabpanel" class="tab-pane" id="salesExtension">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <label class="panel-title"><fmt:message key="lbl.header.salesExtension"/></label>
                        </div>
                        <div class="panel-body">
                        
                            <div class="table-responsive col-md-12">
                                <c:choose>
                                    <c:when test="${empty viewCase.salesExtension}">
                                         No Sales Extension Data Found.
                                     </c:when>
                                     <c:otherwise>
                                         <table class="table">
                                            <thead>
                                                <tr>
                                                    <th><strong><fmt:message key="lbl.table.extensionNumber"/></strong></th>
                                                    <th><strong><fmt:message key="lbl.table.bidReceiptNumber"/></strong></th>
                                                    <th><strong><fmt:message key="lbl.table.purchaser"/></strong></th>
                                                    <th><strong><fmt:message key="lbl.table.status"/></strong></th>
                                                    <th><strong><fmt:message key="lbl.table.bidAmount"/></strong></th>                                    
                                                    <th><strong><fmt:message key="lbl.table.extensionReason"/></strong></th>                                                                           
                                                    <th><strong><fmt:message key="lbl.table.prevSchedClosingDate"/></strong></th>
                                                    <th><strong><fmt:message key="lbl.table.numOfDays"/></strong></th>
                                                    <th><strong><fmt:message key="lbl.table.schedClosingDate"/></strong></th>
                                                    <th><strong><fmt:message key="lbl.table.extFeeWaived"/></strong></th>
                                                    <th><strong><fmt:message key="lbl.table.feePerDay"/></strong></th>
                                                    <th><strong><fmt:message key="lbl.table.totalFeeAmount"/></strong></th>
                                                </tr>
                                            </thead>
                                            <tbody>                                             
                                                <c:forEach items="${viewCase.salesExtension}" var="item">
                                                <tr>
                                                    
                                                    
                                                    <td class="col-md-1"><c:out value="${item.extensionNumber}"/></td>
                                                    <td class="col-md-1"><c:out value="${viewCase190.bidReceiptNumber}"/></td>
                                                    <td class="col-md-2"><c:out value="${viewCase190.name}"/></td>
                                                    <td class="col-md-1"><c:out value="${viewCase190.status}"/></td>
                                                    <td class="col-md-1"><fmt:formatNumber  type = "currency" currencySymbol="" value="${viewCase190.bidAmount}"/></td>                                                                                 
                                                    <td class="col-md-2"><c:out value="${item.extensionReasonDescription}"/></td>
                                                                                         
                                                    <c:catch var="dateException">
                                                        <fmt:parseDate var="prevClosingDate" value="${item.origSchedClosing}" pattern="yyyy-MM-dd"/>
                                                         <c:set var="prevClosingDate"><fmt:formatDate pattern="MM/dd/yyyy"  value="${prevClosingDate}"/></c:set>
                                                     </c:catch>
                                                    <c:if test="${not empty dateException}">
                                                        <c:set var="prevClosingDate" value="${item.origSchedClosing}"/>
                                                    </c:if>
                                                    <td class="col-md-2">${prevClosingDate}</td>
                                                    
                                                    <td class="col-md-1"><c:out value="${item.extensionNumberO}"/></td>
                                                    
                                                    <c:catch var="dateException">
                                                        <fmt:parseDate var="closingDate" value="${item.currSchedClosing}" pattern="yyyy-MM-dd"/>
                                                         <c:set var="closingDate"><fmt:formatDate pattern="MM/dd/yyyy"  value="${closingDate}"/></c:set>
                                                     </c:catch>
                                                    <c:if test="${not empty dateException}">
                                                        <c:set var="closingDate" value="${item.currSchedClosing}"/>
                                                    </c:if>
                                                    <td class="col-md-2">${closingDate}</td>
                                                    
                                                    <td class="col-md-1"><c:out value="${item.extensionFeeWaiv}"/></td>
                                                    
                                                    <td class="col-md-1"><c:out value="${item.extensionFeePer}"/></td>
                                                    <c:choose>
                                                        <c:when test="${item.extensionFeeWaiv == 'Y'}">
                                                            <c:set  var="totalFee">0.00</c:set>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <c:set  var="totalFee" value="${item.extensionNumberO * item.extensionFeePer}"/>
                                                            <c:if test="${totalFee > 9999.99 or totalFee < 0.00}">
                                                                <c:set  var="totalFee" value="Invalid: Modify Data"/>
                                                            </c:if>
                                                        </c:otherwise>
                                                    </c:choose>
                                                    
                                                    <td class="col-md-2"><fmt:formatNumber  type = "currency" currencySymbol="" value="${totalFee}"/></td>
                                                </tr>
                                                </c:forEach>
                                            </tbody>
                                            
                                        </table>
                                     
                                     </c:otherwise>
                                </c:choose>
                                                              
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </form>
    </div>
</main>
</body>
</html>
