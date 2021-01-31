package gui;

import java.awt.event.*;
import java.io.*;
import java.nio.file.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.*;

import simulator.Simulator;

// Klasa glavnog prozora aplikacije
// GUI je automatski izgenerisan u NetBeans IDE
public class MainWindow extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	private JButton browseButton;
    private JTextArea codeArea;
    public JTextArea defArea;
    public JTextArea duChainsArea;
    private JButton finishButton;
    private JTree folderTree;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JLabel jLabel5;
    private JLabel jLabel6;
    private JPanel jPanel1;
    private JPanel jPanel2;
    private JPanel jPanel3;
    private JScrollPane jScrollPane1;
    private JScrollPane jScrollPane2;
    private JScrollPane jScrollPane3;
    private JScrollPane jScrollPane4;
    private JScrollPane jScrollPane5;
    private JScrollPane jScrollPane6;
    private JTextArea lcsajArea;
    private JButton openCodeButton;
    private JButton resetButton;
    private JButton startButton;
    private JTabbedPane tabsPane;
    public JTextArea useArea;
    public JTextField varsArea1;
    private JTextField varsArea2;
	private JFileChooser chooser;
	
	public Simulator simulator;

    public MainWindow() {
        super("DU Simulator");
        initComponents();
        simulator = new Simulator();
    }
                  
    private void initComponents() {

        jScrollPane1 = new JScrollPane();
        codeArea = new JTextArea();
        tabsPane = new JTabbedPane();
        jPanel1 = new JPanel();
        jScrollPane3 = new JScrollPane();
        defArea = new JTextArea();
        jScrollPane4 = new JScrollPane();
        useArea = new JTextArea();
        jLabel4 = new JLabel();
        jLabel1 = new JLabel();
        jLabel2 = new JLabel();
        varsArea1 = new JTextField();
        jPanel2 = new JPanel();
        jScrollPane2 = new JScrollPane();
        duChainsArea = new JTextArea();
        jLabel3 = new JLabel();
        jLabel5 = new JLabel();
        varsArea2 = new JTextField();
        jPanel3 = new JPanel();
        jLabel6 = new JLabel();
        jScrollPane5 = new JScrollPane();
        lcsajArea = new JTextArea();
        jScrollPane6 = new JScrollPane();
        folderTree = new JTree();
        openCodeButton = new JButton();
        browseButton = new JButton();
        finishButton = new JButton();
        resetButton = new JButton();
        startButton = new JButton();
		chooser = new JFileChooser();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        codeArea.setColumns(20);
        codeArea.setRows(5);
        jScrollPane1 = new JScrollPane(codeArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        CodeInput tln = new CodeInput(codeArea);
        jScrollPane1.setRowHeaderView(tln);

        defArea.setColumns(20);
        defArea.setRows(5);
        defArea.setEnabled(false);
        defArea.setDisabledTextColor(new java.awt.Color(204, 0, 0));
        jScrollPane3.setViewportView(defArea);

        useArea.setColumns(20);
        useArea.setRows(5);
        useArea.setEnabled(false);
        useArea.setDisabledTextColor(new java.awt.Color(51, 51, 255));
        jScrollPane4.setViewportView(useArea);

        jLabel4.setText("Definicije:");

        jLabel1.setText("Upotrebe:");

        jLabel2.setText("Promenljive (razdvojene razmakom):");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane4)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(varsArea1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE)
                    .addComponent(jScrollPane4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(varsArea1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        tabsPane.addTab("Upotrebe i definicije", jPanel1);

        duChainsArea.setColumns(20);
        duChainsArea.setRows(5);
        duChainsArea.setEnabled(false);
        duChainsArea.setDisabledTextColor(new java.awt.Color(0, 102, 0));
        jScrollPane2.setViewportView(duChainsArea);

        jLabel3.setText("DU-lanci:");

        jLabel5.setText("Promenljive (razdvojene razmakom):");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addComponent(varsArea2)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel5))
                        .addGap(0, 198, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(varsArea2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        tabsPane.addTab("DU-lanci", jPanel2);

        jLabel6.setText("LCSAJ sekvence:");

        lcsajArea.setColumns(20);
        lcsajArea.setRows(5);
        lcsajArea.setEnabled(false);
        lcsajArea.setDisabledTextColor(new java.awt.Color(102, 0, 102));
        jScrollPane5.setViewportView(lcsajArea);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 373, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 319, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(35, Short.MAX_VALUE))
        );

        tabsPane.addTab("LCSAJ sekvence", jPanel3);

        jScrollPane6.setViewportView(folderTree);
        folderTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        DefaultMutableTreeNode top = new DefaultMutableTreeNode("Nothing to display.");
        folderTree.setModel(new DefaultTreeModel(top));
        ((DefaultTreeModel)folderTree.getModel()).reload();

        openCodeButton.setText("Učitaj kod");
        // Dugme za učitavanje koda dohvata selektovani fajl iz stabla i učitava ga u oblast za kod
        openCodeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) folderTree.getLastSelectedPathComponent();
			    if (node == null || node.getUserObject().equals("Nothing to display."))
			    	return;

			    FileInfo fileInfo = (FileInfo) node.getUserObject();
			    if (node.isLeaf()) {
					try {
						BufferedReader br = new BufferedReader(new FileReader(fileInfo.getFilePath().toString()));
						String code = "";
			    	    String line = br.readLine();

			    	    while (line != null) {
			    	        code += line + "\n";
			    	        line = br.readLine();
			    	    }
			    	    codeArea.setText(code);
			    	    br.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
			    } 
			}
        });
        
        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle("Otvori...");
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Java files", "java", "txt");
        chooser.setFileFilter(filter);

        browseButton.setText("Otvori...");
        // Dugme "Otvori" otvara file chooser za učitavanje foldera/fajlova
        browseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int returnVal = chooser.showOpenDialog(null);
			    if(returnVal == JFileChooser.APPROVE_OPTION) {
			    	fillTree(folderTree, chooser.getSelectedFile().getAbsolutePath());
			    } 
			}
        });

        finishButton.setText("Izvrši algoritam");
		// Dugme "Izvrši algoritam" izvrsava ceo algoritam
        finishButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				simulator = new Simulator();
				startButton.setText("Počni");
				simulator.analyseCode(codeArea.getText(), varsArea1.getText());
				defArea.setText(simulator.getDefinitions());
				useArea.setText(simulator.getUses());
				duChainsArea.setText(simulator.getDuChains());
				lcsajArea.setText(simulator.getLcsaj());
			}
        });

        resetButton.setText("Obriši");
        // Dugme "Obriši" briše sve tekuće rezultate i tekući kod
        resetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				simulator = new Simulator();
				codeArea.setText("");
				varsArea1.setText("");
				varsArea2.setText("");
				defArea.setText("");
				useArea.setText("");
				duChainsArea.setText("");
				lcsajArea.setText("");
				startButton.setText("Počni");
			}
        });

        startButton.setText("Počni");
        // Dugme "Počni" zapocinje step-by-step mod rada ili prelazi na sledeci korak
        startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// Ako je prvi put pritisnuto ovo dugme treba zapoceti step-by-step mod rada
				if (!simulator.isStepByStep()) {
					startButton.setText("Sledeći korak");
					simulator = new Simulator();
					simulator.startStepByStep();
					simulator.analyseCode(codeArea.getText(), varsArea1.getText());
					defArea.setText("");
					useArea.setText("");
					duChainsArea.setText("");
					lcsajArea.setText(simulator.getLcsaj());
				}
				// Prelazimo na sledeci korak simulacije
				boolean end = simulator.nextStep(defArea, useArea, duChainsArea);
				// Ako je simulacija zavrsena, resetujemo tekst na dugmetu
				if (end)
					startButton.setText("Počni");
			}
        });
		
		// Pošto imamo dva polja za unos promenljivih (u 2 taba) moramo da obezbedimo da ona uvek imaju isti sadržaj
        varsArea1.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				varsArea2.setText(varsArea1.getText());
			}
			@Override
			public void keyReleased(KeyEvent arg0) {
				varsArea2.setText(varsArea1.getText());
			}
			@Override
			public void keyTyped(KeyEvent arg0) {
				varsArea2.setText(varsArea1.getText());
			}
        });
        
        varsArea2.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				varsArea1.setText(varsArea2.getText());
			}
			@Override
			public void keyReleased(KeyEvent arg0) {
				varsArea1.setText(varsArea2.getText());
			}
			@Override
			public void keyTyped(KeyEvent arg0) {
				varsArea1.setText(varsArea2.getText());
			}
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 462, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(68, 68, 68)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(openCodeButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(browseButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tabsPane)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(startButton, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(finishButton, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(resetButton, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(35, 35, 35)
                                .addComponent(openCodeButton)
                                .addGap(18, 18, 18)
                                .addComponent(browseButton))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(tabsPane, javax.swing.GroupLayout.PREFERRED_SIZE, 430, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(finishButton)
                            .addComponent(resetButton)
                            .addComponent(startButton))
                        .addGap(0, 8, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
    }                     

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                MainWindow mainWindow = new MainWindow();
                mainWindow.setVisible(true);
                mainWindow.setResizable(false);
            }
        });
    }
	
	// Metoda za popunjavanje stabla sa fajlovima i folderima 
    public void fillTree(JTree tree, String path) {
    	String ROOT = path;
        FileVisitor<Path> fileProcessor = new TreeFiller(folderTree);
        try {
			Files.walkFileTree(Paths.get(ROOT), fileProcessor);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
                 
}
