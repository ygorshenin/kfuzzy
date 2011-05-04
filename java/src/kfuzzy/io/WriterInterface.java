package kfuzzy.io;

import java.io.*;

/**
 * An interface to writers of KFuzzy algorithm output.
 *
 * @author Yuri Gorshenin
 * @version 2011.0505
 * @since 1.6
 */
public interface WriterInterface {
    /**
     * Writes to output stream all resulting data.
     *
     * @param outputStream an output stream
     * @param output an instance of KFuzzyOutput
     * @return true, in the case of success
     */
    boolean write(OutputStreamWriter outputStream, KFuzzyOutput output);
}
