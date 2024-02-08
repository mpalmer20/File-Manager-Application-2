package palmer.matthew.filehandler.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import palmer.matthew.filehandler.model.FileMetadata;
import palmer.matthew.filehandler.util.ResourceUtils;

public class FileInfoPopupController {

  @FXML
  private Label fileNameLabel;
  @FXML
  private Label fileSizeLabel;
  @FXML
  private Label fileOwnerLabel;
  @FXML
  private Label fileCreationDateLabel;
  @FXML
  private Label fileModificationDateLabel;

  // Method to populate the UI with file metadata
  public void setFileMetadata(FileMetadata metadata) {
    fileNameLabel.setText("File Name: " + metadata.getFileName());
    fileSizeLabel.setText("File Size: " + ResourceUtils.convertFileSize(metadata.getFileSize()));
    fileOwnerLabel.setText("Owner: " + metadata.getOwnerId()); // Assuming owner ID is used here.
                                                               // Adapt as needed.
    fileCreationDateLabel.setText("Creation Date: " + metadata.getCreationDate().toString());
    fileModificationDateLabel
        .setText("Modification Date: " + metadata.getModificationDate().toString());
  }

  // Handler for the close button
  @FXML
  private void handleClose() {
    Stage stage = (Stage) fileNameLabel.getScene().getWindow();
    stage.close();
  }
}
