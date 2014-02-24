package MVC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class BookstoreView extends JFrame {
    private static final long serialVersionUID = 33362207L;
    private static final String INITIAL_VALUE = "1";

    private JTextField userInputTextField = new JTextField(5);
    private JTextField totalTextField = new JTextField(20);
    private JButton multiplyButton = new JButton("Multiply");
    private JButton clearButton = new JButton("Clear");

    private BookstoreModel model;

    public BookstoreView(BookstoreModel model) {
        this.model = model;
        this.model.setTotal(INITIAL_VALUE);

        totalTextField.setText(model.getTotal());
        totalTextField.setEditable(false);

        JPanel content = new JPanel();
        content.setLayout(new FlowLayout());

        content.add(new JLabel("Input : "));
        content.add(userInputTextField);

        content.add(multiplyButton);
        content.add(new JLabel("Total : "));

        content.add(totalTextField);

        content.add(clearButton);

        this.setContentPane(content);
        this.pack();

        this.setTitle("Simple Calc - MVC");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void reset() {
        totalTextField.setText(INITIAL_VALUE);
        userInputTextField.setText("");
    }

    public String getUserInput() {
        return userInputTextField.getText();
    }

    public void setTotal(String total) {
        totalTextField.setText(total);
    }

    public void showError(String errorMessage) {
        JOptionPane.showMessageDialog(this,errorMessage);
    }

    public void addMultiplyListener(ActionListener multiplyActionListener) {
        multiplyButton.addActionListener(multiplyActionListener);
    }

    public void addClearListener(ActionListener clearActionListener) {
        clearButton.addActionListener(clearActionListener);
    }
}