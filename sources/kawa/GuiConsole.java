package kawa;

import gnu.expr.Language;
import gnu.mapping.Environment;
import gnu.mapping.OutPort;
import java.awt.BorderLayout;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import kawa.ReplDocument;

public class GuiConsole extends JFrame implements ActionListener, ReplDocument.DocumentCloseListener {
    private static String CLOSE = "Close";
    private static String EXIT = "Exit";
    private static String NEW = "New";
    private static String NEW_SHARED = "New (Shared)";
    private static String PURGE_MESSAGE = "Purge Buffer";
    static int window_number = 0;
    ReplDocument document;
    ReplPane pane;

    public static void main(String[] args) {
        repl.noConsole = false;
        int iArg = repl.processArgs(args, 0, args.length);
        repl.getLanguage();
        repl.setArgs(args, iArg);
        repl.checkInitFile();
        new GuiConsole();
    }

    public GuiConsole() {
        this(Language.getDefaultLanguage(), Environment.getCurrent(), false);
    }

    public GuiConsole(ReplDocument doc) {
        super("Kawa");
        init(doc);
    }

    /* access modifiers changed from: package-private */
    public void init(ReplDocument doc) {
        this.document = doc;
        this.document.addDocumentCloseListener(this);
        this.pane = new ReplPane(this.document);
        window_number++;
        setLayout(new BorderLayout(0, 0));
        add("Center", new JScrollPane(this.pane));
        setupMenus();
        setLocation(window_number * 100, window_number * 50);
        setSize(700, 500);
        setVisible(true);
    }

    public GuiConsole(Language language, Environment penvironment, boolean shared) {
        super("Kawa");
        repl.getLanguage();
        init(new ReplDocument(language, penvironment, shared));
    }

    public void closed(ReplDocument doc) {
        close();
    }

    /* access modifiers changed from: package-private */
    public void close() {
        this.document.removeDocumentCloseListener(this);
        dispose();
    }

    private void setupMenus() {
        WindowListener windowExitCmd = new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                GuiConsole.this.close();
            }
        };
        MenuBar menubar = new MenuBar();
        Menu fileMenu = new Menu("File");
        Menu utilitiesMenu = new Menu("Utilities");
        menubar.add(fileMenu);
        menubar.add(utilitiesMenu);
        MenuItem menuItem = new MenuItem(NEW);
        menuItem.addActionListener(this);
        fileMenu.add(menuItem);
        MenuItem menuItem2 = new MenuItem(NEW_SHARED);
        menuItem2.addActionListener(this);
        fileMenu.add(menuItem2);
        MenuItem menuItem3 = new MenuItem(CLOSE);
        menuItem3.addActionListener(this);
        fileMenu.add(menuItem3);
        MenuItem menuItem4 = new MenuItem(EXIT);
        menuItem4.addActionListener(this);
        addWindowListener(windowExitCmd);
        fileMenu.add(menuItem4);
        MenuItem menuItem5 = new MenuItem(PURGE_MESSAGE);
        menuItem5.addActionListener(this);
        utilitiesMenu.add(menuItem5);
        setMenuBar(menubar);
    }

    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd.equals(NEW)) {
            new GuiConsole(this.document.language, Environment.getGlobal(), false);
        } else if (cmd.equals(NEW_SHARED)) {
            new GuiConsole(this.document.language, this.document.environment, true);
        } else if (cmd.equals(EXIT)) {
            System.exit(0);
        } else if (cmd.equals(CLOSE)) {
            close();
        } else if (cmd.equals(PURGE_MESSAGE)) {
            this.pane.document.deleteOldText();
        } else {
            OutPort.outDefault().println("Unknown menu action: " + cmd);
        }
    }
}
