//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.UIManager.LookAndFeelInfo;

public class G_Lite extends JFrame implements Printable {
    private JButton Assem_Lang_chooser;
    private JButton Assemble;
    private JTextField C_file;
    private JButton C_lang_chooser;
    private JButton Compile_button;
    private JButton Exit_button;
    private JButton Link_button;
    private JButton Print;
    private JButton Run_button;
    private JTextField Run_file;
    private JTextField S_file;
    private JButton file_print;
    private JLabel jLabel1;
    private JPanel jPanel1;
    private JPanel jPanel2;
    private JScrollPane process_output;
    private JTextArea process_text;
    private JTextField studentName;
    private JFileChooser fileChooser = new JFileChooser();
    private String fileName;
    private String workPath;
    private String C_lang = "C_language_file";
    private String A_lang = "Assembly_lang_file";
    private String fullcommand = "arm-none-eabi-gcc -g -c -o ";
    private String batfile = "cs.bat";
    private List<String> lines = new ArrayList();
    int[] pageBreaks;
    String spaces = "                                           ";
    int[] spc = new int[]{4, 3, 2, 1, 4, 3, 2, 1, 4, 3, 2, 1, 4, 3, 2, 1, 4, 3, 2, 1, 4, 3, 2, 1, 4, 3, 2, 1, 4, 3, 2, 1, 4, 3, 2, 1, 4, 3, 2, 1};

    public G_LiteUI() {
        this.initComponents();
    }

