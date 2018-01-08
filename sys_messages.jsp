<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html lang="en">
<head>

    <title>SAMS - Message</title>

    <script>
    //TODO make timepickers check if start time is before end time.
    $(function () {
    	/*
    	************* time and datepicker start init  ***************
    	Time id must be equal to [name of date input id - "Date" + "-Time"]
    	
    	*/
        $("#startDate").datepicker({
        	minDate: new Date(),
            onSelect: function(date){ //don't allow end date to be <= start date
                var selectedDate = new Date(date);
                var msecsInADay = 86400000;
                var endDate = new Date(selectedDate.getTime());
                $("#endDate").datepicker( "option", "minDate", endDate );
                $('#start-Time').prop('disabled', false);
                $('#start-Time').val("");
                $('#start-Time').css( 'cursor', 'pointer');
                $('#end-Time').val("");
            }
        });
        
        $("#endDate").datepicker({
        	minDate: new Date(),
        	onSelect: function(date){
        		$('#end-Time').prop('disabled', false);
        		$('#end-Time').css( 'cursor', 'pointer');
        		$('#end-Time').val("");
        	}
        });
        
        $("#startModalDate").datepicker({
        	minDate: new Date(),
            onSelect: function(date){ //don't allow end date to be <= start date
                var selectedDate = new Date(date);
                var endDate = new Date(selectedDate.getTime());
                
                $("#endModalDate").datepicker( "option", "minDate", endDate );
                $('#startModal-Time').val("");
                $('#endModal-Time').val("");
            }
        });
        
        $("#endModalDate").datepicker({
        	minDate: new Date(),
        	onSelect: function(date){
        		$('#endModal-Time').val("");
        	}
        });
        	
        
        $('.timepicker').timepicker({
            timeFormat: 'h:mm p',
            interval: 30,
            defaultTime: '',
            startTime: '8:00',
            dynamic: false,
            dropdown: true,
            scrollbar: true,
            change: function(time) {
            	
            	setTime(this[0]); 
            	
          }
        });

        //set the time in the date picker
        function setTime(time) {
        	$('#' + time.id.split("-")[0] + "Date").val($('#' + time.id.split("-")[0] + "Date").val().split(" ")[0] + " " + time.value);
        }
        
        function resetModalTime(field) {
        	console.log(field);
        	if(field.includes("start")) {
        		console.log("check");
        	}
        }
       /************  Time and date picker end init ****************/
       
       
        var messagesSearchResultsSel = $("#messages_search_results");
        var ajaxURL = "<c:url value='/messages/displayTable.html'/>";
        
        var table = messagesSearchResultsSel.DataTable(
                {
                    destroy: true,
                    "ajax": ajaxURL,
                    dom: 'bfrtip',
                      //Limit the size of the text in the message column
                    columnDefs: [
                        {
                            render: function (data, type, full, meta) {
                                return "<div class='width-message'>" + data + "</div>";
                            },
                            targets: 2
                        }
                     ],  
                    "fnRowCallback": function () {
                        if (messagesSearchResultsSel.DataTable().data().length >= 1000) {
                            limitedSearchResultsSel.removeAttr('hidden');
                        }
                    },
                    "language": {
                        "emptyTable": "No system messages to display."
                    }
                }
            );
        
        
        messagesSearchResultsSel.on('click', 'tbody tr', function() {
            var data = table.row(this).data();
            $('#startModalDate').val(data[3]);
            $('#endModalDate').val(data[4]);
            $('#messageModal').val(data[2]);
            $('#startModal-Time').val(data[3].split(" ")[1] + " " + data[3].split(" ")[2]);
            $('#endModal-Time').val(data[4].split(" ")[1] + " " + data[4].split(" ")[2]);
            $('#idModal').val(parseInt(data[0]));
            $('#titleModal').val(data[5]);
            $('#isWarningModal').prop('checked', data[6] == "true");
            $('#myModal').modal('show');
    	});
        
        $('#updateMessage').click(function() {
        	var message = $('#messageModal').val().split(/\n|\r|↵/).join("%0A");
        	var result = confirm('Are you sure you want to update this message?');
        	if(result) {
        		var str = $("#messageFormModal").serialize()
        		var updateUrl = "<c:url value='/messages/update.html'/>?";
	        	$.ajax({
	        		type:"post",
	        	    data:str,
	            	url: updateUrl ,
	            	success: function(){
	            		window.location.href = "<c:url value='/messages/create.html'/>";
	            	}
	            });
        	}
        });
        
        $('#deleteMessage').click(function() {
        	var result = confirm('Are you sure you want to delete this message?');
        	if(result) {
	        	var deleteUrl = "<c:url value='/messages/delete.html'/>?idModal=" + $('#idModal').val();
	        	$.ajax({
	            	url: deleteUrl ,
	            	success: function(){
	            		window.location.href = "<c:url value='/messages/create.html'/>";            
	            	}
	            });
        	}
        });
        $('#cancelMessage').click(function() {
        	
	    	window.location.href = "<c:url value='/home.html'/>";            
	        
        });
        
        
        $('#resetForm').click(function() {
        	 $('#startDate').val("");
             $('#endDate').val("");
             $('#message').val("");
             $('#start-Time').val("");
             $('#end-Time').val("");
             $('#Title').val("");
             $('#isWarning').prop('checked', false);
             $('#start-Time').prop('disabled', true);
             $('#end-Time').prop('disabled', true);
             $('#end-Time').css( 'cursor', 'not-allowed');
             $('#start-Time').css( 'cursor', 'not-allowed');
        });
            
            
     });      
