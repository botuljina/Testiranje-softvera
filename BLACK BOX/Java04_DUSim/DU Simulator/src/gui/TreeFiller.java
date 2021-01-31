package gui;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

// Klasa koja popunjava stablo sa fajlovima i folderima
class TreeFiller extends SimpleFileVisitor<Path> {
	
	private JTree tree;
	private DefaultMutableTreeNode currentNode;
	
    public TreeFiller(JTree tree) {
		super();
		this.tree = tree;
		currentNode = null;
	}

	public JTree getTree() {
		return tree;
	}
	
	private String getFileExtension(File file) {
	    String name = file.getName();
	    try {
	        return name.substring(name.lastIndexOf(".") + 1);
	    } catch (Exception e) {
	        return "";
	    }
	}
	
	// Metoda koja posećuje jedan fajl
	@Override 
	public FileVisitResult visitFile(Path aFile, BasicFileAttributes aAttrs) throws IOException {
		// Ignoriše fajlove koji nisu .java ili .txt
		String ext = getFileExtension(aFile.toFile());
		if (!(ext.equals("java") || ext.equals("txt")))
			return FileVisitResult.CONTINUE;
		
		// Ako još uvek nemamo koreni čvor ovde se stvara
		if (currentNode == null) {
			currentNode = new DefaultMutableTreeNode(new FileInfo(aFile.getFileName().toString(), aFile, aFile.getParent()));
			tree.setModel(new DefaultTreeModel(currentNode));
			((DefaultTreeModel)tree.getModel()).reload();
		}
		else {
			// Vraćamo se do čvora (foldera) u kome se nalazi tekući fajl i dodajemo ga u stablo
			FileInfo fileInfo = (FileInfo) currentNode.getUserObject();
			while (!fileInfo.getFilePath().equals(aFile.getParent())) {
				currentNode = (DefaultMutableTreeNode) currentNode.getParent();
				fileInfo = (FileInfo) currentNode.getUserObject();
			}
			DefaultMutableTreeNode newChild = new DefaultMutableTreeNode(new FileInfo(aFile.getFileName().toString(), aFile, aFile.getParent()));
			currentNode.add(newChild);
		}
      
		return FileVisitResult.CONTINUE;
    }
    
	// Metoda koja posećuje jedan folder
    @Override  
    public FileVisitResult preVisitDirectory(Path aDir, BasicFileAttributes aAttrs) throws IOException {
    	// Ako još uvek nemamo koreni čvor ovde se stvara
    	if (currentNode == null) {
    		currentNode = new DefaultMutableTreeNode(new FileInfo(aDir.getFileName().toString(), aDir, aDir.getParent()));
    		tree.setModel(new DefaultTreeModel(currentNode));
    		((DefaultTreeModel)tree.getModel()).reload();
    	}
    	else {
    		// Vraćamo se do čvora (foldera) u kome se nalazi tekući folder i dodajemo ga u stablo
    		FileInfo fileInfo = (FileInfo) currentNode.getUserObject();
    		while (!fileInfo.getFilePath().equals(aDir.getParent())) {
    			currentNode = (DefaultMutableTreeNode) currentNode.getParent();
    			fileInfo = (FileInfo) currentNode.getUserObject();
    		}
    		DefaultMutableTreeNode newChild = new DefaultMutableTreeNode(new FileInfo(aDir.getFileName().toString(), aDir, aDir.getParent()));
    		currentNode.add(newChild);
    		// Novi folder postaje tekući čvor u stablu
    		currentNode = newChild;
    	}
      
    	return FileVisitResult.CONTINUE;
    }
  }