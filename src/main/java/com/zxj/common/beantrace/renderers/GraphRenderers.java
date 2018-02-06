package com.zxj.common.beantrace.renderers;

import java.io.*;

import com.zxj.common.beantrace.BeanTraceException;

/**
 * Basic class to create graph renderers. They can be used to draw different king
 * of bean traces.
 *
 * @see com.zxj.common.beantrace.BeanTraces#printBeanTrace(Object, com.zxj.common.beantrace.renderers.GraphRenderer)
 * @see com.zxj.common.beantrace.TraceConfiguration#setGraphRenderer(com.zxj.common.beantrace.renderers.GraphRenderer)
 */
public class GraphRenderers {

    /**
     * Creates a new {@link GraphvizDotRenderer} that writes the contents to the given
     * appendable.
     */
    public static GraphvizDotRenderer newGraphviz(Writer output) {
        return new GraphvizDotRenderer(output);
    }

    /**
     * Shortcut to create a file graphviz renderer.
     *
     * @see #newGraphviz(Writer)
     */
    public static GraphvizDotRenderer newGraphviz(File outputFile) {
        try {
            return newGraphviz(new BufferedWriter(new FileWriter(outputFile), 1));
        } catch (IOException e) {
            throw new BeanTraceException(e);
        }
    }

    /**
     * Creates a new {@link AsciiRenderer} that prints the graphs to the
     * standard output.
     */
    public static AsciiRenderer newAscii() {
        return newAscii(new PrintWriter(System.out));
    }

    /**
     * Creates a new {@link AsciiRenderer} that prints
     * the graph to the given appendable.
     *
     * @param output Where to write the graph
     * @return the create renderer
     */
    public static AsciiRenderer newAscii(Writer output) {
        final AsciiRenderer ret = new AsciiRenderer();
        ret.getConfig().setOutput(output);
        return ret;
    }

    /**
     * Shortcut to create an ascii renderer that writes to a file.
     *
     * @see #newAscii(Writer)
     */
    public static AsciiRenderer newAscii(File outputFile) {
        try {
            return newAscii(new BufferedWriter(new FileWriter(outputFile), 1));
        } catch (IOException e) {
            throw new BeanTraceException(e);
        }
    }
}
