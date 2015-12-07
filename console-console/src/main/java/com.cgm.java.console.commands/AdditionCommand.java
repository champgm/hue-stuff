package com.cgm.java.console.commands;

import java.util.Arrays;

import org.apache.commons.cli.CommandLine;

import com.cgm.java.utilities.lambdas.Conversion;

/**
 * Sample command to add some numbers
 */
public class AdditionCommand extends Command{

    @Override
    protected int run(CommandLine line) throws Exception {
        final String[] args = line.getArgs();
        Arrays.stream(args).mapToLong(Conversion.STRING_TO_LONG);

        return 0;
    }

    @Override
    public String getName() {
        return "add";
    }

}
