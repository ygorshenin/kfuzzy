#include "vector.h"

#include <cmath>

#ifdef HOME
#include <gtest/gtest.h>
#endif


namespace math {
  const Vector::Type Vector::kZero;
  const Vector::Type Vector::kEpsilon;


  Vector::Vector(int size): size_(size), components_(kZero, size) {
  }

  Vector::Vector(int size, const Type *components): size_(size), components_(components, size) {
  }

  Vector::Vector(const Vector &other): size_(other.Size()), components_(other.components_) {
  }

  int Vector::Size(void) const {
    return size_;
  }

  Vector::Type& Vector::operator [] (int index) {
    return components_[index];
  }

  const Vector::Type& Vector::operator [] (int index) const {
    return components_[index];
  }

  Vector& Vector::operator = (const Vector &other) {
    if (this != &other)
      components_ = other.components_;
    return *this;
  }

  bool Vector::EQ(Vector::Type u, Vector::Type v) {
    return abs(u - v) < kEpsilon;
  }

  Vector::Type Vector::Distance(const Vector &other) const {
    std::valarray<Type> v(kZero, size_);
    for (int i = 0; i < size_; ++i)
      v[i] = abs(components_[i] - other.components_[i]);

    Type largest = v.max(), result = kZero;
    if (!EQ(kZero, largest)) {
      Type tmp;
      for (int i = 0; i < size_; ++i) {
	tmp = v[i] / largest;
	result += tmp * tmp;
      }
      result = largest * sqrt(result);
    }
    return result;
  };

  std::istream& operator >> (std::istream &is, Vector &v) {
    for (int i = 0; i < v.Size(); ++i)
      is >> v[i];
    return is;
  }

  std::ostream& operator << (std::ostream &os, Vector &v) {
    for (int i = 0; i < v.Size(); ++i)
      os << v[i] << ' ';
    return os;
  }

#ifdef HOME
  class TestVector : public ::testing::Test {
  };

  TEST_F(TestVector, TestConstructor) {
    const int kSize = 4;
    Vector u(kSize);

    ASSERT_EQ(kSize, u.Size());
    EXPECT_DOUBLE_EQ(0, u[0]);
    EXPECT_DOUBLE_EQ(0, u[1]);
    EXPECT_DOUBLE_EQ(0, u[2]);
    EXPECT_DOUBLE_EQ(0, u[3]);

    u[0] = 1.5, u[1] = -2.2, u[2] = 3.1415, u[3] = 19.5;
    Vector v(u);
    ASSERT_EQ(u.Size(), v.Size());
    EXPECT_DOUBLE_EQ(1.5, v[0]);
    EXPECT_DOUBLE_EQ(-2.2, v[1]);
    EXPECT_DOUBLE_EQ(3.1415, v[2]);
    EXPECT_DOUBLE_EQ(19.5, v[3]);

    double values[kSize] = { 1.0, 2.0, 3.0, 4.0 };
    Vector w(kSize, values);
    ASSERT_EQ(kSize, w.Size());
    EXPECT_DOUBLE_EQ(1.0, w[0]);
    EXPECT_DOUBLE_EQ(2.0, w[1]);
    EXPECT_DOUBLE_EQ(3.0, w[2]);
    EXPECT_DOUBLE_EQ(4.0, w[3]);
  }

  TEST_F(TestVector, TestDistance) {
    Vector u(2);
    u[0] = 3.0, u[1] = 4.0;

    EXPECT_DOUBLE_EQ(5.0, u.Distance(Vector(2)));
    u[0] = -3.0;
    EXPECT_DOUBLE_EQ(5.0, u.Distance(Vector(2)));
    u[1] = -4.0;
    EXPECT_DOUBLE_EQ(5.0, u.Distance(Vector(2)));

    Vector v(2);
    EXPECT_DOUBLE_EQ(0.0, v.Distance(Vector(2)));
  }

  TEST_F(TestVector, TestAssignment) {
    const int kSize = 5;
    const double kValues[kSize] = { -4.0, 3.75, 2.871, -22.2, 2.182 };

    Vector u(kSize);
    for (int i = 0; i < kSize; ++i)
      u[i] = kValues[i];
    Vector v(kSize), w(kSize);
    w = v = u;
    for (int i = 0; i < kSize; ++i) {
      u[i] = 0.0;
      EXPECT_DOUBLE_EQ(kValues[i], v[i]);
      EXPECT_DOUBLE_EQ(kValues[i], w[i]);
    }
    v = v;
    for (int i = 0; i < kSize; ++i)
      EXPECT_DOUBLE_EQ(kValues[i], v[i]);
  }

#endif
};
