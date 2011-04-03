#ifndef MATH_VECTOR_HPP
#define MATH_VECTOR_HPP

#include <algorithm>
#include <functional>

#ifdef HOME
#include <gtest/gtest.h>
#endif HOME


namespace math {
  using std::copy;
  using std::fill;
  using std::minus;
  using std::transform;

  /*
    Represents a simple vector in n-dimensional euclidian space.
  */
  template<int length>
  class Vector {
  public:
    typedef double CoordType;

    static const CoordType kZero = 0;
    static const CoordType kEpsilon = 1e-9;

    Vector(void);

    Vector(const Vector<length> &other);

    void Sub(const Vector<length> &rhs, Vector<length> *result);
    CoordType Abs(void) const;

    CoordType& operator [] (int index);
    const CoordType& operator [] (int index) const;

  private:
    static bool EQ(CoordType u, CoordType v) {
      return abs(u - v) < kEpsilon;
    }

    CoordType coord_[length];
  };

  template<int length>
  const typename Vector<length>::CoordType Vector<length>::kZero;

  template<int length>
  const typename Vector<length>::CoordType Vector<length>::kEpsilon;

  template<int length>
  Vector<length>::Vector(void) {
    fill(coord_, coord_ + length, Vector<length>::kZero);
  }

  template<int length>
  Vector<length>::Vector(const Vector<length> &other) {
    copy(other.coord_, other.coord_ + length, coord_);
  }

  template<int length>
  void Vector<length>::Sub(const Vector<length> &rhs, Vector<length> *result) {
    transform(coord_, coord_ + length, rhs.coord_, result->coord_, minus<CoordType>());
  }

  template<int length>
  typename Vector<length>::CoordType Vector<length>::Abs(void) const {
    CoordType largest = kZero, result = kZero, tmp;

    for (int i = 0; i < length; ++i) {
      tmp = abs(coord_[i]);
      if (largest < tmp)
	largest = tmp;
    }

    if (EQ(largest, kZero))
      return kZero;

    for (int i = 0; i < length; ++i) {
      tmp = coord_[i] / largest;
      result += tmp * tmp;
    }
    result = largest * sqrt(result);

    return result;
  }

  template<int length>
  typename Vector<length>::CoordType& Vector<length>::operator [] (int index) {
    return coord_[index];
  }

  template<int length>
  const typename Vector<length>::CoordType& Vector<length>::operator [] (int index) const {
    return coord_[index];
  }

#ifdef HOME
  class VectorTest: public ::testing::Test {
  };

  TEST_F(VectorTest, TestConstructor) {
    Vector<3> u3;
    ASSERT_DOUBLE_EQ(0, u3[0]);
    ASSERT_DOUBLE_EQ(0, u3[1]);
    ASSERT_DOUBLE_EQ(0, u3[2]);

    u3[0] = 1;
    u3[1] = 2;
    u3[2] = 3;

    ASSERT_DOUBLE_EQ(1, u3[0]);
    ASSERT_DOUBLE_EQ(2, u3[1]);
    ASSERT_DOUBLE_EQ(3, u3[2]);

    Vector<3> v3(u3);
    ASSERT_DOUBLE_EQ(1, v3[0]);
    ASSERT_DOUBLE_EQ(2, v3[1]);
    ASSERT_DOUBLE_EQ(3, v3[2]);
  }

  TEST_F(VectorTest, TestIndexOperations) {
    Vector<3> u3;
    u3[0] = 3, u3[1] = 7, u3[2] = 4;
    ASSERT_DOUBLE_EQ(3, u3[0]);
    ASSERT_DOUBLE_EQ(7, u3[1]);
    ASSERT_DOUBLE_EQ(4, u3[2]);

    const Vector<3> v3(u3);
    ASSERT_DOUBLE_EQ(3, v3[0]);
    ASSERT_DOUBLE_EQ(7, v3[1]);
    ASSERT_DOUBLE_EQ(4, v3[2]);
  }

  TEST_F(VectorTest, TestSub) {
    Vector<4> u, v, w;
    u[0] = 2, u[1] = 5, u[2] = 6, u[3] = 1;
    v[0] = 3.5, v[1] = 2.2, v[2] = 6.3, v[3] = -11.1;

    u.Sub(v, &w);

    ASSERT_DOUBLE_EQ(-1.5, w[0]);
    ASSERT_DOUBLE_EQ(2.8, w[1]);
    ASSERT_DOUBLE_EQ(-0.3, w[2]);
    ASSERT_DOUBLE_EQ(12.1, w[3]);
  }

  TEST_F(VectorTest, TestAbs) {
    Vector<2> u;
    u[0] = 3.0, u[1] = 4.0;
    ASSERT_DOUBLE_EQ(5, u.Abs());
    u[0] = -3.0;
    ASSERT_DOUBLE_EQ(5, u.Abs());

    Vector<4> v;
    ASSERT_DOUBLE_EQ(0, v.Abs());
  }
#endif

} // namespace math

#endif
