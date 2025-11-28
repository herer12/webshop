package InfiFirstTry.service;

import InfiFirstTry.webapp.CgiParameterController;

public class LoginController {
    private CgiParameterController cgiParameterController;
    public LoginController(CgiParameterController cgiParameterController){
        this.cgiParameterController = cgiParameterController;
    }
    public String getIdUser(){
        return cgiParameterController.getParam("UserID");
    }
}
