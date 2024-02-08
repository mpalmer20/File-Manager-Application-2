package palmer.matthew.filehandler.persistence;


import java.util.List;
import palmer.matthew.filehandler.model.FileMetadata;

public interface FileMetadataDAO {
  FileMetadata findById(long fileId);

  List<FileMetadata> findAll();

  boolean insert(FileMetadata fileMetadata);

  boolean update(FileMetadata fileMetadata);

  boolean delete(int fileId);
}
