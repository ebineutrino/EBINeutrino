package ebiNeutrino.core;

import ebiNeutrinoSDK.gui.component.EBIVisualPanel;

import javax.swing.*;

/**
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; version 2 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * Description:
 * This is the main
 *  class for EBI Neutrino
 *
 * JVM start point:
 * public static void main(String[] args)
 */



public class EBITestContainerMain {

    public static void main(final String[] args) throws Exception {
        final EBIVisualPanel panel = new EBIVisualPanel();
        panel.setModuleTitle("Titolo");
        final JFrame frame = new JFrame();
        frame.setSize(800,600);
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.setVisible(true);
    }


    public EBITestContainerMain() {
    }
}

