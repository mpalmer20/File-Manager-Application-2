package palmer.matthew.filehandler.service;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.Vector;
import palmer.matthew.filehandler.model.FileMetadata;
import palmer.matthew.filehandler.persistence.FileMetadataDAO;
import palmer.matthew.filehandler.persistence.impl.FileMetadataDAOImpl;
import palmer.matthew.filehandler.util.AESUtil;

public class FileService {
  private static final String DIR_SEPARATOR = "/";
  private static final String SFTP_HOST = "localhost"; // Adjust if your Docker host differs
  private static final int SFTP_PORT = 2222; // Port mapped to the Docker SFTP server
  private static final String SFTP_USER = "sftpuser";
  private static final String SFTP_PASS = "pass";
  private static final String SFTP_DIRECTORY = "/upload"; // Target directory in the
                                                          // SFTP server

  private Session session;
  private ChannelSftp sftpChannel;
  private FileMetadataDAO fileMetadataDAO;


  public FileService() throws Exception {
    this.sftpChannel = initializeSFTPChannel();
    this.fileMetadataDAO = new FileMetadataDAOImpl();
  }


  private ChannelSftp initializeSFTPChannel() throws Exception {
    JSch jsch = new JSch();
    session = jsch.getSession(SFTP_USER, SFTP_HOST, SFTP_PORT);
    session.setConfig("StrictHostKeyChecking", "no");
    session.setPassword(SFTP_PASS);
    session.connect();

    ChannelSftp channel = (ChannelSftp) session.openChannel("sftp");
    channel.connect();
    return channel;
  }

  public Vector<ChannelSftp.LsEntry> listFiles() throws Exception {
    return sftpChannel.ls(SFTP_DIRECTORY);
  }

  public void uploadFile(String fileName, byte[] fileContent) throws Exception {
    byte[] encryptedContent = AESUtil.encryptFile(fileContent); // Assume AESUtil.encrypt() exists
    InputStream inputStream = new ByteArrayInputStream(encryptedContent);
    String remoteFilePath = SFTP_DIRECTORY + DIR_SEPARATOR + fileName;
    sftpChannel.put(inputStream, remoteFilePath);
    inputStream.close();

    Vector<ChannelSftp.LsEntry> entries = sftpChannel.ls(remoteFilePath);
    ChannelSftp.LsEntry fileUploaded = null;
    // Iterate over the entries to find the matching file
    for (ChannelSftp.LsEntry entry : entries) {
      if (entry.getFilename().equals(fileName)) {
        fileUploaded = entry;
      }
    }
    // After uploading, create metadata
    createMetadata(fileName, fileUploaded.getLongname().hashCode(),
        fileUploaded.getAttrs().getSize());
  }

  public void updateFile(String filePath, byte[] newFileContent) throws Exception {
    // Assuming update is an overwrite operation
    uploadFile(filePath, newFileContent); // Reuse upload logic for update
  }

  public void deleteFile(String filePath, int fileId) throws Exception {
    String remoteFilePath = SFTP_DIRECTORY + DIR_SEPARATOR + filePath;
    SftpATTRS attrs = sftpChannel.lstat(remoteFilePath);
    sftpChannel.rm(remoteFilePath);
    fileMetadataDAO.delete(fileId);
  }

  public byte[] downloadFile(String filePath) throws Exception {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    sftpChannel.get(SFTP_DIRECTORY + DIR_SEPARATOR + filePath, outputStream);
    byte[] encryptedContent = outputStream.toByteArray();
    return AESUtil.decryptFile(encryptedContent); // Assume AESUtil.decrypt() exists
  }

  public FileMetadata getMetadata(long fileId) throws Exception {
    // This method should fetch metadata based on the fileName or filePath
    return fileMetadataDAO.findById(fileId);
  }


  public void createMetadata(String fileName, int fileId, long fileSize) {
    String remoteFilePath = SFTP_DIRECTORY + DIR_SEPARATOR + fileName;
    String ownerId = SessionManager.getInstance().getLoggedInUserId();
    FileMetadata currentMetadata =
        new FileMetadata(fileId, ownerId, fileName, fileSize, null, null, remoteFilePath);
    FileMetadata existingMetadata = fileMetadataDAO.findById(fileId);

    Timestamp currentTimeStamp = new Timestamp(System.currentTimeMillis());
    if (existingMetadata != null) {
      existingMetadata.setModificationDate(currentTimeStamp);
      fileMetadataDAO.update(existingMetadata);
    } else {
      currentMetadata.setCreationDate(currentTimeStamp);
      currentMetadata.setModificationDate(currentTimeStamp);
      fileMetadataDAO.insert(currentMetadata);
    }
  }


  private void disconnect() {
    if (sftpChannel != null) {
      sftpChannel.disconnect();
    }
    if (session != null) {
      session.disconnect();
    }
  }

  @Override
  protected void finalize() throws Throwable {
    disconnect(); // Ensure resources are freed on garbage collection
    super.finalize();
  }
}
