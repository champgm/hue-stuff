package com.cgm.java.console.commands.implementations;

import java.util.Arrays;
import java.util.stream.DoubleStream;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;

import com.cgm.java.console.commands.Command;
import com.cgm.java.utilities.lambdas.Conversion;
import com.cgm.java.utilities.lambdas.Printers;

/**
 * Sample command to add some numbers
 */
public class AdditionCommand extends Command {
    @Override
    protected int run(final CommandLine line) throws Exception {
        final String[] args = line.getArgs();

        System.out.println("Here are the values you wish to sum: ");
        final DoubleStream streamToPrint = Arrays.stream(args).mapToDouble(Conversion.STRING_TO_DOUBLE);
        streamToPrint.forEach(Printers.DOUBLE_PRINTER);

        // Apparently a stream cannot be reused?
        final DoubleStream streamToSum = Arrays.stream(args).mapToDouble(Conversion.STRING_TO_DOUBLE);
        final double sum = streamToSum.sum();
        System.out.println("The total of your inputs is: " + sum);

        return 0;
    }

    @Override
    public void usage() {
        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(getName() + " <numbers...>",
                "A command to sum numbers",
                getOptions(), null);
    }

    @Override
    public String getName() {
        return "add";
    }

}
