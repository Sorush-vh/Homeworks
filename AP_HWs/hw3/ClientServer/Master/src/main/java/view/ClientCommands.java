package view;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum ClientCommands {

    CREATE_TASK("\\s*k\\s*create\\s+task\\s+--name=(?<name>[^\\s]+)\\s*(--node=(?<workerId>[^\\s]+))?\\s*"),
    GET_TASKS("\\s*k\\s+get\\s*tasks\\s*"),

    GET_NODES("\\s*k\\s+get\\s+nodes\\s*"),

    DELETE_TASK("\\s*k\\s+delete\\s+task\\s+--name=(?<taskName>[^\\s]+)\\s*"),

    CORDON_NODES("\\s*k\\s+cordon\\s+node\\s+(?<nodeName>[^\\s]+)\\s*"),

    UNCORDON_NODES("\\s*k\\s+uncordon\\s+node\\s+(?<nodeName>[^\\s]+)\\s*")
    ;
    private String regex;

    private ClientCommands(String regex) {
        this.regex = regex;
    }

    public static Matcher getMatcher(String input, ClientCommands command) {
        Matcher matcher = Pattern.compile(command.regex).matcher(input);
        if (matcher.matches())
            return matcher;
        return null;
    }
}
