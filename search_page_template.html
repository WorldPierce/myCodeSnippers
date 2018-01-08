<!DOCTYPE html>


<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<html lang="en">
<head>
    <title>SAMS - HUD1 Search</title>
    <style>
        #case_search_results tbody td:nth-of-type(1){
            color: #337ab7;
        }

        #case_search_results tbody td:nth-of-type(1):hover {
            text-decoration: underline;
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

    	$('input').keydown(function(e) {
            if (e.keyCode === 13) {
                caseSearch();
            }
        });
        
    });
    function resetForm() {
    	$("#caseNumber").val("");
    	$("#earnestMoney").val("");
    }
    function caseSearch(e) {
    	var caseNumber = $("#caseNumber").val();
     	var earnestMoney = $("#earnestMoney").val().replace(/,/g, "");

    	var caseSearchResultsSel = $("#case_search_results");
    	$('#case_search_results').css('visibility', 'hidden');
        $('#case_search_results').css('visibility', 'visible');
        var limitedSearchResultsSel = $('#limitedSearchResults');
        limitedSearchResultsSel.attr('hidden', 'hidden');
              
        //construct search params
        var ajaxURL = "<c:url value="/hud1/search.html"/>"
        			  + "?caseNumber=" + caseNumber
        			  + "&earnestMoney=" + earnestMoney;
    	var table = caseSearchResultsSel.DataTable(
                {
                    destroy: true,
                    "ajax": ajaxURL,
                    dom: 'bfrtip',
                    "fnRowCallback": function () {
                        if (caseSearchResultsSel.DataTable().data().length >= 1000) {
                            limitedSearchResultsSel.removeAttr('hidden');
                        } 
                    },
                    "language": {
                        "emptyTable": "No records found."
                    }/* ,
                    "columnDefs": [
                        {"className": "dt-center", "targets": 6}
                    ] */
                }
         );
    }
	</script>
</head>

<body>

	<main>
	
	    <div class="container">
	        <div>
	            <fieldset>
	                <legend class="heading">HUD1 Search</legend>
	            </fieldset>
	        </div>
	
	        <div class="panel panel-default">
	            <div class="panel-heading">
	                <label class="panel-title">Criteria</label>
	            </div>
	            <div class="panel-body">
	                <form>
	                    <div class="form-row">
	                        <div class="form-group col-md-2">
	                            <label for="caseNumber">Case Number</label>
	                            <input type="text" class="form-control" id="caseNumber" aria-describedby="caseNumberHelp" placeholder="e.g. 001-123456">
	                        </div>
	                        <div class="form-group col-md-2">
	                            <label for="earnestMoney">Earnest Money</label>
	                            <input type="text" class="form-control" id="earnestMoney" aria-describedby="earnestMoneyHelp" placeholder="Enter Earnest Money">
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
	                        <th>Finance Type</th>
	                        <th>Address</th>
	                        <th>Date Received</th>
	                        <th>Closing Date</th>
	                        <th>Reconciliation Date</th>
	                        <th>Total Cash at Settlement</th>
	                    </tr>
	                </thead>
	                <tbody onclick="window.location.href='<c:url value="/hud1/view.html"/>'">
	                    
	                </tbody>
	            </table>
	        </div>
	
	    </div>
	</main>
</body>
</html>