package com.squarespace.v6.template.less.model;

import java.nio.file.Path;


/**
 * A dummy node which is placed into a block to indicate where a MIXIN
 * call's generated nodes begin and end.
 */
public class MixinMarker extends BaseNode {

  private MixinCall call;
  
  private Path fileName;
  
  private boolean beginning;
  
  public MixinMarker(MixinCall call, boolean begin) {
    this.call = call;
    this.beginning = begin;
  }
  
  public MixinCall mixinCall() {
    return call;
  }
  
  public Path fileName() {
    return fileName;
  }

  public boolean beginning() {
    return beginning;
  }

  public void fileName(Path path) {
    this.fileName = path;
  }
  
  @Override
  public NodeType type() {
    return NodeType.MIXIN_MARKER;
  }

}