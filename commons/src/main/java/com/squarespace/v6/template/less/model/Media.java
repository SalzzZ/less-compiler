package com.squarespace.v6.template.less.model;

import com.squarespace.v6.template.less.LessException;
import com.squarespace.v6.template.less.core.Buffer;
import com.squarespace.v6.template.less.core.LessUtils;
import com.squarespace.v6.template.less.exec.ExecEnv;


public class Media extends BlockNode {
  
  private Features features;
  
  public Media() {
    features = new Features();
  }

  public Media(Features features) {
    this.features = features;
  }

  public Media(Features features, Block block) {
    super(block);
    this.features = features;
  }
  
  public Media copy(ExecEnv env) throws LessException {
    return new Media((Features)features.eval(env), block.copy());
  }
  
  public Features features() {
    return features;
  }
  
  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Media) {
      Media other = (Media)obj;
      return LessUtils.safeEquals(features, other.features) && LessUtils.safeEquals(block, other.block);
    }
    return false;
  }
  
  @Override
  public NodeType type() {
    return NodeType.MEDIA;
  }
  
  @Override
  public void repr(Buffer buf) {
    buf.append("@media ");
    if (features != null) {
      features.repr(buf);
      buf.append(' ');
    }
    buf.append("{\n");
    buf.incrIndent();
    block.repr(buf);
    buf.decrIndent();
    buf.indent().append("}\n");
  }
  
  @Override
  public void modelRepr(Buffer buf) {
    typeRepr(buf);
    buf.append('\n');
    buf.incrIndent();
    if (features != null) {
      buf.indent();
      features.modelRepr(buf);
      buf.append('\n');
    }
    buf.indent();
    super.modelRepr(buf);
    buf.decrIndent();
  }

}