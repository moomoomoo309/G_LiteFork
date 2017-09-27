//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import javax.print.PrintService;
import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings({"HardcodedFileSeparator", "unused"})
class G_Lite extends JFrame implements Printable {
    private static String compileExecutable = "arm-none-eabi-gcc";
    private static String runExecutable = "arm-none-eabi-run";
    private final JFileChooser fileChooser = new JFileChooser();
    private final String cFileDescription = "C_language_file";
    private final String asmLangFileDescription = "Assembly_lang_file";
    private final String compileFlags = " -g -c -o ";
    private final String linkFlags = " -g -o ";
    private final String compileCommand = compileExecutable + compileFlags;
    private final boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("Windows");
    private final String runFlags = " ";
    private final String runCommand = runExecutable + runFlags;
    private final String windowsFileExtension = "bat";
    private final String nixFileExtension = "sh";
    private final String runfileName = "cs";
    private final String runfile = runfileName + '.' + (isWindows ? windowsFileExtension: nixFileExtension);
    private final List<String> lines = new ArrayList<>();
    private final String spaces = String.join("", Collections.nCopies(43, " "));
    private final int[] spc = new int[]{4, 3, 2, 1, 4, 3, 2, 1, 4, 3, 2, 1, 4, 3, 2, 1, 4, 3, 2, 1, 4, 3, 2, 1, 4, 3, 2, 1, 4, 3, 2, 1, 4, 3, 2, 1, 4, 3, 2, 1};
    private JButton assemblyLangChooser;
    private JButton assembleButton;
    private JTextField cFileField;
    private JButton cLangChooser;
    private JButton compileButton;
    private JButton exitButton;
    private JButton linkButton;
    private JButton printButton;
    private JButton runButton;
    private JTextField runFileField;
    private JTextField asmFileField;
    private JButton filePrint;
    private JLabel studentNameLabel;
    private JPanel mainPanel;
    private JPanel innerPanel;
    private JScrollPane processOutput;
    private JTextArea processText;
    private JTextField studentName;
    private String fileName;
    private String workPath = "./";
    private int[] pageBreaks;

    private G_Lite() {
        this.initComponents();
    }

