package com.squarespace.v6.template.less.model;

import static com.squarespace.v6.template.less.core.LessUtils.safeEquals;

import com.squarespace.v6.template.less.Context;
import com.squarespace.v6.template.less.LessException;
import com.squarespace.v6.template.less.core.Buffer;
import com.squarespace.v6.template.less.exec.ExecEnv;


public class Import extends BaseNode {

  private Node path;

  private Features features;
  
  private boolean once;
  
  public Import(Node path, Features features, boolean once) {
    this.path = path;
    this.features = features;
    this.once = once;
  }
  
  // XXX:
  // 1. evaluate path
  // 2. lookup path in cache to see if rules are already parsed
  //    a. hit: return those rules
  //    b. miss: parse rules and store in cache, then return.

  public Node path() {
    return path;
  }
  
  public Features features() {
    return features;
  }

  public boolean once() {
    return once;
  }
  
  public String renderPath(ExecEnv env) throws LessException {
    Node value = path;
    if (value.is(NodeType.URL)) {
      value = ((Url)value).value();
    }

    Context ctx = env.context();
    Quoted quoted = null;
    String rendered = null;
    if (value.is(NodeType.QUOTED)) {
      // Strip quote delimiters and render inner string. This technique allows
      // for variable substitution inside @import paths, which may or may not
      // be useful.
      quoted = ((Quoted)value).copy();
      quoted.setEscape(true);
      rendered = ctx.render(quoted);

    } else {
      rendered = ctx.render(value);
    }
    return rendered;
  }
  
  @Override
  public boolean needsEval() {
    return path.needsEval() || (features != null && features.needsEval());
  }

  @Override
  public Node eval(ExecEnv env) throws LessException {
    if (!needsEval()) {
      return this;
    }
    return new Import(path.eval(env), features == null ? null : (Features)features.eval(env), once);
  }
  
  @Override
  public boolean equals(Object obj) {
    return (obj instanceof Import) ? safeEquals(path, ((Import)obj).path) : false; 
  }
  
  @Override
  public NodeType type() {
    return NodeType.IMPORT;
  }
  
  @Override
  public void repr(Buffer buf) {
    buf.append("@import");
    if (once) {
      buf.append("-once ");
    }
    path.repr(buf);
    if (features != null) {
      buf.append(" ");
      features.repr(buf);
    }
    buf.append(";\n");
  }
  
  @Override
  public void modelRepr(Buffer buf) {
    typeRepr(buf);
    if (once) {
      buf.append(" [once] ");
    }
    buf.append('\n').incrIndent().indent();
    path.modelRepr(buf);
    buf.append('\n');
    if (features != null) {
      buf.indent();
      ReprUtils.modelRepr(buf, "\n", true, features.features());
    }
    buf.decrIndent();
  }
  
}