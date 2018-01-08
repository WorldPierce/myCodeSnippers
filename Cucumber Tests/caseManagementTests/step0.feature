Feature: STEP ZERO TESTING
	Testing all basic Step zero functionality in SAMS
	so we can assure our logic matches that of legacy SAMS system
	and we retrieve desired/expected outcomes from code
	
Scenario: STEP ZERO NAVIGATE, CHANGE COMMENT, AND CREATE STEP ONE
Given I have logged in
And navigated to "Step Zero Property Listing"
When I update comments
Then element having class "alert" should have text as "Comments successfully updated for Case"
When I click "Move to Step 1"
Then element having class "alert" should have text as "created successfully."

Scenario: STEP ZERO NAVIGATE AND DELETE CASE
Given I have logged in
And navigated to "Step Zero Property Listing"
When I click "Delete Case"
Then element having class "alert" should have text as "Successfully deleted Step Zero Case"

Scenario: STEP ZERO NAVIGATE AND MOVE CASE TO STEP ELEVEN
Given I have logged in
And navigated to "Step Zero Property Listing"
When I click "Move to Step 11"
Then element having class "alert" should have text as "successfully moved from Step Zero to Step 11"