    public static void main(String[] args) {
        for (String arg : args)
            if (arg.toLowerCase().startsWith("--runexecutable="))
                runExecutable = arg.toLowerCase().substring(17);
            else if (arg.toLowerCase().startsWith("--compileexecutable="))
                compileExecutable = arg.toLowerCase().substring(20);

        try {
            //GTK doesn't always want to show up.
            UIManager.setLookAndFeel(com.sun.java.swing.plaf.gtk.GTKLookAndFeel.class.getName());
        } catch (IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException | ClassNotFoundException _e) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | UnsupportedLookAndFeelException | IllegalAccessException | InstantiationException e) {
                Logger.getLogger(G_Lite.class.getName()).log(Level.SEVERE, null, e);
            }
        }

        EventQueue.invokeLater(() -> (new G_Lite()).setVisible(true));
    }

    private void initComponents() {
        this.mainPanel = new JPanel();
        this.compileButton = new JButton();
        this.assembleButton = new JButton();
        this.linkButton = new JButton();
        this.runButton = new JButton();
        this.cFileField = new JTextField();
        this.asmFileField = new JTextField();
        this.runFileField = new JTextField();
        this.cLangChooser = new JButton();
        this.assemblyLangChooser = new JButton();
        this.innerPanel = new JPanel();
        this.exitButton = new JButton();
        this.printButton = new JButton();
        this.filePrint = new JButton();
        this.processOutput = new JScrollPane();
        this.processText = new JTextArea();
        this.studentName = new JTextField();
        this.studentNameLabel = new JLabel();
        this.setDefaultCloseOperation(3);
        this.mainPanel.setBorder(BorderFactory.createTitledBorder("CS252 UI Assistant (S16)"));
        this.compileButton.setText("Compile");
        this.compileButton.setToolTipText("Press this button to compile the C program. A file cout is created in the working directory.");
        this.compileButton.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        this.compileButton.addActionListener(G_Lite.this::onCompilePressed);
        this.assembleButton.setText("assembleButton");
        this.assembleButton.setToolTipText("Press this button to assemble the ARM file. This will create an sout file in the working directory.");
        this.assembleButton.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        this.assembleButton.addActionListener(G_Lite.this::onAssemblePressed);
        this.linkButton.setText("Link");
        this.linkButton.setToolTipText("Press this button to create a runnable file. If a name is entered in the text box tat will be the name of the file, otherwise it is called runit.");
        this.linkButton.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        this.linkButton.addActionListener(G_Lite.this::onLinkPressed);
        this.runButton.setText("Run");
        this.runButton.setToolTipText("Press this button to run the file.");
        this.runButton.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        this.runButton.addActionListener(G_Lite.this::onRunPressed);
        this.cLangChooser.setBackground(new Color(255, 255, 255));
        this.cLangChooser.setText("C  language file");
        this.cLangChooser.setToolTipText("Press this button ot chose the C lanuguage file to compile");
        this.cLangChooser.setActionCommand("C_language_file");
        this.cLangChooser.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        this.cLangChooser.addActionListener(G_Lite.this::onCLangChooserPressed);
        this.assemblyLangChooser.setBackground(new Color(255, 255, 255));
        this.assemblyLangChooser.setText("Assembly Language file");
        this.assemblyLangChooser.setToolTipText("Press this butto to chose the ARM assembly language file to assemble.");
        this.assemblyLangChooser.setActionCommand("Assembly_lang_file");
        this.assemblyLangChooser.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        this.assemblyLangChooser.addActionListener(G_Lite.this::onAssemblyLangChooserPressed);
        this.exitButton.setText("Exit");
        this.exitButton.setToolTipText("This will exit the application.");
        this.exitButton.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        this.exitButton.addActionListener(G_Lite.this::onExitPressed);
        this.printButton.setText("printButton");
        this.printButton.setToolTipText("This button will print the contents of the text area to the left." + System.getProperty("line.separator"));
        this.printButton.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        this.printButton.addActionListener(G_Lite.this::onPrintPressed);
        this.filePrint.setText("File");
        this.filePrint.setToolTipText("This button will create a text file of the source files and execution");
        this.filePrint.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        this.filePrint.addActionListener(G_Lite.this::onFilePressed);
        GroupLayout jPanel2Layout = new GroupLayout(this.innerPanel);
        this.innerPanel.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, jPanel2Layout.createSequentialGroup().addComponent(this.printButton).addGap(18, 18, 18).addComponent(this.filePrint, -1, -1, 32767)).addGroup(Alignment.TRAILING, jPanel2Layout.createSequentialGroup().addGap(0, 0, 32767).addComponent(this.exitButton))).addContainerGap()));
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addGap(20, 20, 20).addGroup(jPanel2Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.printButton).addComponent(this.filePrint)).addPreferredGap(ComponentPlacement.RELATED, 36, 32767).addComponent(this.exitButton).addContainerGap()));
        this.processText.setColumns(20);
        this.processText.setRows(5);
        this.processText.setToolTipText("This text area will show the output of the compile, assemble, link and the result of the running of the  linked file.");
        this.processOutput.setViewportView(this.processText);
        this.studentName.addActionListener(G_Lite.this::onStudentNameChanged);
        this.studentNameLabel.setText("Student Name");
        GroupLayout mainPanelLayout = new GroupLayout(this.mainPanel);
        this.mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(mainPanelLayout.createParallelGroup(Alignment.LEADING).addGroup(mainPanelLayout.createSequentialGroup().addComponent(this.processOutput, -1, 420, 32767).addGap(37, 37, 37).addComponent(this.innerPanel, -2, -1, -2).addGap(22, 22, 22)).addGroup(mainPanelLayout.createSequentialGroup().addGap(30, 30, 30).addGroup(mainPanelLayout.createParallelGroup(Alignment.LEADING).addComponent(this.runButton).addGroup(mainPanelLayout.createSequentialGroup().addGroup(mainPanelLayout.createParallelGroup(Alignment.TRAILING, false).addGroup(Alignment.LEADING, mainPanelLayout.createSequentialGroup().addComponent(this.linkButton).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.runFileField, -1, 172, 32767)).addGroup(Alignment.LEADING, mainPanelLayout.createSequentialGroup().addComponent(this.assembleButton).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.asmFileField)).addGroup(Alignment.LEADING, mainPanelLayout.createSequentialGroup().addComponent(this.compileButton).addPreferredGap(ComponentPlacement.RELATED).addGroup(mainPanelLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.studentName, -1, 172, 32767).addComponent(this.cFileField)))).addGap(18, 18, 18).addGroup(mainPanelLayout.createParallelGroup(Alignment.LEADING).addComponent(this.studentNameLabel).addComponent(this.cLangChooser).addComponent(this.assemblyLangChooser)))).addContainerGap(163, 32767)));
        mainPanelLayout.linkSize(0, this.assembleButton, this.compileButton, this.linkButton, this.runButton);
        mainPanelLayout.linkSize(0, this.assemblyLangChooser, this.cLangChooser);
        mainPanelLayout.setVerticalGroup(mainPanelLayout.createParallelGroup(Alignment.LEADING).addGroup(mainPanelLayout.createSequentialGroup().addContainerGap(50, 32767).addGroup(mainPanelLayout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, mainPanelLayout.createSequentialGroup().addGroup(mainPanelLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.studentName, -2, -1, -2).addGroup(mainPanelLayout.createSequentialGroup().addGap(3, 3, 3).addComponent(this.studentNameLabel, -1, -1, 32767))).addPreferredGap(ComponentPlacement.UNRELATED).addGroup(mainPanelLayout.createParallelGroup(Alignment.BASELINE).addComponent(this.compileButton).addComponent(this.cFileField, -2, -1, -2).addComponent(this.cLangChooser)).addPreferredGap(ComponentPlacement.RELATED).addGroup(mainPanelLayout.createParallelGroup(Alignment.LEADING).addGroup(mainPanelLayout.createParallelGroup(Alignment.BASELINE).addComponent(this.assembleButton).addComponent(this.assemblyLangChooser)).addComponent(this.asmFileField, -2, -1, -2)).addPreferredGap(ComponentPlacement.UNRELATED).addGroup(mainPanelLayout.createParallelGroup(Alignment.BASELINE).addComponent(this.runFileField, -2, -1, -2).addComponent(this.linkButton)).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.runButton).addGap(26, 26, 26).addComponent(this.processOutput, -2, 100, -2)).addComponent(this.innerPanel, Alignment.TRAILING, -2, -1, -2))));
        GroupLayout layout = new GroupLayout(this.getContentPane());
        this.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(this.mainPanel, -1, -1, 32767).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(this.mainPanel, -1, -1, 32767).addContainerGap()));
        this.mainPanel.getAccessibleContext().setAccessibleName("CS252 UI Assistant (S16)");
        this.setTitle("CS252 UI Assistant");
        this.pack();
    }

    public int print(Graphics g, PageFormat pf, int pageIndex) {
        Font font = new Font(Font.MONOSPACED, Font.PLAIN, 12);
        FontMetrics metrics = g.getFontMetrics(font);
        int lineHeight = metrics.getHeight();
        int y;
        int start;
        if (this.pageBreaks == null) {
            int linesPerPage = (int) (pf.getImageableHeight() / (double) lineHeight);
            y = (this.lines.size() - 1) / linesPerPage;
            this.pageBreaks = new int[y];

            for (start = 0; start < y; ++start) {
                this.pageBreaks[start] = (start + 1) * linesPerPage;
            }
        }

        if (pageIndex > this.pageBreaks.length) {
            return 1;
        } else {
            Graphics2D g2d = (Graphics2D) g;
            g2d.translate(pf.getImageableX(), pf.getImageableY());
            y = 0;
            start = pageIndex == 0 ? 0: this.pageBreaks[pageIndex - 1];
            int end = pageIndex == this.pageBreaks.length ? this.lines.size(): this.pageBreaks[pageIndex];

            for (int line = start; line < end; line++) {
                y += lineHeight;
                g.drawString(this.lines.get(line), 0, y);
            }

            return 0;
        }
    }

    private int writeAndExecuteCommand(String filePath, String command, String formatString) {
        File tmpFile = new File(filePath);
        try {
            Files.write(tmpFile.toPath(), ((isWindows ? ("@echo off" + System.getProperty("line.separator")): "") + command).getBytes());
            if (!isWindows)
                if (!tmpFile.setExecutable(true))
                    Logger.getLogger(G_Lite.class.getName()).log(Level.SEVERE, null, "Could not mark file \"" + filePath + "\" as executable.");
            ProcessBuilder pb = new ProcessBuilder(filePath);
            pb.redirectErrorStream(true);
            Process process = pb.start();
            BufferedReader cmdOutputBuffer = new BufferedReader(new InputStreamReader(process.getInputStream()));
            cmdOutputBuffer.lines().forEachOrdered(line -> processText.append(line + System.getProperty("line.separator")));
            int result = process.waitFor();
            if (!formatString.isEmpty())
                this.processText.append(String.format(formatString, result) + System.getProperty("line.separator"));
            process.destroy();
            Files.deleteIfExists(tmpFile.toPath());
            return result;
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }

    private void onCompilePressed(ActionEvent evt) {
        if (this.cFileField.getText().isEmpty())
            return;
        String cfile = '"' + this.workPath + this.cFileField.getText() + '"';
        String outfile = '"' + this.workPath + "cout" + '"';
        String cs252 = this.workPath + this.runfile;
        String Ccommand = this.compileCommand + outfile + ' ' + cfile;
        File tmpFile = new File(cs252);
        writeAndExecuteCommand(cs252, Ccommand, "Compile result: %d");
    }

    private void onExitPressed(ActionEvent evt) {
        System.exit(0);
    }

    private void onCLangChooserPressed(ActionEvent evt) {
        this.openFilePicker(evt, this.cFileField);
    }

    private void onAssemblyLangChooserPressed(ActionEvent evt) {
        this.openFilePicker(evt, this.asmFileField);
    }

    private void onAssemblePressed(ActionEvent evt) {
        if (this.asmFileField.getText().isEmpty())
            return;
        String sfile = '"' + this.workPath + this.asmFileField.getText() + '"';
        String outfile = '"' + this.workPath + "sout" + '"';
        String Acommand = this.compileCommand + outfile + ' ' + sfile;
        String cs252 = this.workPath + this.runfile;
        File tmpFile = new File(cs252);
        writeAndExecuteCommand(cs252, Acommand, "Assemble Result: %d");
    }

    private void onLinkPressed(ActionEvent evt) {
        String cfile = '"' + this.workPath + "cout" + '"';
        String sfile = '"' + this.workPath + "sout" + '"';
        String option = " -T armulator-ram-hosted.ld";
        String cs252 = this.workPath + this.runfile;
        File tmpFile = new File(cs252);
        String rlfile = this.runFileField.getText();
        String outfile;
        if (rlfile.isEmpty()) {
            outfile = '"' + this.workPath + "runit" + '"';
        } else {
            outfile = '"' + this.workPath + rlfile + '"';
        }
        String Lcommand = compileExecutable + linkFlags + outfile + ' ' + cfile + ' ' + sfile + ' ' + option;
        int result = writeAndExecuteCommand(cs252, Lcommand, "Link Result: %d");
        if (result == 0) {
            if (rlfile.equals("")) {
                this.runFileField.setText("runit");
            } else {
                this.runFileField.setText(rlfile);
            }
        }

    }

    private void onRunPressed(ActionEvent evt) {
        String cs252 = this.workPath + this.runfile;
        File tmpFile = new File(cs252);
        String rfile = this.runFileField.getText();
        String outfile;
        if (rfile.equals("")) {
            outfile = '"' + this.workPath + "runit" + '"';
        } else {
            outfile = '"' + this.workPath + rfile + '"';
        }
        String Rcommand = runCommand + outfile;
        writeAndExecuteCommand(cs252, Rcommand, "");
    }

    private String tab2spaces(String in) {
        StringBuilder expand = new StringBuilder();

        for (int index = 0; index < in.length(); index++) {
            if (in.charAt(index) == '\t') {
                expand.append(this.spaces.substring(index, this.spc[index] + index));
            } else {
                expand.append(in.charAt(index));
            }
        }

        return expand.toString();
    }

    private boolean _appendOutput(String filePath) {
        try {
            BufferedReader instream = new BufferedReader(new FileReader(filePath));
            instream.lines().forEachOrdered(line -> this.lines.add(this.tab2spaces(line)));
            instream.close();
        } catch (FileNotFoundException e) {
            System.out.println("Could not find file at \""+filePath+"\". Were the C and S files generated correctly?");
            return false;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    private boolean initFileOutput() {
        String cfile = this.workPath + this.cFileField.getText();
        String sfile = this.workPath + this.asmFileField.getText();
        BufferedReader instream;
        this.lines.add(this.studentName.getText());
        this.lines.add(System.getProperty("line.separator"));
        this.lines.add(this.processText.getText());
        this.lines.add(System.getProperty("line.separator") + " ******************" + System.getProperty("line.separator"));
        this.lines.add(sfile);
        if (!_appendOutput(sfile))
            return false;
        this.lines.add(System.getProperty("line.separator") + "------------------" + System.getProperty("line.separator"));
        this.lines.add(cfile);
        return _appendOutput(cfile);
    }

    private void onPrintPressed(ActionEvent evt) {
        if (!this.initFileOutput())
            return;
        PrinterJob job = PrinterJob.getPrinterJob();
        PrintService printer = job.getPrintService();
        if (printer == null) {
            JOptionPane.showMessageDialog(this, "No Default Printer!", "Printer Error", JOptionPane.ERROR_MESSAGE);
        } else {
            job.setPrintable(this);
            boolean ok = job.printDialog();
            if (ok) {
                try {
                    job.print();
                } catch (PrinterException var6) {
                    System.out.println(var6.getMessage());
                }
            }

        }
    }

    @SuppressWarnings("EmptyMethod")
    private void onStudentNameChanged(ActionEvent evt) {
    }

    private void onFilePressed(ActionEvent evt) {
        PrintWriter resultWriter = null;
        String resultFile = this.workPath + "result.txt";
        this.initFileOutput();

        try {
            resultWriter = new PrintWriter(new FileWriter(resultFile));
            this.lines.forEach(resultWriter::println);
        } catch (IOException e) {
            System.err.println("Trouble writing file: " + e);
        } finally {
            if (resultWriter != null) {
                resultWriter.close();
            }
        }
    }

    private void openFilePicker(ActionEvent evt, JTextField field) {
        int returnVal = this.fileChooser.showOpenDialog(this);
        if (returnVal == 0) {
            File file = this.fileChooser.getSelectedFile();
            this.fileName = file.getName();
            String myPath = file.getPath();
            myPath = myPath.replace('\\', '/');
            int endIndex = myPath.lastIndexOf('/');
            this.workPath = myPath.substring(0, endIndex + 1);
            field.setText(this.fileName);
        }

    }
}
