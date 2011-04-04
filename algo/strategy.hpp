#ifndef ALGO_STRATEGY_HPP
#define ALGO_STRATEGY_HPP

#include <algorithm>
#include <iterator>
#include <limits>
#include <memory>
#include <utility>
#include <vector>

#ifdef HOME
#include <gtest/gtest.h>
#include "math/vector.h"
#endif


namespace algo {
  using std::vector;
  using std::pair;

  template<class V>
  class MostDistantStrategy {
  public:
    typedef typename V::Type Type;

    /*
      Finds m the most distant (from each other) vectors.  If m is
      negative or greater than a number of given vectors, returns false.
      Otherwise, result will contain exactly m integers, where integers
      are indexes of corresponding vectors.
    */
    bool Find(const vector<V> &vectors, int m, vector<int> *result) const;

  private:
    typedef vector<pair<Type, int> > ContainerType;
    typedef typename ContainerType::iterator IteratorType;


    void FindTwoMostDistantVectors(int n, const vector<V> &vectors, int *u, int *v) const;

    void Relax(IteratorType from,
	       IteratorType to,
	       const vector<V> &vectors,
	       const V &v) const;

    bool InternalFind(int n, const vector<V> &vectors, int m, vector<int> *result) const;
  }; // class MostDistantStrategy

  template<class V>
  bool MostDistantStrategy<V>::Find(const vector<V> &vectors, int m, vector<int> *result) const {
    int n = vectors.size();
    if (m < 0 || m > n)
      return false;
    result->resize(m);
    if (m == 0)
      return true;
    else if (m == 1) {
      (*result)[0] = 0;
      return true;
    } else
      return InternalFind(n, vectors, m, result);
  }

  template<class V>
  void MostDistantStrategy<V>::FindTwoMostDistantVectors(int n, const vector<V> &vectors, int *u, int *v) const {
    *u = 0, *v = 1;
    Type best = vectors[0].Distance(vectors[1]), tmp;

    for (int i = 0; i < n; ++i)
      for (int j = i + 1; j < n; ++j) {
	tmp = vectors[i].Distance(vectors[j]);
	if (tmp > best) {
	  best = tmp;
	  *u = i, *v = j;
	}
      }
  }

  template<class V>
  void MostDistantStrategy<V>::Relax(IteratorType from,
				     IteratorType to,
				     const vector<V> &vectors,
				     const V &v) const {
    while (from != to) {
      from->first = std::min(from->first, vectors[from->second].Distance(v));
      ++from;
    }
  }

  template<class V>
  bool MostDistantStrategy<V>::InternalFind(int n, const vector<V> &vectors, int m, vector<int> *result) const {
    int u, v;
    FindTwoMostDistantVectors(n, vectors, &u, &v);

    ContainerType distance(n);
    distance[0].second = u;
    distance[1].second = v;

    for (int w = 0, index = 2; w < n; ++w)
      if (w != u && w != v) {
	distance[index].first = std::numeric_limits<Type>::infinity();
	distance[index].second = w;
	++index;
      }
    Relax(distance.begin() + 2, distance.end(), vectors, vectors[u]);
    Relax(distance.begin() + 2, distance.end(), vectors, vectors[v]);

    for (int i = 2; i < m; ++i) {
      IteratorType from = distance.begin() + i, to = distance.end();
      IteratorType best = std::max_element(from, to);
      std::swap(*from, *best);
      Relax(from + 1, to, vectors, vectors[from->second]);
    }

    for (int i = 0; i < m; ++i)
      (*result)[i] = distance[i].second;
    return true;
  }

#ifdef HOME
  class TestMostDistantStrategy: public ::testing::Test {
  protected:
    void SetUp(void) {
      strategy_.reset(new algo::MostDistantStrategy<math::Vector>());
    }

    std::auto_ptr<algo::MostDistantStrategy<math::Vector> > strategy_;
  };

  TEST_F(TestMostDistantStrategy, TestIncorrect) {
    vector<int> result;

    EXPECT_FALSE(strategy_->Find(vector<math::Vector>(), -1, &result));
    EXPECT_FALSE(strategy_->Find(vector<math::Vector>(), 1, &result));
    EXPECT_FALSE(strategy_->Find(vector<math::Vector>(), 2, &result));

    const int kSize = 10;
    math::Vector v(2);
    vector<math::Vector> vectors(kSize, v);
    EXPECT_FALSE(strategy_->Find(vectors, -10, &result));
    EXPECT_FALSE(strategy_->Find(vectors, kSize + 1, &result));
    EXPECT_FALSE(strategy_->Find(vectors, 2 * kSize, &result));
  }