    private void initComponents() {
        this.jPanel1 = new JPanel();
        this.Compile_button = new JButton();
        this.Assemble = new JButton();
        this.Link_button = new JButton();
        this.Run_button = new JButton();
        this.C_file = new JTextField();
        this.S_file = new JTextField();
        this.Run_file = new JTextField();
        this.C_lang_chooser = new JButton();
        this.Assem_Lang_chooser = new JButton();
        this.jPanel2 = new JPanel();
        this.Exit_button = new JButton();
        this.Print = new JButton();
        this.file_print = new JButton();
        this.process_output = new JScrollPane();
        this.process_text = new JTextArea();
        this.studentName = new JTextField();
        this.jLabel1 = new JLabel();
        this.setDefaultCloseOperation(3);
        this.jPanel1.setBorder(BorderFactory.createTitledBorder("CS252 UI Assistant (S16)"));
        this.Compile_button.setText("Compile");
        this.Compile_button.setToolTipText("Press this button to compile the C program. A file cout is created in the working directory.");
        this.Compile_button.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        this.Compile_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                G_LiteUI.this.Compile_buttonActionPerformed(evt);
            }
        });
        this.Assemble.setText("Assemble");
        this.Assemble.setToolTipText("Press this button to assemble the ARM file. This will create an sout file in the working directory.");
        this.Assemble.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        this.Assemble.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                G_LiteUI.this.AssembleActionPerformed(evt);
            }
        });
        this.Link_button.setText("Link");
        this.Link_button.setToolTipText("Press this button to create a runnable file. If a name is entered in the text box tat will be the name of the file, otherwise it is called runit.");
        this.Link_button.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        this.Link_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                G_LiteUI.this.Link_buttonActionPerformed(evt);
            }
        });
        this.Run_button.setText("Run");
        this.Run_button.setToolTipText("Press this button to run the file.");
        this.Run_button.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        this.Run_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                G_LiteUI.this.Run_buttonActionPerformed(evt);
            }
        });
        this.C_lang_chooser.setBackground(new Color(255, 255, 255));
        this.C_lang_chooser.setText("C  language file");
        this.C_lang_chooser.setToolTipText("Press this button ot chose the C lanuguage file to compile");
        this.C_lang_chooser.setActionCommand("C_language_file");
        this.C_lang_chooser.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        this.C_lang_chooser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                G_LiteUI.this.C_lang_chooserActionPerformed(evt);
            }
        });
        this.Assem_Lang_chooser.setBackground(new Color(255, 255, 255));
        this.Assem_Lang_chooser.setText("Assembly Language file");
        this.Assem_Lang_chooser.setToolTipText("Press this butto to chose the ARM assembly language file to assemble.");
        this.Assem_Lang_chooser.setActionCommand("Assembly_lang_file");
        this.Assem_Lang_chooser.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        this.Assem_Lang_chooser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                G_LiteUI.this.Assem_Lang_chooserActionPerformed(evt);
            }
        });
        this.Exit_button.setText("Exit");
        this.Exit_button.setToolTipText("This will exit the application.");
        this.Exit_button.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        this.Exit_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                G_LiteUI.this.Exit_buttonActionPerformed(evt);
            }
        });
        this.Print.setText("Print");
        this.Print.setToolTipText("This button will print the contents of the text area to the left.\n");
        this.Print.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        this.Print.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                G_LiteUI.this.PrintActionPerformed(evt);
            }
        });
        this.file_print.setText("File");
        this.file_print.setToolTipText("This button will create a text file of the source files and execution");
        this.file_print.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        this.file_print.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                G_LiteUI.this.file_printActionPerformed(evt);
            }
        });
        GroupLayout jPanel2Layout = new GroupLayout(this.jPanel2);
        this.jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, jPanel2Layout.createSequentialGroup().addComponent(this.Print).addGap(18, 18, 18).addComponent(this.file_print, -1, -1, 32767)).addGroup(Alignment.TRAILING, jPanel2Layout.createSequentialGroup().addGap(0, 0, 32767).addComponent(this.Exit_button))).addContainerGap()));
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addGap(20, 20, 20).addGroup(jPanel2Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.Print).addComponent(this.file_print)).addPreferredGap(ComponentPlacement.RELATED, 36, 32767).addComponent(this.Exit_button).addContainerGap()));
        this.process_text.setColumns(20);
        this.process_text.setRows(5);
        this.process_text.setToolTipText("This text area will show the output of the compile, assemble, link and the result of the running of the  linked file.");
        this.process_output.setViewportView(this.process_text);
        this.studentName.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                G_LiteUI.this.studentNameActionPerformed(evt);
            }
        });
        this.jLabel1.setText("Student Name");
        GroupLayout jPanel1Layout = new GroupLayout(this.jPanel1);
        this.jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addComponent(this.process_output, -1, 420, 32767).addGap(37, 37, 37).addComponent(this.jPanel2, -2, -1, -2).addGap(22, 22, 22)).addGroup(jPanel1Layout.createSequentialGroup().addGap(30, 30, 30).addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addComponent(this.Run_button).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(Alignment.TRAILING, false).addGroup(Alignment.LEADING, jPanel1Layout.createSequentialGroup().addComponent(this.Link_button).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.Run_file, -1, 172, 32767)).addGroup(Alignment.LEADING, jPanel1Layout.createSequentialGroup().addComponent(this.Assemble).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.S_file)).addGroup(Alignment.LEADING, jPanel1Layout.createSequentialGroup().addComponent(this.Compile_button).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING, false).addComponent(this.studentName, -1, 172, 32767).addComponent(this.C_file)))).addGap(18, 18, 18).addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addComponent(this.jLabel1).addComponent(this.C_lang_chooser).addComponent(this.Assem_Lang_chooser)))).addContainerGap(163, 32767)));
        jPanel1Layout.linkSize(0, new Component[]{this.Assemble, this.Compile_button, this.Link_button, this.Run_button});
        jPanel1Layout.linkSize(0, new Component[]{this.Assem_Lang_chooser, this.C_lang_chooser});
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap(50, 32767).addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING, false).addComponent(this.studentName, -2, -1, -2).addGroup(jPanel1Layout.createSequentialGroup().addGap(3, 3, 3).addComponent(this.jLabel1, -1, -1, 32767))).addPreferredGap(ComponentPlacement.UNRELATED).addGroup(jPanel1Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.Compile_button).addComponent(this.C_file, -2, -1, -2).addComponent(this.C_lang_chooser)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel1Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.Assemble).addComponent(this.Assem_Lang_chooser)).addComponent(this.S_file, -2, -1, -2)).addPreferredGap(ComponentPlacement.UNRELATED).addGroup(jPanel1Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.Run_file, -2, -1, -2).addComponent(this.Link_button)).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.Run_button).addGap(26, 26, 26).addComponent(this.process_output, -2, 100, -2)).addComponent(this.jPanel2, Alignment.TRAILING, -2, -1, -2))));
        GroupLayout layout = new GroupLayout(this.getContentPane());
        this.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(this.jPanel1, -1, -1, 32767).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(this.jPanel1, -1, -1, 32767).addContainerGap()));
        this.jPanel1.getAccessibleContext().setAccessibleName("CS252 UI Assistant (S16)");
        this.pack();
    }

    public int print(Graphics g, PageFormat pf, int pageIndex) {
        Font font = new Font("Font.MONOSPACED", 0, 12);
        FontMetrics metrics = g.getFontMetrics(font);
        int lineHeight = metrics.getHeight();
        int y;
        int start;
        if (this.pageBreaks == null) {
            int linesPerPage = (int)(pf.getImageableHeight() / (double)lineHeight);
            y = (this.lines.size() - 1) / linesPerPage;
            this.pageBreaks = new int[y];

            for(start = 0; start < y; ++start) {
                this.pageBreaks[start] = (start + 1) * linesPerPage;
            }
        }

        if (pageIndex > this.pageBreaks.length) {
            return 1;
        } else {
            Graphics2D g2d = (Graphics2D)g;
            g2d.translate(pf.getImageableX(), pf.getImageableY());
            y = 0;
            start = pageIndex == 0 ? 0 : this.pageBreaks[pageIndex - 1];
            int end = pageIndex == this.pageBreaks.length ? this.lines.size() : this.pageBreaks[pageIndex];

            for(int line = start; line < end; ++line) {
                y += lineHeight;
                g.drawString((String)this.lines.get(line), 0, y);
            }

            return 0;
        }
    }

    private void Compile_buttonActionPerformed(ActionEvent evt) {
        String cfile = '"' + this.workPath + this.C_file.getText() + '"';
        String outfile = '"' + this.workPath + "cout" + '"';
        String cs252 = this.workPath + this.batfile;
        File tmpFile = new File(cs252);

        try {
            String Ccommand = this.fullcommand + outfile + " " + cfile;
            PrintWriter ostream = new PrintWriter(new FileWriter(tmpFile));
            ostream.println("@echo off");
            ostream.println(Ccommand);
            ostream.close();
            ProcessBuilder pb = new ProcessBuilder(new String[]{cs252});
            pb.redirectErrorStream(true);
            Process p = pb.start();
            InputStream inp = p.getInputStream();
            BufferedReader binp = new BufferedReader(new InputStreamReader(inp));

            for(String line = binp.readLine(); line != null; line = binp.readLine()) {
                this.process_text.append(line + "\n");
            }

            int result = p.waitFor();
            this.process_text.append("Compile Result: " + result + "\n");
            p.destroy();
            boolean var14 = tmpFile.delete();
        } catch (IOException var15) {
            System.out.println(var15.getMessage());
        } catch (InterruptedException var16) {
            System.out.println(var16.getMessage());
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
        String sfile = '"' + this.workPath + this.S_file.getText() + '"';
        String outfile = '"' + this.workPath + "sout" + '"';
        String cs252 = this.workPath + this.batfile;
        File tmpFile = new File(cs252);

        try {
            String Acommand = this.fullcommand + outfile + " " + sfile;
            PrintWriter ostream = new PrintWriter(new FileWriter(tmpFile));
            ostream.println("@echo off");
            ostream.println(Acommand);
            ostream.close();
            ProcessBuilder pb = new ProcessBuilder(new String[]{cs252});
            pb.redirectErrorStream(true);
            Process p = pb.start();
            InputStream inp = p.getInputStream();
            BufferedReader binp = new BufferedReader(new InputStreamReader(inp));

            for(String line = binp.readLine(); line != null; line = binp.readLine()) {
                this.process_text.append(line + "\n");
            }

            int result = p.waitFor();
            this.process_text.append("Assembly Result: " + result + "\n");
            p.destroy();
            boolean var14 = tmpFile.delete();
        } catch (IOException var15) {
            System.out.println(var15.getMessage());
        } catch (InterruptedException var16) {
            System.out.println(var16.getMessage());
        }

    }

    private void Link_buttonActionPerformed(ActionEvent evt) {
        String cfile = '"' + this.workPath + "cout" + '"';
        String sfile = '"' + this.workPath + "sout" + '"';
        String option = " -T armulator-ram-hosted.ld";
        String cs252 = this.workPath + this.batfile;
        File tmpFile = new File(cs252);
        String rlfile = this.Run_file.getText();
        String outfile;
        if (rlfile.equals("")) {
            outfile = '"' + this.workPath + "runit" + '"';
        } else {
            outfile = '"' + this.workPath + rlfile + '"';
        }

        try {
            String Lcommand = "arm-none-eabi-gcc -g -o " + outfile + " " + cfile;
            Lcommand = Lcommand + " " + sfile + " " + option;
            PrintWriter ostream = new PrintWriter(new FileWriter(tmpFile));
            ostream.println("@echo off");
            ostream.println(Lcommand);
            ostream.close();
            ProcessBuilder pb = new ProcessBuilder(new String[]{cs252});
            pb.redirectErrorStream(true);
            Process p = pb.start();
            InputStream inp = p.getInputStream();
            BufferedReader binp = new BufferedReader(new InputStreamReader(inp));

            for(String line = binp.readLine(); line != null; line = binp.readLine()) {
                this.process_text.append(line + "\n");
            }

            int result = p.waitFor();
            this.process_text.append("Link Result: " + result + "\n");
            if (result == 0) {
                if (rlfile.equals("")) {
                    this.Run_file.setText("runit");
                } else {
                    this.Run_file.setText(rlfile);
                }
            }

            p.destroy();
            boolean var17 = tmpFile.delete();
        } catch (IOException var18) {
            System.out.println(var18.getMessage());
        } catch (InterruptedException var19) {
            System.out.println(var19.getMessage());
        }

    }

    private void Run_buttonActionPerformed(ActionEvent evt) {
        String cs252 = this.workPath + this.batfile;
        File tmpFile = new File(cs252);
        String rfile = this.Run_file.getText();
        String outfile;
        if (rfile.equals("")) {
            outfile = '"' + this.workPath + "runit" + '"';
        } else {
            outfile = '"' + this.workPath + rfile + '"';
        }

        try {
            String Rcommand = "arm-none-eabi-run " + outfile;
            PrintWriter ostream = new PrintWriter(new FileWriter(cs252));
            ostream.println("@echo off");
            ostream.println(Rcommand);
            ostream.close();
            ProcessBuilder pb = new ProcessBuilder(new String[]{cs252});
            pb.redirectErrorStream(true);
            Process p = pb.start();
            InputStream inp = p.getInputStream();
            BufferedReader binp = new BufferedReader(new InputStreamReader(inp));
            this.process_text.setText("");

            for(String line = binp.readLine(); line != null; line = binp.readLine()) {
                this.process_text.append(line + "\n");
            }

            int result = p.waitFor();
            p.destroy();
            boolean var14 = tmpFile.delete();
        } catch (IOException var15) {
            System.out.println(var15.getMessage());
        } catch (InterruptedException var16) {
            System.out.println(var16.getMessage());
        }

    }

    private String tab2spaces(String in) {
        StringBuilder expand = new StringBuilder();

        for(int index = 0; index < in.length(); ++index) {
            if (in.charAt(index) == '\t') {
                expand.append(this.spaces.substring(index, this.spc[index] + index));
            } else {
                expand.append(in.charAt(index));
            }
        }

        return expand.toString();
    }

    private void initLines() {
        String cfile = this.workPath + this.C_file.getText();
        String sfile = this.workPath + this.S_file.getText();
        BufferedReader instream = null;
        this.lines.add(this.studentName.getText());
        Boolean didit = this.lines.add("\n");
        this.lines.add(this.process_text.getText());
        didit = this.lines.add("\n ******************\n");
        didit = this.lines.add(sfile);

        String linein;
        try {
            for(instream = new BufferedReader(new FileReader(sfile)); (linein = instream.readLine()) != null; didit = this.lines.add(this.tab2spaces(linein))) {
                ;
            }

            instream.close();
        } catch (IOException var8) {
            System.out.println(var8.getMessage());
        }

        didit = this.lines.add("\n------------------\n");
        didit = this.lines.add(cfile);

        try {
            for(instream = new BufferedReader(new FileReader(cfile)); (linein = instream.readLine()) != null; didit = this.lines.add(this.tab2spaces(linein))) {
                ;
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
            JOptionPane.showMessageDialog(this, "No Default Printer", "Printer Error", 0);
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

    private void studentNameActionPerformed(ActionEvent evt) {
    }

    private void file_printActionPerformed(ActionEvent evt) {
        PrintWriter result_out = null;
        String result_file = this.workPath + "result.txt";
        this.initLines();

        try {
            result_out = new PrintWriter(new FileWriter(result_file));

            for(int i = 0; i < this.lines.size(); ++i) {
                result_out.println((String)this.lines.get(i));
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
            int endIndex = myPath.lastIndexOf("\\");
            this.workPath = myPath.substring(0, endIndex + 1);
            if (evt.getActionCommand().equalsIgnoreCase(this.C_lang)) {
                this.C_file.setText(this.fileName);
            }

            if (evt.getActionCommand().equalsIgnoreCase(this.A_lang)) {
                this.S_file.setText(this.fileName);
            }
        }

    }

    public static void main(String[] args) {
        try {
            LookAndFeelInfo[] arr$ = UIManager.getInstalledLookAndFeels();
            int len$ = arr$.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                LookAndFeelInfo info = arr$[i$];
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException var5) {
            Logger.getLogger(G_LiteUI.class.getName()).log(Level.SEVERE, (String)null, var5);
        } catch (InstantiationException var6) {
            Logger.getLogger(G_LiteUI.class.getName()).log(Level.SEVERE, (String)null, var6);
        } catch (IllegalAccessException var7) {
            Logger.getLogger(G_LiteUI.class.getName()).log(Level.SEVERE, (String)null, var7);
        } catch (UnsupportedLookAndFeelException var8) {
            Logger.getLogger(G_LiteUI.class.getName()).log(Level.SEVERE, (String)null, var8);
        }

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                (new G_LiteUI()).setVisible(true);
            }
        });
    }
}
