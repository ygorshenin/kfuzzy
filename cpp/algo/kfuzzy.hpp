#ifndef ALGO_KFUZZY_HPP
#define ALGO_KFUZZY_HPP

#include <cmath>

#include <algorithm>
#include <functional>
#include <numeric>
#include <vector>

#include "algo/strategy.hpp"


namespace algo {
  using std::vector;

  template<class V, template<class U> class DistantStrategy = algo::MostDistantStrategy>
  class KFuzzyAlgorithm : public DistantStrategy<V> {
  public:
    struct Options {
      double blending;
      size_t num_clusters;
      size_t max_iterations;

      Options(void): blending(0.), num_clusters(0u), max_iterations(0u) {
      }
    }; // struct Options

  private:
    bool FindCenters(size_t num_clusters, const vector<V> &objects, vector<V> *result) {
      vector<size_t> indexes;

      if (!Find(objects, num_clusters, &indexes))
	return false;

      result->clear();
      result->reserve(num_clusters);
      for (size_t i = 0; i < num_clusters; ++i)
	result->push_back(objects[indexes[i]]);
      return true;
    }

    // Returns true, if no significant change in probabilities vector
    void FindProbabilities(const V &point, const vector<V> &centers, const Options &options, double *probabilities) {
      double power = 1. / (options.blending - 1.);

      for (size_t i = 0; i < options.num_clusters; ++i) {
	double distance = point.Distance(centers[i]);
	probabilities[i] = pow(1. / distance, power);
      }
      double total = std::accumulate(probabilities, probabilities + options.num_clusters, 0.);
      std::transform(probabilities, probabilities + options.num_clusters, probabilities, std::bind2nd(std::divides<double>(), total));
    }

    void FindProbabilities(const vector<V> &objects, const vector<V> &centers, const Options &options, double **probabilities) {
      for (size_t i = 0; i < objects.size(); ++i)
	FindProbabilities(objects[i], centers, options, probabilities[i]);
    }

    void RecomputeCenter(const vector<V> &objects, size_t cluster_no, double **probabilities, const Options &options, V &center) {
      double norm = 0.;
      for (size_t i = 0; i < objects.size(); ++i)
	norm += pow(probabilities[i][cluster_no], options.blending);

      for (size_t j = 0; j < center.Size(); ++j) {
	center[j] = V::kZero;
	for (size_t i = 0; i < objects.size(); ++i)
	  center[j] += objects[i][j] * pow(probabilities[i][cluster_no], options.blending) / norm;
      }
    }

    void RecomputeCenters(const vector<V> &objects, double **probabilities, const Options &options, vector<V> &centers) {
      for (size_t i = 0; i < options.num_clusters; ++i)
	RecomputeCenter(objects, i, probabilities, options, centers[i]);
    }

  public:
    bool Clusterize(const vector<V> &objects, const Options &options, vector<int> *indexes) {
      const size_t num_objects = objects.size();
      const size_t num_clusters = std::min(options.num_clusters, num_objects / 2);

      if (num_clusters == 0)
	return num_objects == 0; // if set of objects is empty, it's possible to split into zero clusters.
      if (num_objects == 0)
	return true;
      if (!indexes)
	return false;

      vector<V> centers;
      if (!FindCenters(num_clusters, objects, &centers))
	return false;

      // probabilities[i][j] is a probability of ith object belong to cluster with no j
      double **probabilities = new double*[num_objects];
      for (size_t i = 0; i < num_objects; ++i)
	probabilities[i] = new double[num_clusters];

      FindProbabilities(objects, centers, options, probabilities);
      for (size_t iteration = 0; iteration < options.max_iterations; ++iteration) {
	RecomputeCenters(objects, probabilities, options, centers);
	FindProbabilities(objects, centers, options, probabilities);
      }

      for (size_t i = 0; i < num_objects; ++i)
	delete [] probabilities[i];
      delete [] probabilities;
      return true;
    }
  }; // class KFuzzyAlgorithm
} // namespace algo

#endif
