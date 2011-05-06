This project is an implementation of a clustering k-fuzzy algorithm.

Problem: set of points in n-dimensional Euclidian space are given to
algorithm. Also expected number of clusters is given. Our task is to
determine partition of points among these clusters. In this case,
k-fuzzy algorithm is used.

Used patterns:
1. Strategy. Implementation of kfuzzy algorithm is extracted into
different class, so, it's easy to change used algorithm on the
fly. Another example of Strategy is an algorithm to determine initial
centers of clusters.

2. MVC. Of cource, this is an application with GUI, so, there is MVC
is used (with some modifications, View and Controller are combined).

3. Adapter. Output from method that finds initial cluster centers is
adapted to input of KFuzzy algorithm.

4. Observer. Application has an internal state (unopened,
unclusterized (but opened), clusterized).  Some graphical components
need to change their accessebility. So, they are simply listeners of
internal state.
