#ifndef ALGO_KFUZZY_HPP
#define ALGO_KFUZZY_HPP

#include "algo/strategy.hpp"

namespace algo {
  using std::vector;

  template<class V, template<class U> class S>
  class KFuzzyAlgorithm : public S<V> {
  public:
    bool Clusterize(int num_objects, int num_dimensions, int num_clusters, const vector<V> &objects, vector<int> *indexes) {
      return true;
    }
  }; // class KFuzzyAlgorithm
} // namespace algo

#endif
