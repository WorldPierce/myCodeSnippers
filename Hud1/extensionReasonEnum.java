public enum ExtensionReasonEnum {

	B("Buyer Request"),
	C("C"),
	H("H"),
	F("F"),
	V("V"),
	D("Deffective Paint"),
	L("Lead-Based Paint"),
	O("Other Reason"),
	E("E"),
	T("Title Problems");
	
	private String extensionReasonDescription;
	private ExtensionReasonEnum() {
		//default
	}
	private ExtensionReasonEnum(String extensionReasonDescription) {
		this.extensionReasonDescription = extensionReasonDescription;
	}
	
	public String extensionReasonDescription() {
		return extensionReasonDescription;
	}
	
	public static Optional<ExtensionReasonEnum> of(String value) {

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
