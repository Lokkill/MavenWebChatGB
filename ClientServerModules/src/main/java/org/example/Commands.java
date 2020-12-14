package org.example;

import org.example.commands.*;
import org.example.data.User;

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

    public static Commands addNewUserCommand(String login, String password, String nick){
        Commands commands = new Commands();
        commands.type = CommandsType.ADD_USER;
        commands.data = new AddUserCmd(nick, login, password);
        return commands;
    }

    public static Commands getUsersCommand(){
        Commands commands = new Commands();
        commands.type = CommandsType.GET_USERS;
        commands.data = new GetUsersCmd();
        return commands;
    }

    public static Commands sendUsersCommand(List<User> users){
        Commands commands = new Commands();
        commands.type = CommandsType.SEND_USERS;
        commands.data = new SendUsersCmd(users);
        return commands;
    }

    public static Commands updateListClientsCommand(){
        Commands commands = new Commands();
        commands.type = CommandsType.UPDATE_LIST_CLIENTS;
        commands.data = new UpdateListClientsCmd();
        return commands;
    }

    public static Commands changeNicknameCommand(String oldNick, String newNick){
        Commands commands = new Commands();
        commands.type = CommandsType.AUTH;
        commands.data = new ChangeNickCmd(oldNick, newNick);
        return commands;
    }



    public static Commands authErrorCommand(String errorMsg){
        Commands commands = new Commands();
        commands.type = CommandsType.AUTH_ERR;
        commands.data = new AuthErrorCmd(errorMsg);
        return commands;
    }

    public static Commands authOkCommand(String nick){
        Commands commands = new Commands();
        commands.type = CommandsType.AUTH_OK;
        commands.data = new AuthOkCmd(nick);
        return commands;
    }

    public static Commands errorCommand(String errorMsg){
        Commands commands = new Commands();
        commands.type = CommandsType.ERROR;
        commands.data = new ErrorCmd(errorMsg);
        return commands;
    }

    public static Commands infoCommand(String message, String sender){
        Commands commands = new Commands();
        commands.type = CommandsType.INFO_MSG;
        commands.data = new InfoCmd(message, sender);
        return commands;
    }

    public static Commands privateMsgCommand(String receiver, String message){
        Commands commands = new Commands();
        commands.type = CommandsType.PRIVATE_MSG;
        commands.data = new PrivateMsgCmd(receiver, message);
        return commands;
    }

    public static Commands publicMsgCommand(String sender, String message){
        Commands commands = new Commands();
        commands.type = CommandsType.PUBLIC_MSG;
        commands.data = new PublicMsgCmd(sender, message);
        return commands;
    }

    public static Commands updateUsersCommand(List<String> users){
        Commands commands = new Commands();
        commands.type = CommandsType.UPDATE_USERS;
        commands.data = new UpdateUsersCmd(users);
        return commands;
    }
}
