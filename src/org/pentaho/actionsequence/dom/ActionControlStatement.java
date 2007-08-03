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
package org.pentaho.actionsequence.dom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.tree.DefaultElement;

public abstract class ActionControlStatement implements IActionSequenceExecutableStatement {

  Element controlElement;
  
  public ActionControlStatement(Element controlElement) {
    this.controlElement = controlElement;
  }

  public Element getElement() {
    return controlElement;
  }

  
  /**
   * Adds a new child action definition to the end of this control statements
   * list of children.
   * @param componentName the name of the component that processes
   * the action definition
   * @return the newly created action definition
   */
  public ActionDefinition addAction(int actionId) {
    ActionDefinition action = ActionDefinition.createAction(actionId);
    controlElement.elements().add(action.getElement());
    ActionSequenceDocument.fireActionAdded(action);
    return action;
  }
  
  /**
   * Adds a new child action definition to this control statement.
   * @param componentName the name of the component that processes
   * the action definition
   * @param index the index of where to add the new action. If index
   * is greater than the number of children then the new action is added
   * at the end of the list of children.
   * @return the newly created action definition
   */
  public ActionDefinition addAction(int actionId, int index) {
    Object[] children = getChildren();
    ActionDefinition actionDef = null;
    if (index >= children.length) {
      actionDef = addAction(actionId);
    } else {
      Object childAtIndex = children[index];
      Element childElement = (childAtIndex instanceof ActionControlStatement) ? ((ActionControlStatement) childAtIndex).controlElement : ((ActionDefinition) childAtIndex).actionDefElement;
      List childElements = controlElement.elements();
      index = childElements.indexOf(childElement);
      if (index >= 0) {
        actionDef = ActionDefinition.createAction(actionId);
        childElements.add(index, actionDef.getElement());
        ActionSequenceDocument.fireActionAdded(actionDef);
      } else {
        actionDef = addAction(actionId);
      }
    }
    return actionDef;
  }
  
  /**
   * @return the child actions and control statements
   */
  public IActionSequenceExecutableStatement[] getChildren() {
    List allChildren = controlElement.elements();
    List filteredChildren = new ArrayList();
    for (Iterator iter = allChildren.iterator(); iter.hasNext();) {
      Element element = (Element)iter.next();
      String elementName = element.getName();
      if (elementName.equals(ActionSequenceDocument.ACTION_DEFINITION_NAME)) {
        filteredChildren.add(ActionDefinition.instance(element));
      } else if (elementName.equals(ActionSequenceDocument.ACTIONS_NAME)) {
        if (element.element(ActionSequenceDocument.CONDITION_NAME) == null) {
          filteredChildren.add(new ActionLoop(element));
        } else {
          filteredChildren.add(new ActionIfStatement(element));
        }
      }
    }
    return (IActionSequenceExecutableStatement[])filteredChildren.toArray(new IActionSequenceExecutableStatement[0]);
  }
  
  /* (non-Javadoc)
   * @see org.pentaho.designstudio.dom.IActionSequenceElement#getDocument()
   */
  public ActionSequenceDocument getDocument() {
    ActionSequenceDocument doc = null;
    if ((controlElement != null) && (controlElement.getDocument() != null)) {
      doc = new ActionSequenceDocument(controlElement.getDocument());
    }
    return doc;
  }
  
  /* (non-Javadoc)
   * @see org.pentaho.designstudio.dom.IActionSequenceElement#delete()
   */
  public void delete() {
    Document doc = controlElement.getDocument();
    if (doc != null) {
      controlElement.detach();
      ActionSequenceDocument.fireControlStatementRemoved(new ActionSequenceDocument(doc), this);
    }
  }
  
  /**
   * @return the control statement that contains this action definition or null if there is no parent control statement.
   */
  public ActionControlStatement getParent() {
    ActionControlStatement controlStatement = null;
    if (controlElement != null) {
      Element ancestorElement = controlElement.getParent();
      if ((ancestorElement != null) 
          && ancestorElement.getName().equals(ActionSequenceDocument.ACTIONS_NAME)
          && !ancestorElement.getPath().equals(ActionSequenceDocument.DOC_ACTIONS_PATH)) {
        if (ancestorElement.element(ActionSequenceDocument.CONDITION_NAME) == null) {
          controlStatement = new ActionLoop(ancestorElement);
        } else {
          controlStatement = new ActionIfStatement(ancestorElement);
        }
      }
    }
    return controlStatement;
  }
  
