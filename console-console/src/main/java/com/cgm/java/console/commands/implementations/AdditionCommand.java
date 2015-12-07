package com.cgm.java.console.commands.implementations;

import java.util.Arrays;
import java.util.stream.LongStream;

import org.apache.commons.cli.CommandLine;

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
        final LongStream streamToPrint = Arrays.stream(args).mapToLong(Conversion.STRING_TO_LONG);
        streamToPrint.forEach(Printers.PRINTING_CONSUMER);

        // Apparently a stream cannot be reused?
        final LongStream streamToSum = Arrays.stream(args).mapToLong(Conversion.STRING_TO_LONG);
        final long sum = streamToSum.sum();
        System.out.println("The total of your inputs is: " + sum);

        return 0;
    }

    @Override
    public String getName() {
        return "add";
    }

}
