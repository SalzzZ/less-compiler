package com.squarespace.v6.template.less.model;

import static com.squarespace.v6.template.less.core.LessUtils.safeEquals;

import com.squarespace.v6.template.less.ExecuteErrorType;
import com.squarespace.v6.template.less.LessException;
import com.squarespace.v6.template.less.core.Buffer;
import com.squarespace.v6.template.less.core.ErrorUtils;
import com.squarespace.v6.template.less.exec.ExecEnv;


/**
 * Special rule which represents a variable definition.
 */
public class Definition extends BaseNode {

  private String name;
  
  private Node value;

  // Flag to detect late-binding circular references and raise an error.
  private boolean evaluating;

  public Definition(Variable variable, Node value) {
    this(variable.name(), value);
  }
  
  public Definition(String name, Node value) {
    if (name == null || value == null) {
      throw new IllegalArgumentException("Serious error: name/value cannot be null.");
    }
    this.name = name;
    this.value = value;
  }
  
  public String name() {
    return name;
  }
  
  public Node value() {
    return value;
  }

  /**
   * Resolve the value for this definition.
   */
  public Node dereference(ExecEnv env) throws LessException {
    if (evaluating) {
      throw new LessException(ErrorUtils.error(ExecuteErrorType.VAR_CIRCULAR_REFERENCE).name(name));
    }
    evaluating = true;
    Node result = value.eval(env);
    evaluating = false;
    return result;
  }
  
  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Definition) {
      Definition other = (Definition)obj;
      return safeEquals(name, other.name) && safeEquals(value, other.value);
    }
    return false;
  }
  
  @Override
  public NodeType type() {
    return NodeType.DEFINITION;
  }
  
  @Override
  public Node eval(ExecEnv env) throws LessException {
    // Late binding. Definitions must be explicitly dereferenced to evaluate them.
    return this;
  }
  
  @Override
  public void repr(Buffer buf) {
    buf.append(name).append(": ");
    value.repr(buf);
    buf.append(";\n");
  }
  
  @Override
  public void modelRepr(Buffer buf) {
    typeRepr(buf);
    buf.append(' ').append(name).append('\n');
    if (value != null) {
      buf.incrIndent().indent();
      value.modelRepr(buf);
      buf.decrIndent().append('\n');
    }
  }
  
}