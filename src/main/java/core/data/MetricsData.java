package core.data;

import storage.Storable;

/**
 * This class stores the metrics of a set of files in a commit
 */
public class MetricsData implements Storable<MetricsData>{

  private String file;
  private Double cbo;
  private Double dit;
  private Double numberOfFields;
  private Double numberOfMethods;
  private Double nosi;
  private Double rfc;
  private Double wmc;
  private Double loc;
  private Double lcom;
  
  public MetricsData() {
    
    
  }

  /**
   * @return the cbo
   */
  public Double getCbo() {
    return cbo;
  }

  /**
   * @return the dit
   */
  public Double getDit() {
    return dit;
  }

  /**
   * @return the lcom
   */
  public Double getLcom() {
    return lcom;
  }

  /**
   * @return the loc
   */
  public Double getLoc() {
    return loc;
  }

  /**
   * @return the nosi
   */
  public Double getNosi() {
    return nosi;
  }

  /**
   * @return the numberOfFiles
   */
  public Double getNumberOfFields() {
    return numberOfFields;
  }

  /**
   * @return the numberOfMethods
   */
  public Double getNumberOfMethods() {
    return numberOfMethods;
  }

  /**
   * @return the rfc
   */
  public Double getRfc() {
    return rfc;
  }

  /**
   * @return the wmc
   */
  public Double getWmc() {
    return wmc;
  }

  /**
   * @param cbo the cbo to set
   */
  public void setCbo(Double cbo) {
    this.cbo = cbo;
  }

  /**
   * @param dit the dit to set
   */
  public void setDit(Double dit) {
    this.dit = dit;
  }

  /**
   * @param lcom the lcom to set
   */
  public void setLcom(Double lcom) {
    this.lcom = lcom;
  }

  /**
   * @param loc the loc to set
   */
  public void setLoc(Double loc) {
    this.loc = loc;
  }

  /**
   * @param nosi the nosi to set
   */
  public void setNosi(Double nosi) {
    this.nosi = nosi;
  }

  /**
   * @param numberOfFiles the numberOfFiles to set
   */
  public void setNumberOfFields(Double numberOfFields) {
    this.numberOfFields = numberOfFields;
  }

  /**
   * @param numberOfMethods the numberOfMethods to set
   */
  public void setNumberOfMethods(Double numberOfMethods) {
    this.numberOfMethods = numberOfMethods;
  }

  /**
   * @param rfc the rfc to set
   */
  public void setRfc(Double rfc) {
    this.rfc = rfc;
  }

  /**
   * @param wmc the wmc to set
   */
  public void setWmc(Double wmc) {
    this.wmc = wmc;
  }

  public String getFile() {
    return file;
  }

  public void setFile(String file) {
    this.file = file;
  }

  @Override
  public String[] attributes() {
    
    return new String[]{
      "file", "cbo", "dit",
      "numberOfFiles", "numberOfMethods", "nosi",
      "rfc", "wmc", "loc", "lcom"
    };
  }
  
  @Override
  public String[] write() {
  
    return new String[]{
      getFile(),
      getCbo().toString(),
      getDit().toString(),
      getNumberOfFields().toString(),
      getNumberOfMethods().toString(),
      getNosi().toString(),
      getRfc().toString(),
      getWmc().toString(),
      getLoc().toString(),
      getLcom().toString()
    };
  }

  @Override
  public MetricsData read(String[] values) {
    setFile(values[0]);
    setCbo(Double.valueOf(values[1]));
    setDit(Double.valueOf(values[2]));
    setNumberOfFields(Double.valueOf(values[3]));
    setNumberOfMethods(Double.valueOf(values[4]));
    setNosi(Double.valueOf(values[5]));
    setRfc(Double.valueOf(values[6]));
    setWmc(Double.valueOf(values[7]));
    setLoc(Double.valueOf(values[8]));
    setLcom(Double.valueOf(values[9]));
    return this;
  }
}
