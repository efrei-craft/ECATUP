package fr.efreicraft.ecatup.commands.exceptions;

public class CommandException extends Exception {

    public CommandException(String message) {
        super(message);
    }

    public CommandException() {
        super("Une erreur inattendue est survenue.");
    }
}
