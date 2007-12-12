package org.pentaho.actionsequence.dom.actions;

import org.dom4j.Element;
import org.pentaho.actionsequence.dom.ActionInput;
import org.pentaho.actionsequence.dom.ActionInputConstant;
import org.pentaho.actionsequence.dom.ActionOutput;
import org.pentaho.actionsequence.dom.ActionSequenceDocument;
import org.pentaho.actionsequence.dom.IActionInput;
import org.pentaho.actionsequence.dom.IActionInputVariable;

public class MQLAction extends AbstractRelationalDbAction {
  public static final String QUERY_RESULT_OUTPUT_NAME = "query-result"; //$NON-NLS-1$
  
  public static final String COMPONENT_NAME = "MQLRelationalDataComponent"; //$NON-NLS-1$
  public static final String OUTPUT_NAME_ELEMENT = "output-name"; //$NON-NLS-1$
  public static final String MQL_ELEMENT = "mql"; //$NON-NLS-1$
  public static final String QUERY_RESULT_ELEMENT = "query-result"; //$NON-NLS-1$
  public static final String MAPPED_QUERY_OUTPUT_NAME = "query_result"; //$NON-NLS-1$
  public static final String OUTPUT_RESULT_SET = "output-result-set"; //$NON-NLS-1$
  public static final String DISABLE_DISTINCT_ELEMENT = "DisableDistinct"; //$NON-NLS-1$
  public static final String FORCE_DB_DIALECT_ELEMENT = "ForceDBDialect"; //$NON-NLS-1$
    
  protected static final String[] EXPECTED_INPUTS = new String[] {
    QUERY_ELEMENT
  };
  
  public MQLAction(Element actionDefElement, IActionParameterMgr actionInputProvider) {
    super(actionDefElement, actionInputProvider);
  }

  public MQLAction() {
    super(COMPONENT_NAME);
  }
  
  public static boolean accepts(Element element) {
    return ActionDefinition.accepts(element) && hasComponentName(element, COMPONENT_NAME);
  }
  
  protected void initNewActionDefinition() {
    super.initNewActionDefinition();
    ActionOutput actionOutput = setOutputParam(QUERY_RESULT_OUTPUT_NAME, QUERY_RESULT_OUTPUT_NAME, ActionSequenceDocument.RESULTSET_TYPE);
    actionOutput.setMapping(MAPPED_QUERY_OUTPUT_NAME);
  }
  
  public String[] getReservedInputNames() {
    return EXPECTED_INPUTS;
  }
  
  public String[] getReservedOutputNames() {
    String expectedOutput = QUERY_RESULT_ELEMENT;
    String compDefVal = getComponentDefinitionValue(OUTPUT_NAME_ELEMENT);
    if (compDefVal != null) {
      expectedOutput = compDefVal;
    } else if (getOutputParam(expectedOutput) == null) {
      ActionOutput[] actionOutputs = getOutputParams(ActionSequenceDocument.RESULTSET_TYPE);
      if (actionOutputs.length > 0) {
        expectedOutput = actionOutputs[0].getName();
      }
    }
    return new String[]{expectedOutput};
  }

  
  public IActionInput getQuery() {
    IActionInput query = super.getQuery();
    
    // The following condition covers an alternative way to store the mql
    // within the action definition. This class does not use this method when
    // writing to the dom.
    if (query.getValue() == null) {
      Element element = getComponentDefElement(MQL_ELEMENT);
      if (element != null) {
        query = new ActionInputConstant(element.asXML(), actionParameterMgr);
      }
    }
    
    return query;
  }

  public void setQuery(IActionInput value) {
    super.setQuery(value);
    
    // The following removes an alternative way to store the mql
    // within the action definition. This class does not use this method when
    // writing to the dom.
    Element element = getComponentDefElement(MQL_ELEMENT);
    if (element != null) {
      element.detach();
    }
  }

  public void setDisableDistinct(IActionInput value) {
    setActionInputValue(DISABLE_DISTINCT_ELEMENT, value);
  }
  
  public IActionInput getDisableDistinct() {
    return getActionInputValue(DISABLE_DISTINCT_ELEMENT);
  }
  
  public void setForceDbDialect(IActionInput value) {
    setActionInputValue(FORCE_DB_DIALECT_ELEMENT, value);
  }
  
  // forces the use of the metadata.xmi's dialect, even if the actual 
  // connection is of a different database.  this may be necessary if 
  // there are issues with detecting the correct database.  The value
  // defaults to false if not specified.
  public IActionInput getForceDbDialect() {
    return getActionInputValue(FORCE_DB_DIALECT_ELEMENT);
  }
}
