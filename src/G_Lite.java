//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.PrintService;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

@SuppressWarnings({"HardcodedFileSeparator", "unused"})
class G_Lite extends JFrame implements Printable {
    private JButton AssemLangChooser;
    private JButton Assemble;
    private JTextField CFile;
    private JButton CLangChooser;
    private JButton CompileButton;
    private JButton ExitButton;
    private JButton LinkButton;
    private JButton Print;
    private JButton RunButton;
    private JTextField RunFile;
    private JTextField SFile;
    private JButton filePrint;
    private JLabel jLabel1;
    private JPanel jPanel1;
    private JPanel jPanel2;
    private JScrollPane processOutput;
    private JTextArea processText;
    private JTextField studentName;
    private final JFileChooser fileChooser = new JFileChooser();
    private String fileName;
    private String workPath = "./";
    private final String C_lang = "C_language_file";
    private final String A_lang = "Assembly_lang_file";
    private static String compileExecutable = "arm-none-eabi-gcc";
    private final String compileFlags = " -g -c -o ";
    private final String linkFlags = " -g -o ";
    private final String compileCommand = compileExecutable + compileFlags;
    private final boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("Windows");
    private static String runExecutable = "arm-none-eabi-run";
    private final String runFlags = " ";
    private final String runCommand = runExecutable + runFlags;
    private final String windowsFileExtension = "bat";
    private final String nixFileExtension = "sh";
    private final String runfileName = "cs";
    private final String runfile = runfileName + '.' + (isWindows ? windowsFileExtension: nixFileExtension);
    private final List<String> lines = new ArrayList<>();
    private int[] pageBreaks;
    private final String spaces = String.join("", Collections.nCopies(43, " "));
    private final int[] spc = new int[]{4, 3, 2, 1, 4, 3, 2, 1, 4, 3, 2, 1, 4, 3, 2, 1, 4, 3, 2, 1, 4, 3, 2, 1, 4, 3, 2, 1, 4, 3, 2, 1, 4, 3, 2, 1, 4, 3, 2, 1};

    private G_Lite() {
        this.initComponents();
    }

