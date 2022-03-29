package ru.byme.renue;

import ru.byme.renue.parser.Parser;
import ru.byme.renue.util.PropertiesUtil;


public class AppRunner {

    public static void main(String[] args) {

        String URL_KEY = "columnNumber";
        if (args.length == 1) {
            URL_KEY = args[0];
        }
        Parser parser = new Parser(PropertiesUtil.getProperties(URL_KEY));
        parser.parse();
    }
}
