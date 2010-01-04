package org.esa.nest.util;

import org.esa.beam.framework.ui.GridBagUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;

/**
 * NEST
 * User: lveci
 * Date: Jan 8, 2009
 */
public class DialogUtils {


    public static void enableComponents(JComponent label, JComponent field, boolean flag) {
        label.setVisible(flag);
        field.setVisible(flag);
    }

    public static void addComponent(JPanel contentPane, GridBagConstraints gbc, JLabel label, JComponent component) {
        gbc.gridx = 0;
        contentPane.add(label, gbc);
        gbc.gridx = 1;
        contentPane.add(component, gbc);
    }

    public static void addComponent(JPanel contentPane, GridBagConstraints gbc, String label, JComponent component) {
        gbc.gridx = 0;
        contentPane.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        contentPane.add(component, gbc);
    }

    public static JFormattedTextField createFormattedTextField(final NumberFormat numFormat, final Object value,
                                                     final PropertyChangeListener propListener) {
        final JFormattedTextField field = new JFormattedTextField(numFormat);
        field.setValue(value);
        field.setColumns(10);
        if(propListener != null)
            field.addPropertyChangeListener("value", propListener);

        return field;
    }

    public static void fillPanel(final JPanel panel, final GridBagConstraints gbc) {
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        panel.add(new JPanel(), gbc);
    }

    public static JButton CreateButton(final String name, final String text, final ImageIcon icon, final JPanel panel) {
        JButton button = new JButton();
        button.setName(name);
        button = new JButton();
        button.setIcon(icon);
        button.setBackground(panel.getBackground());
        button.setText(text);
        button.setActionCommand(name);
        return button;
    }

    public static class ComponentListPanel extends JPanel {

        private final JPanel labelPanel;
        private final JPanel fieldPanel;

        public ComponentListPanel() {
            final GridLayout grid = new GridLayout(0,1);
            grid.setVgap(5);

            labelPanel = new JPanel(grid);
            fieldPanel = new JPanel(new GridLayout(0,1));

            this.add(labelPanel, BorderLayout.CENTER);
            this.add(fieldPanel, BorderLayout.LINE_END);
        }

        public void addComponent(final String labelStr, final JComponent component) {
            labelPanel.add(new JLabel(labelStr));
            fieldPanel.add(component);
        }
    }

    public static GridBagConstraints createGridBagConstraints() {
        final GridBagConstraints gbc = GridBagUtils.createDefaultConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets.top = 1;
        gbc.insets.bottom = 1;
        gbc.insets.right = 1;
        gbc.insets.left = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;
        return gbc;
    }

    public static class TextAreaKeyListener implements KeyListener {
        private boolean changedByUser = false;
        public void keyPressed(KeyEvent e) {
        }
        public void keyReleased(KeyEvent e) {
            changedByUser = true;
        }
        public void keyTyped(KeyEvent e) {
        }

        public boolean isChangedByUser() {
            return changedByUser;
        }
    }
}
