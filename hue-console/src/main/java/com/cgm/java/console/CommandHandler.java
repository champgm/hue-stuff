package com.cgm.java.console;

import java.util.HashMap;

import com.cgm.java.console.commands.Command;
import com.google.common.base.Preconditions;

/**
 * This is a holder and runner for available {@link com.cgm.java.console.commands.Command}s
 */
public class CommandHandler {
    private final String banner;
    private final HashMap<String, Command> commandList;

    /**
     * Creates an instance
     *
     * @param banner
     *         the string that should be printed before processing begins
     */
    public CommandHandler(final String banner) {
        this.banner = banner;
        this.commandList = new HashMap<>();
    }

    /**
     * Creates an instance with a blank banner
     */
    public CommandHandler() {
        this("");
    }

    /**
     * Add a command to be handled by this class
     *
     * @param newCommand
     *         the new command to add
     * @return this instance
     */
    public CommandHandler addCommand(final Command newCommand) {
        final Command command = commandList.get(newCommand.getName());
        Preconditions.checkState(command == null, "Duplicate command encountered while trying to add command: " + newCommand.getName());

        commandList.put(newCommand.getName(), newCommand);
        return this;
    }

    /**
     * Run with a set of arguments. This handler will look for the input command and pass along its arguments and
     * options.
     *
     * @param inputArguments
     *         ALL given arguments
     * @return 0 if successful
     * @throws Exception
     */
    public int run(final String[] inputArguments) throws Exception {
        System.out.println(banner);

        if (inputArguments.length < 1) {
            usage();
            throw new RuntimeException("No command found.");
        }

        final String commandName = inputArguments[0];
        final Command command = commandList.get(commandName);
        if (command == null) {
            final String commandNotFound = "Command not found: " + commandName;
            System.out.println(commandNotFound);
            usage();

            throw new RuntimeException(commandNotFound);
        }

        return command.run(inputArguments);
    }

    public void usage() {
        System.out.println(toString());
    }

    @Override
    public String toString() {
        final StringBuilder resultBuilder = new StringBuilder();
        resultBuilder.append("These are the currently available commands: \n");
        commandList.entrySet().stream().forEach(command -> resultBuilder.append(command.getKey()).append("\n"));
        return resultBuilder.toString();
    }
}
