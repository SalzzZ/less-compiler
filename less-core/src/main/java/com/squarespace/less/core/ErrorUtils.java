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

package com.squarespace.less.core;

import java.nio.file.Path;

import com.squarespace.less.LessContext;
import com.squarespace.less.LessException;


/**
 * Utility methods for error handling, formatting, etc.
 */
public class ErrorUtils {

  /**
   * Default amount of the stack frame to show in an error message.
   */
  private static final int STACK_FRAME_WINDOW = 6;

  private ErrorUtils() {
  }

  /**
   * Formats an error message including a full stack trace, in a reusable buffer.
   */
  public static String formatError(LessContext ctx, Path mainPath, LessException exc, int indent) {
    Buffer buf = ctx.acquireBuffer();
    String pathName = (mainPath == null) ? "<source>" : mainPath.toString();
    String error = formatError(buf, pathName, exc, indent);
    ctx.returnBuffer();
    return error;
  }

  /**
   * Formats an error message including a full stack trace, in a newly-allocated buffer.
   */
  public static String formatError(Path mainPath, LessException exc, int indent) {
    Buffer buf = new Buffer(indent);
    return formatError(buf, mainPath.toString(), exc, indent);
  }

  /**
   * Formats an error message including a full stack trace.
   */
  public static String formatError(Buffer buf, String mainPath, LessException exc, int indent) {
    buf.append("An error occurred in '" + mainPath + "':\n\n");
    StackFormatter fmt = new StackFormatter(exc.errorContext(), 4, STACK_FRAME_WINDOW);
    buf.append(fmt.format()).append('\n');
    buf.append(exc.primaryError().getMessage());
    return buf.toString();
  }

}
