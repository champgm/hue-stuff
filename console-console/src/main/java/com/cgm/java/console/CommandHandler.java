package com.cgm.java.console;

import java.util.HashMap;

import com.cgm.java.console.commands.Command;

/**
 * Holds available commands
 */
public class CommandHandler {
    private final String banner;
    private final HashMap<String, Command> commandList;

    public CommandHandler(final String banner) {
        this.banner = banner;
        this.commandList = new HashMap<>();
    }

    public CommandHandler() {
        this("");
    }

    public CommandHandler addCommand(final Command newCommand) {
        final Command command = commandList.get(newCommand.getName());
        if (command != null) {
            throw new RuntimeException("Duplicate command encountered while trying to add command: " + newCommand.getName());
        }

        commandList.put(newCommand.getName(), newCommand);
        return this;
    }

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
        commandList.entrySet().stream().forEach(command -> resultBuilder.append(command).append("\n"));
        return resultBuilder.toString();
    }
}