  TEST_F(TestMostDistantStrategy, TestZero) {
    vector<int> result;

    ASSERT_TRUE(strategy_->Find(vector<math::Vector>(), 0, &result));
    EXPECT_EQ(0u, result.size());

    ASSERT_TRUE(strategy_->Find(vector<math::Vector>(10, math::Vector(2)), 0, &result));
    EXPECT_EQ(0u, result.size());
  }

  TEST_F(TestMostDistantStrategy, TestOne) {
    vector<int> result;

    ASSERT_TRUE(strategy_->Find(vector<math::Vector>(1, math::Vector(3)), 1, &result));
    ASSERT_EQ(1u, result.size());
    EXPECT_EQ(0, result[0]);

    const int kSize = 16;
    ASSERT_TRUE(strategy_->Find(vector<math::Vector>(kSize, math::Vector(2)), 1, &result));
    ASSERT_EQ(1u, result.size());
    EXPECT_TRUE(result[0] >= 0 && result[0] < kSize);
  }

  TEST_F(TestMostDistantStrategy, TestTwo) {
    vector<int> result;

    vector<math::Vector> vectors(2, math::Vector(3));
    ASSERT_TRUE(strategy_->Find(vectors, 2, &result));
    ASSERT_EQ(2u, result.size());
    EXPECT_TRUE((result[0] == 0 && result[1] == 1) || (result[0] == 1 && result[1] == 0));

    math::Vector u(2);
    math::Vector v(2); v[0] = 0, v[1] = 3.0;
    math::Vector w(2); w[0] = 4.0, w[1] = 0.0;
    vectors.resize(3, math::Vector(2));
    vectors[0] = u;
    vectors[1] = v;
    vectors[2] = w;
    ASSERT_TRUE(strategy_->Find(vectors, 2, &result));
    ASSERT_EQ(2u, result.size());
    EXPECT_TRUE((result[0] == 1 && result[1] == 2) || (result[0] == 2 && result[1] == 1));
  }

  TEST_F(TestMostDistantStrategy, TestManyAllEqual) {
    const int kSize = 4;
    vector<int> result;
    vector<math::Vector> vectors(kSize, math::Vector(2));
    EXPECT_TRUE(strategy_->Find(vectors, kSize, &result));
    ASSERT_EQ(kSize, result.size());
    std::sort(result.begin(), result.end());
    EXPECT_EQ(0, result[0]);
    EXPECT_EQ(1, result[1]);
    EXPECT_EQ(2, result[2]);
    EXPECT_EQ(3, result[3]);
  }

  TEST_F(TestMostDistantStrategy, TestManyFour) {
    const int kSize = 4;
    vector<int> result;
    vector<math::Vector> vectors(kSize, math::Vector(2));
    vectors[0][0] = -1.0, vectors[0][1] = 0.0;
    vectors[1][0] = +1.0, vectors[1][1] = 2.0;
    vectors[2][0] = +4.0, vectors[2][1] = 2.0;
    vectors[3][0] = -1.0, vectors[3][1] = -1.0;

    ASSERT_TRUE(strategy_->Find(vectors, 0, &result));
    EXPECT_EQ(0u, result.size());

    ASSERT_TRUE(strategy_->Find(vectors, 1, &result));
    ASSERT_EQ(1u, result.size());
    EXPECT_TRUE(result[0] >= 0 && result[0] < kSize);

    ASSERT_TRUE(strategy_->Find(vectors, 2, &result));
    ASSERT_EQ(2u, result.size());
    std::sort(result.begin(), result.end());
    EXPECT_TRUE(result[0] == 2 && result[1] == 3);

    ASSERT_TRUE(strategy_->Find(vectors, 3, &result));
    ASSERT_EQ(3u, result.size());
    std::sort(result.begin(), result.end());
    EXPECT_TRUE(result[0] == 1 && result[1] == 2 && result[2] == 3);

    ASSERT_TRUE(strategy_->Find(vectors, 4, &result));
    ASSERT_EQ(4u, result.size());
    std::sort(result.begin(), result.end());
    EXPECT_TRUE(result[0] == 0 && result[1] == 1 && result[2] == 2 && result[3] == 3);
  }
#endif
} // namespace algo

#endif
