package kfuzzy.utils;

/**
 * Pair is a class that represents simple key-value pair. Contains two
 * final fields for key and value. This class is thread-safe.
 *
 * @author Yuri Gorshenin
 * @version 2011.0418
 * @since 1.6
 */
public class Pair<K, V> {
    /**
     * Key of the pair
     */
    public final K first;
    /**
     * Value of the pair
     */
    public final V second;
    /**
     * Class constructor, takes key and value of the pair as arguments.
     *
     * @param first key of the pair
     * @param second value of the pair
     */
    public Pair(K first, V second) {
	this.first = first;
	this.second = second;
    }
}
