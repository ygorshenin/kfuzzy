package kfuzzy.io;

import java.io.*;

/**
 * An interface to readers of KFuzzy algorithm input.
 *
 * @author Yuri Gorshenin
 * @version 2011.0424
 * @since 1.6
 */
public interface ReaderInterface {
    /**
     * Reads from input stream all data necessary to KFuzzy algorithm.
     *
     * @param in input stream
     * @return input to KFuzzy algorithm
     * @throws IOException in the case of problems with IO
     */
    KFuzzyInput read(InputStreamReader in) throws IOException;
}
