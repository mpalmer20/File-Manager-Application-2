package palmer.matthew.filehandler.view;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import palmer.matthew.filehandler.util.ResourceUtils;

public class FileModel {
  private final SimpleIntegerProperty fileId; // For storing the file ID
  private final SimpleStringProperty fileName;
  private final SimpleStringProperty fileSize;
  private final SimpleStringProperty lastModified;
  private long fileSizeLong;

  // Adjusted constructor to include fileId
  public FileModel(int fileId, String fileName, long fileSize, String lastModified) {
    this.fileId = new SimpleIntegerProperty(fileId);
    this.fileName = new SimpleStringProperty(fileName);
    this.fileSizeLong = fileSize;
    this.fileSize = new SimpleStringProperty(ResourceUtils.convertFileSize(fileSize));
    this.lastModified = new SimpleStringProperty(lastModified);
  }

  // Getter and setter for fileId
  public int getFileId() {
    return fileId.get();
  }

  public void setFileId(int fileId) {
    this.fileId.set(fileId);
  }

  // Existing getters and setters for fileName, fileSize, and lastModified
  public String getFileName() {
    return fileName.get();
  }

  public void setFileName(String fileName) {
    this.fileName.set(fileName);
  }

  public Long getFileSize() {
    return fileSizeLong;
  }

  public void setFileSize(String fileSize) {
    this.fileSize.set(fileSize);
  }

  public String getLastModified() {
    return lastModified.get();
  }

  public void setLastModified(String lastModified) {
    this.lastModified.set(lastModified);
  }

  // Property getters for table binding
  public SimpleIntegerProperty fileIdProperty() {
    return fileId;
  }

  public SimpleStringProperty fileNameProperty() {
    return fileName;
  }

  public SimpleStringProperty fileSizeProperty() {
    return fileSize;
  }

  public SimpleStringProperty lastModifiedProperty() {
    return lastModified;
  }
}
