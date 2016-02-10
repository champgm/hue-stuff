package com.cgm.java.console.commands.implementations;

import java.net.UnknownHostException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;

import com.cgm.java.console.commands.BridgeCommand;
import com.cgm.java.hue.utilities.HueBridgeCommands;

/**
 * Prints the raw JSON for all available {@link com.cgm.java.hue.models.Rule}s
 */
public class ListRulesCommand extends BridgeCommand {

    @Override
    protected int run(final CommandLine line) throws UnknownHostException {
        setIpAndId(line);
        final String fullRulesList = HUE_BRIDGE_GETTER.rawGet(bridgeIp, token, HueBridgeCommands.RULES);
        System.out.println("Here is the raw JSON for currently available rules: ");
        System.out.println(fullRulesList);

        System.out.println("Here are those rules converted into AVRO: ");
        HUE_BRIDGE_GETTER.getRules(bridgeIp, token).forEach(System.out::println);

        return 0;
    }

    @Override
    public void usage() {
        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(getName() + " <bridge IP> <hue ID>",
                "A command to print a JSON dump of all available rules.",
                getOptions(), null);
    }

    @Override
    public String getName() {
        return "listrules";
    }
}
