package GUI;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

public class BookstoreGUI extends JFrame implements IBookstoreGUI,ActionListener {
    private static final long serialVersionUID = 3336147L;

    private String lineSeparator = System.lineSeparator();

    private JPanel mainPanel;

    private JPanel northPanel;

    private JToolBar northPanelToolBar;
    private JButton executeButton;
    private JButton resetButton;
    private JButton closeButton;

    private JToolBar mediatorToolBar;
    private Vector<String> mediatorHostnames;
    private JLabel mediatorHostnameLabel;
    private JComboBox<String> mediatorHostnamesComboBox;

    private JSpinner mediatorPortSpinner;
    private ArrayList<String> mediatorPorts;
    private SpinnerListModel mediatorPortSpinnerModel;

    private JCheckBox enablePhoneticSearch;

    private JSplitPane centerSplitPane;

    private JPanel queryPanel;
    private JTextArea queryTextArea;

    private JPanel outputPanel;
    private JTextArea outputTextArea;

    private JPanel southPanel;
    private JTextField statusTextField;

    public BookstoreGUI() {
        setTitle("Buchhandlung");

        mainPanel = new JPanel(new BorderLayout());

        northPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buildNorthPanel(northPanel);
        mainPanel.add(northPanel,BorderLayout.NORTH);

        queryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buildQueryPanel(queryPanel);

        outputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buildOutputPanel(outputPanel);

        centerSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,queryPanel,outputPanel);
        centerSplitPane.setOneTouchExpandable(true);
        centerSplitPane.setDividerLocation(175);
        mainPanel.add(centerSplitPane,BorderLayout.CENTER);

        southPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buildSouthPanel(southPanel);
        mainPanel.add(southPanel,BorderLayout.SOUTH);

        getContentPane().add(mainPanel);

        setResizable(false);
        setSize(600,400);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - this.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - this.getHeight()) / 2);
        setLocation(x,y);
    }

    private JToolBar buildNorthPanelToolBar() {
        northPanelToolBar = new JToolBar();

        executeButton = new JButton("Execute");
        executeButton.setMnemonic('E');
        executeButton.addActionListener(this);
        executeButton.setEnabled(false);
        northPanelToolBar.add(executeButton);

        resetButton = new JButton("Reset");
        resetButton.setMnemonic('R');
        resetButton.addActionListener(this);
        northPanelToolBar.add(resetButton);

        northPanelToolBar.addSeparator();

        closeButton = new JButton("Close");
        closeButton.setMnemonic('C');
        closeButton.addActionListener(this);
        northPanelToolBar.add(closeButton);

        return northPanelToolBar;
    }

    private JToolBar buildMediatorToolBar() {
        mediatorToolBar = new JToolBar();

        mediatorHostnameLabel = new JLabel("Mediator.Mediator : ");
        mediatorToolBar.add(mediatorHostnameLabel);

        initMediatorHostnames();
        mediatorHostnamesComboBox = new JComboBox<>(mediatorHostnames);
        mediatorHostnamesComboBox.setToolTipText("mediator (hostname)");
        mediatorToolBar.add(mediatorHostnamesComboBox);

        mediatorToolBar.addSeparator();

        initMediatorPorts();
        mediatorPortSpinner = new JSpinner();
        mediatorPortSpinner.setToolTipText("mediator (port)");
        mediatorPortSpinnerModel = new SpinnerListModel(mediatorPorts);
        mediatorPortSpinner.setModel(mediatorPortSpinnerModel);
        mediatorToolBar.add(mediatorPortSpinner);

        return mediatorToolBar;
    }

    private void buildNorthPanel(JPanel panel) {
        panel.add(buildNorthPanelToolBar());
        panel.add(buildMediatorToolBar());

        enablePhoneticSearch = new JCheckBox("phoneticSearch",true);
        panel.add(enablePhoneticSearch);
    }

    private void buildQueryPanel(JPanel panel) {
        queryTextArea = new JTextArea(10,53);
        queryTextArea.setLineWrap(true);
        queryTextArea.getDocument().addDocumentListener(new QueryTextAreaListener());
        JScrollPane queryTextAreaScrollPane = new JScrollPane(queryTextArea);
        queryTextAreaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        panel.add(queryTextAreaScrollPane);
    }

    private void buildOutputPanel(JPanel panel) {
        outputTextArea = new JTextArea(10,53);
        outputTextArea.setLineWrap(true);
        outputTextArea.setEditable(false);
        JScrollPane outputTextAreaScrollPane = new JScrollPane(outputTextArea);
        outputTextAreaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        panel.add(outputTextAreaScrollPane);
    }

    private void buildSouthPanel(JPanel panel) {
        statusTextField = new JTextField(53);
        statusTextField.setEditable(false);

        panel.add(statusTextField);
    }

    public void enableExecuteButton() {
        executeButton.setEnabled(true);
    }

    public void disableExecuteButton() {
        executeButton.setEnabled(false);
    }

    public String getQuery() {
        return queryTextArea.getText().toLowerCase().trim();
    }

    public void appendOutputText(String text) {
        outputTextArea.append(text + lineSeparator);
    }

    public void resetTextAreas() {
        queryTextArea.setText("");
        outputTextArea.setText("");
    }

    public void initMediatorHostnames() {
        mediatorHostnames = new Vector<String>();
        mediatorHostnames.add("10.10.10.10");
    }

    public void initMediatorPorts() {
        mediatorPorts = new ArrayList<String>();
        mediatorPorts.add("9900");
        mediatorPorts.add("9910");
        mediatorPorts.add("9920");
    }

    public void execute(String query) {
        appendOutputText("execute : " + query);
    }

    public void reset() {
        appendOutputText("reset()");
        resetTextAreas();
    }

    public void close() {
        appendOutputText("close()");
        dispose();
        System.exit(0);
    }

    class QueryTextAreaListener implements DocumentListener {
        public void enableDisableExecuteButton() {
            if (!queryTextArea.getText().isEmpty())
                enableExecuteButton();
            else
                disableExecuteButton();
        }

        public void insertUpdate(DocumentEvent e) {
            enableDisableExecuteButton();
        }

        public void removeUpdate(DocumentEvent e) {
            enableDisableExecuteButton();
        }

        public void changedUpdate(DocumentEvent e) {
            enableDisableExecuteButton();
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == executeButton)
            execute(getQuery());

        if (e.getSource() == resetButton)
            reset();

        if (e.getSource() == closeButton)
            close();
    }

    public static void main(String... args) {
        BookstoreGUI buchhandlungGUI = new BookstoreGUI();
    }
}