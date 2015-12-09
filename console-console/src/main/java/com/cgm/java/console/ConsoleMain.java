package com.cgm.java.console;

import java.util.Set;

import org.reflections.Reflections;

import com.cgm.java.console.commands.Command;

/**
 * Created by mc023219 on 12/7/15.
 */
public class ConsoleMain {
    public static void main(final String[] args) throws Exception {
        final CommandHandler commandHandler = new CommandHandler();

        // I don't really want to have to maintain a list of commands here each time I add a new one
        // So let's try to do something interesting.
        final Reflections reflections = new Reflections("com.cgm.java.console.commands.implementations");
        final Set<Class<? extends Command>> allCommands = reflections.getSubTypesOf(Command.class);
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
