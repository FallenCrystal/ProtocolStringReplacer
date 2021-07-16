package me.rothes.protocolstringreplacer.commands.subcommands;

import me.rothes.protocolstringreplacer.ProtocolStringReplacer;
import me.rothes.protocolstringreplacer.replacer.ListenType;
import me.rothes.protocolstringreplacer.user.User;
import me.rothes.protocolstringreplacer.api.ChatColors;
import me.rothes.protocolstringreplacer.commands.SubCommand;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parse extends SubCommand {

    public Parse() {
        super("parse", "protocolstringreplacer.command.parse", "测试替换字符串");
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onExecute(@NotNull User user, @NotNull String[] args) {
        if (args.length == 4) {
            Player player;
            if ("null".equals(args[2])) {
                player = null;
            } else {
                player = Bukkit.getPlayer(args[2]);
                if (player == null) {
                    user.sendFilteredText("§c§lP§6§lS§3§lR §e> §c该玩家 §f" + args[2] + " §c不在线.");
                    return;
                }
            }
            ListenType listenType = null;
            for (var type : ListenType.values()) {
                if (type.getName().equalsIgnoreCase(args[3])) {
                    listenType = type;
                    break;
                }
            }
            if (listenType == null) {
                user.sendFilteredText("§c§lP§6§lS§3§lR §e> §c监听类型 §f" + args[3] + " §c不存在.");
                return;
            }

            user.sendFilteredText("§c§lP§6§lS§3§lR §e> §a测试正在进行中, 请稍等...");
            ListenType finalListenType = listenType;
            Bukkit.getScheduler().runTaskAsynchronously(ProtocolStringReplacer.getInstance(), () -> {
                long startTime = System.currentTimeMillis();
                String original = ChatColors.getColored(args[1]);
                String text = original;
                LinkedList<HoverEvent> results = new LinkedList<>();
                for (var replacerConfig : ProtocolStringReplacer.getInstance().getReplacerManager().getReplacerConfigList()) {
                    if (replacerConfig.getListenTypeList().contains(finalListenType)) {
                        Object object = replacerConfig.getReplaces().entrySet();
                        switch (replacerConfig.getMatchType()) {
                            case CONTAIN:
                                var containSet = (Set<Map.Entry<String, String>>) object;
                                for (var entry : containSet) {
                                    String key = entry.getKey();
                                    String value = entry.getValue();
                                    if (text.contains(key)) {
                                        text = StringUtils.replace(text, key, value);
                                        results.add(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(
                                                "§7 --- §6第 §l" + (results.size() + 1) + "§6 次替换§7 --- \n" +
                                                        "§3§l替换配置文件: §b" + replacerConfig.getRelativePath() + "\n" +
                                                        "§3§l匹配方式: §b包含匹配\n" +
                                                        "§3§l项原始文本: §b" + ChatColors.showColorCodes(key) + "\n" +
                                                        "§3§l项替换文本: §b" + ChatColors.showColorCodes(value) + "\n" +
                                                        "§3§l替换结果: §b" + ChatColors.showColorCodes(text))));
                                    }
                                }
                                break;
                            case EQUAL:
                                var equalSet = (Set<Map.Entry<String, String>>) object;
                                for (var entry : equalSet) {
                                    String key = entry.getKey();
                                    String value = entry.getValue();
                                    if (text.equals(key)) {
                                        text = value;
                                        results.add(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(
                                                "§7 --- §6第 §l" + (results.size() + 1) + "§6 次替换§7 --- \n" +
                                                        "§3§l替换配置文件: §b" + replacerConfig.getRelativePath() + "\n" +
                                                        "§3§l匹配方式: §b完全匹配\n" +
                                                        "§3§l项原始文本: §b" + ChatColors.showColorCodes(key) + "\n" +
                                                        "§3§l项替换文本: §b" + ChatColors.showColorCodes(value) + "\n" +
                                                        "§3§l替换结果: §b" + ChatColors.showColorCodes(text))));
                                    }
                                }
                                break;
                            case REGEX:
                                var regexSet = (Set<Map.Entry<Pattern, String>>) object;
                                for (var entry : regexSet) {
                                    Pattern key = entry.getKey();
                                    String value = entry.getValue();
                                    Matcher matcher = key.matcher(text);
                                    while (matcher.find()) {
                                        text = matcher.replaceAll(value);
                                        results.add(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(
                                                "§7 --- §6第 §l" + (results.size() + 1) + "§6 次替换§7 --- \n" +
                                                        "§3§l替换配置文件: §b" + replacerConfig.getRelativePath() + "\n" +
                                                        "§3§l匹配方式: §b正则表达式\n" +
                                                        "§3§l项原始文本: §b" + ChatColors.showColorCodes(key.toString()) + "\n" +
                                                        "§3§l项替换文本: §b" + ChatColors.showColorCodes(value) + "\n" +
                                                        "§3§l替换结果: §b" + ChatColors.showColorCodes(text))));
                                    }
                                }
                        }
                    }
                }
                ComponentBuilder placeholderMessage = new ComponentBuilder(" §7* §3§l占位符替换: ");
                if (ProtocolStringReplacer.getInstance().getReplacerManager().hasPlaceholder(text)) {
                    String original1 = text;
                    User placeholderTarget = player == null? ProtocolStringReplacer.getInstance().getUserManager().getConsoleUser() :
                            ProtocolStringReplacer.getInstance().getUserManager().getUser(player);
                    text = ProtocolStringReplacer.getInstance().getReplacerManager().setPlaceholder(placeholderTarget, text);
                    placeholderMessage.append("§a占位符已替换 §7§o悬停查看详细内容").event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(
                            "§7 ---§6§l 占位符替换信息 §7--- \n" +
                            "§3§l占位符目标: §b" + (placeholderTarget.getPlayer() == null? "§7§onull" : placeholderTarget.getPlayer().getName()) + "\n" +
                                    "§3§l解析前: §b" + ChatColors.showColorCodes(original1) + "\n" +
                                    "§3§l解析后: §b" + ChatColors.showColorCodes(text))));
                } else {
                    placeholderMessage.append("§c未发现占位符");
                }

                long duration = System.currentTimeMillis() - startTime;
                user.sendFilteredText("§7§m-----------§7§l §7[ §c§lP§6§lS§3§lR §7- §e替换测试结果§7 ]§l §7§m-----------");
                user.sendFilteredText(" §7* §3§l测试耗时: §b" + duration + " ms");
                user.sendFilteredText(" §7* §3§l监听类型: §b" + finalListenType.getName());
                user.sendFilteredText(" §7* §3§l原始文本: §b" + ChatColors.showColorCodes(original));
                user.sendFilteredText(" §7* §3§l最终文本: §b" + ChatColors.showColorCodes(text));
                ComponentBuilder componentBuilder = new ComponentBuilder(" §7* §3§l详细步骤:");
                if (results.isEmpty()) {
                    componentBuilder.append(" §c未进行任何替换");
                } else {
                    for (int i = 0; i < results.size(); i++) {
                        componentBuilder.append(" " + (i + 1) + " ");
                        if (i % 2 == 1) {
                            componentBuilder.color(ChatColor.YELLOW);
                        } else {
                            componentBuilder.color(ChatColor.GOLD);
                        }
                        componentBuilder.event(results.get(i)).append("|").reset();
                    }
                    componentBuilder.append("§7 §o悬停查看详细内容").reset();
                }
                user.sendFilteredMessage(componentBuilder.create());
                user.sendFilteredMessage(placeholderMessage.create());
                user.sendFilteredText("§7§m-----------------------------------------------");
            });
            return;
        }
        sendHelp(user);
    }

    @Override
    public List<String> onTab(@NotNull User user, @NotNull String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length == 2) {
            list.add("<要测试的字符串>");
        } else if (args.length == 3) {
            list.add("<占位符目标玩家>");
            for (var player : Bukkit.getOnlinePlayers()) {
                list.add(player.getName());
            }
            list.add("null");
        } else if (args.length == 4) {
            list.add("<监听类型>");
            for (var listenType : ListenType.values()) {
                list.add(listenType.getName());
            }
        }
        return list;
    }

    @Override
    public void sendHelp(@NotNull User user) {
        user.sendFilteredText("§7§m---------------------§7§l §7[ §c§lP§6§lS§3§lR §7- §e替换测试§7 ]§l §7§m---------------------");
        user.sendFilteredText("§7 * §e/psr parse <字符串> <玩家|null> <监听类型> §7- §b测试替换字符串");
        user.sendFilteredText("§7 | §b通过此指令, 您可以测试您设置的替换配置文件是否生效,");
        user.sendFilteredText("§7 | §b并了解字符串替换的流程.");
        user.sendFilteredText("§7 | §b由于进行了额外的操作, 测试耗时会远高于实际替换耗时.");
        user.sendFilteredText("§7§m---------------------------------------------------------------");
    }

}