    private void initComponents() {
        this.jPanel1 = new JPanel();
        this.CompileButton = new JButton();
        this.Assemble = new JButton();
        this.LinkButton = new JButton();
        this.RunButton = new JButton();
        this.CFile = new JTextField();
        this.SFile = new JTextField();
        this.RunFile = new JTextField();
        this.CLangChooser = new JButton();
        this.AssemLangChooser = new JButton();
        this.jPanel2 = new JPanel();
        this.ExitButton = new JButton();
        this.Print = new JButton();
        this.filePrint = new JButton();
        this.processOutput = new JScrollPane();
        this.processText = new JTextArea();
        this.studentName = new JTextField();
        this.jLabel1 = new JLabel();
        this.setDefaultCloseOperation(3);
        this.jPanel1.setBorder(BorderFactory.createTitledBorder("CS252 UI Assistant (S16)"));
        this.CompileButton.setText("Compile");
        this.CompileButton.setToolTipText("Press this button to compile the C program. A file cout is created in the working directory.");
        this.CompileButton.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        this.CompileButton.addActionListener(G_Lite.this::Compile_buttonActionPerformed);
        this.Assemble.setText("Assemble");
        this.Assemble.setToolTipText("Press this button to assemble the ARM file. This will create an sout file in the working directory.");
        this.Assemble.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        this.Assemble.addActionListener(G_Lite.this::AssembleActionPerformed);
        this.LinkButton.setText("Link");
        this.LinkButton.setToolTipText("Press this button to create a runnable file. If a name is entered in the text box tat will be the name of the file, otherwise it is called runit.");
        this.LinkButton.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        this.LinkButton.addActionListener(G_Lite.this::Link_buttonActionPerformed);
        this.RunButton.setText("Run");
        this.RunButton.setToolTipText("Press this button to run the file.");
        this.RunButton.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        this.RunButton.addActionListener(G_Lite.this::Run_buttonActionPerformed);
        this.CLangChooser.setBackground(new Color(255, 255, 255));
        this.CLangChooser.setText("C  language file");
        this.CLangChooser.setToolTipText("Press this button ot chose the C lanuguage file to compile");
        this.CLangChooser.setActionCommand("C_language_file");
        this.CLangChooser.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        this.CLangChooser.addActionListener(G_Lite.this::C_lang_chooserActionPerformed);
        this.AssemLangChooser.setBackground(new Color(255, 255, 255));
        this.AssemLangChooser.setText("Assembly Language file");
        this.AssemLangChooser.setToolTipText("Press this butto to chose the ARM assembly language file to assemble.");
        this.AssemLangChooser.setActionCommand("Assembly_lang_file");
        this.AssemLangChooser.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        this.AssemLangChooser.addActionListener(G_Lite.this::Assem_Lang_chooserActionPerformed);
        this.ExitButton.setText("Exit");
        this.ExitButton.setToolTipText("This will exit the application.");
        this.ExitButton.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        this.ExitButton.addActionListener(G_Lite.this::Exit_buttonActionPerformed);
        this.Print.setText("Print");
        this.Print.setToolTipText("This button will print the contents of the text area to the left." + System.getProperty("line.separator"));
        this.Print.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        this.Print.addActionListener(G_Lite.this::PrintActionPerformed);
        this.filePrint.setText("File");
        this.filePrint.setToolTipText("This button will create a text file of the source files and execution");
        this.filePrint.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        this.filePrint.addActionListener(G_Lite.this::file_printActionPerformed);
        GroupLayout jPanel2Layout = new GroupLayout(this.jPanel2);
        this.jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, jPanel2Layout.createSequentialGroup().addComponent(this.Print).addGap(18, 18, 18).addComponent(this.filePrint, -1, -1, 32767)).addGroup(Alignment.TRAILING, jPanel2Layout.createSequentialGroup().addGap(0, 0, 32767).addComponent(this.ExitButton))).addContainerGap()));
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addGap(20, 20, 20).addGroup(jPanel2Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.Print).addComponent(this.filePrint)).addPreferredGap(ComponentPlacement.RELATED, 36, 32767).addComponent(this.ExitButton).addContainerGap()));
        this.processText.setColumns(20);
        this.processText.setRows(5);
        this.processText.setToolTipText("This text area will show the output of the compile, assemble, link and the result of the running of the  linked file.");
        this.processOutput.setViewportView(this.processText);
        this.studentName.addActionListener(G_Lite.this::studentNameActionPerformed);
        this.jLabel1.setText("Student Name");
        GroupLayout jPanel1Layout = new GroupLayout(this.jPanel1);
        this.jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addComponent(this.processOutput, -1, 420, 32767).addGap(37, 37, 37).addComponent(this.jPanel2, -2, -1, -2).addGap(22, 22, 22)).addGroup(jPanel1Layout.createSequentialGroup().addGap(30, 30, 30).addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addComponent(this.RunButton).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(Alignment.TRAILING, false).addGroup(Alignment.LEADING, jPanel1Layout.createSequentialGroup().addComponent(this.LinkButton).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.RunFile, -1, 172, 32767)).addGroup(Alignment.LEADING, jPanel1Layout.createSequentialGroup().addComponent(this.Assemble).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.SFile)).addGroup(Alignment.LEADING, jPanel1Layout.createSequentialGroup().addComponent(this.CompileButton).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING, false).addComponent(this.studentName, -1, 172, 32767).addComponent(this.CFile)))).addGap(18, 18, 18).addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addComponent(this.jLabel1).addComponent(this.CLangChooser).addComponent(this.AssemLangChooser)))).addContainerGap(163, 32767)));
        jPanel1Layout.linkSize(0, this.Assemble, this.CompileButton, this.LinkButton, this.RunButton);
        jPanel1Layout.linkSize(0, this.AssemLangChooser, this.CLangChooser);
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap(50, 32767).addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING, false).addComponent(this.studentName, -2, -1, -2).addGroup(jPanel1Layout.createSequentialGroup().addGap(3, 3, 3).addComponent(this.jLabel1, -1, -1, 32767))).addPreferredGap(ComponentPlacement.UNRELATED).addGroup(jPanel1Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.CompileButton).addComponent(this.CFile, -2, -1, -2).addComponent(this.CLangChooser)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel1Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.Assemble).addComponent(this.AssemLangChooser)).addComponent(this.SFile, -2, -1, -2)).addPreferredGap(ComponentPlacement.UNRELATED).addGroup(jPanel1Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.RunFile, -2, -1, -2).addComponent(this.LinkButton)).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.RunButton).addGap(26, 26, 26).addComponent(this.processOutput, -2, 100, -2)).addComponent(this.jPanel2, Alignment.TRAILING, -2, -1, -2))));
        GroupLayout layout = new GroupLayout(this.getContentPane());
        this.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(this.jPanel1, -1, -1, 32767).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(this.jPanel1, -1, -1, 32767).addContainerGap()));
        this.jPanel1.getAccessibleContext().setAccessibleName("CS252 UI Assistant (S16)");
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

            for (int line = start; line < end; ++line) {
                y += lineHeight;
                g.drawString(this.lines.get(line), 0, y);
            }

            return 0;
        }
    }

    private void Compile_buttonActionPerformed(ActionEvent evt) {
        if (this.CFile.getText().isEmpty())
            return;
        String cfile = '"' + this.workPath + this.CFile.getText() + '"';
        String outfile = '"' + this.workPath + "cout" + '"';
        String cs252 = this.workPath + this.runfile;
        File tmpFile = new File(cs252);

        try {
            String Ccommand = this.compileCommand + outfile + ' ' + cfile;
            PrintWriter ostream = new PrintWriter(new FileWriter(tmpFile));
            if (isWindows)
                ostream.println("@echo off");
            ostream.println(Ccommand);
            ostream.close();
            if (!isWindows)
                if (!tmpFile.setExecutable(true))
                    Logger.getLogger(G_Lite.class.getName()).log(Level.SEVERE, null, "Could not mark file \"" + cs252 + "\" as executable.");
            ProcessBuilder pb = new ProcessBuilder(cs252);
            pb.redirectErrorStream(true);
            Process p = pb.start();
            InputStream inp = p.getInputStream();
            BufferedReader binp = new BufferedReader(new InputStreamReader(inp));

            for (String line = binp.readLine(); line != null; line = binp.readLine()) {
                this.processText.append(line + System.getProperty("line.separator"));
            }

            int result = p.waitFor();
            this.processText.append("Compile Result: " + result + System.getProperty("line.separator"));
            p.destroy();
            @SuppressWarnings("unused")
            boolean var14 = tmpFile.delete();
        } catch (IOException | InterruptedException var15) {
            System.out.println(var15.getMessage());
        }

    }

    private void Exit_buttonActionPerformed(ActionEvent evt) {
        System.exit(0);
    }

    private void C_lang_chooserActionPerformed(ActionEvent evt) {
        this.OpenActionPerformed(evt);
    }

    private void Assem_Lang_chooserActionPerformed(ActionEvent evt) {
        this.OpenActionPerformed(evt);
    }

    private void AssembleActionPerformed(ActionEvent evt) {
        if (this.SFile.getText().isEmpty())
            return;
        String sfile = '"' + this.workPath + this.SFile.getText() + '"';
        String outfile = '"' + this.workPath + "sout" + '"';
        String cs252 = this.workPath + this.runfile;
        File tmpFile = new File(cs252);

        try {
            String Acommand = this.compileCommand + outfile + ' ' + sfile;
            PrintWriter ostream = new PrintWriter(new FileWriter(tmpFile));
            if (isWindows)
                ostream.println("@echo off");
            ostream.println(Acommand);
            ostream.close();
            if (!isWindows)
                if (!tmpFile.setExecutable(true))
                    Logger.getLogger(G_Lite.class.getName()).log(Level.SEVERE, null, "Could not mark file \"" + cs252 + "\" as executable.");
            ProcessBuilder pb = new ProcessBuilder(cs252);
            pb.redirectErrorStream(true);
            Process p = pb.start();
            InputStream inp = p.getInputStream();
            BufferedReader binp = new BufferedReader(new InputStreamReader(inp));

            for (String line = binp.readLine(); line != null; line = binp.readLine()) {
                this.processText.append(line + System.getProperty("line.separator"));
            }

            int result = p.waitFor();
            this.processText.append("Assembly Result: " + result + System.getProperty("line.separator"));
            p.destroy();
            @SuppressWarnings("unused")
            boolean var14 = tmpFile.delete();
        } catch (IOException | InterruptedException var15) {
            System.out.println(var15.getMessage());
        }

    }

    private void Link_buttonActionPerformed(ActionEvent evt) {
        String cfile = '"' + this.workPath + "cout" + '"';
        String sfile = '"' + this.workPath + "sout" + '"';
        String option = " -T armulator-ram-hosted.ld";
        String cs252 = this.workPath + this.runfile;
        File tmpFile = new File(cs252);
        String rlfile = this.RunFile.getText();
        String outfile;
        if (rlfile.isEmpty()) {
            outfile = '"' + this.workPath + "runit" + '"';
        } else {
            outfile = '"' + this.workPath + rlfile + '"';
        }

        try {
            String Lcommand = compileExecutable + linkFlags + outfile + ' ' + cfile;
            Lcommand = Lcommand + ' ' + sfile + ' ' + option;
            PrintWriter ostream = new PrintWriter(new FileWriter(tmpFile));
            if (isWindows)
                ostream.println("@echo off");
            ostream.println(Lcommand);
            ostream.close();
            if (!isWindows)
                if (!tmpFile.setExecutable(true))
                    Logger.getLogger(G_Lite.class.getName()).log(Level.SEVERE, null, "Could not mark file \"" + cs252 + "\" as executable.");
            ProcessBuilder pb = new ProcessBuilder(cs252);
            pb.redirectErrorStream(true);
            Process p = pb.start();
            InputStream inp = p.getInputStream();
            BufferedReader binp = new BufferedReader(new InputStreamReader(inp));

            for (String line = binp.readLine(); line != null; line = binp.readLine()) {
                this.processText.append(line + System.getProperty("line.separator"));
            }

            int result = p.waitFor();
            this.processText.append("Link Result: " + result + System.getProperty("line.separator"));
            if (result == 0) {
                if (rlfile.equals("")) {
                    this.RunFile.setText("runit");
                } else {
                    this.RunFile.setText(rlfile);
                }
            }

            p.destroy();
            boolean var17 = tmpFile.delete();
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }

    }

    private void Run_buttonActionPerformed(ActionEvent evt) {
        String cs252 = this.workPath + this.runfile;
        File tmpFile = new File(cs252);
        String rfile = this.RunFile.getText();
        String outfile;
        if (rfile.equals("")) {
            outfile = '"' + this.workPath + "runit" + '"';
        } else {
            outfile = '"' + this.workPath + rfile + '"';
        }

        try {
            String Rcommand = runCommand + outfile;
            PrintWriter ostream = new PrintWriter(new FileWriter(cs252));
            if (isWindows)
                ostream.println("@echo off");
            ostream.println(Rcommand);
            ostream.close();
            if (!isWindows)
                if (!tmpFile.setExecutable(true))
                    Logger.getLogger(G_Lite.class.getName()).log(Level.SEVERE, null, "Could not mark file \"" + cs252 + "\" as executable.");
            ProcessBuilder pb = new ProcessBuilder(cs252);
            pb.redirectErrorStream(true);
            Process p = pb.start();
            InputStream inp = p.getInputStream();
            BufferedReader binp = new BufferedReader(new InputStreamReader(inp));
            this.processText.setText("");

            for (String line = binp.readLine(); line != null; line = binp.readLine()) {
                this.processText.append(line + System.getProperty("line.separator"));
            }

            int result = p.waitFor();
            p.destroy();
            @SuppressWarnings("unused")
            boolean var14 = tmpFile.delete();
        } catch (IOException | InterruptedException var15) {
            System.out.println(var15.getMessage());
        }

    }

    private String tab2spaces(String in) {
        StringBuilder expand = new StringBuilder();

        for (int index = 0; index < in.length(); ++index) {
            if (in.charAt(index) == '\t') {
                expand.append(this.spaces.substring(index, this.spc[index] + index));
            } else {
                expand.append(in.charAt(index));
            }
        }

        return expand.toString();
    }

    private void initLines() {
        String cfile = this.workPath + this.CFile.getText();
        String sfile = this.workPath + this.SFile.getText();
        BufferedReader instream;
        this.lines.add(this.studentName.getText());
        this.lines.add(System.getProperty("line.separator"));
        this.lines.add(this.processText.getText());
        this.lines.add(System.getProperty("line.separator") + " ******************" + System.getProperty("line.separator"));
        this.lines.add(sfile);

        String linein;
        try {
            //noinspection StatementWithEmptyBody
            for (instream = new BufferedReader(new FileReader(sfile)); (linein = instream.readLine()) != null; this.lines.add(this.tab2spaces(linein))) {
            }

            instream.close();
        } catch (IOException var8) {
            System.out.println(var8.getMessage());
        }

        this.lines.add(System.getProperty("line.separator") + "------------------" + System.getProperty("line.separator"));
        this.lines.add(cfile);

        try {
            //noinspection StatementWithEmptyBody
            for (instream = new BufferedReader(new FileReader(cfile)); (linein = instream.readLine()) != null; this.lines.add(this.tab2spaces(linein))) {
            }

            instream.close();
        } catch (IOException var7) {
            System.out.println(var7.getMessage());
        }

    }

    private void PrintActionPerformed(ActionEvent evt) {
        this.initLines();
        PrinterJob job = PrinterJob.getPrinterJob();
        PrintService printer = job.getPrintService();
        if (printer == null) {
            JOptionPane.showMessageDialog(this, "No Default Printer", "Printer Error", JOptionPane.ERROR_MESSAGE);
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
    private void studentNameActionPerformed(ActionEvent evt) {
    }

    private void file_printActionPerformed(ActionEvent evt) {
        PrintWriter result_out = null;
        String result_file = this.workPath + "result.txt";
        this.initLines();

        try {
            result_out = new PrintWriter(new FileWriter(result_file));

            for (String line : this.lines) {
                result_out.println(line);
            }
        } catch (IOException var8) {
            System.err.println("Trouble writing file: " + var8);
        } finally {
            if (result_out != null) {
                result_out.close();
            }

        }

    }

    private void OpenActionPerformed(ActionEvent evt) {
        int returnVal = this.fileChooser.showOpenDialog(this);
        if (returnVal == 0) {
            File file = this.fileChooser.getSelectedFile();
            this.fileName = file.getName();
            String myPath = file.getPath();
            myPath = myPath.replace('\\', '/');
            int endIndex = myPath.lastIndexOf('/');
            this.workPath = myPath.substring(0, endIndex + 1);
            if (evt.getActionCommand().equalsIgnoreCase(this.C_lang)) {
                this.CFile.setText(this.fileName);
            }

            if (evt.getActionCommand().equalsIgnoreCase(this.A_lang)) {
                this.SFile.setText(this.fileName);
            }
        }

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
}
