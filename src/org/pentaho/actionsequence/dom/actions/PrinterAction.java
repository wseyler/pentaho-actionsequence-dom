/*
 * Copyright 2006 Pentaho Corporation.  All rights reserved. 
 * This software was developed by Pentaho Corporation and is provided under the terms 
 * of the Mozilla Public License, Version 1.1, or any later version. You may not use 
 * this file except in compliance with the license. If you need a copy of the license, 
 * please go to http://www.mozilla.org/MPL/MPL-1.1.txt. The Original Code is the Pentaho 
 * BI Platform.  The Initial Developer is Pentaho Corporation.
 *
 * Software distributed under the Mozilla Public License is distributed on an "AS IS" 
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to 
 * the license for the specific language governing your rights and limitations.
*/
package org.pentaho.actionsequence.dom.actions;

import org.dom4j.Element;
import org.pentaho.actionsequence.dom.ActionDefinition;
import org.pentaho.actionsequence.dom.ActionInput;
import org.pentaho.actionsequence.dom.IActionVariable;

public class PrinterAction extends ActionDefinition {

  public static final String COMPONENT_NAME = "org.pentaho.component.PrintComponent"; //$NON-NLS-1$
  public static final String PRINTER_ELEMENT = "printer-name"; //$NON-NLS-1$
  public static final String COPIES_ELEMENT = "copies"; //$NON-NLS-1$
  public static final String FILE_ELEMENT = "printFile"; //$NON-NLS-1$
  
  protected static final String[] EXPECTED_RESOURCES = new String[] {
    FILE_ELEMENT
  };
  
  protected static final String[] EXPECTED_INPUTS = new String[] {
    PRINTER_ELEMENT,
    COPIES_ELEMENT,
    FILE_ELEMENT
  };

  public PrinterAction(Element actionDefElement) {
    super(actionDefElement);
  }

  public PrinterAction() {
    super(COMPONENT_NAME);
  }
  
  public String[] getExpectedInputs() {
    return EXPECTED_INPUTS;
  }
  
  public String[] getExpectedResources() {
    return EXPECTED_RESOURCES;
  }
  
  public void setPrintfile(String value) {
    setInputValue(FILE_ELEMENT, value);
  }
  
  public String getPrintfile() {
    return getComponentDefinitionValue(FILE_ELEMENT);
  }
  
  public void setPrintfileParam(IActionVariable variable) {
    setReferencedVariable(FILE_ELEMENT, variable);
  }
  
  public ActionInput getPrintfileParam() {
    return getInputParam(FILE_ELEMENT);
  }
  
  public void setCopies(String value) {
    setInputValue(COPIES_ELEMENT, value);
  }
  
  public String getCopies() {
    return getComponentDefinitionValue(COPIES_ELEMENT);
  }
  
  public void setCopiesParam(IActionVariable variable) {
    setReferencedVariable(COPIES_ELEMENT, variable);
  }
  
  public ActionInput getCopiesParam() {
    return getInputParam(COPIES_ELEMENT);
  }
  
  public void setPrinterName(String value) {
    setInputValue(PRINTER_ELEMENT, value);
  }
  
  public String getPrinterName() {
    return getComponentDefinitionValue(PRINTER_ELEMENT);
  }
  
  public void setPrinterNameParam(IActionVariable variable) {
    setReferencedVariable(PRINTER_ELEMENT, variable);
  }
  
  public ActionInput getPrinterNameParam() {
    return getInputParam(PRINTER_ELEMENT);
  }
  
}
