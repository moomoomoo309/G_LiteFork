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
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

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
    private final String lineSeparator = System.getProperty("line.separator");
    private final String workPathIsNullErrorMessage = "You need to compile or assemble something first!";
    private final String markExecutableErrorMessage = "Could not mark file \"%s\" as executable.";
    private final String InterruptedErrorMessage = "Command failed! Error message: \"%s\"";
    private final String IOErrorMessage = "File error! Message: \"%s\"";
    private final String compileFieldEmptyErrorMessage = "You need to pick a file to compile!";
    private final String assembleFieldEmptyErrorMessage = "You need to pick a file to assemble!";
    private final List<String> lines = new ArrayList<>();
    private JTextField cFileField;
    private JTextField runFileField;
    private JTextField asmFileField;
    private JTextArea processText;
    private JTextField studentName;
    private String workPath;
    private int[] pageBreaks;

    /**
     * Required for Swing to work correctly.
     */
    private G_Lite() {
        initComponents();
    }

    /**
     * Parses command line arguments and sets the look and feel of Swing.
     *
     * @param args Command line arguments.<br>
     *             The program looks for:
     *             <ol>
     *             <li>
     *             --runexecutable=(executablename)
     *             </li>
     *             <li>
     *             --compileexecutable=(compileexecutable)\
     *             </li>
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
                JFrame frame = new JFrame();
                JOptionPane.showMessageDialog(frame, "Initialization error", e.getMessage(), JOptionPane.ERROR_MESSAGE);
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
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

        cFileField = new JTextField();
        asmFileField = new JTextField();
        runFileField = new JTextField();

        JButton cLangChooser = new JButton();
        JButton assemblyLangChooser = new JButton();
        JButton exitButton = new JButton();
        JButton printButton = new JButton();
        JButton fileButton = new JButton();
        JButton cleanButton = new JButton();

        JPanel innerPanel = new JPanel();
        JScrollPane processOutput = new JScrollPane();
        processText = new JTextArea();
        studentName = new JTextField();
        JLabel studentNameLabel = new JLabel();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        mainPanel.setBorder(BorderFactory.createTitledBorder("CS252 UI Assistant (S16)"));

        compileButton.setText("Compile");
        compileButton.setToolTipText("Press this button to compile the C program. A file cout is created in the working directory.");
        compileButton.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        compileButton.addActionListener(evt -> onCompilePressed());

        assembleButton.setText("Assemble");
        assembleButton.setToolTipText("Press this button to assemble the ARM file. This will create an sout file in the working directory.");
        assembleButton.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        assembleButton.addActionListener(evt1 -> onAssemblePressed());

        linkButton.setText("Link");
        linkButton.setToolTipText("Press this button to create a runnable file. If a name is entered in the text box tat will be the name of the file, otherwise it is called runit.");
        linkButton.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        linkButton.addActionListener(evt1 -> onLinkPressed());

        runButton.setText("Run");
        runButton.setToolTipText("Press this button to run the file.");
        runButton.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        runButton.addActionListener(evt1 -> onRunPressed());

        cLangChooser.setBackground(new Color(255, 255, 255));
        cLangChooser.setText("C  language file");
        cLangChooser.setToolTipText("Press this button ot chose the C lanuguage file to compile");
        cLangChooser.setActionCommand("C_language_file");
        cLangChooser.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        cLangChooser.addActionListener(evt2 -> onCLangChooserPressed());

        assemblyLangChooser.setBackground(new Color(255, 255, 255));
        assemblyLangChooser.setText("Assembly Language file");
        assemblyLangChooser.setToolTipText("Press this butto to chose the ARM assembly language file to assemble.");
        assemblyLangChooser.setActionCommand("Assembly_lang_file");
        assemblyLangChooser.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        assemblyLangChooser.addActionListener(evt2 -> onAssemblyLangChooserPressed());

        exitButton.setText("Exit");
        exitButton.setToolTipText("This will exit the application.");
        exitButton.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        exitButton.addActionListener(evt -> onExitPressed());

        cleanButton.setText("Clean");
        cleanButton.setToolTipText("This will remove any leftover files, such as cout, sout, and runit.");
        cleanButton.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        cleanButton.addActionListener(evt -> onCleanPressed());

        printButton.setText("Print");
        printButton.setToolTipText("This button will print the contents of the text area to the left." + lineSeparator);
        printButton.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        printButton.addActionListener(actionListener -> onPrintPressed());

        fileButton.setText("File");
        fileButton.setToolTipText("This button will create a text file of the source files and execution");
        fileButton.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        fileButton.addActionListener(evt1 -> onFilePressed());

        GroupLayout innerLayout = new GroupLayout(innerPanel);
        innerLayout.setHorizontalGroup(innerLayout
                                               .createParallelGroup(Alignment.LEADING)
                                               .addGroup(innerLayout
                                                                 .createSequentialGroup()
                                                                 .addGroup(innerLayout
                                                                                   .createParallelGroup(Alignment.LEADING)
                                                                                   .addGroup(Alignment.TRAILING, innerLayout
                                                                                           .createSequentialGroup()
                                                                                           .addComponent(printButton)
                                                                                           .addGap(18, 18, 18)
                                                                                           .addComponent(fileButton, -1, -1, 32767))
                                                                                   .addGroup(Alignment.TRAILING, innerLayout
                                                                                           .createSequentialGroup()
                                                                                           .addComponent(cleanButton)
                                                                                           .addGap(0, 0, 32767)
                                                                                           .addComponent(exitButton)))
                                                                 .addContainerGap()));

        innerLayout.setVerticalGroup(innerLayout
                                             .createParallelGroup(Alignment.LEADING)
                                             .addGroup(innerLayout
                                                               .createSequentialGroup()
                                                               .addGap(20, 20, 20)
                                                               .addGroup(innerLayout
                                                                                 .createParallelGroup(Alignment.BASELINE)
                                                                                 .addComponent(printButton)
                                                                                 .addComponent(fileButton))
                                                               .addPreferredGap(ComponentPlacement.RELATED, 36, 32767)
                                                               .addGroup(innerLayout
                                                                                 .createParallelGroup(Alignment.BASELINE)
                                                                                 .addComponent(cleanButton)
                                                                                 .addComponent(exitButton))
                                                               .addContainerGap()));
        innerPanel.setLayout(innerLayout);

        processText.setColumns(20);
        processText.setRows(5);
        processText.setToolTipText("This text area will show the output of the compile, assemble, link and the result of the running of the  linked file.");

        processOutput.setViewportView(processText);

        studentName.addActionListener(evt -> onStudentNameChanged());
        studentNameLabel.setText("Student Name");

        GroupLayout mainPanelLayout = new GroupLayout(mainPanel);
        mainPanelLayout.setHorizontalGroup(mainPanelLayout
                                                   .createParallelGroup(Alignment.LEADING)
                                                   .addGroup(mainPanelLayout
                                                                     .createSequentialGroup()
                                                                     .addComponent(processOutput, -1, 420, 32767)
                                                                     .addGap(37, 37, 37)
                                                                     .addComponent(innerPanel, -2, -1, -2)
                                                                     .addGap(22, 22, 22))
                                                   .addGroup(mainPanelLayout
                                                                     .createSequentialGroup()
                                                                     .addGap(30, 30, 30)
                                                                     .addGroup(mainPanelLayout
                                                                                       .createParallelGroup(Alignment.LEADING)
                                                                                       .addComponent(runButton)
                                                                                       .addGroup(mainPanelLayout
                                                                                                         .createSequentialGroup()
                                                                                                         .addGroup(mainPanelLayout
                                                                                                                           .createParallelGroup(Alignment.TRAILING, false)
                                                                                                                           .addGroup(Alignment.LEADING, mainPanelLayout
                                                                                                                                   .createSequentialGroup()
                                                                                                                                   .addComponent(linkButton)
                                                                                                                                   .addPreferredGap(ComponentPlacement.RELATED)
                                                                                                                                   .addComponent(runFileField, -1, 172, 32767))
                                                                                                                           .addGroup(Alignment.LEADING, mainPanelLayout
                                                                                                                                   .createSequentialGroup()
                                                                                                                                   .addComponent(assembleButton)
                                                                                                                                   .addPreferredGap(ComponentPlacement.RELATED)
                                                                                                                                   .addComponent(asmFileField))
                                                                                                                           .addGroup(Alignment.LEADING, mainPanelLayout
                                                                                                                                   .createSequentialGroup()
                                                                                                                                   .addComponent(compileButton)
                                                                                                                                   .addPreferredGap(ComponentPlacement.RELATED)
                                                                                                                                   .addGroup(mainPanelLayout
                                                                                                                                                     .createParallelGroup(Alignment.LEADING, false)
                                                                                                                                                     .addComponent(studentName, -1, 172, 32767)
                                                                                                                                                     .addComponent(cFileField))))
                                                                                                         .addGap(18, 18, 18)
                                                                                                         .addGroup(mainPanelLayout
                                                                                                                           .createParallelGroup(Alignment.LEADING)
                                                                                                                           .addComponent(studentNameLabel)
                                                                                                                           .addComponent(cLangChooser)
                                                                                                                           .addComponent(assemblyLangChooser))))
                                                                     .addContainerGap(163, 32767)));
        mainPanelLayout.linkSize(0, assembleButton, compileButton, linkButton, runButton);
        mainPanelLayout.linkSize(0, assemblyLangChooser, cLangChooser);
        mainPanelLayout.setVerticalGroup(mainPanelLayout
                                                 .createParallelGroup(Alignment.LEADING)
                                                 .addGroup(mainPanelLayout
                                                                   .createSequentialGroup()
                                                                   .addContainerGap(50, 32767)
                                                                   .addGroup(mainPanelLayout
                                                                                     .createParallelGroup(Alignment.LEADING)
                                                                                     .addGroup(Alignment.TRAILING, mainPanelLayout
                                                                                             .createSequentialGroup()
                                                                                             .addGroup(mainPanelLayout
                                                                                                               .createParallelGroup(Alignment.LEADING, false)
                                                                                                               .addComponent(studentName, -2, -1, -2)
                                                                                                               .addGroup(mainPanelLayout
                                                                                                                                 .createSequentialGroup()
                                                                                                                                 .addGap(3, 3, 3)
                                                                                                                                 .addComponent(studentNameLabel, -1, -1, 32767)))
                                                                                             .addPreferredGap(ComponentPlacement.UNRELATED)
                                                                                             .addGroup(mainPanelLayout
                                                                                                               .createParallelGroup(Alignment.BASELINE)
                                                                                                               .addComponent(compileButton)
                                                                                                               .addComponent(cFileField, -2, -1, -2)
                                                                                                               .addComponent(cLangChooser))
                                                                                             .addPreferredGap(ComponentPlacement.RELATED)
                                                                                             .addGroup(mainPanelLayout
                                                                                                               .createParallelGroup(Alignment.LEADING)
                                                                                                               .addGroup(mainPanelLayout
                                                                                                                                 .createParallelGroup(Alignment.BASELINE)
                                                                                                                                 .addComponent(assembleButton)
                                                                                                                                 .addComponent(assemblyLangChooser))
                                                                                                               .addComponent(asmFileField, -2, -1, -2))
                                                                                             .addPreferredGap(ComponentPlacement.UNRELATED)
                                                                                             .addGroup(mainPanelLayout
                                                                                                               .createParallelGroup(Alignment.BASELINE)
                                                                                                               .addComponent(runFileField, -2, -1, -2)
                                                                                                               .addComponent(linkButton))
                                                                                             .addPreferredGap(ComponentPlacement.UNRELATED)
                                                                                             .addComponent(runButton)
                                                                                             .addGap(26, 26, 26)
                                                                                             .addComponent(processOutput, -2, 100, -2))
                                                                                     .addComponent(innerPanel, Alignment.TRAILING, -2, -1, -2))));

        mainPanel.setLayout(mainPanelLayout);

        GroupLayout windowLayout = new GroupLayout(getContentPane());
        getContentPane().setLayout(windowLayout);
        windowLayout.setHorizontalGroup(windowLayout
                                                .createParallelGroup(Alignment.LEADING)
                                                .addGroup(windowLayout
                                                                  .createSequentialGroup()
                                                                  .addContainerGap()
                                                                  .addComponent(mainPanel, -1, -1, 32767)
                                                                  .addContainerGap()));
        windowLayout.setVerticalGroup(windowLayout
                                              .createParallelGroup(Alignment.LEADING)
                                              .addGroup(windowLayout
                                                                .createSequentialGroup()
                                                                .addContainerGap()
                                                                .addComponent(mainPanel, -1, -1, 32767)
                                                                .addContainerGap()));

        mainPanel.getAccessibleContext().setAccessibleName("CS252 UI Assistant (S16)");
        setTitle("CS252 UI Assistant");
        pack();
    }

    /**
     * Prints the output in {@link #lines} onto a page.
     *
     * @param g         The {@link Graphics} backend used to draw the text
     * @param pf        How the page should be formatted.
     * @param pageIndex The page to print.
     * @return 0 if successful, 1 if not.
     */
    public int print(Graphics g, PageFormat pf, int pageIndex) {
        Font font = new Font(Font.MONOSPACED, Font.PLAIN, 12);
        FontMetrics metrics = g.getFontMetrics(font);
        int lineHeight = metrics.getHeight();
        int numPages;
        int start;
        if (pageBreaks == null) { //Initialize the printing vars.
            int linesPerPage = (int) (pf.getImageableHeight() / (double) lineHeight);
            numPages = (lines.size() - 1) / linesPerPage;
            pageBreaks = new int[numPages];

            for (start = 0; start < numPages; start++) {
                pageBreaks[start] = (start + 1) * linesPerPage;
            }
        }

        if (pageIndex > pageBreaks.length) {
            // You tried to print past the number of pages.
            return 1;
        } else {
            Graphics2D g2d = (Graphics2D) g;
            g2d.translate(pf.getImageableX(), pf.getImageableY());
            numPages = 0;
            start = pageIndex == 0 ? 0: pageBreaks[pageIndex - 1];
            int end = pageIndex == pageBreaks.length ? lines.size(): pageBreaks[pageIndex];

            for (int line = start; line < end; line++) {
                numPages += lineHeight;
                g.drawString(lines.get(line), 0, numPages);
            }

            return 0;
        }
    }

    /**
     * Writes to a temporary .sh/.bat file, puts command in it, executes it, and appends formatString with the exit code to {@link #lines}.
     *
     * @param filePath     The path to the temporary file.
     * @param command      The command to be put into the file and run.
     * @param formatString The string used by .format, passed the exit code of the result.
     * @return The exit code of the command, or -1 if the command could not be run.
     */
    private int writeAndExecuteCommand(String filePath, String command, String formatString) {
        if (filePath == null) {
            JOptionPane.showMessageDialog(this, workPathIsNullErrorMessage, "User Input Error", JOptionPane.ERROR_MESSAGE);
            return -1;
        }
        File tmpFile = new File(filePath);
        try {
            Files.write(tmpFile.toPath(), ((isWindows ? ("@echo off" + lineSeparator): "") + command).getBytes());
            if (!isWindows)
                if (!tmpFile.setExecutable(true))
                    JOptionPane.showMessageDialog(this, String.format(markExecutableErrorMessage, filePath), "File Permissions Error", JOptionPane.ERROR_MESSAGE);

            //Make a process out of the file written
            ProcessBuilder pb = new ProcessBuilder(filePath);
            pb.redirectErrorStream(true);
            Process process = pb.start();

            //Read the output of the command. (It's recommended to be buffered, so it is)
            BufferedReader cmdOutputBuffer = new BufferedReader(new InputStreamReader(process.getInputStream()));
            cmdOutputBuffer.lines().forEachOrdered(line -> processText.append(line + lineSeparator));

            //Grab the result of the program
            int result = process.waitFor();

            //Put the results into the "console", then clean up and return the exit code.
            if (formatString != null && !formatString.isEmpty())
                processText.append(String.format(formatString, result) + lineSeparator);
            process.destroy();
            Files.deleteIfExists(tmpFile.toPath());
            return result;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, String.format(IOErrorMessage, e.getMessage()), "File I/O error", JOptionPane.ERROR_MESSAGE);
        } catch (InterruptedException e) {
            JOptionPane.showMessageDialog(this, String.format(InterruptedErrorMessage, e.getMessage()), "Command execution error", JOptionPane.ERROR_MESSAGE);
        }
        return -1;
    }

    /**
     * Compiles the program, or does nothing if no file is in {@link #cFileField}.
     */
    private void onCompilePressed() {
        if (cFileField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, compileFieldEmptyErrorMessage, "User Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (workPath == null) {
            JOptionPane.showMessageDialog(this, workPathIsNullErrorMessage, "User Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String cfile = '"' + workPath + cFileField.getText() + '"';
        String outfile = '"' + workPath + "cout" + '"';
        String tmpFilePath = workPath + runfile;
        writeAndExecuteCommand(tmpFilePath, compileCommand + outfile + ' ' + cfile, "Compile result: %d");
    }

    /**
     * Exits the program.
     */
    private void onExitPressed() {
        System.exit(0);
    }

    /**
     * Cleans up sout, cout, and runit files that are left behind.
     */
    private void onCleanPressed() {
        String[] filesToClean = {"cout", "sout", runFileField.getText().isEmpty() ? "runit": runFileField.getText()};
        byte filesDeleted = 0;
        try {
            for (String filePath : filesToClean)
                filesDeleted += Files.deleteIfExists(new File(filePath).toPath()) ? 1: 0;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, String.format(IOErrorMessage, e.getMessage()), "Clean error", JOptionPane.ERROR_MESSAGE);
        }
        JOptionPane.showMessageDialog(this, filesDeleted > 0 ? (filesDeleted + " File(s) cleaned."): "No files needed to be cleaned.");
    }

    /**
     * Pops up the filepicker for the user pick to the file to compile.
     */
    private void onCLangChooserPressed() {
        openFilePicker(cFileField);
    }

    /**
     * Pops up the filepicker for the user pick to the file to assemble.
     */
    private void onAssemblyLangChooserPressed() {
        openFilePicker(asmFileField);
    }

    /**
     * Assembles the .s file, or does nothing if {@link #asmFileField} is empty.
     */
    private void onAssemblePressed() {
        if (asmFileField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, assembleFieldEmptyErrorMessage, "User Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (workPath == null) {
            JOptionPane.showMessageDialog(this, workPathIsNullErrorMessage, "User Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String sfile = '"' + workPath + asmFileField.getText() + '"';
        String outfile = '"' + workPath + "sout" + '"';
        String assembleCommand = compileCommand + outfile + ' ' + sfile;
        String tmpFilePath = workPath + runfile;
        writeAndExecuteCommand(tmpFilePath, assembleCommand, "Assemble Result: %d");
    }

    /**
     * Tries to link the files.
     */
    private void onLinkPressed() {
        if (workPath == null) {
            JOptionPane.showMessageDialog(this, workPathIsNullErrorMessage, "User Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String cfile = '"' + workPath + "cout" + '"';
        String sfile = '"' + workPath + "sout" + '"';
        String option = " -T armulator-ram-hosted.ld";
        String tmpFilePath = workPath + runfile;
        String fileToRun = runFileField.getText();
        String outfile;
        if (fileToRun.isEmpty()) {
            outfile = '"' + workPath + "runit" + '"';
        } else {
            outfile = '"' + workPath + fileToRun + '"';
        }
        String linkFlags = " -g -o ";
        String linkCommand = compileExecutable + linkFlags + outfile + ' ' + cfile + ' ' + sfile + ' ' + option;
        int result = writeAndExecuteCommand(tmpFilePath, linkCommand, "Link Result: %d");
        if (result == 0) {
            if (fileToRun.isEmpty()) {
                runFileField.setText("runit");
            } else {
                runFileField.setText(fileToRun);
            }
        }

    }

    /**
     * Tries to run the file.
     */
    private void onRunPressed() {
        if (workPath == null) {
            JOptionPane.showMessageDialog(this, workPathIsNullErrorMessage, "User Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String tmpFilePath = workPath + runfile;
        String fileToRun = runFileField.getText();
        String outfile = '"' + workPath + (fileToRun.equals("") ? "runit": fileToRun) + '"';
        writeAndExecuteCommand(tmpFilePath, runCommand + outfile, "");
    }

    /**
     * Converts tabs to 4 spaces.
     *
     * @param in The string to replace.
     * @return The string, but with all tabs replaced with 4 spaces.
     */
    private String tab2spaces(String in) {
        return in.replace("\t", "    ");
    }

    /**
     * Appends the contents of filePath to {@link #lines}.
     *
     * @param filePath The path of the file to read.
     * @return If the file could read correctly.
     */
    private boolean appendOutput(String filePath) {
        try {
            for (String line : Files.lines(new File(filePath).toPath()).toArray(String[]::new))
                lines.add(tab2spaces(line));
        } catch (FileNotFoundException | NoSuchFileException e) {
            JOptionPane.showMessageDialog(this, "Could not find file at \"" + filePath + "\". Were the C and S files generated correctly?", "File not found", JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "File read error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    /**
     * Generates the final output for printing/file writing, and puts it into {@link #lines}.
     *
     * @return If the output could be appended correctly.
     */
    private boolean initFileOutput() {
        String cfile = workPath + cFileField.getText();
        String sfile = workPath + asmFileField.getText();
        lines.add(studentName.getText());
        lines.add(lineSeparator);
        lines.add(processText.getText());
        lines.add(lineSeparator + " ******************" + lineSeparator);
        lines.add(sfile);
        if (!appendOutput(sfile))
            return false;
        lines.add(lineSeparator + "------------------" + lineSeparator);
        lines.add(cfile);
        return appendOutput(cfile);
    }

    /**
     * Prints the file if ready, or does nothing otherwise.
     */
    private void onPrintPressed() {
        if (workPath == null) {
            JOptionPane.showMessageDialog(this, workPathIsNullErrorMessage, "User Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!initFileOutput())
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
                } catch (PrinterException e) {
                    JOptionPane.showMessageDialog(this, "Printing error", e.getMessage(), JOptionPane.ERROR_MESSAGE);
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
        if (workPath == null) {
            JOptionPane.showMessageDialog(this, "You need to compile or assemble first!", "User Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String resultFile = workPath + "result.txt";
        initFileOutput();

        try {
            Files.write(new File(resultFile).toPath(), lines, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
            JOptionPane.showMessageDialog(this, "File written to " + workPath + "result.txt.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Trouble writing file: " + e, "File output error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Opens the file picker, then outputs the result into the given text field.
     *
     * @param outputField The field to output the path onto.
     */
    private void openFilePicker(JTextField outputField) {
        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == 0) {
            File file = fileChooser.getSelectedFile();
            String fileName = file.getName();
            String myPath = file.getPath();
            myPath = myPath.replace('\\', '/');
            int endIndex = myPath.lastIndexOf('/');
            workPath = myPath.substring(0, endIndex + 1);
            outputField.setText(fileName);
        }

    }
}
