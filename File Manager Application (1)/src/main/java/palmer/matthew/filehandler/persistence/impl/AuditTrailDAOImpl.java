package palmer.matthew.filehandler.persistence.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;
import palmer.matthew.filehandler.model.AuditTrail;
import palmer.matthew.filehandler.persistence.AuditTrailDAO;

/**
 * Implementation of the AuditTrailDAO interface for interacting with the audit_trail table. This
 * class provides methods to insert audit trail records into the database.
 */
public class AuditTrailDAOImpl implements AuditTrailDAO {
  private static final Logger LOGGER = Logger.getLogger(AuditTrailDAOImpl.class.getName());
  private static final String INSERT_AUDIT_TRAIL =
      "INSERT INTO audit_trail (file_id, user_id, action, action_date) VALUES (?, ?, ?, DEFAULT)";

  private final Connection connection;

  /**
   * Constructs a new AuditTrailDAOImpl with a database connection.
   *
   * @param connection the database connection to use for data access operations.
   */
  public AuditTrailDAOImpl(Connection connection) {
    this.connection = connection;
  }

  /**
   * Inserts a new audit record into the audit trail.
   *
   * @param auditTrail The audit trail record to insert. Must not be null.
   */
  @Override
  public void insert(AuditTrail auditTrail) {
    try (PreparedStatement pstmt = connection.prepareStatement(INSERT_AUDIT_TRAIL)) {
      pstmt.setLong(1, auditTrail.getFileId());
      pstmt.setString(2, auditTrail.getUserId());
      pstmt.setString(3, auditTrail.getAction().toString());

      pstmt.executeUpdate();
    } catch (SQLException e) {
      LOGGER.severe("Failed to insert audit trail record: " + e.getMessage());
    }
  }
}
