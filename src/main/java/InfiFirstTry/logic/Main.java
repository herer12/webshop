package InfiFirstTry.logic;

import InfiFirstTry.webapp.CgiParameterController;

public class Main {

    public static void main(String[] args) throws Exception {
        CgiParameterController cgi = new CgiParameterController();

        LogicForSearchingForProducInList logicForSearchingForProducInList = new LogicForSearchingForProducInList(cgi);
        logicForSearchingForProducInList.getAccessesReason();






    }
}
