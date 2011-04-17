#ifndef IO_READER_HPP
#define IO_READER_HPP

#include <iostream>
#include <vector>


namespace io {
  using std::istream;
  using std::vector;

  template<class V>
  struct TABReader {
    void Read(istream &is, size_t *num_objects, size_t *num_dimensions, size_t *num_clusters, vector<V> *objects) {
      is >> *num_objects >> *num_dimensions >> *num_clusters;

      objects->resize(*num_objects, V(*num_objects));
      for (size_t i = 0; i < *num_objects; ++i)
	for (size_t j = 0; j < *num_dimensions; ++j)
	  is >> (*objects)[i][j];
    }
  }; // struct TABReader
} // namespace io

#endif
