#ifdef HOME
#include <gflags/gflags.h>
#include <gtest/gtest.h>

DEFINE_bool(run_all_tests, false, "run all tests");
#endif

#include "algo/kfuzzy.hpp"
#include "algo/strategy.hpp"
#include "io/reader.hpp"
#include "math/vector.h"

#include <iostream>
#include <vector>


int main(int argc, char **argv) {
#ifdef HOME
  google::ParseCommandLineFlags(&argc, &argv, true);
  if (FLAGS_run_all_tests) {
    testing::InitGoogleTest(&argc, argv);
    return RUN_ALL_TESTS();
  }
#endif
  int num_objects, num_dimensions, num_clusters;
  std::vector<math::Vector> objects;

  io::TABReader<math::Vector> reader;
  reader.Read(std::cin, &num_objects, &num_dimensions, &num_clusters, &objects);

  std::vector<int> indexes;
  algo::KFuzzyAlgorithm<math::Vector> algo;
  algo.Clusterize(num_objects, num_dimensions, num_clusters, objects, 1.0, &indexes);
  return 0;
}
