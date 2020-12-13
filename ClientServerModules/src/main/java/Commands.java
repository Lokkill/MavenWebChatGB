import org.example.commands.*;

import java.io.Serializable;
import java.util.List;

public class Commands<T> implements Serializable {
    private CommandsType type;
    private T data;

    public CommandsType getType() {
        return type;
    }

    public T getData() {
        return data;
    }

    public static Commands authCommand(String login, String password){
        Commands commands = new Commands();
        commands.type = CommandsType.AUTH;
        commands.data = new AuthCmd(login, password);
        return commands;
    }

    public static Commands authErrorCommand(String errorMsg){
        Commands commands = new Commands();
        commands.type = CommandsType.AUTH;
        commands.data = new AuthErrorCmd(errorMsg);
        return commands;
    }

    public static Commands authOkCommand(String nick){
        Commands commands = new Commands();
        commands.type = CommandsType.AUTH;
        commands.data = new AuthOkCmd(nick);
        return commands;
    }

    public static Commands errorCommand(String errorMsg){
        Commands commands = new Commands();
        commands.type = CommandsType.AUTH;
        commands.data = new ErrorCmd(errorMsg);
        return commands;
    }

    public static Commands infoCommand(String message){
        Commands commands = new Commands();
        commands.type = CommandsType.AUTH;
        commands.data = new InfoCmd(message);
        return commands;
    }

    public static Commands privateMsgCommand(String receiver, String message){
        Commands commands = new Commands();
        commands.type = CommandsType.AUTH;
        commands.data = new PrivateMsgCmd(receiver, message);
        return commands;
    }

    public static Commands publicMsgCommand(String sender, String message){
        Commands commands = new Commands();
        commands.type = CommandsType.AUTH;
        commands.data = new PublicMsgCmd(sender, message);
        return commands;
    }

    public static Commands updateUsersCommand(List<String> users){
        Commands commands = new Commands();
        commands.type = CommandsType.AUTH;
        commands.data = new UpdateUsersCmd(users);
        return commands;
    }
}
