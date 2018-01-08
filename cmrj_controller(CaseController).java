@RequestMapping("/cmrjUMode")
    public String cmrjUMode(Model model, HttpServletRequest request) {
    	List<A8spt295> list = caseManager.loadCmrj((String)request.getSession().getAttribute(USER_HUD_OFFICE_REQUEST_ATTRIBUTE));
    	model.addAttribute("cmrjForm", new CMRJObject(list));
    	return "/caseManagement/cmrj";
    }
    
    @RequestMapping("/cmrjQMode")
    public String cmrjQMode(Model model, HttpServletRequest request) {
    	List<A8spt295> list = caseManager.loadCmrj("HQ");
    	model.addAttribute("cmrjForm", list);
    	return "/caseManagement/cmrjQ";
    }
    
    
    @RequestMapping("/cmrj/update")
    public String cmrjUpdate(@ModelAttribute(value = "cmrjForm") CMRJObject cmrjFormObj, BindingResult result, SessionStatus status, 
    					   HttpServletRequest request, HttpServletResponse response, Model model) {
    	cmrjFormObj.setUpdatedBy((String)request.getSession().getAttribute(NaidConstants.SESSION_LOGGED_IN_USER));
    	try {
    		caseManager.updateCmrj(cmrjFormObj);
    		return "redirect:/caseManagement/cmrjUMode.html";
    	} catch (Exception e) {
			log.error(e);
			model.addAttribute("error", "There was an error while attempting to update the records.");
			return "/caseManagement/cmrj";
		}	
    