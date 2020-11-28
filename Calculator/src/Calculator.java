import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * Calculator class creates a calculator.
 * @author Asma
 * @version 1.0
 */
public class Calculator {
    private JFrame jFrame;
    // to show the calculation
    private JTextArea jTextArea;
    // stores the history of the entered numbers and operators
    private ArrayList<String> calculationHistory;
    // stores final calculation
    private ArrayList<String> calculation;
    // stores some operations in which can not be used together
    private static final Set<String> operations = new HashSet<String>(Arrays.asList("%", "/",
            "*","-","+","=","sin","cos","tan","cot","exp","log"));
    // stores numbers on the calculator
    private static final Set<String> numbers = new HashSet<String>(Arrays.asList("0", "1",
            "2","3","4","5","6","7","8","9"));
    // stores functions on the calculator
    private static final Set<String> functions = new HashSet<String>(Arrays.asList("sin","cos","tan","cot","exp","log"));

    /**
     * Create a calculator.
     */
    public Calculator(){
        init();
    }

    /**
     * Initialize the frame and draw the calculator.
     */
    private void init(){
        calculationHistory = new ArrayList<>();
        calculation = new ArrayList<>();
        // set jFrame properties
        jFrame = new JFrame();
        jFrame.setSize(350,600);
        jFrame.setLocation(500, 100);
        jFrame.setLayout(null);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setTitle("Calculator");

        jTextArea = new JTextArea();
        // draw the calculator
        draw();
        jFrame.setVisible(true);
        // to have focus on start button and use key listener at the begining
        JButton start = new JButton("start");
        jFrame.add(start);
        start.addKeyListener(new keyListenerForPanel());
        jFrame.getRootPane().setDefaultButton(start);
        start.requestFocus();
    }

    /**
     * Draw calculator's keyboard, screen, and menu bar.
     */
    private void draw(){
        drawKeyBoard();
        drawScreen();
        drawMenuBar();
    }

