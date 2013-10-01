package com.squarespace.v6.template.less.parse;

import static com.squarespace.v6.template.less.SyntaxErrorType.EXPECTED_MISC;
import static com.squarespace.v6.template.less.core.ErrorUtils.error;
import static com.squarespace.v6.template.less.parse.Parselets.ALPHA;
import static com.squarespace.v6.template.less.parse.Parselets.ASSIGNMENT;
import static com.squarespace.v6.template.less.parse.Parselets.EXPRESSION;
import static com.squarespace.v6.template.less.parse.Parselets.QUOTED;
import static com.squarespace.v6.template.less.parse.Parselets.VARIABLE;

import com.squarespace.v6.template.less.LessException;
import com.squarespace.v6.template.less.core.CharClass;
import com.squarespace.v6.template.less.core.Chars;
import com.squarespace.v6.template.less.model.Anonymous;
import com.squarespace.v6.template.less.model.FunctionCall;
import com.squarespace.v6.template.less.model.Node;
import com.squarespace.v6.template.less.model.Url;


public class FunctionCallParselet implements Parselet {

  @Override
  public Node parse(LessStream stm) throws LessException {
    Mark position = stm.mark();
    if (!CharClass.callStart(stm.peek()) || !stm.matchCallName()) {
      return null;
    }

    String name = stm.token();
    String nameLC = name.toLowerCase();
    if (nameLC.equals("url")) {
      return parseUrl(stm);
    }
    
    // Special handling for IE's alpha function.
    if (nameLC.equals("alpha")) {
      Node result = stm.parse(ALPHA);
      if (result != null) {
        return result;
      }
      // Fall through, assuming the built-in alpha function.
    }
  
    FunctionCall call = new FunctionCall(name);
    parseArgs(stm, call);

    stm.skipWs();
    if (!stm.seekIf(Chars.RIGHT_PARENTHESIS)) {
      stm.restore(position);
      return null;
    }
    return call;
  }
  

  private void parseArgs(LessStream stm, FunctionCall call) throws LessException {
    while (true) {
      Node value = stm.parse(ASSIGNMENT, EXPRESSION);
      if (value != null) {
        call.add(value);
      }
      
      stm.skipWs();
      if (!stm.seekIf(Chars.COMMA)) {
        break;
      }
    }
  }

  public static Node parseUrl(LessStream stm) throws LessException {
    stm.skipWs();
    Node value = stm.parse(QUOTED, VARIABLE);
    if (value == null && stm.matchUrlEndBare()) {
      value = new Anonymous(stm.token());
    }
    stm.skipWs();
    if (!stm.seekIf(Chars.RIGHT_PARENTHESIS)) {
      // XXX: use error handler.
      throw new LessException(error(EXPECTED_MISC).arg0(')').arg1(stm.remainder()));
    }
    return new Url(value);
  }
}