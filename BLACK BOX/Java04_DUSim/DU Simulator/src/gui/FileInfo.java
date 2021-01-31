package gui;

import java.nio.file.Path;

// Klasa koja čuva informacije o fajlovima i folderima u stablu 
public class FileInfo {
	
	private String fileName;
	private Path filePath;
	private Path parentPath;
	
	public FileInfo(String fileName, Path filePath, Path parentPath) {
		super();
		this.fileName = fileName;
		this.filePath = filePath;
		this.parentPath = parentPath;
	}

	public String getFileName() {
		return fileName;
	}

	public Path getFilePath() {
		return filePath;
	}
	
	public Path getParentPath() {
		return parentPath;
	}

	public String toString() {
		return fileName;
	}

}
