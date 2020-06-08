package metrics;

import java.util.ArrayList;
import java.util.List;

import persistence.csv.CSVTransformer;

public class CommitMetrics implements CSVTransformer<CommitMetrics> {
 
  private List<FileMetrics> files;

  public CommitMetrics() {
    this.files = new ArrayList<>();
  }

  public CommitMetrics(List<FileMetrics> files) {
    this.files = files;
  }

  public void add(FileMetrics a) {
    files.add(a);
  }

  @Override
  public List<String> toCSV() {
    var result = new ArrayList<String>();
    for(var file: files) {
      result.add(String.join(",", file.toCSVItem()));
    }
    return result;
  }

  @Override
  public String toString() {
    var header = String.join(",", this.getHeader());
    var list = this.toCSV();
    list.add(0, header);
    return String.join("\n", list);
  }

  public String[] getHeader() {
    return new String[] {
      "filename",
      "cbo",
      "dit",
      "lcom",
      "loc",
      "nosi",
      "numberOfFields",
      "numberOfMethods",
      "rfc",
      "wmc"
    };
  }

  @Override
  public CommitMetrics fromCSV(List<String> content) {
    for(var item: content) {
      files.add(new FileMetrics().fromCSVItem(item.split(",")));
    }
    return this;
  }
}
