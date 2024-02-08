package palmer.matthew.filehandler.model;

import java.sql.Timestamp;

public class FileMetadata {
  private long fileId;
  private String ownerId;
  private String fileName;
  private long fileSize;
  private Timestamp creationDate;
  private Timestamp modificationDate;
  private String filePath;

  // Constructor for fetching FileMetadata from the database
  public FileMetadata(long fileId, String ownerId, String fileName, long fileSize,
      Timestamp creationDate, Timestamp modificationDate, String filePath) {
    this.fileId = fileId;
    this.ownerId = ownerId;
    this.fileName = fileName;
    this.fileSize = fileSize;
    this.creationDate = creationDate;
    this.modificationDate = modificationDate;
    this.filePath = filePath;
  }

  public long getFileId() {
    return fileId;
  }

  public void setFileId(int fileId) {
    this.fileId = fileId;
  }

  public String getOwnerId() {
    return ownerId;
  }

  public void setOwnerId(String ownerId) {
    this.ownerId = ownerId;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public long getFileSize() {
    return fileSize;
  }

  public void setFileSize(long fileSize) {
    this.fileSize = fileSize;
  }

  public Timestamp getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(Timestamp creationDate) {
    this.creationDate = creationDate;
  }

  public Timestamp getModificationDate() {
    return modificationDate;
  }

  public void setModificationDate(Timestamp modificationDate) {
    this.modificationDate = modificationDate;
  }

  public String getFilePath() {
    return filePath;
  }

  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }
}
