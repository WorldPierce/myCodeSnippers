button span.myText {
  max-width: 0;
  -webkit-transition: max-width 1s;
  transition: max-width 1s;
  display: inline-block;
  vertical-align: top;
  white-space: nowrap;
  overflow: hidden;
}
button:hover span.myText {
  max-width: 7rem;
}

 <button id="addBtn" type="button" class="btn btn-success btn-sm">
  <span class="glyphicon glyphicon-plus"> </span> <span class="myText">Add</span> 
</button>
<button id="editBtn" type="button" class="btn btn-default btn-sm">
  <span class="glyphicon glyphicon-pencil"> </span> <span class="myText">Edit</span> 
</button>
<button id="deleteBtn" type="button" class="btn btn-danger btn-sm">
  <span class="glyphicon glyphicon-trash" ></span> <span class="myText">Delete</span> 
</button>