#ifndef MATH_VECTOR_H
#define MATH_VECTOR_H

#include <iostream>
#include <valarray>


namespace math {
  /*
    Represents a simple vector in n-dimensional euclidian space.
  */
  class Vector {
  public:
    typedef double Type;

    static const Type kZero;
    static const Type kEpsilon;

    Vector(size_t size);
    Vector(size_t size, const Type *components);
    Vector(const Vector &other);

    size_t Size(void) const;
    Type Distance(const Vector &other) const;

    Vector& operator = (const Vector &other);
    Type& operator [] (size_t index);
    const Type& operator [] (size_t index) const;

    friend std::istream& operator >> (std::istream &is, Vector &v);
    friend std::ostream& operator << (std::ostream &os, Vector &v);

  private:
    const size_t size_;
    std::valarray<Type> components_;

    static bool EQ(Type u, Type v);
  }; // class Vector

} // namespace math

#endif
