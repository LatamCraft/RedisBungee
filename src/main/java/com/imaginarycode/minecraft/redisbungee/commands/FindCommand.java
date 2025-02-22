package com.imaginarycode.minecraft.redisbungee.commands;

import com.imaginarycode.minecraft.redisbungee.RedisBungee;
import com.imaginarycode.minecraft.redisbungee.utils.NyaUtils;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import lombok.AllArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.UUID;

/**
 * This code has been created by Tux, and modified by gatogamer.
 */
@AllArgsConstructor
public class FindCommand implements SimpleCommand {

    private static final TextComponent NO_PLAYER_SPECIFIED = Component.text("You must specify a player name.").color(NamedTextColor.RED);
    private static final TextComponent PLAYER_NOT_FOUND = Component.text("No such player found.").color(NamedTextColor.RED);
    private static final TextComponent NO_PERMISSION = Component.text("You have no permissions to do that.").color(NamedTextColor.RED);

    private final RedisBungee redisBungee;

    @Override
    public void execute(final Invocation invocation) {
        CommandSource commandSource = invocation.source();
        String[] args = invocation.arguments();

        if (!commandSource.hasPermission("redisbungee.command.find")) {
            commandSource.sendMessage(NO_PERMISSION);
            return;
        }
        NyaUtils.run(() -> {
            if (args.length > 0) {
                UUID uuid = redisBungee.getUuidTranslator().getTranslatedUuid(args[0], true);
                if (uuid == null) {
                    commandSource.sendMessage(PLAYER_NOT_FOUND);
                    return;
                }
                RegisteredServer registeredServer = RedisBungee.getApi().getServerFor(uuid);
                if (registeredServer != null) {
                    TextComponent message = Component.text(args[0] + " is at " + registeredServer.getServerInfo().getName() + ".").color(NamedTextColor.GREEN);
                    commandSource.sendMessage(message);
                } else {
                    commandSource.sendMessage(PLAYER_NOT_FOUND);
                }
            } else {
                commandSource.sendMessage(NO_PLAYER_SPECIFIED);
            }
        });
    }
}