  /**
   * Adds an action definition to the end of this control statements list of 
   * children. This control statement becomes the new parent of the action
   * definition.
   * @param actionDef the action definition to be added.
   */
  public void add(ActionDefinition actionDef) {
    actionDef.delete();
    controlElement.elements().add(actionDef.actionDefElement);
    ActionSequenceDocument.fireActionAdded(actionDef);
  }
  
  /**
   * Adds a new child action definition to this control statement. 
   * @param actionDef the action definition to be added.
   * @param index the index of where to add the new action. If index
   * is greater than the number of children then the new action is added
   * at the end of the list of children.
   */
  public void add(ActionDefinition actionDef, int index) {
    IActionSequenceElement[] children = getChildren();
    if (index >= children.length) {
      add(actionDef);
    } else {
      Element childElement = children[index].getElement();
      List childElements = controlElement.elements();
      index = childElements.indexOf(childElement);
      int actionDefIndex = childElements.indexOf(actionDef.getElement());
      if (index >= 0) {
        actionDef.delete();
        if  ((actionDefIndex >= 0) && (actionDefIndex < index)) {
          index--;
        }
        controlElement.elements().add(index, actionDef.actionDefElement);
        ActionSequenceDocument.fireActionAdded(actionDef);
      } else {
        add(actionDef);
      }
    }
  }
  
  /**
   * Adds a control statement to the end of this control statments list of 
   * children. This control statement becomes the new parent of the specified control statement.
   * @param controlStatement the control statment to be added.
   */
  public void add(ActionControlStatement controlStatement) {
    controlStatement.delete();
    controlElement.elements().add(controlStatement.controlElement);
    ActionSequenceDocument.fireControlStatementAdded(controlStatement);
  }
  
  /**
   * Adds a control statement to this conrtol statement's list of 
   * children. 
   * @param controlStatement the control statement to be added.
   * @param index the index of where to add the new control statement. If index
   * is greater than the number of children then the new control statement is added
   * at the end of the list of children.
   */
  public void add(ActionControlStatement controlStatement, int index) {
    IActionSequenceElement[] children = getChildren();
    if (index >= children.length) {
      add(controlStatement);
    } else {
      Element childElement = children[index].getElement();
      List childElements = controlElement.elements();
      index = childElements.indexOf(childElement);
      int actionLoopIndex = childElements.indexOf(controlStatement.getElement());
      if (index >= 0) {
        controlStatement.delete();
        if  ((actionLoopIndex >= 0) && (actionLoopIndex < index)) {
          index--;
        }
        controlElement.elements().add(index, controlStatement.controlElement);
        ActionSequenceDocument.fireControlStatementAdded(controlStatement);
      } else {
        add(controlStatement);
      }
    }
  }
  
  /** 
   * Creates a new child action loop to the end of this control statement's children.
   * @param loopOn the loop on variable name
   */
  public ActionLoop addLoop(String loopOn) {
    Element child = createLoopElement();
    controlElement.elements().add(child);
    ActionLoop loop = new ActionLoop(child);
    ActionSequenceDocument.fireControlStatementAdded(loop);
    return loop;
  }
  
  /** 
   * Creates a new child action loop.
   * @param loopOn the loop on variable name
   * @param index the index where the new loop should be created. If index
   * is greater than the number of children then the new loop is added
   * at the end of the list of children.
   */
  public ActionLoop addLoop(String loopOn, int index) {
    Object[] children = getChildren();
    ActionLoop actionLoop = null;
    if (index >= children.length) {
      actionLoop = addLoop(loopOn);
    } else {
      Object childAtIndex = children[index];
      Element childElement = (childAtIndex instanceof ActionControlStatement) ? ((ActionControlStatement)childAtIndex).controlElement : ((ActionDefinition)childAtIndex).actionDefElement;
      List childElements = controlElement.elements();
      index = childElements.indexOf(childElement);
      if (index >= 0) {
        Element child = createLoopElement();
        childElements.add(index, child);
        actionLoop = new ActionLoop(child);
        ActionSequenceDocument.fireControlStatementAdded(actionLoop);
      } else {
        actionLoop = addLoop(loopOn);
      }
    }
    return actionLoop;
  }
  
