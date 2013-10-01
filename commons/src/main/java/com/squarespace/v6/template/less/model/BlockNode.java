package com.squarespace.v6.template.less.model;

import static com.squarespace.v6.template.less.core.LessUtils.safeEquals;

import com.squarespace.v6.template.less.core.Buffer;


public abstract class BlockNode extends BaseNode {

  protected Block block;

  protected BlockNode originalBlockNode;

  protected boolean important;
  
  public BlockNode() {
    this(new Block());
  }
  
  public BlockNode(Block block) {
    this.block = block;
    this.originalBlockNode = this;
  }
  
  public Block block() {
    return block;
  }
  
  public BlockNode original() {
    return originalBlockNode;
  }
  
  public void markOriginal() {
    originalBlockNode = this;
  }

  public boolean important() {
    return important;
  }
  
  public void markImportant() {
    important = true;
  }
  
  public void add(Node node) {
    block.append(node);
  }
  
  public void setBlock(Block block) {
    this.block = block;
  }
  
  @Override
  public boolean equals(Object obj) {
    if (obj instanceof BlockNode) {
      BlockNode other = (BlockNode)obj;
      return important == other.important
          && safeEquals(block, ((BlockNode)obj).block);
    }
    return false;
  }

  @Override
  public void modelRepr(Buffer buf) {
    if (block != null) {
      block.modelRepr(buf);
    }
  }
  
}