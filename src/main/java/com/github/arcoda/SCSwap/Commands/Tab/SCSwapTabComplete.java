package com.github.arcoda.SCSwap.Commands.Tab;

import com.github.arcoda.SCSwap.SCSwap;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SCSwapTabComplete implements TabCompleter {
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        ArrayList<String> baseList = new ArrayList<String>();
        if (args.length == 1) {
            baseList.add("reload");
            baseList.add("about");
            baseList.add("block");
            return baseList;
        }
        if (args[0].equals("block") && args.length == 2) {
            baseList.add("add");
            baseList.add("add-uuid");
            baseList.add("list");
            baseList.add("remove");
            return baseList;
        }
        return(new ArrayList<>());
    }
}
