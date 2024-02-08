package palmer.matthew.filehandler.model;

public enum AuditAction {
  CREATE("create"), UPDATE("update"), DELETE("delete"), READ("read");

  private final String action;

  AuditAction(String action) {
    this.action = action;
  }

  @Override
  public String toString() {
    return action;
  }
}
