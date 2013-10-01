package com.squarespace.v6.template.less.model;

import static com.squarespace.v6.template.less.core.LessUtils.safeEquals;

import java.util.List;

import com.squarespace.v6.template.less.LessException;
import com.squarespace.v6.template.less.core.Buffer;
import com.squarespace.v6.template.less.core.LessUtils;
import com.squarespace.v6.template.less.exec.ExecEnv;


public class MixinCallArgs extends BaseNode {

  private List<Argument> args;
  
  private char delimiter;

  private boolean evaluate;

  public MixinCallArgs(char delimiter) {
    this.delimiter = delimiter;
  }
  
  public char delim() {
    return delimiter;
  }
  
  public List<Argument> args() {
    return LessUtils.safeList(args);
  }
  
  public void add(Argument arg) {
    if (arg == null) {
      throw new IllegalArgumentException("Serious error: arg cannot be null.");
    }
    args = LessUtils.initList(args, 3);
    args.add(arg);
    evaluate |= arg.needsEval();
  }

  public boolean isEmpty() {
    return args().isEmpty();
  }
  
  @Override
  public boolean equals(Object obj) {
    if (obj instanceof MixinCallArgs) {
      MixinCallArgs other = (MixinCallArgs)obj;
      return delimiter == other.delimiter && safeEquals(args, other.args);
    }
    return false;
  }
  
  @Override
  public NodeType type() {
    return NodeType.MIXIN_ARGS;
  }

  @Override
  public boolean needsEval() {
    return evaluate;
  }
  
  @Override
  public Node eval(ExecEnv env) throws LessException {
    if (!evaluate) {
      return this;
    }
    MixinCallArgs res = new MixinCallArgs(delimiter);
    for (Argument arg : args) {
      res.add((Argument)arg.eval(env));
    }
    return res;
  }
  
  @Override
  public void repr(Buffer buf) {
    buf.append('(');
    if (args != null) {
      int size = args.size();
      for (int i = 0; i < size; i++) {
        if (i > 0) {
          buf.append(delimiter).append(' ');
        }
        args.get(i).repr(buf);
      }
    }
    buf.append(')');
  }
  
  @Override
  public void modelRepr(Buffer buf) {
    buf.append(type().toString()).append(" delim=").append(delimiter).append('\n');
    buf.incrIndent();
    ReprUtils.modelRepr(buf, "\n", true, args);
    buf.decrIndent();
  }
  
}