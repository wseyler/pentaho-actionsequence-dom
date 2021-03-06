/*
 * Copyright 2002 - 2013 Pentaho Corporation.  All rights reserved.
 * 
 * This software was developed by Pentaho Corporation and is provided under the terms
 * of the Mozilla Public License, Version 1.1, or any later version. You may not use
 * this file except in compliance with the license. If you need a copy of the license,
 * please go to http://www.mozilla.org/MPL/MPL-1.1.txt. TThe Initial Developer is Pentaho Corporation.
 *
 * Software distributed under the Mozilla Public License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to
 * the license for the specific language governing your rights and limitations.
 */

package org.pentaho.actionsequence.dom.actions;

import org.dom4j.Element;
import org.pentaho.actionsequence.dom.IActionInput;
import org.pentaho.actionsequence.dom.IActionInputSource;

public class HelloWorldAction extends ActionDefinition {

  public static final String COMPONENT_NAME = "org.pentaho.component.HelloWorldComponent"; //$NON-NLS-1$
  public static final String QUOTE_ELEMENT = "quote"; //$NON-NLS-1$

  protected static final String[] EXPECTED_INPUTS = new String[] { QUOTE_ELEMENT };

  public HelloWorldAction( Element actionDefElement, IActionParameterMgr actionInputProvider ) {
    super( actionDefElement, actionInputProvider );
  }

  public HelloWorldAction() {
    super( COMPONENT_NAME );
  }

  public static boolean accepts( Element element ) {
    return ActionDefinition.accepts( element ) && hasComponentName( element, COMPONENT_NAME );
  }

  public String[] getReservedInputNames() {
    return EXPECTED_INPUTS;
  }

  public void setQuote( IActionInputSource value ) {
    setActionInputValue( QUOTE_ELEMENT, value );
  }

  public IActionInput getQuote() {
    return getInput( QUOTE_ELEMENT );
  }

}
