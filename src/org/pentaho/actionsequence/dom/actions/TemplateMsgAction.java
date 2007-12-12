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

import java.net.URI;

import org.dom4j.Element;
import org.pentaho.actionsequence.dom.ActionInput;
import org.pentaho.actionsequence.dom.ActionOutput;
import org.pentaho.actionsequence.dom.ActionResource;
import org.pentaho.actionsequence.dom.ActionSequenceDocument;
import org.pentaho.actionsequence.dom.IActionInputVariable;

public class TemplateMsgAction extends ActionDefinition {

  public static final String COMPONENT_NAME = "org.pentaho.component.TemplateComponent"; //$NON-NLS-1$
  public static final String TEMPLATE_ELEMENT = "template" ; //$NON-NLS-1$
  public static final String OUTPUT_MSG_ELEMENT = "output-message" ; //$NON-NLS-1$
  public static final String OUTPUT_STRING = "output-string"; //$NON-NLS-1$
  public static final String TEMPLATE_FILE = "template-file" ; //$NON-NLS-1$
  
  protected static final String[] EXPECTED_INPUTS = new String[] {
    TEMPLATE_ELEMENT
  };
  protected static final String[] EXPECTED_RESOURCES = new String[] {
    TEMPLATE_ELEMENT
  };

  public TemplateMsgAction(Element actionDefElement, IActionParameterMgr actionInputProvider) {
    super(actionDefElement, actionInputProvider);
  }

  public TemplateMsgAction() {
    super(COMPONENT_NAME);
  }
  
  public static boolean accepts(Element element) {
    return ActionDefinition.accepts(element) && hasComponentName(element, COMPONENT_NAME);
  }
  
  public String[] getReservedInputNames() {
    return EXPECTED_INPUTS;
  }
  
  public String[] getReservedOutputNames() {
    String expectedOutput = OUTPUT_MSG_ELEMENT;
    if (getOutputParam(expectedOutput) ==  null) { 
      ActionOutput[] actionOutputs = getOutputParams(ActionSequenceDocument.STRING_TYPE);
      if (actionOutputs.length > 0) {
        expectedOutput = actionOutputs[0].getName();
      }
    }
    return new String[]{expectedOutput};
  }
  
  public String[] getReservedResourceNames() {
    return EXPECTED_RESOURCES;
  }

  protected void initNewActionDefinition() {
    setInputValue(TEMPLATE_ELEMENT, ""); //$NON-NLS-1$
  }

  public void setTemplate(String value) {
    setInputValue(TEMPLATE_ELEMENT, value);
  }
  
  public String getTemplate() {
    return getComponentDefinitionValue(TEMPLATE_ELEMENT);
  }
  
  public void setTemplateParam(IActionInputVariable variable) {
    setInputParam(TEMPLATE_ELEMENT, variable);
  }
  
  public ActionInput getTemplateParam() {
    return getInputParam(TEMPLATE_ELEMENT);
  }
  
  public void setOutputStringName(String name) {
    setOutputParam(OUTPUT_MSG_ELEMENT, name, ActionSequenceDocument.STRING_TYPE);
  }
  
  public String getOutputStringName() {
    return getPublicOutputName(OUTPUT_MSG_ELEMENT);
  }
  
  public ActionOutput getOutputStringParam() {
    return getOutputParam(OUTPUT_MSG_ELEMENT);
  }
  
  public ActionResource setTemplateFile(URI uri, String mimeType) {
    return setResourceUri(TEMPLATE_ELEMENT, uri, mimeType);
  }
  
  public ActionResource getFileToPrint() {
    return getResourceParam(TEMPLATE_ELEMENT);
  }
}
