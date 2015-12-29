package com.cgm.java.console;

import java.util.HashSet;

import org.reflections.Reflections;

import com.cgm.java.console.commands.BridgeCommand;
import com.cgm.java.console.commands.Command;

/**
 * Entry point for this console JAR
 */
public class ConsoleMain {
    public static void main(final String[] args) throws Exception {
        final CommandHandler commandHandler = new CommandHandler();

        // I don't really want to have to maintain a list of commands here each time I add a new one
        // So let's try to do something interesting.
        final Reflections reflections = new Reflections("com.cgm.java.console.commands.implementations");
        final HashSet<Class<? extends Command>> allCommands = new HashSet<>();
        allCommands.addAll(reflections.getSubTypesOf(Command.class));
        allCommands.addAll(reflections.getSubTypesOf(BridgeCommand.class));

        // final Set<Class<? extends Command>> allCommands =
        allCommands.stream().forEach(classToInstantiate -> {
            try {
                final Command command = classToInstantiate.newInstance();
                commandHandler.addCommand(command);
            } catch (Exception e) {
                throw new RuntimeException("Could not instantiate command class: " + classToInstantiate.getName(), e);
            }
        });

        commandHandler.run(args);
    }
}
