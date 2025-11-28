package InfiFirstTry.service;

import InfiFirstTry.webapp.CgiParameterController;

public class ProductControllerInput {

    private final CgiParameterController cgi;

    public ProductControllerInput(CgiParameterController cgi) {
        this.cgi = cgi;
    }

    public String getSearchKeyword(){
        return cgi.getParam("Product");
    }

}