    /**
     * Draw calculator's menu bar.
     */
    private void drawMenuBar(){
        // create a menu bar.
        JMenuBar menuBar = new JMenuBar();

        // create a menu for copy
        JMenu copy = new JMenu(" copy screen ");
        copy.setToolTipText("click or press CTRL+C to copy the screen");
        copy.setMnemonic('C');
        // create an item for copy
        JMenuItem copyItem = new JMenuItem("copy");
        // add an action listener for copy item
        copyItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringSelection screenText = new StringSelection(jTextArea.getText());
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(screenText, null);
            }
        });
        // add an accelerator for copy item
        copyItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        // add copy item to the copy menu
        copy.add(copyItem);

        // create exit menu
        JMenu exit = new JMenu(" exit ");
        exit.setToolTipText("click or press ALT-X");
        exit.setMnemonic('X');
        // create exit item
        JMenuItem exitItem = new JMenuItem("exit");
        // add exit item to exit menu
        exit.add(exitItem);
        // add an action listener to exit item
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        // add menus to menu bar
        menuBar.add(copy);
        menuBar.add(exit);
        jFrame.setJMenuBar(menuBar);
    }

    /**
     * Draw both normal and scientific keyboard for the calculator.
     */
    public void drawKeyBoard(){
        JTabbedPane tabbedPane = new JTabbedPane();
        JPanel normalKeyboard = drawNormalKeyboard();
        JPanel scientificKeyboard = drawScientificKeyboard();
        tabbedPane.setBounds(50,150,250,350);
        tabbedPane.add("Normal", normalKeyboard);
        tabbedPane.add("Scientific", scientificKeyboard);
        jFrame.getContentPane().add(tabbedPane);
    }

    /**
     * Generate a normal keyboard for calculator.
     * @return  normal keyboard as a JPanel
     */
    private JPanel drawNormalKeyboard(){
        JPanel normalKeyboard = new JPanel();
        normalKeyboard.setLayout(new GridLayout(0,4));

        keyListenerForPanel keyListener = new keyListenerForPanel();

        for (int i = 9; i >= 0; i--) {
            JButton button = new JButton();
            button.setText("" + i);
            button.setToolTipText("click or press "+ i + " on keyboard");
            // set key listener
            button.addKeyListener(keyListener);
            // set an action listener for every number button
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    jTextArea.append(button.getText());
                    calculationHistory.add(button.getText());
                }
            });
            // add button to the panel
            normalKeyboard.add(button); }

        // add residual button
        JButton residualButton = new JButton("%");
        normalKeyboard.add(residualButton,3);
        residualButton.setToolTipText("click or press "+ residualButton.getText() + " on keyboard");
        // set key listener
        residualButton.addKeyListener(keyListener);
        // set an action listener for button
        residualButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (calculationHistory.size() !=0 &&
                numbers.contains(calculationHistory.get(calculationHistory.size()-1))){
                    jTextArea.append(residualButton.getText());
                    addToCalculationList(residualButton.getText());
                }
            }
        });

        // add division button
        JButton divisionButton = new JButton("/");
        normalKeyboard.add(divisionButton,7);
        divisionButton.setToolTipText("click or press "+ divisionButton.getText() + " on keyboard");
        // set key listener
        divisionButton.addKeyListener(keyListener);
        // set an action listener for button
        divisionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (calculationHistory.size() !=0 &&
                        numbers.contains(calculationHistory.get(calculationHistory.size()-1))){
                    jTextArea.append(divisionButton.getText());
                    addToCalculationList(divisionButton.getText());
                }
            }
        });

        // add multiplication button
        JButton multiplicationButton = new JButton("*");
        normalKeyboard.add(multiplicationButton,11);
        multiplicationButton.setToolTipText("click or press "+ multiplicationButton.getText() + " on keyboard");
        // set key listener
        multiplicationButton.addKeyListener(keyListener);
        // set an action listener for button
        multiplicationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (calculationHistory.size() !=0 &&
                        numbers.contains(calculationHistory.get(calculationHistory.size()-1))){
                    jTextArea.append(multiplicationButton.getText());
                    addToCalculationList(multiplicationButton.getText());
                }
            }
        });

        // add equal button
        JButton equalButton = new JButton("=");
        normalKeyboard.add(equalButton);
        equalButton.setToolTipText("click or press "+ equalButton.getText() + " on keyboard");
        // add a key listener
        equalButton.addKeyListener(keyListener);
        // set an action listener for button
        equalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (calculationHistory.size() !=0 &&
                        numbers.contains(calculationHistory.get(calculationHistory.size()-1))){
                    jTextArea.append(equalButton.getText());
                    addToCalculationList(equalButton.getText());
                    jTextArea.append(calculate() + "\n");
                }
            }
        });

        // add plus button
        JButton plusButton = new JButton("+");
        normalKeyboard.add(plusButton);
        plusButton.setToolTipText("click or press "+ plusButton.getText() + " on keyboard");
        // set key listener
        plusButton.addKeyListener(keyListener);
        // set an action listener for button
        plusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (calculationHistory.size() !=0 &&
                        numbers.contains(calculationHistory.get(calculationHistory.size()-1))){
                    jTextArea.append(plusButton.getText());
                    addToCalculationList(plusButton.getText());
                }
            }
        });

        // add minus button
        JButton minusButton = new JButton("-");
        normalKeyboard.add(minusButton);
        minusButton.setToolTipText("click or press "+ minusButton.getText() + " on keyboard");
        // set key listener
        minusButton.addKeyListener(keyListener);
        // set an action listener for button
        minusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (calculationHistory.size() !=0 &&
                        numbers.contains(calculationHistory.get(calculationHistory.size()-1))){
                    jTextArea.append(minusButton.getText());
                    addToCalculationList(minusButton.getText());
                }
            }
        });

        return normalKeyboard;
    }

    /**
     * Generate a scientific keyboard for calculator.
     * @return  scientific keyboard as a JPanel
     */
    private JPanel drawScientificKeyboard(){
        JPanel scientificKeyboard = new JPanel();
        scientificKeyboard.setLayout(new BorderLayout());

        keyListenerForPanel keyListener = new keyListenerForPanel();

        // define calculator functions
        String[] functionTexts = {"sin", "tan", "log"};
        String[] buttonTextsWithShift = {"cos", "cot", "exp"}; // when the shift button pressed

        JPanel scientificKeyboard1 = new JPanel();
        scientificKeyboard1.setLayout(new GridLayout(0,4));

        for (String ele:functionTexts) {
            JButton button = new JButton(ele);
            scientificKeyboard1.add(button);
            button.setToolTipText(""+ button.getText() + ": based on degree");
            // set key listener
            button.addKeyListener(keyListener);
            // add an action listener for button
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (calculationHistory.size() == 0 || operations.contains(calculationHistory.get(calculationHistory.size()-1))){
                        jTextArea.append(button.getText());
                        calculation.add(button.getText());
                    }
                }
            });
        }

        // add residual button
        JButton residualButton = new JButton("%");
        scientificKeyboard1.add(residualButton);
        residualButton.setToolTipText("click or press "+ residualButton.getText() + " on keyboard");
        // set key listener
        residualButton.addKeyListener(keyListener);
        // set an action listener for button
        residualButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ((calculationHistory.size() !=0 &&
                        numbers.contains(calculationHistory.get(calculationHistory.size()-1))) ||
                        calculation.get(calculation.size()-1).equals("e") || calculation.get(calculation.size()-1).equals("pi")){
                    jTextArea.append(residualButton.getText());
                    addToCalculationList(residualButton.getText());
                }
            }
        });

        for (int i = 9; i > 0; i--) {
            JButton button = new JButton();
            button.setText("" + i);
            button.setToolTipText("click or press "+ button.getText() + " on keyboard");
            // set key listener
            button.addKeyListener(keyListener);
            // set an action listener for every number button
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (calculation.size() == 0 || (!calculation.get(calculation.size()-1).equals("e") && !calculation.get(calculation.size()-1).equals("pi"))){
                        jTextArea.append(button.getText());
                        calculationHistory.add(button.getText());
                    }
                }
            });
            scientificKeyboard1.add(button); }

        // add division button
        JButton divisionButton = new JButton("/");
        scientificKeyboard1.add(divisionButton,7);
        divisionButton.setToolTipText("click or press "+ divisionButton.getText() + " on keyboard");
        // set key listener
        divisionButton.addKeyListener(keyListener);
        // set an action listener for button
        divisionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ((calculationHistory.size() !=0 &&
                        numbers.contains(calculationHistory.get(calculationHistory.size()-1))) ||
                        calculation.get(calculation.size()-1).equals("e") || calculation.get(calculation.size()-1).equals("pi")){
                    jTextArea.append(divisionButton.getText());
                    addToCalculationList(divisionButton.getText());
                }
            }
        });

        // add multiplication button
        JButton multiplicationButton = new JButton("*");
        scientificKeyboard1.add(multiplicationButton,11);
        multiplicationButton.setToolTipText("click or press "+ multiplicationButton.getText() + " on keyboard");
        // set key listener
        multiplicationButton.addKeyListener(keyListener);
        // set an action listener for button
        multiplicationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ((calculationHistory.size() !=0 &&
                        numbers.contains(calculationHistory.get(calculationHistory.size()-1))) ||
                        calculation.get(calculation.size()-1).equals("e") || calculation.get(calculation.size()-1).equals("pi")){
                    jTextArea.append(multiplicationButton.getText());
                    addToCalculationList(multiplicationButton.getText());
                }
            }
        });

        // add minus button
        JButton minusButton = new JButton("-");
        scientificKeyboard1.add(minusButton,15);
        minusButton.setToolTipText("click or press "+ minusButton.getText() + " on keyboard");
        // set key listener
        minusButton.addKeyListener(keyListener);
        // set an action listener for button
        minusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ((calculationHistory.size() !=0 &&
                        numbers.contains(calculationHistory.get(calculationHistory.size()-1))) ||
                        calculation.get(calculation.size()-1).equals("e") || calculation.get(calculation.size()-1).equals("pi")){
                    jTextArea.append(minusButton.getText());
                    addToCalculationList(minusButton.getText());
                }
            }
        });

        // add pi number button
        JButton piButton = new JButton("pi");
        scientificKeyboard1.add(piButton);
        piButton.setToolTipText("click or press P on keyboard");
        // set key listener
        piButton.addKeyListener(keyListener);
        // set an action listener for every number button
        piButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ((calculationHistory.size() == 0 || !numbers.contains(calculationHistory.get(calculationHistory.size()-1))) &&
                        (calculation.size() == 0 ||(!calculation.get(calculation.size()-1).equals("e")) && !calculation.get(calculation.size()-1).equals("pi"))){
                    jTextArea.append(piButton.getText());
                    calculation.add(piButton.getText());
                }
            }
        });

        // add zero number button
        JButton zeroButton = new JButton("0");
        scientificKeyboard1.add(zeroButton);
        zeroButton.setToolTipText("click or press "+ zeroButton.getText() + " on keyboard");
        // set key listener
        zeroButton.addKeyListener(keyListener);
        // set an action listener for every number button
        zeroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!calculation.get(calculation.size()-1).equals("e") && !calculation.get(calculation.size()-1).equals("pi")){
                    jTextArea.append(zeroButton.getText());
                    calculationHistory.add(zeroButton.getText());
                }
            }
        });

        // add e number button
        JButton eButton = new JButton("e");
        scientificKeyboard1.add(eButton);
        eButton.setToolTipText("click or press E on keyboard");
        // set key listener
        eButton.addKeyListener(keyListener);
        // set an action listener for every number button
        eButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ((calculationHistory.size() == 0 || !numbers.contains(calculationHistory.get(calculationHistory.size()-1))) &&
                        (calculation.size() == 0 ||(!calculation.get(calculation.size()-1).equals("e")) && !calculation.get(calculation.size()-1).equals("pi"))){
                    jTextArea.append(eButton.getText());
                    calculation.add(eButton.getText());
                }
            }
        });

        // add plus button
        JButton plusButton = new JButton("+");
        scientificKeyboard1.add(plusButton);
        plusButton.setToolTipText("click or press "+ plusButton.getText() + " on keyboard");
        // set key listener
        plusButton.addKeyListener(keyListener);
        // set an action listener for button
        plusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(calculation.get(calculation.size()-1)); // test
                if ((calculationHistory.size() !=0 &&
                        numbers.contains(calculationHistory.get(calculationHistory.size()-1))) ||
                        calculation.get(calculation.size()-1).equals("e") || calculation.get(calculation.size()-1).equals("pi")){
                    jTextArea.append(plusButton.getText());
                    addToCalculationList(plusButton.getText());
                }
            }
        });


        JPanel scientificKeyboard2 = new JPanel();
        scientificKeyboard2.setLayout(new BorderLayout());

        // add equal button
        JButton equalButton = new JButton("=");
        scientificKeyboard2.add(equalButton, BorderLayout.CENTER);
        equalButton.setToolTipText("click or press "+ equalButton.getText() + " on keyboard");
        // set key listener
        equalButton.addKeyListener(keyListener);
        // set an action listener for button
        equalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ((calculationHistory.size() !=0 &&
                        numbers.contains(calculationHistory.get(calculationHistory.size()-1))) ||
                calculation.get(calculation.size()-1).equals("e") || calculation.get(calculation.size()-1).equals("pi")){
                    jTextArea.append(equalButton.getText());
                    addToCalculationList(equalButton.getText());
                    jTextArea.append(calculate() + "\n");
                }
            }
        });

        // add shift button
        JButton shiftButton = new JButton("shift");
        scientificKeyboard2.add(shiftButton,BorderLayout.EAST);
        shiftButton.setToolTipText("click to change sin/tan/log to cos/cot/exp");
        // add action listener for shift button
        shiftButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < 3; i++){
                    scientificKeyboard1.remove(i);
                    JButton button = new JButton(buttonTextsWithShift[i]);
                    scientificKeyboard1.add(button, i);
                    button.setToolTipText(""+ button.getText() + ": based on degree");
                    // set key listener
                    button.addKeyListener(keyListener);
                    // add an action listener for button
                    button.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (calculationHistory.size() == 0 || operations.contains(calculationHistory.get(calculationHistory.size()-1))){
                                jTextArea.append(button.getText());
                                calculation.add(button.getText());
                            }
                        }
                    });
                }


                scientificKeyboard1.revalidate();
                scientificKeyboard1.repaint();
            }
        });
        scientificKeyboard.add(scientificKeyboard1,BorderLayout.CENTER);
        scientificKeyboard.add(scientificKeyboard2,BorderLayout.SOUTH);

        return scientificKeyboard;
    }


    /**
     * Draw a screen for calculator.
     */
    private void drawScreen(){
        jTextArea.setEditable(false);
        jTextArea.setFont(new Font("Arial", 14, 14));

        JScrollPane jScrollPane = new JScrollPane(jTextArea);
        jScrollPane.setSize(250,100);
        jScrollPane.setLocation(50,30);

        jTextArea.setToolTipText("calculations history");

        jFrame.add(jScrollPane);
    }

    /**
     * Add digits before operator to calculation list as a double number.
     * Add operator to the calculation list.
     * @param operation  the operator which entered by the user
     */
    private void addToCalculationList(String operation){
        String number = "";
        for (String ele:calculationHistory) {
            number += ele;
        }
        calculation.add(number);
        if (!operation.equals("=")) calculation.add(operation);
        calculationHistory.removeAll(calculationHistory);
    }

    /**
     * Calculate and return the result.
     * @return  calculation result.
     */
    private Double calculate(){
        // preprocess step: calculating sin,cos,tan,cot,exp and log functions and e and pi numbers
        for (int i = 0; i < calculation.size(); i++){
            if (calculation.get(i).equals("e")) calculation.set(i, Double.toString(Math.E));
            if (calculation.get(i).equals("pi")) calculation.set(i, Double.toString(Math.PI));}
        for (int i = calculation.size()-1; i >= 0 ; i--){
            if (functions.contains(calculation.get(i))){
                calculation.set(i, Double.toString(everyStepCalculationForFunctions(calculation.get(i), Double.valueOf(calculation.get(i+1)))));
                calculation.remove(i+1);
            }
        }

        Double result = null;
        for (int i = 0; i < calculation.size(); i++){
            if (result == null) result = Double.valueOf(calculation.get(0));
            if (operations.contains(calculation.get(i))) result = everyStepCalculation(result, Double.valueOf(calculation.get(i+1)),
                    calculation.get(i));
        }
        calculation.removeAll(calculation);
        return result;
    }

    /**
     * Calculation for two doubles based on one operator.
     * @param number1  first double number
     * @param number2  second double number
     * @param operation  the operator
     * @return  calculation result
     */
    private Double everyStepCalculation(Double number1, Double number2, String operation){
        switch (operation){
            case "%":
                return number1%number2;
            case "/":
                return number1/number2;
            case "*":
                return number1*number2;
            case "-":
                return number1-number2;
            case "+":
                return number1+number2;
            default: return 0.0;
        }
    }

    /**
     * Calculation for a function.
     * @param function  function which entered by the user.
     * @param number  the number which the function will be applied to it.
     * @return  calculation result
     */
    private Double everyStepCalculationForFunctions(String function, Double number){
        switch (function){
            case "sin":
                return Math.round(Math.sin(number*Math.PI/180) * 100000d) / 100000d;
            case "cos":
                return Math.round(Math.cos(number*Math.PI/180) * 100000d) / 100000d;
            case "tan":
                return Math.round(Math.tan(number*Math.PI/180) * 100000d) / 100000d;
            case "cot":
                return Math.round((1/Math.tan(number*Math.PI/180)) * 100000d) / 100000d;
            case "log":
                return Math.round(Math.log(number) * 100000d) / 100000d;
            case "exp":
                return Math.round(Math.exp(number) * 100000d) / 100000d;
            default: return 0.0;
        }
    }

    /**
     * Key listener class.
     * All the buttons have a key listener.
     */
    private class keyListenerForPanel extends KeyAdapter{
        KeyEvent previousKeyEvent = null;
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            int key = e.getKeyCode();
            switch (key){
                case KeyEvent.VK_0:
                    jTextArea.append("0");
                    calculationHistory.add("0");
                    break;
                case KeyEvent.VK_1:
                    jTextArea.append("1");
                    calculationHistory.add("1");
                    break;
                case KeyEvent.VK_2:
                    jTextArea.append("2");
                    calculationHistory.add("2");
                    break;
                case KeyEvent.VK_3:
                    jTextArea.append("3");
                    calculationHistory.add("3");
                    break;
                case KeyEvent.VK_4:
                    jTextArea.append("4");
                    calculationHistory.add("4");
                    break;
                case KeyEvent.VK_5:
                    if (previousKeyEvent != null && previousKeyEvent.getKeyCode() == (KeyEvent.VK_SHIFT)){
                        if (calculationHistory.size() !=0 &&
                                numbers.contains(calculationHistory.get(calculationHistory.size()-1))){
                            jTextArea.append("%");
                            addToCalculationList("%");
                        }
                    }
                    else {
                        jTextArea.append("5");
                        calculationHistory.add("5");
                    }
                    break;
                case KeyEvent.VK_6:
                    jTextArea.append("6");
                    calculationHistory.add("6");
                    break;
                case KeyEvent.VK_7:
                    jTextArea.append("7");
                    calculationHistory.add("7");
                    break;
                case KeyEvent.VK_8:
                    if (previousKeyEvent != null && previousKeyEvent.getKeyCode() == (KeyEvent.VK_SHIFT)){
                        if (calculationHistory.size() !=0 &&
                                numbers.contains(calculationHistory.get(calculationHistory.size()-1))){
                            jTextArea.append("*");
                            addToCalculationList("*");
                        }
                    }
                    else {
                        jTextArea.append("8");
                        calculationHistory.add("8");
                    }
                    break;
                case KeyEvent.VK_9:
                    jTextArea.append("9");
                    calculationHistory.add("9");
                    break;
                case KeyEvent.VK_EQUALS:
                    if (calculationHistory.size() !=0 &&
                            numbers.contains(calculationHistory.get(calculationHistory.size()-1))){
                        if (previousKeyEvent != null && previousKeyEvent.getKeyCode() == (KeyEvent.VK_SHIFT)) {
                            jTextArea.append("+");
                            addToCalculationList("+");
                        }
                        else {
                            jTextArea.append("=");
                            addToCalculationList("=");
                            jTextArea.append(calculate() + "\n");
                        }
                    }
                    break;
                case KeyEvent.VK_SLASH:
                    if (calculationHistory.size() !=0 &&
                            numbers.contains(calculationHistory.get(calculationHistory.size()-1))){
                        jTextArea.append("/");
                        addToCalculationList("/");
                    }
                    break;
                case KeyEvent.VK_MINUS:
                    if (calculationHistory.size() !=0 &&
                            numbers.contains(calculationHistory.get(calculationHistory.size()-1))){
                        jTextArea.append("-");
                        addToCalculationList("-");
                    }
                    break;
                case KeyEvent.VK_E:
                    if ((calculationHistory.size() == 0 || !numbers.contains(calculationHistory.get(calculationHistory.size()-1))) &&
                            (calculation.size() == 0 ||(!calculation.get(calculation.size()-1).equals("e")) && !calculation.get(calculation.size()-1).equals("pi"))){
                        jTextArea.append("e");
                        calculation.add("e");
                    }
                    break;
                case KeyEvent.VK_P:
                    if ((calculationHistory.size() == 0 || !numbers.contains(calculationHistory.get(calculationHistory.size()-1))) &&
                            (calculation.size() == 0 ||(!calculation.get(calculation.size()-1).equals("e")) && !calculation.get(calculation.size()-1).equals("pi"))){
                        jTextArea.append("pi");
                        calculation.add("pi");
                    }
                    break;
            }
            previousKeyEvent = e;
        }
    }


}
