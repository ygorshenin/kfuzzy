#ifndef ALGO_STRATEGY_HPP
#define ALGO_STRATEGY_HPP

#include <cassert>

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
      Finds m the most distant (from each other) vectors. If m is
      greater than a number of given vectors, returns false.
      Otherwise, result will contain exactly m integers, where
      integers are indexes of corresponding vectors.
    */
    bool Find(const vector<V> &vectors, size_t m, vector<size_t> *result) const;

  private:
    typedef vector<pair<Type, size_t> > ContainerType;
    typedef typename ContainerType::iterator IteratorType;


    void FindTwoMostDistantVectors(size_t n, const vector<V> &vectors, size_t *u, size_t *v) const;

    void Relax(IteratorType from,
	       IteratorType to,
	       const vector<V> &vectors,
	       const V &v) const;

    bool InternalFind(size_t n, const vector<V> &vectors, size_t m, vector<size_t> *result) const;
  }; // class MostDistantStrategy

  template<class V>
  bool MostDistantStrategy<V>::Find(const vector<V> &vectors, size_t m, vector<size_t> *result) const {
    size_t n = vectors.size();
    // begin verification of user input
    if (m > n)
      return false;
    if (!result)
      return false;
    for (size_t i = 1; i < n; ++i)
      if (vectors[i].Size() != vectors[i - 1].Size())
    	return false;
    // end verification of user input
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
  void MostDistantStrategy<V>::FindTwoMostDistantVectors(size_t n, const vector<V> &vectors, size_t *u, size_t *v) const {
    *u = 0, *v = 1;
    Type best = vectors[0].Distance(vectors[1]), tmp;

    for (size_t i = 0; i < n; ++i)
      for (size_t j = i + 1; j < n; ++j) {
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
  bool MostDistantStrategy<V>::InternalFind(size_t n, const vector<V> &vectors, size_t m, vector<size_t> *result) const {
    size_t u, v;
    FindTwoMostDistantVectors(n, vectors, &u, &v);

    ContainerType distance(n);
    distance[0].second = u;
    distance[1].second = v;

    for (size_t w = 0, index = 2; w < n; ++w)
      if (w != u && w != v) {
	distance[index].first = std::numeric_limits<Type>::infinity();
	distance[index].second = w;
	++index;
      }
    Relax(distance.begin() + 2, distance.end(), vectors, vectors[u]);
    Relax(distance.begin() + 2, distance.end(), vectors, vectors[v]);

    for (size_t i = 2; i < m; ++i) {
      IteratorType from = distance.begin() + i, to = distance.end();
      IteratorType best = std::max_element(from, to);
      std::swap(*from, *best);
      Relax(from + 1, to, vectors, vectors[from->second]);
    }

    for (size_t i = 0; i < m; ++i)
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
    vector<size_t> result;

    EXPECT_FALSE(strategy_->Find(vector<math::Vector>(), -1, &result));
    EXPECT_FALSE(strategy_->Find(vector<math::Vector>(), 1, &result));
    EXPECT_FALSE(strategy_->Find(vector<math::Vector>(), 2, &result));

    const size_t kSize = 10u;
    math::Vector v(2);
    vector<math::Vector> vectors(kSize, v);
    EXPECT_FALSE(strategy_->Find(vectors, -10, &result));
    EXPECT_FALSE(strategy_->Find(vectors, kSize + 1, &result));
    EXPECT_FALSE(strategy_->Find(vectors, 2 * kSize, &result));
  }

  TEST_F(TestMostDistantStrategy, TestZero) {
    vector<size_t> result;

    ASSERT_TRUE(strategy_->Find(vector<math::Vector>(), 0, &result));
    EXPECT_EQ(0u, result.size());

    ASSERT_TRUE(strategy_->Find(vector<math::Vector>(10, math::Vector(2)), 0, &result));
    EXPECT_EQ(0u, result.size());
  }

  TEST_F(TestMostDistantStrategy, TestOne) {
    vector<size_t> result;

    ASSERT_TRUE(strategy_->Find(vector<math::Vector>(1, math::Vector(3)), 1, &result));
    ASSERT_EQ(1u, result.size());
    EXPECT_EQ(0, result[0]);

    const size_t kSize = 16u;
    ASSERT_TRUE(strategy_->Find(vector<math::Vector>(kSize, math::Vector(2)), 1, &result));
    ASSERT_EQ(1u, result.size());
    EXPECT_TRUE(result[0] >= 0 && result[0] < kSize);
  }

  TEST_F(TestMostDistantStrategy, TestTwo) {
    vector<size_t> result;

    vector<math::Vector> vectors(2, math::Vector(3));
    ASSERT_TRUE(strategy_->Find(vectors, 2, &result));
    ASSERT_EQ(2u, result.size());
    EXPECT_TRUE((result[0] == 0 && result[1] == 1) || (result[0] == 1 && result[1] == 0));

    math::Vector u(3);
    math::Vector v(3); v[0] = 0, v[1] = 3.0;
    math::Vector w(3); w[0] = 4.0, w[1] = 0.0;
    vectors.resize(3, math::Vector(3));
    vectors[0] = u;
    vectors[1] = v;
    vectors[2] = w;
    ASSERT_TRUE(strategy_->Find(vectors, 2, &result));
    ASSERT_EQ(2u, result.size());
    EXPECT_TRUE((result[0] == 1 && result[1] == 2) || (result[0] == 2 && result[1] == 1));
  }

  TEST_F(TestMostDistantStrategy, TestManyAllEqual) {
    const size_t kSize = 4u;
    vector<size_t> result;
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
    const size_t kSize = 4u;
    vector<size_t> result;
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
