package net.nillus.ion.Net.Messages;

/**
 * Represents a Habbo client > server protocol structured message, providing methods to identify and 'read' the message.
 */
public interface IHabboMessage {
    // Properties
    int getID();
    String getHeader();
    int getContentLength();

    // Methods
    String getContentString();
}