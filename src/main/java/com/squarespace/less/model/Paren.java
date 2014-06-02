/**
 * Copyright (c) 2014 SQUARESPACE, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.squarespace.less.model;

import static com.squarespace.less.core.LessUtils.safeEquals;

import com.squarespace.less.LessException;
import com.squarespace.less.core.Buffer;
import com.squarespace.less.exec.ExecEnv;


public class Paren extends BaseNode {

  private final Node node;

  public Paren(Node node) {
    this.node = node;
  }

  public Node node() {
    return node;
  }

  @Override
  public boolean equals(Object obj) {
    return (obj instanceof Paren) ? safeEquals(node, ((Paren)obj).node) : false;
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }

  @Override
  public NodeType type() {
    return NodeType.PAREN;
  }

  @Override
  public boolean needsEval() {
    return node.needsEval();
  }

  @Override
  public Node eval(ExecEnv env) throws LessException {
    return node.needsEval() ? new Paren(node.eval(env)) : this;
  }

  @Override
  public void repr(Buffer buf) {
    buf.append('(');
    node.repr(buf);
    buf.append(')');
  }

  @Override
  public void modelRepr(Buffer buf) {
    typeRepr(buf);
    posRepr(buf);
    buf.append('\n');
    if (node != null) {
      buf.incrIndent();
      buf.indent();
      node.modelRepr(buf);
      buf.decrIndent();
    }
  }

}
