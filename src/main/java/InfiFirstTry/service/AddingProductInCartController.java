package InfiFirstTry.service;

import InfiFirstTry.webapp.CgiParameterController;

public class AddingProductInCartController {
    CgiParameterController cgiParameterController;

    public AddingProductInCartController(CgiParameterController cgiParameterController) {
        this.cgiParameterController = cgiParameterController;
    }

    public String getIdProduct(){
        return cgiParameterController.getParam("productId");
    }
}
