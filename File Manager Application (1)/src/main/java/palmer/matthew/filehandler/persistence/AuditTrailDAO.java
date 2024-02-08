package palmer.matthew.filehandler.persistence;


import palmer.matthew.filehandler.model.AuditTrail;

public interface AuditTrailDAO {
  /**
   * Inserts a new audit record into the audit trail.
   * 
   * @param auditTrail The audit trail record to insert.
   */
  void insert(AuditTrail auditTrail);
}
