package org.automation.web;

import org.automation.web.runnable.UnitExecutor;
import org.automation.web.xml.Xml;

import java.io.File;

public class Startup {

    public static void main(String[] args) {
        File file = new File("E:\\idea-workspace\\automation\\template\\project1.xml");
        UnitExecutor.execute(Xml.parse(file));
    }
}
