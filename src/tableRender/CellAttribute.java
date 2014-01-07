/*
 * CellAttribute.java
 *
 * Created on December 23, 2006, 11:43 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tableRender;

import java.awt.Dimension;

/**
 *
 * @author root
 */
public interface CellAttribute {

  public void addColumn();

  public void addRow();

  public void insertRow(int row);

  public Dimension getSize();

  public void setSize(Dimension size);


}