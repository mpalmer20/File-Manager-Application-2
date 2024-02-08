package palmer.matthew.filehandler.persistence.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import palmer.matthew.filehandler.model.AuditAction;
import palmer.matthew.filehandler.model.AuditTrail;
import palmer.matthew.filehandler.model.FileMetadata;
import palmer.matthew.filehandler.persistence.AuditTrailDAO;
import palmer.matthew.filehandler.persistence.DatabaseConnector;
import palmer.matthew.filehandler.persistence.FileMetadataDAO;

/**
 * Implementation of FileMetadataDAO to interact with the database for file metadata operations.
 */
public class FileMetadataDAOImpl implements FileMetadataDAO {
  private static final Logger LOGGER = Logger.getLogger(FileMetadataDAOImpl.class.getName());
  private static final String INSERT_FILE_METADATA =
      "INSERT INTO file_metadata(file_id, owner_id, file_name, file_size, file_path, creation_date, modification_date) VALUES(?,?,?,?,?,?,?)";
  private static final String UPDATE_FILE_METADATA =
      "UPDATE file_metadata SET file_name = ?, file_size = ?, file_path = ?, modification_date = ? WHERE file_id = ?";
  private static final String DELETE_FILE_METADATA = "DELETE FROM file_metadata WHERE file_id = ?";
  private static final String SELECT_FILE_METADATA_BY_ID =
      "SELECT * FROM file_metadata WHERE file_id = ?";
  private static final String SELECT_ALL_FILE_METADATA = "SELECT * FROM file_metadata";


  private AuditTrailDAO auditTrailDao;

  /**
   * Inserts a new file metadata record into the database.
   * 
   * @param fileMetadata The file metadata to be inserted.
   * @return true if the operation was successful, false otherwise.
   */
  @Override
  public boolean insert(FileMetadata fileMetadata) {
    try (Connection conn = DatabaseConnector.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(INSERT_FILE_METADATA)) {
      auditTrailDao = new AuditTrailDAOImpl(conn);
      auditTrailDao.insert(getAuditDataFromFileMetadata(fileMetadata, AuditAction.CREATE));
      pstmt.setLong(1, fileMetadata.getFileId());
      pstmt.setString(2, fileMetadata.getOwnerId());
      pstmt.setString(3, fileMetadata.getFileName());
      pstmt.setLong(4, fileMetadata.getFileSize());
      pstmt.setString(5, fileMetadata.getFilePath());
      pstmt.setTimestamp(6, fileMetadata.getCreationDate());
      pstmt.setTimestamp(7, fileMetadata.getModificationDate());
      pstmt.executeUpdate();
      return true;
    } catch (SQLException e) {
      LOGGER.severe("Failed to insert file metadata: " + e.getMessage());
      return false;
    }
  }

  /**
   * Updates an existing file metadata record in the database.
   * 
   * @param fileMetadata The file metadata to update.
   * @return true if the operation was successful, false otherwise.
   */
  @Override
  public boolean update(FileMetadata fileMetadata) {
    try (Connection conn = DatabaseConnector.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(UPDATE_FILE_METADATA)) {
      auditTrailDao = new AuditTrailDAOImpl(conn);
      auditTrailDao.insert(getAuditDataFromFileMetadata(fileMetadata, AuditAction.UPDATE));
      pstmt.setString(1, fileMetadata.getFileName());
      pstmt.setLong(2, fileMetadata.getFileSize());
      pstmt.setString(3, fileMetadata.getFilePath());
      pstmt.setTimestamp(4, fileMetadata.getModificationDate());
      pstmt.setLong(5, fileMetadata.getFileId());
      pstmt.executeUpdate();
      return true;
    } catch (SQLException e) {
      LOGGER.severe("Failed to update file metadata: " + e.getMessage());
      return false;
    }
  }

  private AuditTrail getAuditDataFromFileMetadata(FileMetadata fileMetadata, AuditAction action) {
    return new AuditTrail(fileMetadata.getFileId(), fileMetadata.getOwnerId(), action);
  }

  /**
   * Deletes a file metadata record from the database.
   * 
   * @param fileId The ID of the file metadata to delete.
   * @return true if the operation was successful, false otherwise.
   */
  @Override
  public boolean delete(int fileId) {
    try (Connection conn = DatabaseConnector.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(DELETE_FILE_METADATA)) {
      auditTrailDao = new AuditTrailDAOImpl(conn);
      FileMetadata fileMetadata = findById(fileId);
      pstmt.setInt(1, fileId);
      pstmt.executeUpdate();
      return true;
    } catch (SQLException e) {
      LOGGER.severe("Failed to delete file metadata: " + e.getMessage());
      return false;
    }
  }

  /**
   * Retrieves a file metadata record by its ID.
   * 
   * @param fileId The ID of the file metadata to retrieve.
   * @return The FileMetadata object if found, null otherwise.
   */
  @Override
  public FileMetadata findById(long fileId) {
    try (Connection conn = DatabaseConnector.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(SELECT_FILE_METADATA_BY_ID)) {
      auditTrailDao = new AuditTrailDAOImpl(conn);
      pstmt.setLong(1, fileId);
      try (ResultSet rs = pstmt.executeQuery()) {
        if (rs.next()) {
          FileMetadata fileMetadata = new FileMetadata(rs.getLong("file_id"),
              rs.getString("owner_id"), rs.getString("file_name"), rs.getLong("file_size"),
              rs.getTimestamp("creation_date"), rs.getTimestamp("modification_date"),
              rs.getString("file_path"));
          auditTrailDao.insert(getAuditDataFromFileMetadata(fileMetadata, AuditAction.READ));
          return fileMetadata;
        }
      }
    } catch (SQLException e) {
      LOGGER.severe("Failed to find file metadata by ID: " + e.getMessage());
    }
    return null;
  }

  /**
   * Retrieves all file metadata records from the database.
   * 
   * @return A list of FileMetadata objects.
   */
  @Override
  public List<FileMetadata> findAll() {
    List<FileMetadata> fileList = new ArrayList<>();
    try (Connection conn = DatabaseConnector.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(SELECT_ALL_FILE_METADATA);
        ResultSet rs = pstmt.executeQuery()) {
      while (rs.next()) {
        fileList.add(new FileMetadata(rs.getLong("file_id"), rs.getString("owner_id"),
            rs.getString("file_name"), rs.getLong("file_size"), rs.getTimestamp("creation_date"),
            rs.getTimestamp("modification_date"), rs.getString("file_path")));
      }
    } catch (SQLException e) {
      LOGGER.severe("Failed to find all file metadata: " + e.getMessage());
    }
    return fileList;
  }
}
