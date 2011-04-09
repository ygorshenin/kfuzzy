#ifndef IO_READER_HPP
#define IO_READER_HPP

#include <iostream>
#include <vector>


namespace io {
  using std::istream;
  using std::vector;

  template<class V>
  struct TABReader {
    void Read(istream &is, int *num_objects, int *num_dimensions, int *num_clusters, vector<V> *objects) {
      is >> *num_objects >> *num_dimensions >> *num_clusters;

      objects->resize(*num_objects, V(*num_objects));
      for (int i = 0; i < *num_objects; ++i)
	for (int j = 0; j < *num_dimensions; ++j)
	  is >> (*objects)[i][j];
    }
  }; // struct TABReader
} // namespace io

#endif
