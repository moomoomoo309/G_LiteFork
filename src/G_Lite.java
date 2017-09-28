import javax.print.PrintService;
import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

class G_Lite extends JFrame implements Printable {
    private static String compileExecutable = "arm-none-eabi-gcc";
    private static String runExecutable = "arm-none-eabi-run";
    private final JFileChooser fileChooser = new JFileChooser();
    private final String compileFlags = " -g -c -o ";
    private final String compileCommand = compileExecutable + compileFlags;
    private final boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("Windows");
    private final String runFlags = " ";
    private final String runCommand = runExecutable + runFlags;
    private final String windowsFileExtension = "bat";
    private final String nixFileExtension = "sh";
    private final String runfileName = "cs";
    private final String runfile = runfileName + '.' + (isWindows ? windowsFileExtension: nixFileExtension);
    private final List<String> lines = new ArrayList<>();
    private JTextField cFileField;
    private JTextField runFileField;
    private JTextField asmFileField;
    private JTextArea processText;
    private JTextField studentName;
    private String workPath = "./";
    private int[] pageBreaks;

    /**
     * Required for Swing to work correctly.
     */
    private G_Lite() {
        this.initComponents();
    }

    /**
     * Parses command line arguments and sets the look and feel of Swing.
     * @param args Command line arguments. <br>
     *             The program looks for:
     *             <ol>
     *                 <li>
     *                     --runexecutable=(executablename)
     *                 </li>
     *                 <li>
     *                     --compileexecutable=(compileexecutable)\
     *                 </li>
     *             </ol>
     */
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
            } catch (IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException | ClassNotFoundException e) {
                Logger.getLogger(G_Lite.class.getName()).log(Level.SEVERE, null, e);
            }
        }

        EventQueue.invokeLater(() -> (new G_Lite()).setVisible(true));
    }

    /**
     * Sets up all of the components for the UI.
     */
    private void initComponents() {
        JPanel mainPanel = new JPanel();
        JButton compileButton = new JButton();
        JButton assembleButton = new JButton();
        JButton linkButton = new JButton();
        JButton runButton = new JButton();

        this.cFileField = new JTextField();
        this.asmFileField = new JTextField();
        this.runFileField = new JTextField();

        JButton cLangChooser = new JButton();
        JButton assemblyLangChooser = new JButton();
        JButton exitButton = new JButton();
        JButton printButton = new JButton();
        JButton filePrint = new JButton();

        JPanel innerPanel = new JPanel();
        JScrollPane processOutput = new JScrollPane();
        this.processText = new JTextArea();
        this.studentName = new JTextField();
        JLabel studentNameLabel = new JLabel();

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        mainPanel.setBorder(BorderFactory.createTitledBorder("tmpFilePath UI Assistant (S16)"));

        compileButton.setText("Compile");
        compileButton.setToolTipText("Press this button to compile the C program. A file cout is created in the working directory.");
        compileButton.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        compileButton.addActionListener(evt -> G_Lite.this.onCompilePressed());

        assembleButton.setText("Assemble");
        assembleButton.setToolTipText("Press this button to assemble the ARM file. This will create an sout file in the working directory.");
        assembleButton.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        assembleButton.addActionListener(evt1 -> G_Lite.this.onAssemblePressed());

        linkButton.setText("Link");
        linkButton.setToolTipText("Press this button to create a runnable file. If a name is entered in the text box tat will be the name of the file, otherwise it is called runit.");
        linkButton.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        linkButton.addActionListener(evt1 -> G_Lite.this.onLinkPressed());

        runButton.setText("Run");
        runButton.setToolTipText("Press this button to run the file.");
        runButton.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        runButton.addActionListener(evt1 -> G_Lite.this.onRunPressed());

        cLangChooser.setBackground(new Color(255, 255, 255));
        cLangChooser.setText("C  language file");
        cLangChooser.setToolTipText("Press this button ot chose the C lanuguage file to compile");
        cLangChooser.setActionCommand("C_language_file");
        cLangChooser.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        cLangChooser.addActionListener(evt2 -> G_Lite.this.onCLangChooserPressed());

        assemblyLangChooser.setBackground(new Color(255, 255, 255));
        assemblyLangChooser.setText("Assembly Language file");
        assemblyLangChooser.setToolTipText("Press this butto to chose the ARM assembly language file to assemble.");
        assemblyLangChooser.setActionCommand("Assembly_lang_file");
        assemblyLangChooser.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        assemblyLangChooser.addActionListener(evt2 -> G_Lite.this.onAssemblyLangChooserPressed());

        exitButton.setText("Exit");
        exitButton.setToolTipText("This will exit the application.");
        exitButton.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        exitButton.addActionListener(evt -> G_Lite.this.onExitPressed());

        printButton.setText("Print");
        printButton.setToolTipText("This button will print the contents of the text area to the left." + System.getProperty("line.separator"));
        printButton.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        printButton.addActionListener(actionListener -> this.onPrintPressed());

        filePrint.setText("File");
        filePrint.setToolTipText("This button will create a text file of the source files and execution");
        filePrint.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        filePrint.addActionListener(evt1 -> G_Lite.this.onFilePressed());

        GroupLayout innerLayout = new GroupLayout(innerPanel);
        innerLayout.setHorizontalGroup(innerLayout.createParallelGroup(Alignment.LEADING).addGroup(innerLayout.createSequentialGroup().addGroup(innerLayout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, innerLayout.createSequentialGroup().addComponent(printButton).addGap(18, 18, 18).addComponent(filePrint, -1, -1, 32767)).addGroup(Alignment.TRAILING, innerLayout.createSequentialGroup().addGap(0, 0, 32767).addComponent(exitButton))).addContainerGap()));
        innerLayout.setVerticalGroup(innerLayout.createParallelGroup(Alignment.LEADING).addGroup(innerLayout.createSequentialGroup().addGap(20, 20, 20).addGroup(innerLayout.createParallelGroup(Alignment.BASELINE).addComponent(printButton).addComponent(filePrint)).addPreferredGap(ComponentPlacement.RELATED, 36, 32767).addComponent(exitButton).addContainerGap()));
        innerPanel.setLayout(innerLayout);

        this.processText.setColumns(20);
        this.processText.setRows(5);
        this.processText.setToolTipText("This text area will show the output of the compile, assemble, link and the result of the running of the  linked file.");

        processOutput.setViewportView(this.processText);

        this.studentName.addActionListener(evt -> this.onStudentNameChanged());
        studentNameLabel.setText("Student Name");

        GroupLayout mainPanelLayout = new GroupLayout(mainPanel);
        mainPanelLayout.setHorizontalGroup(mainPanelLayout.createParallelGroup(Alignment.LEADING).addGroup(mainPanelLayout.createSequentialGroup().addComponent(processOutput, -1, 420, 32767).addGap(37, 37, 37).addComponent(innerPanel, -2, -1, -2).addGap(22, 22, 22)).addGroup(mainPanelLayout.createSequentialGroup().addGap(30, 30, 30).addGroup(mainPanelLayout.createParallelGroup(Alignment.LEADING).addComponent(runButton).addGroup(mainPanelLayout.createSequentialGroup().addGroup(mainPanelLayout.createParallelGroup(Alignment.TRAILING, false).addGroup(Alignment.LEADING, mainPanelLayout.createSequentialGroup().addComponent(linkButton).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.runFileField, -1, 172, 32767)).addGroup(Alignment.LEADING, mainPanelLayout.createSequentialGroup().addComponent(assembleButton).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.asmFileField)).addGroup(Alignment.LEADING, mainPanelLayout.createSequentialGroup().addComponent(compileButton).addPreferredGap(ComponentPlacement.RELATED).addGroup(mainPanelLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.studentName, -1, 172, 32767).addComponent(this.cFileField)))).addGap(18, 18, 18).addGroup(mainPanelLayout.createParallelGroup(Alignment.LEADING).addComponent(studentNameLabel).addComponent(cLangChooser).addComponent(assemblyLangChooser)))).addContainerGap(163, 32767)));
        mainPanelLayout.linkSize(0, assembleButton, compileButton, linkButton, runButton);
        mainPanelLayout.linkSize(0, assemblyLangChooser, cLangChooser);
        mainPanelLayout.setVerticalGroup(mainPanelLayout.createParallelGroup(Alignment.LEADING).addGroup(mainPanelLayout.createSequentialGroup().addContainerGap(50, 32767).addGroup(mainPanelLayout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, mainPanelLayout.createSequentialGroup().addGroup(mainPanelLayout.createParallelGroup(Alignment.LEADING, false).addComponent(this.studentName, -2, -1, -2).addGroup(mainPanelLayout.createSequentialGroup().addGap(3, 3, 3).addComponent(studentNameLabel, -1, -1, 32767))).addPreferredGap(ComponentPlacement.UNRELATED).addGroup(mainPanelLayout.createParallelGroup(Alignment.BASELINE).addComponent(compileButton).addComponent(this.cFileField, -2, -1, -2).addComponent(cLangChooser)).addPreferredGap(ComponentPlacement.RELATED).addGroup(mainPanelLayout.createParallelGroup(Alignment.LEADING).addGroup(mainPanelLayout.createParallelGroup(Alignment.BASELINE).addComponent(assembleButton).addComponent(assemblyLangChooser)).addComponent(this.asmFileField, -2, -1, -2)).addPreferredGap(ComponentPlacement.UNRELATED).addGroup(mainPanelLayout.createParallelGroup(Alignment.BASELINE).addComponent(this.runFileField, -2, -1, -2).addComponent(linkButton)).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(runButton).addGap(26, 26, 26).addComponent(processOutput, -2, 100, -2)).addComponent(innerPanel, Alignment.TRAILING, -2, -1, -2))));
        mainPanel.setLayout(mainPanelLayout);

        GroupLayout windowLayout = new GroupLayout(this.getContentPane());
        this.getContentPane().setLayout(windowLayout);
        windowLayout.setHorizontalGroup(windowLayout.createParallelGroup(Alignment.LEADING).addGroup(windowLayout.createSequentialGroup().addContainerGap().addComponent(mainPanel, -1, -1, 32767).addContainerGap()));
        windowLayout.setVerticalGroup(windowLayout.createParallelGroup(Alignment.LEADING).addGroup(windowLayout.createSequentialGroup().addContainerGap().addComponent(mainPanel, -1, -1, 32767).addContainerGap()));

        mainPanel.getAccessibleContext().setAccessibleName("tmpFilePath UI Assistant (S16)");
        this.setTitle("tmpFilePath UI Assistant");
        this.pack();
    }

    /**
     * Prints the output in {@link #lines} onto a page.
     * @param g The {@link Graphics} backend used to draw the text
     * @param pf How the page should be formatted.
     * @param pageIndex The page to print.
     * @return 0 if successful, 1 if not.
     */
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

    /**
     * Writes to a temporary .sh/.bat file, puts command in it, executes it, and appends formatString with the exit code to {@link #lines}.
     * @param filePath The path to the temporary file.
     * @param command The command to be put into the file and run.
     * @param formatString The string used by .format, passed the exit code of the result.
     * @return The exit code of the command, or -1 if the command could not be run.
     */
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

    /**
     * Compiles the program, or does nothing if no file is in {@link #cFileField}.
     */
    private void onCompilePressed() {
        if (this.cFileField.getText().isEmpty())
            return;
        String cfile = '"' + this.workPath + this.cFileField.getText() + '"';
        String outfile = '"' + this.workPath + "cout" + '"';
        String tmpFilePath = this.workPath + this.runfile;
        String compileCommand = this.compileCommand + outfile + ' ' + cfile;
        writeAndExecuteCommand(tmpFilePath, compileCommand, "Compile result: %d");
    }

    /**
     * Exits the program.
     */
    private void onExitPressed() {
        System.exit(0);
    }

    /**
     * Pops up the filepicker for the user pick to the file to compile.
     */
    private void onCLangChooserPressed() {
        this.openFilePicker(this.cFileField);
    }

    /**
     * Pops up the filepicker for the user pick to the file to assemble.
     */
    private void onAssemblyLangChooserPressed() {
        this.openFilePicker(this.asmFileField);
    }

    /**
     * Assembles the .s file, or does nothing if {@link #asmFileField} is empty.
     */
    private void onAssemblePressed() {
        if (this.asmFileField.getText().isEmpty())
            return;
        String sfile = '"' + this.workPath + this.asmFileField.getText() + '"';
        String outfile = '"' + this.workPath + "sout" + '"';
        String assembleCommand = this.compileCommand + outfile + ' ' + sfile;
        String tmpFilePath = this.workPath + this.runfile;
        writeAndExecuteCommand(tmpFilePath, assembleCommand, "Assemble Result: %d");
    }

    /**
     * Tries to link the files.
     */
    private void onLinkPressed() {
        String cfile = '"' + this.workPath + "cout" + '"';
        String sfile = '"' + this.workPath + "sout" + '"';
        String option = " -T armulator-ram-hosted.ld";
        String tmpFilePath = this.workPath + this.runfile;
        String fileToRun = this.runFileField.getText();
        String outfile;
        if (fileToRun.isEmpty()) {
            outfile = '"' + this.workPath + "runit" + '"';
        } else {
            outfile = '"' + this.workPath + fileToRun + '"';
        }
        String linkFlags = " -g -o ";
        String linkCommand = compileExecutable + linkFlags + outfile + ' ' + cfile + ' ' + sfile + ' ' + option;
        int result = writeAndExecuteCommand(tmpFilePath, linkCommand, "Link Result: %d");
        if (result == 0) {
            if (fileToRun.equals("")) {
                this.runFileField.setText("runit");
            } else {
                this.runFileField.setText(fileToRun);
            }
        }

    }

    /**
     * Tries to run the file.
     */
    private void onRunPressed() {
        String tmpFilePath = this.workPath + this.runfile;
        String fileToRun = this.runFileField.getText();
        String outfile = '"' + this.workPath + (fileToRun.equals("") ? "runit": fileToRun) + '"';
        String runCommand = this.runCommand + outfile;
        writeAndExecuteCommand(tmpFilePath, runCommand, "");
    }

    /**
     * Converts tabs to 4 spaces.
     * @param in The string to replace.
     * @return The string, but with all tabs replaced with 4 spaces.
     */
    private String tab2spaces(String in) {
        return in.replace("\t", "    ");
    }

    /**
     * Appends the contents of filePath to {@link #lines}.
     * @param filePath The path of the file to read.
     * @return If the file could read correctly.
     */
    private boolean _appendOutput(String filePath) {
        try {
            Files.lines(new File(filePath).toPath()).forEachOrdered(line -> this.lines.add(this.tab2spaces(line)));
        } catch (FileNotFoundException e) {
            System.out.println("Could not find file at \"" + filePath + "\". Were the C and S files generated correctly?");
            return false;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Generates the final output for printing/file writing, and puts it into {@link #lines}.
     * @return If the output could be appended correctly.
     */
    private boolean initFileOutput() {
        String cfile = this.workPath + this.cFileField.getText();
        String sfile = this.workPath + this.asmFileField.getText();
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

    /**
     * Prints the file if ready, or does nothing otherwise.
     */
    private void onPrintPressed() {
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

    /**
     * Does nothing, since nothing needs to be done on student name change.
     */
    @SuppressWarnings("EmptyMethod")
    private void onStudentNameChanged() {
    }

    /**
     * Runs {@link #initFileOutput()} to get the output in place, then tries to write it to a file.
     */
    private void onFilePressed() {
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

    /**
     * Opens the file picker, then outputs the result into the given text field.
     * @param outputField The field to output the path onto.
     */
    private void openFilePicker(JTextField outputField) {
        int returnVal = this.fileChooser.showOpenDialog(this);
        if (returnVal == 0) {
            File file = this.fileChooser.getSelectedFile();
            String fileName = file.getName();
            String myPath = file.getPath();
            myPath = myPath.replace('\\', '/');
            int endIndex = myPath.lastIndexOf('/');
            this.workPath = myPath.substring(0, endIndex + 1);
            outputField.setText(fileName);
        }

    }
}
