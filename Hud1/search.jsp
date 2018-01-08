<!DOCTYPE html>


<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<html lang="en">
<head>
    <title>SAMS - Closing Disclosure Search</title>
    <style>
        #case_search_results tbody td:nth-of-type(1){
            color: #337ab7;
        }

        #case_search_results tbody td:nth-of-type(1):hover {
            text-decoration: underline;
        }

        #case_search_results tbody td:nth-of-type(8) {
            text-align: right;
        }

        .i0{ margin-left: 1em;}
    </style>
    <script>
    $(document).ready(function () {
        $("#caseNumber").mask("999-******");
        $('#earnestMoney').autoNumeric('init', {
            vMax : '999999.99',
            vMin : '000000.00'
        });
        $('#contractArea').multiselect({
            maxHeight: 200,
            buttonWidth: '120px',
            enableClickableOptGroups: true
        });
        var dateReconciledFromSel = $( "#dateReconciledFrom" );
        dateReconciledFromSel.datepicker({onSelect: function() {$(this).focus();}});
        dateReconciledFromSel.mask("99/99/9999");

        var dateReconciledToSel = $( "#dateReconciledTo" );
        dateReconciledToSel.datepicker({onSelect: function() {$(this).focus();}});
        dateReconciledToSel.mask("99/99/9999");

        var dateClosedFromSel = $( "#dateClosedFrom" );
        dateClosedFromSel.datepicker({onSelect: function() {$(this).focus();}});
        dateClosedFromSel.mask("99/99/9999");

        var dateClosedToSel = $( "#dateClosedTo" );
        dateClosedToSel.datepicker({onSelect: function() {$(this).focus();}});
        dateClosedToSel.mask("99/99/9999");

        $('input').keydown(function(e) {
            if (e.keyCode === 13) {
                caseSearch();
            }
        });
        
        $('#case_search_results').on('click', 'tbody tr', function() {
            window.location.href='<c:url value="/hud1/view.html"/>?caseNumber=' + $(this)[0].children[0].textContent;    
        }); 
        
    });

    function resetForm() {
        document.getElementById("caseSearchForm").reset();
        var contractAreaSel = $('#contractArea');
        contractAreaSel.prop('selected', false);
        contractAreaSel.multiselect('refresh')
    }
    function caseSearch() {
        
        if (moment($('#dateClosedFrom').val(), 'MM/DD/YYYY').isAfter(moment($('#dateClosedTo').val(), 'MM/DD/YYYY'))) {
            alert("Acqusition Date From cannot be after the To Date.");
        } else if (moment($('#dateReconciledFrom').val(), 'MM/DD/YYYY').isAfter(moment($('#dateReconciledTo').val(), 'MM/DD/YYYY'))) {
            alert("Date Reconciled From Date cannot be after the To Date.");
        } else {
            $('#titleId').val($('#titleId').val().toUpperCase());
            var caseSearchResultsSel = $("#case_search_results");
            caseSearchResultsSel.css('visibility', 'hidden');
            caseSearchResultsSel.css('visibility', 'visible');
            var limitedSearchResultsSel = $('#limitedSearchResults');
            limitedSearchResultsSel.attr('hidden', 'hidden');
            var str = $("#caseSearchForm").serialize();
            var ajaxURL = "<c:url value="/hud1/search.html"/>?" + str;      
            var exportButtons = ['excel', 'csv', 'pdf', 'print'];
            var tableFormat = "<'row'<'col-md-4'l><'col-md-4'B><'col-md-4'f>><'row'<'col-md-12't>><'row'<'col-md-3'i><'col-md-2'r><'col-md-7'p>>";
            caseSearchResultsSel.DataTable({
                destroy: true,
                "ajax": ajaxURL,
                dom: tableFormat,
                buttons: exportButtons,
                "fnRowCallback": function () {
                    if (caseSearchResultsSel.DataTable().data().length >= 1000) {
                        limitedSearchResultsSel.removeAttr('hidden');
                    }
                },columnDefs: [
                    {
                        render: function (data) {
                            if(data !== '' && data != null) {
                                var temp = data.split("-");
                                data = temp[1] + "/" + temp[2] + "/" + temp[0];
                            }
                            return  data;
                        },
                        targets: [4, 5, 6]
                    },
                    {
                        "render": function ( data, type, row ) {
                            return accounting.formatMoney(data, "");
                        },
                        targets: [7]
                    }
                 ],
                "language": {
                    "emptyTable": "No records found."
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
                    <legend class="heading">Closing Disclosure Search</legend>
                </fieldset>
            </div>
    
            <div class="panel panel-default">
                <div class="panel-heading">
                    <label class="panel-title">Criteria</label>
                </div>
                <div class="panel-body">
                    <form id="caseSearchForm">
                        <div class="form-row">
                            <div class="form-group col-md-2">
                                <label for="caseNumber">Case Number</label>
                                <input type="text" class="form-control" name="caseNumber" id="caseNumber" aria-describedby="caseNumberHelp" placeholder="e.g. 001-123456">
                            </div>
                            <div class="form-group col-md-2">
                                <label for="earnestMoney">Earnest Money</label>
                                <input type="text" class="form-control" name="earnestMoney" id="earnestMoney" aria-describedby="earnestMoneyHelp" placeholder="Enter Earnest Money">
                            </div>
                            <div class="form-group col-md-2">
                                <label for="titleId">Title Id</label>
                                <input type="text" class="form-control" name="titleId" id="titleId" aria-describedby="" placeholder="Title Id" maxlength="10">
                            </div>
                            <div class="form-group col-md-2">
                                <label for="financeType">Finance Type</label>
                                                        
                                <%-- <input type="text" class="form-control input-sm  " id="financeType" placeholder="" value='<c:out value="${hudOne.a8spt191.financeType}"/>'/> --%>
                                <select class="form-control input-sm" name="financeType" id="financeType">
                                    <option value="">Not Selected</option>
                                    <option value="IN">IN</option>
                                    <option value="UI">UI</option>
                                    <option value="AK">AK</option>
                                    <option value="PM">PM</option>
                                </select>
                        </div>
                        
                        </div>
                        <div class="form-group col-md-4">
                            <label for="contractArea">Area</label>
                            <div class="input-group col-md-6">
                                <select id="contractArea" name="contractArea" multiple class="form-control">
                                    <c:set var="previousHOC" value=""/>
                                    <c:forEach items="${mm3HocAreas}" var="area">
                                        <c:if test="${previousHOC != area.hocId}">
                                            <c:if test="${previousHOC != ''}"></optgroup></c:if>
                                            <c:set var="previousHOC" value="${area.hocId}"/>
                                            <optgroup label="${area.hocId} - MM3" >
                                        </c:if>
                                        <option value="${area.areaId}" class="i0">${area.areaId}</option>
                                    </c:forEach>

                                    <c:set var="previousHOC" value=""/>
                                    <c:forEach items="${mm2HocAreas}" var="area">
                                        <c:if test="${previousHOC != area.hocId}">
                                            <c:if test="${previousHOC != ''}"></optgroup></c:if>
                                            <c:set var="previousHOC" value="${area.hocId}"/>
                                            <optgroup label="${area.hocId} - MM2">
                                        </c:if>
                                        <option value="${area.areaId}" class="i0">${area.areaId}</option>
                                    </c:forEach>

                                    <c:set var="previousHOC" value=""/>
                                    <c:forEach items="${mm1HocAreas}" var="area">
                                        <c:if test="${previousHOC != area.hocId}">
                                            <c:if test="${previousHOC != ''}"></optgroup></c:if>
                                            <c:set var="previousHOC" value="${area.hocId}"/>
                                             <optgroup label="${area.hocId} - MM1">
                                        </c:if>
                                        <option value="${area.areaId}" class="i0">${area.areaId}</option>
                                    </c:forEach>

                                    <c:set var="previousHOC" value=""/>
                                    <c:forEach items="${otherHocAreas}" var="area">
                                        <c:if test="${previousHOC != area.hocId}">
                                            <c:if test="${previousHOC != ''}"></optgroup></c:if>
                                            <c:set var="previousHOC" value="${area.hocId}"/>
                                            <optgroup label="${area.hocId}" >
                                        </c:if>

                                        <option value="${area.areaId}" class="i0">${area.areaId}</option>
                                    </c:forEach>
                                   
                                </select>
                            </div>
                            
                        </div>
                        

                        <div class="form-group col-md-4">
                            <label for="dateClosedFrom">Date Closed</label>
                            <div class="input-group col-md-12">
                                <span class="input-group-addon">From</span>
                                <input class="form-control" name="dateClosedFrom" id="dateClosedFrom" placeholder="" maxlength="10" style="width: 6.5em" />
                                <span class="input-group-addon">To</span>
                                <input class="form-control" name="dateClosedTo" id="dateClosedTo" placeholder="" maxlength="10" style="width: 6.5em" />
                            </div>
                        </div>
                        <div class="form-group col-md-4">
                            <label>Date Reconciled</label>
                            <div class="input-group col-md-12">
                                <span class="input-group-addon">From</span>
                                <input  class="form-control" name="dateReconciledFrom" id="dateReconciledFrom" placeholder="" maxlength="10" style="width: 6.5em"/>
                                <span class="input-group-addon">To</span>
                                <input  class="form-control" name="dateReconciledTo" id="dateReconciledTo" placeholder="" maxlength="10" style="width: 6.5em" />
                            </div>
                        </div>
                                        
                        <div class="form-group col-md-2">
                            <label>&nbsp;</label>
                            <div class="input-group col-md-12">
                                <input id="resetSearch" type="button" value="Reset" class=" floatR btn btn-sm btn-default" onclick="resetForm()"/>
                                <input  type="button" onclick="caseSearch()" value="Search" class="floatR btn btn-sm btn-success"/>
                            </div>
                        </div>
                        
                    </form>
                </div>
            </div>
    
            <div>
                <fieldset>
                    <legend class="heading">Search Results&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span id="limitedSearchResults" style=" color: orange; font-size: 18px;" hidden="hidden">Warning: Search output is limited to 1,000 records.</span></legend>
                </fieldset>
            </div>
    
            <div>
                <table id="case_search_results" style="visibility: hidden" class="table display">
                    <thead>
                        <tr>
                            <th>Case Number</th>
                            <th>Address</th>
                            <th>Area</th>
                            <th>Finance Type</th>
                            <th>Date Received</th>
                            <th>Closing Date</th>
                            <th>Reconciliation Date</th>
                            <th style="text-align: right">Total Cash at Settlement</th>
                        </tr>
                    </thead>
                    <tbody>
                        
                    </tbody>
                </table>
            </div>
    
        </div>
    </main>
</body>
</html>