</script>

</head>
<body>

<main>

    <div class="container">
    <div id="myModal" class="modal fade" role="dialog">
	  <div class="modal-dialog modal-lg">
	
	    <!-- Modal content-->
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal">&times;</button>
	      </div> 
	      <div class="modal-body">
	        <form:form method="POST" modelAttribute="messageFormModal" enctype="multipart/form-data">
            <p>${ACPLmessage}</p>
            <div class="panel panel-default">
                <div class="panel-heading">
                    <label class="panel-title">Edit/Delete Message</label>
                </div>
                <div class="panel-body">
                    <div class="form-group col-md-2" >
                        <label>Start Date:</label>
                        <input id="startModalDate" name="startDate" type="text"
                               title="Select Starting Post Date" style="cursor: pointer;"
                               class="form-control fitDate"
                               required>
                               <label>Start Time:</label>
                       		   <input id="startModal-Time" style="cursor: pointer;" class="timepicker text-center form-control fitDate" >
                    </div>
                    <div class="form-group col-md-2" >
                        <label>End Date:</label>
                        <input id="endModalDate" name="endDate" type="text"
                               title="Select Ending Post Date" style="cursor: pointer;"
                               class="form-control fitDate" 
                               required>
                               <label>End Time:</label>
		                       <input id="endModal-Time" style="cursor: pointer;" class="timepicker text-center form-control fitDate" > 
                    </div>
                    <input id="idModal" name="id" type="number"
                               title="Select Ending Post Date" style="width: 9em; display: none"
                               class="form-control" required>
                    <div class="clearfix"></div>
                    
                    <div class="form-group col-md-6">
  						<label for="messageTitleModal">Message Title:</label>
  						<input class="form-control" maxlength="50" id="titleModal" name="Title" required>
  						
					</div>
                    <div class="clearfix"></div>
  					<div class="form-check col-md-2" title="For System Warning announcements only! Do not check unless needed.">
					    <label class="form-check-label">
					      <input type="checkbox" class="form-check-input" id="isWarningModal" name="isWarning">
					      Warning
					    </label>
					</div>
					<div class="clearfix"></div>
                    <div class="form-group col-md-12">
  						<label for="message">Message:</label>
  						<textarea class="form-control" rows="5" id="messageModal" name="message" required></textarea>
					</div>
					<div class="clearfix"></div>
                    <div class="col-md-12" >
                        <input id="updateMessage" type="button" value="Update" class=" btn btn-success" ></input>
                        <input id="deleteMessage" type="button" value="Delete" class=" btn btn-danger" ></input>
                    </div>
                    

                </div>

            </div>
        </form:form>
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
	      </div>
	    </div>
	
	  </div>
	</div>

        <div class="col-md-12">
            <legend class="heading">System Messages</legend>
        </div>
        <div class="clearfix"></div>
        <c:set var="uploadUrl"><c:url value="/messages/sendMessage.html"/></c:set>
        <form:form method="POST" modelAttribute="messageForm" enctype="multipart/form-data" action="${uploadUrl}">
            <p>${ACPLmessage}</p>
            <div class="panel panel-default">
                <div class="panel-heading">
                    <label class="panel-title">Create Message</label>
                </div>
                <div class="panel-body">
                    <div class="form-group col-md-2" id="datePairStart">
                        <label>Start Date:</label>
                        <input id="startDate" name="startDate" type="text"
                               title="Select Starting Date" 
                               class="form-control fitDate"
                               style="cursor: pointer;" required>
                               <label>Start Time:</label>
                       		   <input id="start-Time" title="Select Start Date and then Start Time" name="startDateTime" class="timepicker text-center form-control fitDate" disabled="true" readonly>
                    </div>
                    <div class="form-group col-md-2" >
                        <label>End Date:</label>
                        <input id="endDate" name="endDate" type="text"
                               title="Select Ending Date" 
                               class="form-control fitDate" 
                               style="cursor: pointer;"required>
                                <label>End Time:</label>
		                        <input id="end-Time" title="Select End Date and then End Time" name="startDateTime" class="timepicker text-center form-control fitDate" disabled="true" readonly>                         
                    </div>
                    <div class="clearfix"></div>
                    <div class="form-group col-md-6">
  						<label for="messageTitle">Message Title:</label>
  						<input class="form-control" maxlength="50" id="Title" name="Title" required>
  						
					</div>
                    <div class="clearfix"></div>
  					<div class="form-check col-md-2" title="For System Warning announcements only! Do not check unless needed.">
					    <label class="form-check-label">
					      <input type="checkbox" class="form-check-input" id="isWarning" name="isWarning">
					      Warning
					    </label>
					</div>
  					<div class="clearfix"></div>
                    <div class="form-group col-md-12">
  						<label for="message">Message:</label>
  						<textarea class="form-control" rows="5" id="message" name="message" required></textarea>
					</div>
					<div class="clearfix"></div>
                    <div class="col-md-12" >
                        <input id="sendMessage" type="submit" value="Save" class=" btn btn-success "  ></input>
                        <input id="resetForm" type="button" value="Reset" class=" btn btn-default" ></input>
                        <input id="cancelMessage" type="button" value="Cancel" class=" btn btn-secondary" ></input>
                    </div>
                    

                </div>

            </div>
        </form:form>
        <legend>
                    		
  		<p class="subheading">Edit/Delete Messages&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span id="limitedSearchResults" style=" color: orange; font-size: 18px;" hidden="hidden">Warning: NAID Search output is limited to 1,000 records.</span>
  		</p>
		</legend>
	    <div class="flextable">
	        <table id="messages_search_results" class="table display dataTable fixed">
	            <thead>
		            <tr>
		            	<th>Id</th>
		                <th>User Name</th>
		                <th>Message</th>
		                <th>Start Date</th>
		                <th>End Date</th>
		                <th>Title</th>
		                <th>Warning</th>
		            </tr>
	            </thead>
	            <tbody style="cursor: pointer;">
	            </tbody>
	        </table>
	    </div>
    </div>
</main>

</body>
</html>
