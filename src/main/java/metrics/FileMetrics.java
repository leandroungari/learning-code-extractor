package metrics;

import core.csv.CSVTransformerItem;

public class FileMetrics implements CSVTransformerItem<FileMetrics> {

  private String filename;
  private Double cbo;
  private Double dit;
  private Double lcom;
  private Double loc;
  private Double nosi;
  private Double numberOfFields;
  private Double numberOfMethods;
  private Double rfc;
  private Double wmc;

  public FileMetrics() {
    
  }

  public void setCbo(Double cbo) {
    this.cbo = cbo;
  }

  public void setDit(Double dit) {
    this.dit = dit;
  }

  public void setFilename(String filename) {
    this.filename = filename;
  }

  public void setLcom(Double lcom) {
    this.lcom = lcom;
  }

  public void setLoc(Double loc) {
    this.loc = loc;
  }

  public void setNosi(Double nosi) {
    this.nosi = nosi;
  }

  public void setNumberOfFields(Double numberOfFields) {
    this.numberOfFields = numberOfFields;
  }

  public void setNumberOfMethods(Double numberOfMethods) {
    this.numberOfMethods = numberOfMethods;
  }

  public void setRfc(Double rfc) {
    this.rfc = rfc;
  }

  public void setWmc(Double wmc) {
    this.wmc = wmc;
  }

  public Double getCbo() {
    return cbo;
  }

  public Double getDit() {
    return dit;
  }

  public String getFilename() {
    return filename;
  }

  public Double getLcom() {
    return lcom;
  }

  public Double getLoc() {
    return loc;
  }

  public Double getNosi() {
    return nosi;
  }

  public Double getNumberOfFields() {
    return numberOfFields;
  }

  public Double getNumberOfMethods() {
    return numberOfMethods;
  }

  public Double getRfc() {
    return rfc;
  }

  public Double getWmc() {
    return wmc;
  }


  public String[] toCSVItem() {
    return new String[]{
      filename,
      cbo.toString(),
      dit.toString(),
      lcom.toString(),
      loc.toString(),
      nosi.toString(),
      numberOfFields.toString(),
      numberOfMethods.toString(),
      rfc.toString(),
      wmc.toString()
    };
  }

  public FileMetrics fromCSVItem(String[] content) {
    this.filename = content[0];
    this.cbo = Double.valueOf(content[1] + "d");
    this.dit = Double.valueOf(content[2] + "d");
    this.lcom = Double.valueOf(content[3] + "d");
    this.loc = Double.valueOf(content[4] + "d");
    this.nosi = Double.valueOf(content[5] + "d");
    this.numberOfFields = Double.valueOf(content[6] + "d");
    this.numberOfMethods = Double.valueOf(content[7] + "d");
    this.rfc = Double.valueOf(content[7] + "d");
    this.wmc = Double.valueOf(content[8] + "d");
    return this;
  }

}
