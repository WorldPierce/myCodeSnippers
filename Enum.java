public enum ErrorCodeEnum {
	OB("Receivable Out of Balance"),
	IH("Incomplete HUD 1"),
	DR("Date Received Error"),
	DC("Date Closed Error"),
	CE("Close Date Exceeded"),
	CA("Closing Agent Fee Error"),
	BC("Broker Commission Error"),
	BB("Broker Bonus Error");
	
	
	
	private String errorCode;
	private ErrorCodeEnum() {
		//default
	}
	private ErrorCodeEnum(String errorCode) {
		this.errorCode = errorCode;
	}
	
	public String errorCode() {
		return errorCode;
	}
	
	public static Optional<ErrorCodeEnum> of(String value) {

		return Arrays.stream(values()).filter(v -> value.equals(v.name())).findFirst();

	}

}

@Transient
	private String extensionReasonDescription;

    	public String getExtensionReasonDescription() {
    			if(this.extensionReasonDescription == null) {
    			Optional<ExtensionReasonEnum> t = ExtensionReasonEnum.of(this.extensionReasonC);
    			if (t.isPresent()) {
    				extensionReasonDescription = t.get().extensionReasonDescription();
    	           } else {

    	        	   extensionReasonDescription = "No Extension Reason Description Found.";
    	           }
    			}
    			return extensionReasonDescription;
    	} 


	
	@Transient
	private String errorDesc;

	public String getErrorDesc() {
		if(this.errorDesc == null) {
			this.setErrorDesc(ErrorCodeEnum.of(this.packageErrorCode).get().errorCode());
		}
		return errorDesc;
	}