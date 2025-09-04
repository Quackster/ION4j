package net.nillus.ion;

import java.io.IOException;

public class Ion {

    public static void main(String[] args) throws IOException {
        System.setProperty("sun.awt.disableClipboardAccess", "true");
        System.out.print("\033]0;Ion: Habbo Hotel server emulation environment\007");

        // Set window size to maximum (Java does not have direct console size manipulation like C#)
        IonEnvironment.initialize();

        // Input loop
        while (true) {
            System.in.read();
            IonEnvironment.destroy();
        }
    }
}