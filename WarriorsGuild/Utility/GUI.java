package pinqer.WarriorsGuild.Utility;

import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Equipment;
import org.powerbot.script.rt4.Game;
import pinqer.WarriorsGuild.WarriorsGuild;
import pinqer.WarriorsGuild.tasks.*;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class GUI extends JFrame {

    ClientContext ctx;
    WarriorsGuild main;

    public GUI(ClientContext ctx, WarriorsGuild main) {
        this.main = main;
        this.ctx = ctx;

        initialize();
    }

    private void initialize() {

        setSize(450,180);
        setTitle("Warriors Guild Token Gatherer");

        mainPanel = new JPanel();
        //mainPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();



        bankCbox = new JCheckBox("Banking");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5,0,0,15);
        c.ipady = 0;
        c.ipadx = 0;
        c.weightx = 0.5;
        c.weighty = 0;
        c.gridwidth = 1;
        c.gridx = 2;
        c.gridy = 2;
        mainPanel.add(bankCbox, c);

        foodIdLbl = new JLabel("Food:");
        //foodIdLbl.setHorizontalAlignment(SwingConstants.RIGHT);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(5,75,0,0);
        c.ipady = 0;
        c.ipadx = 0;
        c.weightx = 0.5;
        c.weighty = 0;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 1;
        mainPanel.add(foodIdLbl, c);

        foodItemsComBox = new JComboBox<String>();
        foodItemsComBox.setModel(new DefaultComboBoxModel<String>(Constants.FOOD_ITEMS));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(5,0,0,0);
        c.ipady = 0;
        c.ipadx = 0;
        c.weightx = 0.0;
        c.weighty = 0;
        c.gridwidth = 0;
        c.gridx = 1;
        c.gridy = 1;
        mainPanel.add(foodItemsComBox, c);



        foodAmntLbl = new JLabel("Amount to withdraw:");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.LINE_END;
        c.insets = new Insets(0,75,0,0);
        c.ipady = 0;
        c.ipadx = 0;
        c.weightx = 0;
        c.weighty = 0;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 0;
        mainPanel.add(foodAmntLbl, c);

        foodAmntField = new JTextField("10");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(0,0,0,0);
        c.ipady = 0;
        c.ipadx = 30;
        c.weightx = 0.5;
        c.weighty = 0;
        c.gridwidth = 1;
        c.gridx = 2;
        c.gridy = 0;
        mainPanel.add(foodAmntField, c);

        eatAtPercentLbl = new JLabel("Select percentage to eat at:");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(0,75,0,0);
        c.ipady = 0;
        c.ipadx = 0;
        c.weightx = 0;
        c.weighty = 0;
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 2;
        mainPanel.add(eatAtPercentLbl, c);

        healthSlider = new JSlider();
        healthSlider.setMajorTickSpacing(10);
        healthSlider.setMinimum(10);
        healthSlider.setMaximum(100);
        healthSlider.setPaintLabels(true);
        healthSlider.setPaintTicks(true);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(0,5,0,10);
        c.ipady = 0;
        c.ipadx = 0;
        c.weightx = 0;
        c.weighty = 0;
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 3;
        mainPanel.add(healthSlider, c);

        startBtn = new JButton("Start");
        startBtn.addActionListener((event)-> finishSetup());
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.PAGE_END;
        c.insets = new Insets(0,0,0,15);
        c.ipady = 15;
        c.ipadx = 0;
        c.weightx = 0.0;
        c.weighty = 0;
        c.gridwidth = 2;
        c.gridx = 2;
        c.gridy = 3;
        mainPanel.add(startBtn, c);


        this.add(mainPanel);

        //pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(true);

    }
    private void finishSetup() {
        // Add tasks depending on selected options
        main.taskList.add(new Heal(ctx,main));
        main.taskList.add(new Attack(ctx,main));
        //main.taskList.add(new PickLoot(ctx,main));

        if (bankCbox.isSelected()) {
            main.taskList.addAll(Arrays.asList(new OpenBank(ctx,main), new Bank(ctx,main), new WalkToBank(ctx,main), new WalkToMonsters(ctx,main)));
        }

        //
        main.foodName = String.valueOf(foodItemsComBox.getSelectedItem());
        main.foodAmount = Integer.parseInt(foodAmntField.getText());
        main.eatAtHealth = (int) (healthSlider.getValue());

        // We're done with gui, enable looping through tasks
        main.startScript = true;
        // Close gui window
        this.dispose();
    }

    JPanel mainPanel;

    JButton startBtn;

    JSlider healthSlider;

    JComboBox<String> foodItemsComBox;

    JLabel foodIdLbl;
    JLabel foodAmntLbl;
    JLabel eatAtPercentLbl;

    JCheckBox bankCbox;

    JTextField foodAmntField;
}
