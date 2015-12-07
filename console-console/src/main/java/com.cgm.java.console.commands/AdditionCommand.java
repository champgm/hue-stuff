package com.cgm.java.console.commands;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;

/**
 * Sample command to add some numbers
 */
public class AdditionCommand extends Command{

    @Override
    protected int run(CommandLine line) throws Exception {
        return 0;
    }

    @Override
    public String getName() {
        return "add";
    }

}
