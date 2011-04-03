#ifdef HOME
#include <gflags/gflags.h>
#include <gtest/gtest.h>

DEFINE_bool(run_all_tests, false, "run all tests");
#endif

#include "math/vector.hpp"


int main(int argc, char **argv) {
#ifdef HOME
  google::ParseCommandLineFlags(&argc, &argv, true);
  if (FLAGS_run_all_tests) {
    testing::InitGoogleTest(&argc, argv);
    return RUN_ALL_TESTS();
  }
#endif
  return 0;
}
