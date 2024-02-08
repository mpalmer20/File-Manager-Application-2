package palmer.matthew.filehandler.model;

public class AuditTrail {
  private int auditId; // Not used in insertion as it's SERIAL
  private long fileId;
  private String userId;
  private AuditAction action;

  public AuditTrail(long fileId, String userId, AuditAction action) {
    this.fileId = fileId;
    this.userId = userId;
    this.action = action;
  }

  public int getAuditId() {
    return auditId;
  }

  public void setAuditId(int auditId) {
    this.auditId = auditId;
  }


  public long getFileId() {
    return fileId;
  }

  public void setFileId(long fileId) {
    this.fileId = fileId;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public AuditAction getAction() {
    return action;
  }

  public void setAction(AuditAction action) {
    this.action = action;
  }

}