  /** 
   * Creates a new child if statement to the end of this control statement's children.
   * @param condition the if condition
   */
  public ActionIfStatement addIf(String condition) {
    Element child = createIfElement();
    controlElement.elements().add(child);
    ActionIfStatement actionIf = new ActionIfStatement(child);
    ActionSequenceDocument.fireControlStatementAdded(actionIf);
    return actionIf;
  }
  
  /** 
   * Creates a new child if statement.
   * @param condition the if condition
   * @param index the index where the new if statement should be created. If index
   * is greater than the number of children then the new if statement is added
   * at the end of the list of children.
   */
  public ActionIfStatement addIf(String condition, int index) {
    Object[] children = getChildren();
    ActionIfStatement actionIf = null;
    if (index >= children.length) {
      actionIf = addIf(condition);
    } else {
      Object childAtIndex = children[index];
      Element childElement = (childAtIndex instanceof ActionControlStatement) ? ((ActionControlStatement)childAtIndex).controlElement : ((ActionDefinition)childAtIndex).actionDefElement;
      List childElements = controlElement.elements();
      index = childElements.indexOf(childElement);
      if (index >= 0) {
        Element child = createIfElement();
        childElements.add(index, child);
        actionIf = new ActionIfStatement(child);
        ActionSequenceDocument.fireControlStatementAdded(actionIf);
      } else {
        actionIf = addIf(condition);
      }
    }
    return actionIf;
  }
  
  private Element createLoopElement() {
    Element element = null;
    element = new DefaultElement(ActionSequenceDocument.ACTIONS_NAME);
    return element;
  }
  
  private Element createIfElement() {
    Element element = null;
    element = new DefaultElement(ActionSequenceDocument.ACTIONS_NAME);
    element.addElement(ActionSequenceDocument.CONDITION_NAME);
    return element;
  }
  
  /**
   * Returns the list of ActionSequenceInputs and ActionOutputs that are available as inputs to 
   * this control statement.
   * @param types the desired input type
   * @return the list of available inputs
   */
  public IActionVariable[] getAvailInputVariables() {
    return getDocument().getAvailInputVariables(this);
  }
  
  public boolean equals(Object arg0) {
    boolean result = false;
    if (arg0 != null) {
      if (arg0.getClass() == this.getClass()) {
        ActionControlStatement controlStatement = (ActionControlStatement)arg0;
        result = (controlStatement.controlElement != null ? controlStatement.controlElement.equals(this.controlElement) : (controlStatement == this));
      }
    }
    return result;
  }
  
  /**
   * Returns the list of action definitions that precede this control statement in the action sequence. 
   * @param types the desired input type
   * @return the preceding acton defintions.
   */
  public ActionDefinition[] getPrecedingActionDefinitions() {
    return getDocument().getPrecedingActionDefinitions(this);
  }

  public IActionSequenceExecutableStatement[] getPrecedingExecutableStatements() {
    return getDocument().getPrecedingExecutables(this);
  }
  
  protected abstract ActionSequenceValidationError[] validateThis();
  
  public ActionSequenceValidationError[] validate() {
    return validate(false);
  }
  
  public ActionSequenceValidationError[] validate(boolean validateDescendants) {
    ArrayList errors = new ArrayList();
    errors.add(validateThis());
    if (validateDescendants) {
      IActionSequenceExecutableStatement[] children = getChildren();
      for (int i = 0; i < children.length; i++) {
        if (children[i] instanceof ActionDefinition) {
          ActionDefinition actionDefinition = (ActionDefinition)children[i];
          errors.addAll(Arrays.asList(actionDefinition.validate()));
        } else if (children[i] instanceof ActionControlStatement) {
          ActionControlStatement actionControlStatement = (ActionControlStatement)children[i];
          errors.addAll(Arrays.asList(actionControlStatement.validate(true)));
        }
      }
    }
    return (ActionSequenceValidationError[])errors.toArray(new ActionSequenceValidationError[0]);
  }
}
